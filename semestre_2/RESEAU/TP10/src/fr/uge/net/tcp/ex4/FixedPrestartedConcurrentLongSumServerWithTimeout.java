package fr.uge.net.tcp.ex4;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FixedPrestartedConcurrentLongSumServerWithTimeout {

    private static final Logger logger = Logger.getLogger(FixedPrestartedConcurrentLongSumServerWithTimeout.class.getName());
    private static final int BUFFER_SIZE = 1024;
    private final ServerSocketChannel ssc;
    private final int maxThreads = 5;
    private final ArrayList<ThreadData> data = new ArrayList<>();
    private int timeout;

    public FixedPrestartedConcurrentLongSumServerWithTimeout(int port, int timeout) throws IOException {
        ssc = ServerSocketChannel.open();
        ssc.bind(new InetSocketAddress(port));
        logger.info(this.getClass().getName() + " starts on port " + port);
        this.timeout = timeout;
    }

    private void managerRun() {
        try {
            while (true) {
                synchronized (data) {
                    for(var threadData : data) {
                        threadData.closeIfInactive(timeout);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("IOError");
        }
    }

    /**
     * Iterative server main loop
     *
     * @throws IOException
     */

    private void threadRun() {
        var threadData = new ThreadData();
        synchronized (data) {
            data.add(threadData);
        }
        while(!Thread.interrupted()) {
            SocketChannel client;
            try {
                try {
                    client = ssc.accept();
                    threadData.setSocketChannel(client);
                } catch (IOException ioe) {
                    logger.log(Level.SEVERE, "Connection terminated with client by IOException", ioe.getCause());
                    return;
                }
                logger.info("Connection accepted from " + client.getRemoteAddress());
                serve(client, threadData);
            } catch (IOException ioe) {
                logger.log(Level.WARNING, "Connection terminated with client by IOException", ioe.getCause());
                threadData.close();
            }
        }
    }

    public void launch() {
        var threads = new ArrayList<Thread>();
        logger.info("Server started");
        threads.add(new Thread(this::managerRun));
        for(var i = 0; i < maxThreads; i++) {
            threads.add(new Thread(this::threadRun));
        }
        for(var thread : threads) {
            thread.start();
        }
    }

    private void sendResult(SocketChannel sc, ThreadData threadData, Long result) throws IOException {
        var bb = ByteBuffer.allocate(Long.BYTES);
        bb.putLong(result);
        bb.flip();
        sc.write(bb);
        threadData.tick();
    }

    /**
     * Treat the connection sc applying the protocol. All IOException are thrown
     *
     * @param sc
     * @throws IOException
     */
    private void serve(SocketChannel sc, ThreadData threadData) throws IOException {
        var bb = ByteBuffer.allocate(Integer.BYTES);
        while(true){
            var result = 0L;
            bb.clear();
            if(!readFully(sc, threadData, bb)) {
                return;
            }
            bb.flip();
            var size = bb.getInt();
            var buffer = ByteBuffer.allocate(Integer.min(size * Long.BYTES, BUFFER_SIZE));
            if(!readFully(sc, threadData, buffer)) {
                return;
            }
            buffer.flip();
            for(var i = 0; i < size; i++) {
                if(!buffer.hasRemaining()) {
                    buffer.clear();
                    if(!readFully(sc, threadData, buffer)) {
                        return;
                    }
                    buffer.flip();
                }
                result += buffer.getLong();
            }
            sendResult(sc, threadData, result);
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

    boolean readFully(SocketChannel sc, ThreadData threadData, ByteBuffer buffer) throws IOException {
        while (buffer.hasRemaining()) {
            if (sc.read(buffer) == -1) {
                logger.info("Input stream closed");
                return false;
            }
            threadData.tick();
        }
        return true;
    }

    public static void main(String[] args) throws NumberFormatException, IOException {
        var server = new FixedPrestartedConcurrentLongSumServerWithTimeout(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        server.launch();
    }
}
