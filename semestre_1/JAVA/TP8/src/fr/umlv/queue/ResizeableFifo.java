package fr.umlv.queue;

import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.StringJoiner;

public class ResizeableFifo <E> extends AbstractQueue<E> implements Iterable<E> {
	private int size = 0;
	private E[] elements;
	private int tail = 0;
	private int head = 0;
	
	public ResizeableFifo(int capacity) {
		if(capacity < 1) {
			throw new IllegalArgumentException("Capacity should be more than 0");
		}
		@SuppressWarnings("unchecked")
		E[] elements = (E[]) new Object[capacity];
		this.elements = elements;
	}
	
	private void grow() {
		@SuppressWarnings("unchecked")
		var newElements = (E[]) new Object[elements.length * 2];
		System.arraycopy(elements, head, newElements, 0, size - head);
		System.arraycopy(elements, 0, newElements, size - head, tail);
		head = 0;
		tail = size;
		this.elements = newElements;
	}
	
	public boolean offer(E value) {
		Objects.requireNonNull(value);
		if(size == elements.length) {
			grow();
		}
		elements[tail] = value;
		tail = (tail + 1) % elements.length;
		size++;
		return true;
	}
	
	public E poll() {
		if(size == 0) {
			throw new IllegalStateException("Queue is empty");
		}
		var element = elements[head];
		elements[head] = null;
		head = (head + 1) % elements.length;
		size--;
		return element;
	}
	
	@Override
	public String toString() {
		var messages = new StringJoiner(", ", "[", "]");
		if(this.size == 0) {
			return messages.toString();
		}
		var j = head;
		for(var i = 0; i < size; i++) {
			var element = elements[j];
			j = (j + 1) % elements.length;
			messages.add(element.toString());
		}
		return messages.toString();
	}
	
	public int size() {
		return this.size;
	}
	
	public boolean isEmpty() {
		return this.size == 0;
	}
	
	public Iterator<E> iterator() {
		return new Iterator<>() {
			private int j = head;
			private int i = 0;
			
			@Override
			public boolean hasNext() {
				return i < size;
			}

			@Override
			public E next() {
				if(!hasNext()) {
					throw new NoSuchElementException("Iterator has no more element");
				}
				var element = elements[j];
				j = (j + 1) % elements.length;
				i++;
				return element;
			}
		};
	}

	@Override
	public E peek() {
		return elements[head];
	}
}
