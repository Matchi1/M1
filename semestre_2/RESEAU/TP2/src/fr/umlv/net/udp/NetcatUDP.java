package fr.umlv.net.udp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class NetcatUDP {
    public static final int BUFFER_SIZE = 1024;

    public static void display(SocketAddress sender, ByteBuffer buffer, Charset format) {
        buffer.flip();
        System.out.println("received " + buffer.remaining() + " from " + sender);
        System.out.println("content :" + format.decode(buffer));
    }

    private static void send(DatagramChannel dc, String ip, String port, String message, Charset format) throws IOException {
        var address = InetAddress.getByName(ip);
        var socket = new InetSocketAddress(address, Integer.parseInt(port));
        var buffer = ByteBuffer.allocate(256);
        buffer.put(message.getBytes(format));
        dc.send(buffer, socket);
    }

    public static SocketAddress receive(DatagramChannel dc, ByteBuffer buffer) throws IOException {
        var sender = dc.receive(buffer);
        buffer.flip();
        return sender;
    }

    private static void usage() {
        System.out.println("Usage : NetcatUDP host port charset");
    }

    public static void main(String[] args) throws IOException {
        if(args.length != 3) {
            usage();
            return;
        }

        var server = new InetSocketAddress(args[0], Integer.parseInt(args[1]));
        var cs = Charset.forName(args[2]);
        var buffer = ByteBuffer.allocate(BUFFER_SIZE);

        try (var dc = DatagramChannel.open();
             var scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                var line = scanner.nextLine();
                buffer.clear();
                buffer.put(line.getBytes(cs));
                buffer.flip();
                dc.send(buffer, server);
                buffer.clear();
                var sender = dc.receive(buffer);
                display(sender, buffer, cs);
            }
        }
    }
}
