package fr.umlv.thread;

import java.util.ArrayList;

public class HelloThreadJoin {
    public static void main(String[] args) throws InterruptedException {
    	var list = new ArrayList<Thread>();
		for(var i = 0; i < 4; i++) {
			var j = i;
			var thread = new Thread(() -> {
				for(var k = 0; k < 5000; k++) {
					System.out.println("hello " + j + " " + k);
				}
			});
			list.add(thread);
		}
		list.forEach(Thread::start);
		for(var thread : list) {
			thread.join();
		}
		System.out.println("Le programme est fini");
    }
}
