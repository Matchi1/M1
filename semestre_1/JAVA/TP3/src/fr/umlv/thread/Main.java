package fr.umlv.thread;

import java.util.ArrayList;

public class Main {
	public static void main(String[] args) throws InterruptedException {
		var nbThreads = 4;
		var list = new ThreadSafeList<String>();
		var threads = new ArrayList<Thread>();
		for(var i = 0; i < nbThreads; i++) {
			var thread = new Thread(() -> {
				for(var k = 0; k < 10_000; k++) {
					list.add("heloo");
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
