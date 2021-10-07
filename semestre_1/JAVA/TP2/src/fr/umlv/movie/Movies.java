package fr.umlv.movie;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Movies {

    public static List<Movie> movies(Path path) throws IOException{
    	var lines = Files.lines(path);
        var movies = lines.map(line -> {
            var tokens = line.split(";");
            var movie = new Movie(tokens[0], Arrays.stream(tokens).skip(1).toList());
            return movie;
        }).toList();
        lines.close();
        return movies;
    }
    
    public static Map<String, Movie> movieMap(List<Movie> movies) {
    	return movies.stream()
    			.collect(Collectors.toUnmodifiableMap(Movie::title, Function.identity()));
    }
    
    public static long numberOfUniqueActors(List<Movie> movies) {
    	return movies.stream()
    			.flatMap(movie -> movie.actors().stream())
    			.distinct()
    			.count();
    }
    
    public static Map<String, Long> numberOfMoviesByActor(List<Movie> movies) {
    	return movies.stream()
    			.flatMap(movie -> movie.actors().stream())
    			.collect(Collectors.groupingBy(actor -> actor, Collectors.counting()));
    }
    
    public static Optional<ActorMovieCount> actorInMostMovies(Map<String, Long> actors) {
    	return actors.entrySet().stream()
                .map(entry -> new ActorMovieCount(entry.getKey(), entry.getValue()))
    			.collect(Collectors.maxBy((a, b) -> {
                    if(a.movieCount() > b.movieCount()) {
                        return 1;
                    }
                    return -1;
                }));
    }
}
 