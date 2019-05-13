package org.fuelteam.watt.lucky.net;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

import javax.net.ServerSocketFactory;

import org.fuelteam.watt.lucky.io.IOUtil;
import org.fuelteam.watt.lucky.net.NetUtil;
import org.junit.Test;

public class NetUtilTest {

    @Test
    public void localhost() {
        assertThat(NetUtil.getLocalHost()).isNotEqualTo("127.0.0.1");
        assertThat(NetUtil.getLocalAddress().getHostAddress()).isNotEqualTo("127.0.0.1");
    }

    @Test
    public void portDetect() throws UnknownHostException, IOException {
        int port1 = NetUtil.findRandomAvailablePort(20000, 20100);
        assertThat(port1).isBetween(20000, 20100);
        System.out.println("random available port: " + port1);

        assertThat(NetUtil.isPortAvailable(port1)).isTrue();

        int port2 = NetUtil.findAvailablePortFrom(port1);
        assertThat(port2).isEqualTo(port1);

        int port3 = NetUtil.findRandomAvailablePort();
        assertThat(port3).isBetween(NetUtil.PORT_RANGE_MIN, NetUtil.PORT_RANGE_MAX);
        System.out.println("random available port: " + port3);

        // 尝试占住一个端口
        ServerSocket serverSocket = null;
        try {
            serverSocket = ServerSocketFactory.getDefault().createServerSocket(port1, 1, InetAddress.getByName("localhost"));

            assertThat(NetUtil.isPortAvailable(port1)).isFalse();

            int port4 = NetUtil.findAvailablePortFrom(port1);
            assertThat(port4).isEqualTo(port1 + 1);

            try {
                NetUtil.findRandomAvailablePort(port1, port1);
                fail("should fail before");
            } catch (Throwable t) {
                assertThat(t).isInstanceOf(IllegalStateException.class);
            }
        } finally {
            IOUtil.closeQuietly(serverSocket);
        }
    }
}