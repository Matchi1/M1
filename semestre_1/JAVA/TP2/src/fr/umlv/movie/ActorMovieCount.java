package fr.umlv.movie;

import java.util.Objects;

public record ActorMovieCount(String actor, long movieCount) {
   public ActorMovieCount {
       Objects.requireNonNull(actor);
       if(movieCount < 0) {
            throw new IllegalArgumentException("Number of movie have to be above 0");
       }
   }
}
