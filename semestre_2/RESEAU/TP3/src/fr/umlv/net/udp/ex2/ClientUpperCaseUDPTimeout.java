package fr.umlv.net.udp.ex2;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class ClientUpperCaseUDPTimeout {
    public static final int BUFFER_SIZE = 1024;
    public static final int TIMEOUT = 1000;

    public static void display(SocketAddress sender, ByteBuffer buffer, Charset format) {
        buffer.flip();
        System.out.println("received " + buffer.remaining() + " from " + sender);
        System.out.println("content :" + format.decode(buffer));
    }

    private static void usage() {
        System.out.println("Usage : NetcatUDP host port charset");
    }

    private static Thread createListener(DatagramChannel channel, BlockingQueue<String> queue, Charset cs) {
        return new Thread(() -> {
            var buffer = ByteBuffer.allocate(BUFFER_SIZE);
            while(true) {
                try {
                    channel.receive(buffer);
                    buffer.flip();
                    queue.add(cs.decode(buffer).toString());
                } catch (IOException e) {
                    System.out.println("Can't read the channel");
                }
                buffer.clear();
            }
        });
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        if(args.length != 3) {
            usage();
            return;
        }

        var server = new InetSocketAddress(args[0], Integer.parseInt(args[1]));
        var cs = Charset.forName(args[2]);
        var buffer = ByteBuffer.allocate(BUFFER_SIZE);
        var queue = new ArrayBlockingQueue<String>(10);

        try (var dc = DatagramChannel.open();
             var scanner = new Scanner(System.in)) {
            createListener(dc, queue, cs).start();
            while (scanner.hasNextLine()) {
                var line = scanner.nextLine();
                buffer.clear();
                buffer.put(line.getBytes(cs));
                buffer.flip();
                dc.send(buffer, server);

                var message = queue.poll(TIMEOUT, TimeUnit.MILLISECONDS);
                if(message == null) {
                    System.out.println("Le serveur n'a pas répondu");
                } else {
                    System.out.println("message : " + message);
                }
            }
        }
    }
}
