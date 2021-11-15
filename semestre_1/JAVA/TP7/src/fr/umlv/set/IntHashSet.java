package fr.umlv.set;

import java.util.Objects;
import java.util.function.IntConsumer;

public class IntHashSet {
	private int size;
	private final Entry[] entries;
	
	private record Entry(int value, Entry next) {
	}
	
	public IntHashSet() {
		this.entries = new Entry[8];
	}
	
	public int hash(int value) {
		// return value % entries.length
		return value & (entries.length - 1);
	}
	
	public boolean contains(int value) {
		for(var browse = entries[hash(value)]; browse != null; browse = browse.next) {
			if(browse.value == value) {
				return true;
			}
		}
		return false;
	}
	
	public void add(int value) {
		if(!contains(value)) {
			var index = hash(value);
			var entry = new Entry(value, entries[index]);
			entries[index] = entry;
			size++;
		}
	}
	
	public int size() {
		return size;
	}
	
	public void forEach(IntConsumer consumer) {
		Objects.requireNonNull(consumer);
		for(var entry : entries) {
			for(var browse = entry; browse != null; browse = browse.next) {
				consumer.accept(browse.value);
			}
		}
 	}
}
