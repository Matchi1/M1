package fr.uge.net.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Logger;

public class ClientConcatenation {
    public static final int BUFFER_SIZE = 1024;
    public static final Charset UTF8 = StandardCharsets.UTF_8;
    public static final Logger logger = Logger.getLogger(ClientLongSum.class.getName());

    private static List<Long> randomLongList(int size) {
        return new Random().longs(size).boxed().toList();
    }

    private static boolean checkSum(List<Long> list, long response) {
        return list.stream().reduce(Long::sum).orElse(0L) == response;
    }

    static boolean readFully(SocketChannel sc, ByteBuffer buffer, int size) throws IOException {
        var closed = 0;
        var sum = 0;
        do {
            sum += closed;
            if(sum == size) {
                return false;
            }
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
    private static Optional<String> requestConcat(SocketChannel sc, List<String> list) throws IOException {
        var bb = ByteBuffer.allocateDirect(BUFFER_SIZE);
        var length = 0;
        bb.putInt(list.size());
        for(var value : list) {
            length += value.length();
            bb.putInt(value.length());
            bb.put(UTF8.encode(value));
        }
        bb.flip();
        sc.write(bb);
        bb.clear();
        if(readFully(sc, bb, length + list.size() - 1 + Integer.BYTES)) {
            return Optional.empty();
        }
        bb.flip();
        bb.getInt();
        if(bb.remaining() == 0) {
            return Optional.empty();
        }
        return Optional.of(UTF8.decode(bb).toString());
    }

    public static void main(String[] args) throws IOException {
        var server = new InetSocketAddress(args[0], Integer.parseInt(args[1]));
        var list = new ArrayList<String>();
        try (var sc = SocketChannel.open(server);
            var scan = new Scanner(System.in)) {
            while(scan.hasNextLine()) {
                var line = scan.nextLine();
                if(line.equals("")) {
                    break;
                }
                list.add(line);
            }
            var result = requestConcat(sc, list);
            if(result.isPresent()) {
                System.out.println(result.get());
            } else {
                System.out.println("Connection with server lost.");
            }
        }
    }
}
