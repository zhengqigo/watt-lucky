package org.fuelteam.watt.lucky.net;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import org.fuelteam.watt.lucky.number.NumberUtil;
import org.fuelteam.watt.lucky.text.MoreStringUtil;

import com.google.common.net.InetAddresses;

// InetAddress与String的转换消耗较大，建议进行缓存
public class IPUtil {

    /**
     * @see com.google.common.net.InetAddresses#coerceToInteger(InetAddress)
     */
    public static int toInt(InetAddress inetAddress) {
        return InetAddresses.coerceToInteger(inetAddress);
    }

    /**
     * @see com.google.common.net.InetAddresses#toAddrString(InetAddress)
     */
    public static String toIpString(InetAddress inetAddress) {
        return InetAddresses.toAddrString(inetAddress);
    }

    public static Inet4Address fromInt(int i) {
        return InetAddresses.fromInteger(i);
    }

    public static InetAddress fromIpString(String ipString) {
        return InetAddresses.forString(ipString);
    }

    public static Inet4Address fromIpv4String(String ipv4String) {
        byte[] bytes = ip4StringToBytes(ipv4String);
        if (bytes == null) return null;
        try {
            return (Inet4Address) Inet4Address.getByAddress(bytes);
        } catch (UnknownHostException e) {
            throw new AssertionError(e);
        }
    }

    public static String intToIpv4String(int i) {
        return new StringBuilder(15).append((i >> 24) & 0xff).append('.').append((i >> 16) & 0xff).append('.')
                .append((i >> 8) & 0xff).append('.').append(i & 0xff).toString();
    }

    public static int ipv4StringToInt(String ipv4String) {
        byte[] byteAddress = ip4StringToBytes(ipv4String);
        if (byteAddress == null) return 0;
        return NumberUtil.toInt(byteAddress);
    }

    private static byte[] ip4StringToBytes(String ipv4String) {
        if (ipv4String == null) return null;
        List<String> it = MoreStringUtil.split(ipv4String, '.', 4);
        if (it.size() != 4) return null;
        byte[] byteAddress = new byte[4];
        for (int i = 0; i < 4; i++) {
            int tempInt = Integer.parseInt(it.get(i));
            if (tempInt > 255) return null;
            byteAddress[i] = (byte) tempInt;
        }
        return byteAddress;
    }
}