package fr.uge.net.udp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.DatagramChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static java.nio.file.StandardOpenOption.*;

public class ClientIdUpperCaseUDPOneByOne {

	private static final Logger logger = Logger.getLogger(ClientIdUpperCaseUDPOneByOne.class.getName());
	private static final Charset UTF8 = StandardCharsets.UTF_8;
	private static final int BUFFER_SIZE = 1024;

	private record Response(long id, String message) {
	}

	private final String inFilename;
	private final String outFilename;
	private final long timeout;
	private final InetSocketAddress server;
	private final DatagramChannel dc;
	private final SynchronousQueue<Response> queue = new SynchronousQueue<>();

	public static void usage() {
		System.out.println("Usage : ClientIdUpperCaseUDPOneByOne in-filename out-filename timeout host port ");
	}

	public ClientIdUpperCaseUDPOneByOne(String inFilename, String outFilename, long timeout, InetSocketAddress server)
			throws IOException {
		this.inFilename = Objects.requireNonNull(inFilename);
		this.outFilename = Objects.requireNonNull(outFilename);
		this.timeout = timeout;
		this.server = server;
		this.dc = DatagramChannel.open();
		dc.bind(null);
	}

	private void listenerThreadRun() {
		try {
			var buffer = ByteBuffer.allocate(BUFFER_SIZE);
			while (true) {
				dc.receive(buffer);
				buffer.flip();
				var id = buffer.getLong();
				var message = UTF8.decode(buffer).toString();
				queue.put(new Response(id, message));
				buffer.clear();
			}
		} catch (InterruptedException | AsynchronousCloseException e) {
			logger.info("Interruption of the thread");
		} catch (IOException e) {
			logger.severe("I/O issues");
		}
	}

	public void launch() throws IOException, InterruptedException {
		try {

			var listenerThread = new Thread(this::listenerThreadRun);
			listenerThread.start();

			// Read all lines of inFilename opened in UTF-8
			var lines = Files.readAllLines(Path.of(inFilename), UTF8);

			var upperCaseLines = new ArrayList<String>();
			var buffer = ByteBuffer.allocate(BUFFER_SIZE);
			var id = 0L;
			Response response;

			for(var line : lines) {
				buffer.clear();
				buffer.putLong(id);
				buffer.put(UTF8.encode(line));
				var lastSent = 0L;
				for(;;) {
					var currentTime = System.currentTimeMillis();
					if(currentTime - lastSent > timeout) {
						buffer.flip();
						dc.send(buffer, server);
						lastSent = currentTime;
					}
					var remainingTime = (lastSent + timeout) - currentTime;
					response = queue.poll(remainingTime, TimeUnit.MILLISECONDS);
					if(response == null) {
						lastSent = 0;
						continue;
					}
					if(response.id() == id) {
						upperCaseLines.add(response.message());
						break;
					}
				}
				id++;
			}

			listenerThread.interrupt();
			Files.write(Paths.get(outFilename), upperCaseLines, UTF8, CREATE, WRITE, TRUNCATE_EXISTING);
		} finally {
			dc.close();
		}
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		if (args.length != 5) {
			usage();
			return;
		}

		var inFilename = args[0];
		var outFilename = args[1];
		var timeout = Long.parseLong(args[2]);
		var server = new InetSocketAddress(args[3], Integer.parseInt(args[4]));

		// Create client with the parameters and launch it
		new ClientIdUpperCaseUDPOneByOne(inFilename, outFilename, timeout, server).launch();
	}
}
