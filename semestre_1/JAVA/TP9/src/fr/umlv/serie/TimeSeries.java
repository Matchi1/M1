package fr.umlv.serie;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TimeSeries <E> {
	private final ArrayList<Data<E>> list = new ArrayList<>();
	
	public record Data<F>(long timestamp, F element) {
		public Data {
			Objects.requireNonNull(element);
		}
		
		@Override
		public String toString() {
			var builder = new StringBuilder();
			builder.append(timestamp);
			builder.append(" | ");
			builder.append(element);
			return builder.toString();

		}
	}
	
	public class Index implements Iterable<Data<E>> {
		private final int[] indexes;
		
		private Index(int[] indexes) {
			this.indexes = indexes;
		}
		
		public int size() {
			return indexes.length;
		}
		
		public void forEach(Consumer<? super Data<E>> consumer) {
			Arrays.stream(indexes)
					.mapToObj(list::get)
					.forEach(data -> consumer.accept(data));
		}
		
		@Override
		public String toString() {
			return Arrays.stream(indexes)
					.mapToObj(list::get)
					.map(Data::toString)
					.collect(Collectors.joining("\n"));
		}

		@Override
		public Iterator<Data<E>> iterator() {
			return new Iterator<Data<E>>() {
				private int i;
				@Override
				public boolean hasNext() {
					return i < indexes.length;
				}

				@Override
				public Data<E> next() {
					if(!hasNext()) {
						throw new NoSuchElementException("No available element");
					}
					return list.get(indexes[i++]);
				}
			};
		}
		
		public Index or(Index index) {
			var array = IntStream.concat(Arrays.stream(indexes), Arrays.stream(index.indexes));
			return new Index(array);
		}
	}
	
	public void add(long timestamp, E element) {
		if(!list.isEmpty() && list.get(list.size() - 1).timestamp > timestamp) {
			throw new IllegalStateException("Current timestamp should be superior to last timestamp");
		}
		list.add(new Data<>(timestamp, element));
	}
	
	public int size() {
		return list.size();
	}
	
	public Data<E> get(int index) {
		return list.get(index);
	}
	
	public Index index() {
		var indexes = IntStream.range(0, list.size()).toArray();
		return new Index(indexes);
	} 
	
	public Index index(Predicate<? super E> predicate) {
		var array = IntStream.range(0,  list.size())
				.filter(i -> predicate.test(list.get(i).element))
				.toArray();
		return new Index(array);
	}

}
