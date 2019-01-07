package org.fuelteam.watt.lucky.utils;

import java.io.IOException;
import java.net.ServerSocket;

public class OSUtil {

    public static int getFreePort(int defaultPort) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(defaultPort)) {
            return serverSocket.getLocalPort();
        } catch (IOException ioe) {
            return getFreePort();
        }
    }

    public static int getFreePort() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(0)) {
            return serverSocket.getLocalPort();
        }
    }

    public static boolean busy(int port) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            return false;
        } catch (Exception ex) {
            //
        } finally {
            close(serverSocket);
        }
        return true;
    }

    private static void close(ServerSocket serverSocket) {
        if (serverSocket == null) return;
        try {
            serverSocket.close();
        } catch (IOException ioe) {
            //
        }
    }

    private static final String OS = System.getProperty("os.name").toLowerCase();

    public static boolean windows() {
        return OS.indexOf("win") >= 0;
    }

    public static boolean mac() {
        return OS.indexOf("mac") >= 0;
    }

    public static boolean unix() {
        return OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") >= 0;
    }

    public static boolean solaris() {
        return (OS.indexOf("sunos") >= 0);
    }

    private static final String ARCH = System.getProperty("sun.arch.data.model");

    public static boolean arch64() {
        return "64".equals(ARCH);
    }

    public static boolean arch32() {
        return "32".equals(ARCH);
    }
}