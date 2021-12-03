package fr.umlv.seq;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Seq <E> implements Iterable<E> {
	private final List<Object> liste;
	private final Function<? super Object, ? extends E> mapping;
	
	private Seq(List<Object> elements, Function<? super Object, ? extends E> mapping) {
		this.liste = elements;
		this.mapping = mapping;
	}

	@SuppressWarnings("unchecked")
	private Seq(List<? extends E> elements) {
		this(List.copyOf(elements), o -> (E)o);
	}
	
	public static <T> Seq<T> from(List<? extends T> elements) {
		return new Seq<>(elements);
	}
	
	public Object get(int index) {
		return mapping.apply(liste.get(index));
	}
	
	public int size() {
		return liste.size();
	}
	
	@SafeVarargs
	public static <T> Seq<T> of(T... elements) {
		var temporary = Arrays.asList(elements);
		return new Seq<>(temporary);
	}
	
	public void forEach(Consumer<? super E> consumer) {
		Objects.requireNonNull(consumer);
		liste.forEach(element -> consumer.accept(mapping.apply(element)));
	}
	
	public <T> Seq<T> map(Function<? super E, ? extends T> function) {
		return new Seq<>(liste, function.compose(mapping));
	}
	
	public Optional<E> findFirst() {
		return liste.stream().<E>map(mapping).findFirst();
	}
	
	@Override
	public String toString() {
		var joiner = new StringJoiner(", ", "<", ">");
		liste.forEach(element -> joiner.add(mapping.apply(element).toString()));
		return joiner.toString();
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
				var element = mapping.apply(liste.get(i));
				i++;
				return element;
			}
		};
	}
	
	public Stream<E> stream() {
		return StreamSupport.stream(liste.spliterator(), false).map(mapping);
	}
}
