package fr.umlv.thread;

import java.util.ArrayList;

public class HelloThread {
	public static void main(String[] args) throws InterruptedException {
		var list = new ArrayList<Thread>();
		for(var i = 0; i < 4; i++) {
    		var k = i;
    		var thread = new Thread(() -> {
                for(var j = 0; j < 5000; j++) {
                    System.out.println("hello " + k + " " + j);
                }
            });
    		list.add(thread);
    	}
		list.forEach(Thread::start);
	}
}
