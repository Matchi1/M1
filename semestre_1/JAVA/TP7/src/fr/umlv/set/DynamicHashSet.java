package fr.umlv.set;

import java.util.Objects;
import java.util.function.Consumer;

public class DynamicHashSet <E> {
	private int size;
	private Entry<E>[] entries;
	
	private record Entry<T>(T value, Entry<T> next) {
	}
	
	@SuppressWarnings("unchecked")
	public DynamicHashSet() {
		this.entries = (Entry<E>[]) new Entry<?>[8];
	}
	
	public int hash(E value) {
		Objects.requireNonNull(value);
		// return value % entries.length
		return value.hashCode() & (entries.length - 1);
	}
	
	public boolean contains(E value) {
		Objects.requireNonNull(value);
		for(var browse = entries[hash(value)]; browse != null; browse = browse.next) {
			if(value.equals(browse.value)) {
				return true;
			}
		}
		return false;
	}
	
	public void add(E value) {
		Objects.requireNonNull(value);
		var index = hash(value);
		for(var browse = entries[index]; browse != null; browse = browse.next) {
			if(value.equals(browse.value)) {
				return;
			}
		}
		entries[index] = new Entry(value, entries[index]);
		size++;
	}
	
	public int size() {
		return size;
	}
	
	public void forEach(Consumer<? super E> consumer) {
		Objects.requireNonNull(consumer);
		for(var entry : entries) {
			for(var browse = entry; browse != null; browse = browse.next) {
				consumer.accept(browse.value);
			}
		}
 	}
}
