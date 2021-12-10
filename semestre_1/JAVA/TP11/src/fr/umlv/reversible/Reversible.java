package fr.umlv.reversible;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface Reversible <T> extends Iterable<T> {
	public int size();
	
	public T get(int index);
	
	@SafeVarargs
	public static <E> Reversible<E> fromArray(E... elements) {
		Objects.requireNonNull(elements);
		Arrays.stream(elements).forEach(Objects::requireNonNull);
		return fromList(Arrays.asList(elements));
	}
	
	public static <E> Reversible<E> fromList(List<? extends E> list) {
		Objects.requireNonNull(list);
		list.forEach(Objects::requireNonNull);
		var size = list.size();
		return new Reversible<>() {
			@Override
			public int size() {
				return size;
			}

			@Override
			public E get(int index) {
				Objects.checkIndex(index, size);
				if(list.size() < size) {
					throw new IllegalStateException("elements has been removed");
				}
				var i = index < size ? index : size - 1;
				return Objects.requireNonNull(list.get(i));
			}

			@Override
			public Stream<? extends E> stream() {
				return StreamSupport.stream(spliterator(), false);
			}
		};
	}
	
	public default Iterator<T> iterator() {
		return new Iterator<T>() {
			private int i;
			@Override
			public boolean hasNext() {
				return i < size();
			}

			@Override
			public T next() {
				if(!hasNext()) {
					throw new NoSuchElementException("No more available element");
				}
				try {
					return get(i++);
				} catch(IllegalStateException e) {
					throw new ConcurrentModificationException();
				}
			}
		};
	}
	
	public default Reversible<T> reversed() {
		var current = this;
		return new Reversible<T>() {
			@Override
			public int size() {
				return current.size();
			}

			@Override
			public T get(int index) {
				return current.get(current.size() - 1 - index);
			}

			@Override
			public Reversible<T> reversed() {
				return current;
			}
			
			@Override
			public Stream<? extends T> stream() {
				return StreamSupport.stream(this.spliterator(), false);
			}
		};
	}
	
	public default Spliterator<T> spliterator() {
		return new ReversibleSpliterator<>(0, size(), this);
	}
	
	public Stream<? extends T> stream();
	
	static class ReversibleSpliterator <T> implements Spliterator<T> {
		private int index;
		private final int end;
		private final Reversible<T> reversible;
		
		public ReversibleSpliterator(int start, int end, Reversible<T> reversible) {
			this.index = start;
			this.end = end;
			this.reversible = reversible;
		}
		
		@Override
		public boolean tryAdvance(Consumer<? super T> action) {
			Objects.requireNonNull(action);
			if(index == end) {
				return false;
			}
			try {
				action.accept(reversible.get(index++));
			} catch (IllegalStateException e) {
				throw new ConcurrentModificationException();
			}
			
			return true;
		}

		@Override
		public Spliterator<T> trySplit() {
			return null;
		}

		@Override
		public long estimateSize() {
			return end - index;
		}

		@Override
		public int characteristics() {
			return SIZED | ORDERED | NONNULL;
		}
	}
}
