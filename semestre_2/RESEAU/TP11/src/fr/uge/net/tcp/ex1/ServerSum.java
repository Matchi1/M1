package fr.uge.net.tcp.ex1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.logging.Logger;

public class ServerSum {

	private static final int BUFFER_SIZE = 2 * Integer.BYTES;
	private static final Logger logger = Logger.getLogger(ServerSum.class.getName());

	private final ServerSocketChannel serverSocketChannel;
	private final Selector selector;

	public ServerSum(int port) throws IOException {
		serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.bind(new InetSocketAddress(port));
		selector = Selector.open();
	}

	public void launch() throws IOException {
		serverSocketChannel.configureBlocking(false);
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT, ByteBuffer.allocate(BUFFER_SIZE));
		while (!Thread.interrupted()) {
			Helpers.printKeys(selector); // for debug
			System.out.println("Starting select");
			selector.select(this::treatKey);
			System.out.println("Select finished");
		}
	}

	private void treatKey(SelectionKey key) {
		Helpers.printSelectedKey(key); // for debug
		try {
			if (key.isValid() && key.isAcceptable()) {
				doAccept(key);
			}
		} catch (IOException ioe) {
			logger.severe("Server is having issues");
		}
		try {
			if (key.isValid() && key.isWritable()) {
				doWrite(key);
			}
			if (key.isValid() && key.isReadable()) {
				doRead(key);
			}
		} catch (IOException ioe) {
			logger.warning("Client is having issues");
		}
	}

	private void doAccept(SelectionKey key) throws IOException {
		logger.info("I am accepting the client");
		// only the ServerSocketChannel is registered in OP_ACCEPT
		var ssc = (ServerSocketChannel) key.channel();
		var sc = ssc.accept();
		if (sc == null) {
			return; // the selector gave a bad hint
		}
		sc.configureBlocking(false);
		sc.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(BUFFER_SIZE));
	}

	private void doRead(SelectionKey key) throws IOException {
		logger.info("now listening");
		var sc = (SocketChannel) key.channel();
		var buffer = (ByteBuffer) key.attachment();
		if(sc.read(buffer) == -1) {
			silentlyClose(key);
			return;
		}
		if(buffer.hasRemaining()) {
			return;
		}
		buffer.flip();
		var sum = buffer.getInt() + buffer.getInt();
		buffer.clear();
		buffer.putInt(sum);
		key.interestOps(SelectionKey.OP_WRITE);
	}

	private void doWrite(SelectionKey key) throws IOException {
		logger.info("now writing");
		var sc = (SocketChannel) key.channel();
		var buffer = (ByteBuffer) key.attachment();
		buffer.flip();
		sc.write(buffer);
		if(buffer.hasRemaining()) {
			buffer.compact();
			return;
		}
		buffer.clear();
		key.interestOps(SelectionKey.OP_READ);
	}

	private void silentlyClose(SelectionKey key) {
		var sc = (Channel) key.channel();
		try {
			sc.close();
		} catch (IOException e) {
			// ignore exception
		}
	}

	public static void main(String[] args) throws NumberFormatException, IOException {
		if (args.length != 1) {
			usage();
			return;
		}
		new ServerSum(Integer.parseInt(args[0])).launch();
	}

	private static void usage() {
		System.out.println("Usage : ServerSumOneShot port");
	}
}