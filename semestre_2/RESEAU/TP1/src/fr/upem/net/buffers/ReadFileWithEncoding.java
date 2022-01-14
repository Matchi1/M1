package fr.upem.net.buffers;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.Scanner;

import static java.nio.file.StandardOpenOption.*;

public class ReadFileWithEncoding {

	private static void usage() {
		System.out.println("Usage: ReadFileWithEncoding charset filename");
	}

	private static String stringFromFile(Charset cs, Path path) throws IOException {
		String message;
		try (var inChannel = FileChannel.open(path, READ)) {
			var size = Long.valueOf(inChannel.size()).intValue();
			var bb = ByteBuffer.allocate(size);
			while(bb.hasRemaining()) {
				inChannel.read(bb);
			}
			bb.flip();
			message = cs.decode(bb).toString();
		}
		return message;

	}

	public static void main(String[] args) throws IOException {
		if (args.length != 2) {
			usage();
			return;
		}
		var cs = Charset.forName(args[0]);
		var path = Path.of(args[1]);
		System.out.print(stringFromFile(cs, path));
	}
}
