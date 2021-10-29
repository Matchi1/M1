package fr.umlv.main;

import java.util.ArrayDeque;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class LockedBlockingBuffer <E> implements BlockingBuffer<E> {
	private final int capacity;
	private final ArrayDeque<E> buffer;
	private Condition condition;
	private final ReentrantLock lock = new ReentrantLock();
	
	public LockedBlockingBuffer(int capacity) {
		if(capacity < 1) {
			throw new IllegalArgumentException("Length have to be positive"); 
		}
		this.capacity = capacity;
		this.buffer = new ArrayDeque<E>(capacity);
		this.condition = lock.newCondition();
	}
	
	public void put(E element) throws InterruptedException {
		lock.lock();
		try {
			while(buffer.size() == capacity) {
				condition.await();
			}
			buffer.addFirst(element);
			condition.signalAll();
		} finally {
			lock.unlock();
		}
	}
	
	public E take() throws InterruptedException {
		lock.lock();
		try {
			while(buffer.size() == 0) {
				condition.await();
			}
			condition.signalAll();
			return buffer.removeLast();
		} finally {
			lock.unlock();
		}
	}
	
}
