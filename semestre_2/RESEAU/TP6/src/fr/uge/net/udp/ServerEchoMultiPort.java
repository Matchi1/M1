package fr.uge.net.udp;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.logging.Logger;

public class ServerEchoMultiPort {
    private static final Logger logger = Logger.getLogger(ServerEchoPlus.class.getName());

    private final Selector selector;
    private final int BUFFER_SIZE = 1024;
    private int port;

    public ServerEchoMultiPort(int start, int end) throws IOException {
        selector = Selector.open();
        for(; start < end; start++) {
            var dc = DatagramChannel.open();
            dc.bind(new InetSocketAddress(port));
            dc.configureBlocking(false);
            dc.register(selector, SelectionKey.OP_READ, new Context());
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
        var dc = (DatagramChannel) key.channel();
        var buffer = ByteBuffer.allocate(BUFFER_SIZE);
        context.sender = dc.receive(buffer);
        if(context.sender == null) {
            logger.info("nothing has been sent");
            return;
        }
        buffer.flip();
        while(buffer.hasRemaining()) {
            var b = buffer.get() + 1;
            context.buffer.put(Integer.valueOf(b).byteValue());
        }
        context.buffer.flip();
        key.interestOps(SelectionKey.OP_WRITE);
    }

    private void doWrite(SelectionKey key) throws IOException {
        var context = (Context) key.attachment();
        var dc = (DatagramChannel) key.channel();
        dc.send(context.buffer, context.sender);
        if(context.buffer.hasRemaining()) {
            logger.info("data not sent");
            return;
        }
        context.buffer.clear();
        key.interestOps(SelectionKey.OP_READ);
    }

    public static void usage() {
        System.out.println("Usage : ServerEchoMultiPort start end");
    }

    public class Context {
        private SocketAddress sender;
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
