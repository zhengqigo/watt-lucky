package org.fuelteam.watt.lucky.net;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Map;
import java.util.Random;

import javax.net.ServerSocketFactory;

import org.fuelteam.watt.lucky.properties.SystemPropertiesUtil;
import org.fuelteam.watt.lucky.utils.Platforms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.annotations.Beta;
import com.google.common.collect.Maps;

@Beta
public class NetUtil {

    private static final Logger logger = LoggerFactory.getLogger(NetUtil.class);

    public static final int PORT_RANGE_MIN = 1024;
    public static final int PORT_RANGE_MAX = 65535;

    private static Random random = new Random();

    // 获得本地地址
    public static InetAddress getLocalAddress() {
        return LocalAddressHoler.INSTANCE.localInetAddress;
    }

    // 获得本地Ip地址
    public static String getLocalHost() {
        return LocalAddressHoler.INSTANCE.localHost;
    }

    // 获得本地HostName
    public static String getHostName() {
        return LocalAddressHoler.INSTANCE.hostName;
    }

    // 懒加载进行探测
    private static class LocalAddressHoler {
        static final LocalAddress INSTANCE = new LocalAddress();
    }

    private static class LocalAddress {

        private InetAddress localInetAddress;
        private String localHost;
        private String hostName;

        public LocalAddress() {
            initLocalAddress();
            hostName = Platforms.IS_WINDOWS ? System.getenv("COMPUTERNAME") : System.getenv("HOSTNAME");
        }

        private void initLocalAddress() {
            NetworkInterface ni = null;
            try {
                localInetAddress = InetAddress.getLocalHost();
                ni = NetworkInterface.getByInetAddress(localInetAddress);
            } catch (Exception ignored) { // NOSONAR
            }

            // 结果为空或是loopback地址(127.0.0.1)或是ipv6地址，再遍历网卡尝试获取
            if (localInetAddress == null || ni == null || localInetAddress.isLoopbackAddress()
                    || localInetAddress instanceof Inet6Address) {
                InetAddress lookedUpAddr = findLocalAddressViaNetworkInterface();
                // 仍不符合要求，只好使用127.0.0.1
                try {
                    localInetAddress = lookedUpAddr != null ? lookedUpAddr : InetAddress.getByName("127.0.0.1");
                } catch (UnknownHostException ignored) {// NOSONAR
                }
            }
            localHost = IPUtil.toIpString(localInetAddress);
            logger.info("localhost is {}", localHost);
        }

        // 根据preferNamePrefix与defaultNicList的配置找出合适的网卡
        private static InetAddress findLocalAddressViaNetworkInterface() {
            // 如果hostname+/etc/hosts得到的是127.0.0.1则首选
            String preferNamePrefix = SystemPropertiesUtil.getString("localhost.prefer.nic.prefix", "LOCALHOST_PREFER_NIC_PREFIX",
                    "bond0.");
            // 如果hostname+/etc/hosts得到的是127.0.0.1和首选网卡都不符合要求则按顺序遍历
            String defaultNicList = SystemPropertiesUtil.getString("localhost.default.nic.list", "LOCALHOST_DEFAULT_NIC_LIST",
                    "bond0,eth0,em0,br0");

            InetAddress resultAddress = null;
            Map<String, NetworkInterface> candidateInterfaces = Maps.newHashMap();

            // 遍历找出所有可用网卡，尝试找出符合prefer前缀的网卡
            try {
                for (Enumeration<NetworkInterface> allInterfaces = NetworkInterface.getNetworkInterfaces(); allInterfaces
                        .hasMoreElements();) {
                    NetworkInterface ni = allInterfaces.nextElement();
                    // 检查网卡可用并支持广播
                    try {
                        if (!ni.isUp() || !ni.supportsMulticast()) continue;
                    } catch (SocketException ignored) { // NOSONAR
                        continue;
                    }

                    // 检查是否符合prefer前缀
                    String name = ni.getName();
                    if (name.startsWith(preferNamePrefix)) {
                        // 检查有否非ipv6、非127.0.0.1的InetAddress
                        resultAddress = findAvailableInetAddress(ni);
                        if (resultAddress != null) return resultAddress;
                    } else {
                        // 不是Prefer前缀则先放入可选列表
                        candidateInterfaces.put(name, ni);
                    }
                }

                for (String nifName : defaultNicList.split(",")) {
                    NetworkInterface nic = candidateInterfaces.get(nifName);
                    if (nic != null) {
                        resultAddress = findAvailableInetAddress(nic);
                        if (resultAddress != null) return resultAddress;
                    }
                }
            } catch (SocketException e) {// NOSONAR
                return null;
            }
            return null;
        }

        // 检查有否非ipv6、非127.0.0.1的InetAddress
        private static InetAddress findAvailableInetAddress(NetworkInterface ni) {
            for (Enumeration<InetAddress> indetAddresses = ni.getInetAddresses(); indetAddresses.hasMoreElements();) {
                InetAddress inetAddress = indetAddresses.nextElement();
                if (!(inetAddress instanceof Inet6Address) && !inetAddress.isLoopbackAddress()) return inetAddress;
            }
            return null;
        }
    }

    public static boolean isPortAvailable(int port) {
        try {
            InetAddress inetAddress = InetAddress.getByName("localhost");
            ServerSocket serverSocket = ServerSocketFactory.getDefault().createServerSocket(port, 1, inetAddress);
            serverSocket.close();
            return true;
        } catch (Exception ex) { // NOSONAR
            return false;
        }
    }

    public static int findRandomAvailablePort() {
        return findRandomAvailablePort(PORT_RANGE_MIN, PORT_RANGE_MAX);
    }

    public static int findRandomAvailablePort(int minPort, int maxPort) {
        int portRange = maxPort - minPort;
        int candidatePort;
        int searchCounter = 0;
        do {
            if (++searchCounter > portRange) {
                String message = "Could not find an available tcp port in the range [%d, %d] after %d attempts";
                throw new IllegalStateException(String.format(message, minPort, maxPort, searchCounter));
            }
            candidatePort = minPort + random.nextInt(portRange + 1);
        } while (!isPortAvailable(candidatePort));
        return candidatePort;
    }

    public static int findAvailablePortFrom(int minPort) {
        for (int port = minPort; port < PORT_RANGE_MAX; port++) {
            if (isPortAvailable(port)) return port;
        }
        String message = "Could not find an available tcp port in the range [%d, %d]";
        throw new IllegalStateException(String.format(message, minPort, PORT_RANGE_MAX));
    }
}