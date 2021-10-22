package fr.umlv.conc;

import java.util.concurrent.locks.ReentrantLock;

public class HonorBoard {
  private String firstName;
  private String lastName;
  // private final Object monitor = new Object();
  private final ReentrantLock lock = new ReentrantLock();
  
  public void set(String firstName, String lastName) {
	  /*
	  synchronized(monitor) {
		  this.firstName = firstName;
		  this.lastName = lastName;
	  }
	  */
	  lock.lock();
	  try {
		  this.firstName = firstName;
		  this.lastName = lastName;
	  } finally {
		  lock.unlock();
	  }
  }
  
  @Override
  public String toString() {
	  /*
	  synchronized (monitor) {
		  return firstName + ' ' + lastName;
	  }
	  */
	  lock.lock();
	  try {
		  return firstName + ' ' + lastName;
	  } finally {
		  lock.unlock();
	  }
  }
  
  public static void main(String[] args) {
    var board = new HonorBoard();
    new Thread(() -> {
      for(;;) {
        board.set("John", "Doe");
      }
    }).start();
    
    new Thread(() -> {
      for(;;) {
        board.set("Jane", "Odd");
      }
    }).start();
    
    new Thread(() -> {
      for(;;) {
        System.out.println(board);
      }
    }).start();
  }
}
