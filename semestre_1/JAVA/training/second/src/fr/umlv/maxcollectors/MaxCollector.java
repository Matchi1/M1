package fr.umlv.maxcollectors;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

public class MaxCollector {
	  private final PriorityQueue<Integer> maxes = new PriorityQueue<>();
	  private final Object monitor = new Object();
	  
	  public void proposeValue(int value) {
		  synchronized (monitor) {
			  maxes.offer(value);
			    if (maxes.size() > 10) {
			      maxes.poll();
			    }
		}
	  }
	  
	  public List<Integer> winners() {
		  var list = new ArrayList<>(maxes);
		  list.sort(null);
		  return list;
	  }
	  
	  public static void main(String[] args) throws InterruptedException {
		  var numberOfThread = 4;
			var threads = new ArrayList<Thread>();
			var maxCollector = new MaxCollector();
			for(var i = 0; i < numberOfThread; i++) {
				threads.add(new Thread(() -> {
					var random = new Random(Thread.currentThread().getId());
					random.ints(10_000, 0, 1_000_000).forEach(maxCollector::proposeValue);
				}));
			}
			for(var thread : threads) {
				thread.start();
			}
			for(var thread : threads) {
				thread.join();
			}
			System.out.println(maxCollector.winners());		
	}
}