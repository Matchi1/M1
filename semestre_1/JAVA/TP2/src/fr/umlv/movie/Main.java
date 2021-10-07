package fr.umlv.movie;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
	public static void main(String[] args) {
		var path = Path.of("movies.txt");
		try (var text = Files.lines(path)){
			System.out.println(text.count());
		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(1);
			return;
		}
	}

}
