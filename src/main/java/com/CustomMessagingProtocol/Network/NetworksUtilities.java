package com.CustomMessagingProtocol.Network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class NetworksUtilities {

    public static InetAddress getActuallyIp() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();

                // Saltar interfaces que estén apagadas o loopback
                if (!ni.isUp() || ni.isLoopback() || ni.isVirtual()) {
                    continue;
                }
                Enumeration<InetAddress> address = ni.getInetAddresses();
                while (address.hasMoreElements()) {
                    InetAddress ip = address.nextElement();
                    // Saltar IPv6 o loopback
                    if (ip.isLoopbackAddress() || ip.getHostAddress().contains(":")) {
                        continue;
                    }
                    return ip;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return InetAddress.getLoopbackAddress();
    }
}
