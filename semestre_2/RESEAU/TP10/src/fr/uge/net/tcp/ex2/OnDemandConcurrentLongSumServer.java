package fr.uge.net.tcp.ex2;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OnDemandConcurrentLongSumServer {

    private static final Logger logger = Logger.getLogger(OnDemandConcurrentLongSumServer.class.getName());
    private static final int BUFFER_SIZE = 1024;
    private final ServerSocketChannel ssc;

    public OnDemandConcurrentLongSumServer(int port) throws IOException {
        ssc = ServerSocketChannel.open();
        ssc.bind(new InetSocketAddress(port));
        logger.info(this.getClass().getName() + " starts on port " + port);
    }

    /**
     * Iterative server main loop
     *
     * @throws IOException
     */

    public void launch() throws IOException {
        logger.info("Server started");
        while (!Thread.interrupted()) {
            SocketChannel client = ssc.accept();
            try {
                logger.info("Connection accepted from " + client.getRemoteAddress());
                serve(client);
            } catch (IOException ioe) {
                logger.log(Level.SEVERE, "Connection terminated with client by IOException", ioe.getCause());
            } finally {
                silentlyClose(client);
            }
        }
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
            var buffer = ByteBuffer.allocate(Long.BYTES * size);
            readFully(sc, buffer);
            buffer.flip();
            for(var i = 0; i < size; i++) {
                result += buffer.getLong();
            }
            buffer.clear();
            buffer.putLong(result);
            buffer.flip();
            sc.write(buffer);
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

    public static void main(String[] args) throws NumberFormatException, IOException {
        var server = new OnDemandConcurrentLongSumServer(Integer.parseInt(args[0]));
        server.launch();
    }
}
