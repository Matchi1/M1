package fr.umlv.thread;

import java.util.ArrayList;

public class ThreadSafeList <T> {
	private final ArrayList<T> list = new ArrayList<>();
	private final Object monitor = new Object();
	
	public void add(T object) {
		synchronized (monitor) {
			list.add(object);
		}
	}
	
	public int size() {
		synchronized (monitor) {
			return list.size();
		}
	}
}
