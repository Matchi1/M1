package fr.uge.net.tcp.ex3;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FixedPrestartedLongSumServer {

    private static final Logger logger = Logger.getLogger(FixedPrestartedLongSumServer.class.getName());
    private static final int BUFFER_SIZE = 1024;
    private final ServerSocketChannel ssc;
    private final int maxThreads = 5;
    private final ArrayList<Thread> threads = new ArrayList<>();

    public FixedPrestartedLongSumServer(int port) throws IOException {
        ssc = ServerSocketChannel.open();
        ssc.bind(new InetSocketAddress(port));
        logger.info(this.getClass().getName() + " starts on port " + port);
    }

    /**
     * Iterative server main loop
     *
     * @throws IOException
     */

    private void threadRun() {
        while(!Thread.interrupted()) {
            SocketChannel client = null;
            try {
                client = ssc.accept();
                logger.info("Connection accepted from " + client.getRemoteAddress());
                serve(client);
            } catch (IOException ioe) {
                logger.log(Level.SEVERE, "Connection terminated with client by IOException", ioe.getCause());
            } finally {
                silentlyClose(client);
            }
        }
    }

    public void launch() throws IOException, InterruptedException {
        logger.info("Server started");
        for(var i = 0; i < maxThreads; i++) {
            threads.add(new Thread(this::threadRun));
        }
        for(var thread : threads) {
            thread.start();
        }
    }

    private void sendResult(SocketChannel sc, Long result) throws IOException {
        var bb = ByteBuffer.allocate(Long.BYTES);
        bb.putLong(result);
        bb.flip();
        sc.write(bb);
    }

    /**
     * Treat the connection sc applying the protocol. All IOException are thrown
     *
     * @param sc
     * @throws IOException
     */
    private void serve(SocketChannel sc) throws IOException {
        var bb = ByteBuffer.allocate(Integer.BYTES);
        while(true){
            var result = 0L;
            bb.clear();
            if(!readFully(sc, bb)) {
                return;
            }
            bb.flip();
            var size = bb.getInt();
            var buffer = ByteBuffer.allocate(Integer.min(size * Long.BYTES, BUFFER_SIZE));
            if(!readFully(sc, buffer)) {
                return;
            }
            buffer.flip();
            for(var i = 0; i < size; i++) {
                if(!buffer.hasRemaining()) {
                    buffer.clear();
                    if(!readFully(sc, buffer)) {
                        return;
                    }
                    buffer.flip();
                }
                result += buffer.getLong();
            }
            sendResult(sc, result);
        }
    }

    /**
     * Close a SocketChannel while ignoring IOExecption
     *
     * @param sc
     */

    private void silentlyClose(Closeable sc) {
        if (sc != null) {
            try {
                sc.close();
            } catch (IOException e) {
                // Do nothing
            }
        }
    }

    static boolean readFully(SocketChannel sc, ByteBuffer buffer) throws IOException {
        while (buffer.hasRemaining()) {
            if (sc.read(buffer) == -1) {
                logger.info("Input stream closed");
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) throws NumberFormatException, IOException, InterruptedException {
        var server = new FixedPrestartedLongSumServer(Integer.parseInt(args[0]));
        server.launch();
    }
}
