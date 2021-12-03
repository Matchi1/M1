package fr.umlv.seq;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.StringJoiner;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Seq2 <E> implements Iterable<E> {
	private final E[] elements;
	private final Function<? super Object, ? extends E> mapping;
	
	@SuppressWarnings("unchecked")
	private Seq2(List<Object> elements, Function<? super Object, ? extends E> mapping) {
		this.elements = (E[]) elements.toArray();
		this.mapping = mapping;
	}
	
	@SuppressWarnings("unchecked")
	private Seq2(List<? extends E> elements) {
		this(List.copyOf(elements), o -> (E)o);
	}
	
	public static <T> Seq2<T> from(List<? extends T> elements) {
		for(var element : elements) {
			Objects.requireNonNull(element);
		}
		return new Seq2<>(elements);
	}
	
	public Object get(int index) {
		Objects.checkIndex(index, elements.length);
		return mapping.apply(elements[index]);
	}
	
	public int size() {
		return elements.length;
	}
	
	@SafeVarargs
	public static <T> Seq2<T> of(T... elements) {
		for(var element : elements) {
			Objects.requireNonNull(element);
		}
		var temporary = Arrays.asList(elements);
		return new Seq2<>(temporary);
	}
	
	public void forEach(Consumer<? super E> consumer) {
		Objects.requireNonNull(consumer);
		List.of(elements).forEach(element -> consumer.accept(mapping.apply(element)));
	}
	
	public <T> Seq2<T> map(Function<? super E, ? extends T> function) {
		return new Seq2<>(List.of(elements), function.compose(mapping));
	}
	
	public Optional<E> findFirst() {
		return List.of(elements).stream().<E>map(mapping).findFirst();
	}
	
	@Override
	public Iterator<E> iterator() {
		return new Iterator<E>() {
			private int i = 0;

			@Override
			public boolean hasNext() {
				return i < size();
			}

			@Override
			public E next() {
				if(!hasNext()) {
					throw new NoSuchElementException("No more available element");
				}
				var element = mapping.apply(elements[i]);
				i++;
				return element;
			}
		};
	}
	
	public Spliterator<E> spliterator() {
		return Spliterators.spliterator(List.of(elements).stream().map(mapping).toArray(), 
				Spliterator.IMMUTABLE | Spliterator.NONNULL | Spliterator.ORDERED);
	}
	
	public Stream<E> stream() {
		return StreamSupport.stream(this::spliterator, 
				Spliterator.IMMUTABLE | Spliterator.NONNULL | Spliterator.ORDERED,
				false);
	}
	
	@Override
	public String toString() {
		var joiner = new StringJoiner(", ", "<", ">");
		for(var i = 0; i < elements.length; i++) {
			joiner.add(mapping.apply(elements[i]).toString());
		}
		return joiner.toString();
	}
}
