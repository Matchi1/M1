package fr.uge.net.udp;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Logger;

public class ServerLongSum {
    private static final Logger logger = Logger.getLogger(ServerIdUpperCaseUDP.class.getName());
    private static final Charset UTF8 = StandardCharsets.UTF_8;
    private static final int BUFFER_SIZE = 1024;

    private final DatagramChannel dc;
    private final HashMap<SocketAddress, HashMap<Long, Session>> users = new HashMap<>();

    public ServerLongSum(int port) throws IOException {
        dc = DatagramChannel.open();
        dc.bind(new InetSocketAddress(port));
        logger.info("ServerLongSum started on port " + port);
    }

    public void serve() throws IOException {
        try {
            var bb = ByteBuffer.allocate(BUFFER_SIZE);
            while (!Thread.interrupted()) {
                // 1) receive request from client
                bb.clear();
                var client = dc.receive(bb);
                // 2) read id
                bb.flip();
                if(checkOPPacketSize(bb) && bb.get() == 1) {
                    var sessionId = bb.getLong();
                    var idPosOper = Long.valueOf(bb.getLong()).intValue();
                    var totalOper = Long.valueOf(bb.getLong()).intValue();
                    var opValue = bb.getLong();
                    var session = processOPPacket(client, sessionId, idPosOper, totalOper, opValue);
                    if(session.isFull()) {
                        sendRESPacket(dc, client, sessionId, session.result());
                    }
                    sendACKPacket(dc, client, sessionId, idPosOper);
                }
            }
        } finally {
            dc.close();
        }
    }

    public boolean checkOPPacketSize(ByteBuffer bb) {
        var size = bb.limit() - bb.position();
        return size == (Byte.BYTES + Long.BYTES * 4);
    }

    public Session processOPPacket(SocketAddress client, long sessionId, int idPosOper, int totalOper, long opValue) {
        var sessions = users.computeIfAbsent(client, c -> new HashMap<>());
        var session = sessions.computeIfAbsent(sessionId, i -> new Session(i, totalOper));
        session.addValue(idPosOper, opValue);
        return session;
    }

    public void sendRESPacket(DatagramChannel dc, SocketAddress client, long sessionId, long result) throws IOException {
        var bb = ByteBuffer.allocate(BUFFER_SIZE);
        logger.info("sent res packet");
        bb.put(Integer.valueOf(3).byteValue());
        bb.putLong(sessionId);
        bb.putLong(result);
        bb.flip();
        dc.send(bb, client);
    }

    public void sendACKPacket(DatagramChannel dc, SocketAddress client, long sessionId, long opId) throws IOException {
        var bb = ByteBuffer.allocate(BUFFER_SIZE);
        logger.info("Send ACK packet " + opId);
        bb.put(Integer.valueOf(2).byteValue());
        bb.putLong(sessionId);
        bb.putLong(opId);
        bb.flip();
        dc.send(bb, client);
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
            new ServerLongSum(port).serve();
        } catch (BindException e) {
            logger.severe("Server could not bind on " + port + "\nAnother server is probably running on this port.");
        }
    }

    public static void usage() {
        System.out.println("Usage : ServerLongSum port");
    }

    public static class Session {
        private long id = 0L;
        private long result;
        private final BitSet received;

        public Session(long id, int size) {
            this.id = id;
            this.result = 0;
            this.received = new BitSet(size);
        }

        public boolean addValue(int index, long value) {
            if(!received.get(index)) {
                result += value;
                received.set(index);
                return true;
            }
            return false;
        }

        public boolean isFull() {
            for(var i = 0; i < received.length(); i++) {
                if(!received.get(i)) {
                    return false;
                }
            }
            return true;
        }

        public long result() {
            return result;
        }
    }
}
