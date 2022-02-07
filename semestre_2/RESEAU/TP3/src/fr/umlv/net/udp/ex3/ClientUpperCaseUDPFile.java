package fr.umlv.net.udp.ex3;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.DatagramChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static java.nio.file.StandardOpenOption.*;

public class ClientUpperCaseUDPFile {
    private final static Logger logger = Logger.getLogger(ClientUpperCaseUDPFile.class.getName());
    private final static Charset UTF8 = StandardCharsets.UTF_8;
    private final static int BUFFER_SIZE = 1024;

    private static void usage() {
        System.out.println("Usage : ClientUpperCaseUDPFile in-filename out-filename timeout host port ");
    }

    private static Thread createListener(DatagramChannel channel, BlockingQueue<String> queue, Charset cs) {
        return new Thread(() -> {
            try {
                var buffer = ByteBuffer.allocate(BUFFER_SIZE);
                while (true) {
                    channel.receive(buffer);
                    buffer.flip();
                    queue.put(cs.decode(buffer).toString());
                    buffer.clear();
                }
            } catch (InterruptedException | AsynchronousCloseException e) {
                logger.info("Interruption of the listener");
            } catch (IOException e) {
                logger.severe("I/O issues");
            }
        });
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length != 5) {
            usage();
            return;
        }

        var inFilename = args[0];
        var outFilename = args[1];
        var timeout = Integer.parseInt(args[2]);
        var server = new InetSocketAddress(args[3], Integer.parseInt(args[4]));

        // Read all lines of inFilename opened in UTF-8
        var lines = Files.readAllLines(Path.of(inFilename), UTF8);
        var upperCaseLines = new ArrayList<String>();
        var queue = new ArrayBlockingQueue<String>(10);
        var buffer = ByteBuffer.allocate(BUFFER_SIZE);

        try (var dc = DatagramChannel.open()) {
            createListener(dc, queue, StandardCharsets.UTF_8).start();
            for(var line : lines) {
                buffer.clear();
                buffer.put(UTF8.encode(line));
                String message;
                do {
                    buffer.flip();
                    dc.send(buffer, server);
                    message = queue.poll(timeout, TimeUnit.MILLISECONDS);
                } while (message == null);
                upperCaseLines.add(message);
            }
        }

        // Write upperCaseLines to outFilename in UTF-8
        Files.write(Path.of(outFilename), upperCaseLines, UTF8, CREATE, WRITE, TRUNCATE_EXISTING);
    }
}

/* Ce système ne fonctionne pas car on ne peut pas garantir l'arrivé d'un paquet à un certain moment en UDP,
* Cela explique les duplications de lignes dans le fichier out.txt, un paquet peut être considérer comme drop out
* après 300 ms, puis être ajouter dans la queue, on se retrouve avec 2 paquets similaires dans la queue */
