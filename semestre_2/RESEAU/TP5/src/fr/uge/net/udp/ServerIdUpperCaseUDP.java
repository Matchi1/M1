package fr.uge.net.udp;

import java.util.Locale;
import java.util.logging.Logger;
import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ServerIdUpperCaseUDP {

    private static final Logger logger = Logger.getLogger(ServerIdUpperCaseUDP.class.getName());
    private static final Charset UTF8 = StandardCharsets.UTF_8;
    private static final int BUFFER_SIZE = 1024;

    private final DatagramChannel dc;
    private final ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);

    public ServerIdUpperCaseUDP(int port) throws IOException {
        dc = DatagramChannel.open();
        dc.bind(new InetSocketAddress(port));
        logger.info("ServerBetterUpperCaseUDP started on port " + port);
    }

    public void serve() throws IOException {
        try {
            var bb = ByteBuffer.allocate(BUFFER_SIZE);
            var send = ByteBuffer.allocate(BUFFER_SIZE);
            while (!Thread.interrupted()) {
                // 1) receive request from client
                bb.clear();
                var client = dc.receive(bb);
                // 2) read id
                bb.flip();
                var id = bb.getLong();
                // 3) decode msg in request String upperCaseMsg = msg.toUpperCase();
                var msg = UTF8.decode(bb).toString();
                // 4) create packet with id, upperCaseMsg in UTF-8
                send.clear();
                send.putLong(id);
                send.put(UTF8.encode(msg.toUpperCase()));
                // 5) send the packet to client
                send.flip();
                dc.send(send, client);
            }
        } finally {
            dc.close();
        }
    }

    public static void usage() {
        System.out.println("Usage : ServerIdUpperCaseUDP port");
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            usage();
            return;
        }

        var port = Integer.parseInt(args[0]);

        if (!(port >= 1024) & port <= 65535) {
            logger.severe("The port number must be between 1024 and 65535");
            return;
        }

        try {
            new ServerIdUpperCaseUDP(port).serve();
        } catch (BindException e) {
            logger.severe("Server could not bind on " + port + "\nAnother server is probably running on this port.");
            return;
        }
    }
}