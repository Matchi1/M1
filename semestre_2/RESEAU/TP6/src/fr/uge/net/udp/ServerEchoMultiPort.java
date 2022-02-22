package fr.uge.net.udp;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.ArrayList;
import java.util.logging.Logger;

public class ServerEchoMultiPort {
    private static final Logger logger = Logger.getLogger(ServerEchoPlus.class.getName());

    private final Selector selector;
    private final int BUFFER_SIZE = 1024;
    private final ArrayList<DatagramChannel> channels = new ArrayList<>();
    private SocketAddress sender;
    private int port;

    public ServerEchoMultiPort(int start, int end) throws IOException {
        this.port = port;
        selector = Selector.open();
        for(; start < end; start++) {
            var dc = DatagramChannel.open();
            dc.bind(new InetSocketAddress(port));
            dc.configureBlocking(false);
            dc.register(selector, SelectionKey.OP_READ, new Context());
            channels.add(dc);
        }
    }

    public void serve() throws IOException {
        logger.info("ServerEcho started on port " + port);
        var keys = selector.selectedKeys();
        while (!Thread.interrupted()) {
            selector.select(this::treatKey);
            keys.clear();
        }
    }

    private void treatKey(SelectionKey key) {
        try {
            if (key.isValid() && key.isWritable()) {
                doWrite(key);
            }
            if (key.isValid() && key.isReadable()) {
                doRead(key);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void doRead(SelectionKey key) throws IOException {
        var context = (Context) key.attachment();
        context.buffer.clear();
        if(sender == null) {
            logger.info("nothing has been sent");
            return;
        }
        buffer.flip();
        send.clear();
        while(buffer.hasRemaining()) {
            var b = buffer.get() + 1;
            send.put(Integer.valueOf(b).byteValue());
        }
        key.interestOps(SelectionKey.OP_WRITE);
    }

    private void doWrite(SelectionKey key) throws IOException {
        send.flip();
        dc.send(send, sender);
        if(send.hasRemaining()) {
            logger.info("data not sent");
            return;
        }
        key.interestOps(SelectionKey.OP_READ);
    }

    public static void usage() {
        System.out.println("Usage : ServerEchoMultiPort start end");
    }

    public class Context {
        private InetSocketAddress sender;
        private final ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            usage();
            return;
        }
        new ServerEchoMultiPort(Integer.parseInt(args[0]), Integer.parseInt(args[1])).serve();
    }
}
