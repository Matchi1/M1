package fr.uge.net.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.logging.Logger;

public class ClientLongSum {

    public static final Logger logger = Logger.getLogger(ClientLongSum.class.getName());

    private static List<Long> randomLongList(int size) {
        return new Random().longs(size).boxed().toList();
    }

    private static boolean checkSum(List<Long> list, long response) {
        return list.stream().reduce(Long::sum).orElse(0L) == response;
    }

    static boolean readFully(SocketChannel sc, ByteBuffer buffer) throws IOException {
        var closed = 0;
        do {
            closed = sc.read(buffer);
        } while (closed > 0);
        return closed == -1;
    }

    /**
     * Write all the longs in list in BigEndian on the server and read the long sent
     * by the server and returns it
     *
     * returns Optional.empty if the protocol is not followed by the server but no
     * IOException is thrown
     *
     * @param sc
     * @param list
     * @return
     * @throws IOException
     */
    private static Optional<Long> requestSumForList(SocketChannel sc, List<Long> list) throws IOException {
        var bb = ByteBuffer.allocateDirect(list.size() * Long.BYTES + Integer.BYTES);
        bb.putInt(list.size());
        for(var value : list) {
            bb.putLong(value);
        }
        bb.flip();
        sc.write(bb);
        sc.shutdownOutput();
        bb.clear();
        while(!readFully(sc, bb));
        bb.flip();
        if(bb.remaining() == 0) {
            return Optional.empty();
        }
        return Optional.of(bb.getLong());
    }

    public static void main(String[] args) throws IOException {
        var server = new InetSocketAddress(args[0], Integer.parseInt(args[1]));
        try (var sc = SocketChannel.open(server)) {
            for (var i = 0; i < 5; i++) {
                var list = randomLongList(50);

                var sum = requestSumForList(sc, list);
                if (!sum.isPresent()) {
                    logger.warning("Connection with server lost.");
                    return;
                }
                if (!checkSum(list, sum.get())) {
                    logger.warning("Oups! Something wrong happened!");
                }
            }
            logger.info("Everything seems ok");
        }
    }
}