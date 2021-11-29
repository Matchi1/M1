package fr.umlv.aggregator;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiFunction;

public class Aggregator {
	private final int numberOfThreads;
	private final BiFunction<Integer, Integer, Integer> operation;
	private volatile int result = 0;
	private final ReentrantLock lock = new ReentrantLock();
	private int counter = 0;
	 
	  // constructeur
	public Aggregator(int numberOfThreads, BiFunction<Integer, Integer, Integer> operation) {
		this.numberOfThreads = numberOfThreads;
		this.operation = operation;
	}
	  
	  public int aggregate(int value) {
		lock.lock();
		result = operation.apply(result, value);
		counter++;
		lock.unlock();
		for(;;) {
			Thread.onSpinWait();
			if(numberOfThreads == counter) {
				return result;
			}
		}
	  }
	  
	  public static void main(String[] args) throws InterruptedException {
	    var numberOfThreads = 5;
	    var aggregator = new Aggregator(numberOfThreads, Integer::sum);
	    var threads = new ArrayList<Thread>();
	    
	    for(var i = 0; i < numberOfThreads; i++) {
	    	var j = i;
	    	threads.add(new Thread(() -> {
	    		var value = aggregator.aggregate(j);
	    		
	    		System.out.println("Thread " + j + " " + value);
	    	}));
	    }
	    threads.forEach(thread -> thread.start());
	    for(var thread : threads) {
	    	thread.join();
	    }
	  }
	}