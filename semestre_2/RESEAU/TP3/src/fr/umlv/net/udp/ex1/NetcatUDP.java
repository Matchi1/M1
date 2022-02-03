package fr.umlv.net.udp.ex1;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.Charset;
import java.util.Scanner;

public class NetcatUDP {
    public static final int BUFFER_SIZE = 1024;

    public static void display(SocketAddress sender, ByteBuffer buffer, Charset format) {
        buffer.flip();
        System.out.println("received " + buffer.remaining() + " from " + sender);
        System.out.println("content :" + format.decode(buffer));
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
        var receive = ByteBuffer.allocate(BUFFER_SIZE);

        try (var dc = DatagramChannel.open();
             var scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                var line = scanner.nextLine();
                buffer.clear();
                buffer.put(line.getBytes(cs));
                buffer.flip();
                dc.send(buffer, server);

                var sender = dc.receive(receive);
                display(sender, buffer, cs);
                receive.clear();
            }
        }
    }
}
