package fr.umlv.election;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.VarHandle;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class LockFreeElection {
	private volatile Thread leader = null;
	private static final VarHandle REF;
	// private final AtomicReference<Thread> ref = new AtomicReference<>();
	static {
		var lookup = MethodHandles.lookup();
		try {
			REF = lookup.findVarHandle(LockFreeElection.class, "leader", Thread.class);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new AssertionError(e); 
		}
	}
	public Thread iWantToBeTheGloriousLeader() {
		/*
		for(;;) {
			if(ref.compareAndSet(null, leader)) {
				return leader;
			}
		}
		*/
		for(;;) {
			if(REF.compareAndSet(null, Thread.currentThread())) {
				return leader;
			}
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
