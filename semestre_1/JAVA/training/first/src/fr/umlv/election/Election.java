package fr.umlv.election;

import java.util.ArrayList;

public class Election {
	private Thread leader = null;
	private Object monitor = new Object();
	
  public Thread iWantToBeTheGloriousLeader() {
	  synchronized (monitor) {
		  if(leader == null) {
			  leader = Thread.currentThread();
		  }
		  return leader;
	  }
  }
  
  public static void main(String[] args) throws InterruptedException {
    var numberOfThreads = 4;
    var sleepTime = 1_000;
    var threads = new ArrayList<Thread>();
    var election = new Election();
    for(var i = 0; i < numberOfThreads; i++) {
    	threads.add(new Thread(() -> {
    		try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				return;
			}
    		System.out.println("current: " + Thread.currentThread().getName() + " leader: " + election.iWantToBeTheGloriousLeader().getName());
    	}));
    }
    threads.forEach(thread -> thread.start());
    for(var thread : threads) {
    	thread.join();
    }
    System.out.println("end !");
  }
}