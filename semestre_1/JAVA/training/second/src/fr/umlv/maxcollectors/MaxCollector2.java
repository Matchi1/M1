package fr.umlv.maxcollectors;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MaxCollector2 {
	private final PriorityQueue<Integer> maxes = new PriorityQueue<>();
	  private final Object monitor = new Object();
	  private final ReentrantLock lock = new ReentrantLock();
	  private final Condition condition = lock.newCondition();
	  private volatile int numberOfThread;
	  private long lastCalled;
	  
	  public MaxCollector2(int numberOfThread) {
		this.numberOfThread = numberOfThread;
	}

	public void proposeValue(int value) throws InterruptedException {
		  lock.lock();
		  while(lastCalled == Thread.currentThread().getId() && numberOfThread != 1) {
			  condition.await();
			  Thread.onSpinWait();
		  }
		  maxes.offer(value);
		    if (maxes.size() > 10) {
		      maxes.poll();
		    }
		    condition.signalAll();
		    lastCalled = Thread.currentThread().getId();
		lock.unlock();
	  }
	  
	  public List<Integer> winners() {
		  var list = new ArrayList<>(maxes);
		  list.sort(null);
		  return list;
	  }
	  
	  public void end() {
		  numberOfThread--;
	  }
	  
	  public static void main(String[] args) throws InterruptedException {
		  var numberOfThread = 4;
			var threads = new ArrayList<Thread>();
			var maxCollector = new MaxCollector2(numberOfThread);
			for(var i = 0; i < numberOfThread; i++) {
				var j = i;
				threads.add(new Thread(() -> {
					var random = new Random(j);
					random.ints(10_000, 0, 1_000_000).forEach(value -> {
						try {
							maxCollector.proposeValue(value);
						} catch (InterruptedException e) {
							return;
						}
					});
					maxCollector.end();
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
