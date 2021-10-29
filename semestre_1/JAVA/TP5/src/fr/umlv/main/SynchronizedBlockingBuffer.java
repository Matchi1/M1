package fr.umlv.main;

import java.util.ArrayDeque;

public class SynchronizedBlockingBuffer <E> implements BlockingBuffer<E> {
	private final int capacity;
	private final ArrayDeque<E> buffer;
	
	public SynchronizedBlockingBuffer(int capacity) {
		if(capacity < 0) {
			throw new IllegalArgumentException("Length have to be positive"); 
		}
		this.capacity = capacity;
		this.buffer = new ArrayDeque<E>(capacity);
	}
	
	public void put(E element) throws InterruptedException {
		synchronized (buffer) {
			while(buffer.size() == capacity) {
				buffer.wait();
			}
			buffer.addFirst(element);
			buffer.notifyAll();
		}
	}
	
	public E take() throws InterruptedException {
		synchronized (buffer) {
			while(buffer.size() == 0) {
				buffer.wait();
			}
			buffer.notifyAll();
			return buffer.removeLast();
		}
	}
}
