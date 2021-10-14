package fr.umlv.thread;

import java.util.ArrayList;

public class HelloThreadBug {
	public static void main(String[] args) throws InterruptedException {
		var nbThreads = 4;
		var list = new ThreadSafeList<Integer>();
		var threads = new ArrayList<Thread>();
		for(var i = 0; i < nbThreads; i++) {
			var thread = new Thread(() -> {
				for(var k = 0; k < 5000; k++) {
					list.add(k);
				}
			});
			threads.add(thread);
		}
		threads.forEach(Thread::start);
		for(var thread : threads) {
			thread.join();
		}
		System.out.println("array size : " + list.size());
	}
}
