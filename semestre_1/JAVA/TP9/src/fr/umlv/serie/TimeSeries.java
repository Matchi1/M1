package fr.umlv.serie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
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
		
		private TimeSeries<E> getTimeSeriesInstance() {
			return getTimeSeries();
		}

		public Index or(Index index) {
			Objects.requireNonNull(index);
			index.
			if(this.getTimeSeriesInstance() != index.getTimeSeriesInstance()) {
				throw new IllegalArgumentException("Indexes should be created from the same timeseries");
			}
			var array = IntStream.concat(Arrays.stream(indexes), Arrays.stream(index.indexes))
					.sorted()
					.distinct()
					.toArray();
			return new Index(array);
		}
		
		public Index and(Index index) {
			Objects.requireNonNull(index);
			if(this.getTimeSeriesInstance() != index.getTimeSeriesInstance()) {
				throw new IllegalArgumentException("Indexes should be created from the same timeseries");
			}
			var set = new HashSet<>();
			Arrays.stream(indexes).forEach(set::add);
			var array = Arrays.stream(index.indexes).filter(i -> set.contains(i)).toArray();
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
		return index(value -> true);
	} 
	
	private TimeSeries<E> getTimeSeries() {
		return this;
	}
	
	public Index index(Predicate<? super E> predicate) {
		var array = IntStream.range(0,  list.size())
				.filter(i -> predicate.test(list.get(i).element))
				.toArray();
		return new Index(array);
	}

}
