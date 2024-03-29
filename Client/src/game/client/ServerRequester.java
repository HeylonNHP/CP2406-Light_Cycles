package game.client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ServerRequester {
    private String destIPAddress;
    private int destPort;
    private final int responseTimeout = 2000; //2 second timeout
    public ServerRequester(){
        this("127.0.0.1",56970);
    }

    public ServerRequester(String destIPAddress, int destPort){
        System.out.printf("Setting up server requester with IP: %s:%s",
                destIPAddress,destPort);
        setDestIPAddress(destIPAddress);
        setDestPort(destPort);
    }

    public void sendNonRespondingRequest(String request) throws Exception{
        InetAddress destinationAddress = InetAddress.getByName(getDestIPAddress());

        try(DatagramSocket socket = new DatagramSocket(getDestPort())){
            DatagramPacket packet = new DatagramPacket(request.getBytes(),request.length(),destinationAddress,56971);
            socket.send(packet);
        }
    }

    public String getRequestResponse(String request) throws Exception{
        InetAddress destinationAddress = InetAddress.getByName(getDestIPAddress());

        try(DatagramSocket socket = new DatagramSocket(getDestPort())){
            DatagramPacket packet = new DatagramPacket(request.getBytes(),request.length(),destinationAddress,56971);
            socket.send(packet);

        /*Get response*/
            byte[] responseBuffer = new byte[1024];
            DatagramPacket responsePacket = new DatagramPacket(responseBuffer,responseBuffer.length);

            //Set the timeout incase we never receive a response
            socket.setSoTimeout(responseTimeout);

            //Will throw an exception if no response is received within the timeout
            socket.receive(responsePacket);

            String responseString = new String(responseBuffer);
            responseString = responseString.trim();
            return responseString;
        }
    }

    public String getDestIPAddress() {
        return destIPAddress;
    }

    public void setDestIPAddress(String destIPAddress) {
        this.destIPAddress = destIPAddress;
    }

    public int getDestPort() {
        return destPort;
    }

    public void setDestPort(int destPort) {
        this.destPort = destPort;
    }
}
