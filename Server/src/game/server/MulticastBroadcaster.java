package game.server;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;

public class MulticastBroadcaster {
    InetAddress multicastGroup;
    MulticastSocket multicastSocket;
    int port;
    public MulticastBroadcaster(String ipAddress, int port) throws IOException{
        multicastGroup = InetAddress.getByName(ipAddress);
        multicastSocket = new MulticastSocket(port);
        InetAddress address = localAddress();
        System.out.printf("Broadcast address: %s:%s\n",address.getHostAddress(),port);
        multicastSocket.setInterface(address);
        multicastSocket.joinGroup(multicastGroup);
        this.port = port;
    }

    public void broadcastString(String value) throws IOException{
        DatagramPacket packet = new DatagramPacket(value.getBytes(),value.length(),multicastGroup,port);
        multicastSocket.send(packet);
    }

    public void close(){
        multicastSocket.close();
    }

    private static InetAddress localAddress()
    {
        /*https://stackoverflow.com/questions/31928317/java-udp-multicast-only-working-on-localhost*/
        String ip;
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                if (iface.isLoopback() || !iface.isUp() || iface.isVirtual())
                    continue;

                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while(addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    ip = addr.getHostAddress();
                    if (ip.startsWith("10.") || ip.startsWith("172.31.") || ip.startsWith("192.168"))
                        return addr;
                }
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
