package fr.umlv.reversible;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public abstract class Reversible2<T> extends AbstractList<T> implements Iterable<T>, List<T> {
	public int size() {
		return 0;
	}
	
	public T get(int index) {
		return null;
	}
	
	@SafeVarargs
	public static <E> Reversible2<E> fromArray(E... elements) {
		Objects.requireNonNull(elements);
		Arrays.stream(elements).forEach(Objects::requireNonNull);
		return fromList(Arrays.asList(elements));
	}
	
	public static <E> Reversible2<E> fromList(List<? extends E> list) {
		Objects.requireNonNull(list);
		list.forEach(Objects::requireNonNull);
		if(!(list instanceof RandomAccess)) {
			throw new IllegalArgumentException("sequential list are not permitted");
		}
		var size = list.size();
		return new Reversible2<E>() {
			@SuppressWarnings("unchecked")
			private final List<E> current = (List<E>)list;
			
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
			public Stream<E> stream() {
				// return StreamSupport.stream(spliterator(), false);
				return StreamSupport.stream(spliterator(), true);
			}
			
			@Override
			public E set(int index, E element) {
				Objects.checkIndex(index, size());
				Objects.requireNonNull(element);
				return current.set(index, element);
			}
			
			@Override
			public void replaceAll(UnaryOperator<E> operator) {
				Objects.requireNonNull(operator);
				current.replaceAll(operator);
			}
			
			@Override
			public void sort(Comparator<? super E> comparator) {
				current.sort(comparator);
			}
			
			@Override
			public ListIterator<E> listIterator(int index) {
				Objects.checkIndex(index, size());
				return current.listIterator();
			}
			
			@Override
			public void clear() {
				throw new UnsupportedOperationException("reversible cannot be cleared");
			}
		};
	}

	public Iterator<T> iterator() {
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
	
	public Reversible2<T> reversed() {
		var current = this;
		return new Reversible2<T>() {
			@Override
			public int size() {
				return current.size();
			}

			@Override
			public T get(int index) {
				return current.get(current.size() - 1 - index);
			}

			@Override
			public Reversible2<T> reversed() {
				return current;
			}
			
			@Override
			public Stream<T> stream() {
				// return StreamSupport.stream(this.spliterator(), false);
				return StreamSupport.stream(this.spliterator(), true);
			}
			
			@Override
			public T set(int index, T element) {
				Objects.checkIndex(index, size());
				Objects.requireNonNull(element);
				return current.set(current.size() - 1 - index, element);
			}
			
			@Override
			public void replaceAll(UnaryOperator<T> operator) {
				Objects.requireNonNull(operator);
				current.replaceAll(operator);
			}
			
			@Override
			public void sort(Comparator<? super T> comparator) {
				if(comparator == null) {
					current.sort(Collections.reverseOrder());
				} else {
					current.sort(comparator.reversed());
				}
			}
			
			@Override
			public ListIterator<T> listIterator(int index) {
				Objects.checkIndex(index, size());
				return current.listIterator();
			}
			
			@Override
			public void clear() {
				throw new UnsupportedOperationException("reversible cannot be cleared");
			}
		};
	}

	private static <E> Spliterator<E> fromReversible(int start, int end, Reversible2<E> reversible) {
		return new Spliterator<E>() {
			private int index = start;
			
			@Override
			public Spliterator<E> trySplit() {
				var middle = (end + index) >>> 1;
				if(middle == index) {
					return null;
				}
				var spliterator = fromReversible(index, middle, reversible);
				index = middle;
				return spliterator; 
			}
	
			@Override
			public int characteristics() {
				return SIZED | ORDERED | NONNULL;
			}
			
			@Override
			public long estimateSize() {
				return end - index;
			}

			@Override
			public boolean tryAdvance(Consumer<? super E> action) {
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
		};
	}
	
	public Spliterator<T> spliterator() {
		return fromReversible(0, size(), this);
	}
	
	public Stream<T> stream() {
		// return StreamSupport.stream(spliterator(), false);
		return StreamSupport.stream(spliterator(), true);
	}
}
