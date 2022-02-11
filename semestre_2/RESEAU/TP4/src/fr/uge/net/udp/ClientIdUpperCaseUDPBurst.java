package fr.uge.net.udp;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.DatagramChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientIdUpperCaseUDPBurst {

        private static final Logger logger = Logger.getLogger(ClientIdUpperCaseUDPBurst.class.getName());
        private static final Charset UTF8 = StandardCharsets.UTF_8;
        private static final int BUFFER_SIZE = 1024;
        private final List<String> lines;
        private final int nbLines;
        private final String[] upperCaseLines; //
        private final int timeout;
        private final String outFilename;
        private final InetSocketAddress serverAddress;
        private final DatagramChannel dc;
        private final AnswersLog answersLog;         // Thread-safe structure keeping track of missing responses

        private final ReentrantLock lock = new ReentrantLock();

        public static void usage() {
            System.out.println("Usage : ClientIdUpperCaseUDPBurst in-filename out-filename timeout host port ");
        }

        public ClientIdUpperCaseUDPBurst(List<String> lines,int timeout,InetSocketAddress serverAddress,String outFilename) throws IOException {
            this.lines = lines;
            this.nbLines = lines.size();
            this.timeout = timeout;
            this.outFilename = outFilename;
            this.serverAddress = serverAddress;
            this.dc = DatagramChannel.open();
            dc.bind(null);
            this.upperCaseLines = new String[nbLines];
            this.answersLog = new AnswersLog(nbLines);
        }

        private void senderThreadRun() {
            try {
                var buffer = ByteBuffer.allocate(BUFFER_SIZE);
                var lastSent = 0L;
                while (true) {
                    var remainingResponse = answersLog.getRemainingResponses();
                    if (remainingResponse.isEmpty()) {
                        break;
                    }
                    var currentTime = System.currentTimeMillis();
                    if (currentTime - lastSent > timeout) {
                        for (var i : remainingResponse) {
                            buffer.clear();
                            buffer.putLong(i);
                            buffer.put(UTF8.encode(lines.get(i)));
                            buffer.flip();
                            dc.send(buffer, serverAddress);
                        }
                        lastSent = System.currentTimeMillis();
                    }
                }
            } catch (AsynchronousCloseException e) {
                logger.info("Interruption of the thread");
            } catch (IOException e) {
                logger.severe("IOExceptionn issues");
            }
        }

        private void listenerThreadRun() {
            try {
                var buffer = ByteBuffer.allocate(BUFFER_SIZE);
                while(true) {
                    buffer.clear();
                    dc.receive(buffer);
                    buffer.flip();
                    var id = buffer.getLong();
                    if(id < nbLines) {
                        var message = UTF8.decode(buffer).toString();
                        if(!message.isEmpty()) {
                            var index = Long.valueOf(id).intValue();
                            upperCaseLines[index] = message;
                            answersLog.setResponse(index);
                        }
                    }
                }
            } catch (AsynchronousCloseException e) {
                logger.info("Interruption of the thread");
            } catch (IOException e) {
                logger.severe("IOException issues");
            }
        }

        public void launch() throws IOException {
            var senderThread = new Thread(this::senderThreadRun);
            var listenerThread = new Thread(this::listenerThreadRun);
            senderThread.start();
            listenerThread.start();
            while(!answersLog.isFull());
            senderThread.interrupt();
            listenerThread.interrupt();
            Files.write(Paths.get(outFilename),Arrays.asList(upperCaseLines), UTF8,
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE,
                StandardOpenOption.TRUNCATE_EXISTING);

        }

        public static void main(String[] args) throws IOException, InterruptedException {
            if (args.length !=5) {
                usage();
                return;
            }

            String inFilename = args[0];
            String outFilename = args[1];
            int timeout = Integer.parseInt(args[2]);
            String host=args[3];
            int port = Integer.parseInt(args[4]);
            InetSocketAddress serverAddress = new InetSocketAddress(host,port);

            //Read all lines of inFilename opened in UTF-8
            List<String> lines= Files.readAllLines(Paths.get(inFilename),UTF8);
            //Create client with the parameters and launch it
            ClientIdUpperCaseUDPBurst client = new ClientIdUpperCaseUDPBurst(lines,timeout,serverAddress,outFilename);
            client.launch();

        }

        private static class AnswersLog {
            private final ReentrantLock lock = new ReentrantLock();
            private final Condition condition = lock.newCondition();
            private final int size;
            private final BitSet responses;

            public AnswersLog(int size) {
                this.size = size;
                this.responses = new BitSet(size);
            }

            public List<Integer> getRemainingResponses() {
                lock.lock();
                try {
                    var list = new ArrayList<Integer>();
                    for(var i = 0; i < size; i++) {
                        if(!responses.get(i)) {
                            list.add(i);
                        }
                    }
                    return list;
                } finally {
                    lock.unlock();
                }
            }

            public void setResponse(int index) {
                lock.lock();
                try {
                    responses.set(index);
                } finally {
                    lock.unlock();
                }
            }

            public boolean isFull() {
                lock.lock();
                try {
                    return responses.cardinality() == size;
                } finally {
                    lock.unlock();
                }
            }
        }
    }


