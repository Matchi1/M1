package fr.umlv.conc;

import java.util.ArrayList;
import java.util.Scanner;

public class InterruptNonBlocking {
	  private static int slow() {
		  var result = 1;
		  for (var i = 0; i < 1_000_000; i++) {
			  result += (result * 7) % 513;
		  }
		  return result;
	  }
	  
	  private static int slow36() {
		  var result = 1;
		  for (var i = 0; i < 1_000_000; i++) {
			  result += (result * 7) % 513;
		  }
		  return result;
	  }
	  
	  public static void ex31() throws InterruptedException {
		  var thread = new Thread(() -> {
			  var forNothing = 0;
			  while (true) {
				  forNothing += slow();
				  if(Thread.interrupted()) {
					  return;
				  }
			  }
		  });
		  thread.start();
		  Thread.sleep(1_000);
		  thread.interrupt();
		  System.out.println("end");
	  }

	  public static void ex35() throws InterruptedException {
		  var thread = new Thread(() -> {
			  var forNothing = 0;
			  while(!Thread.interrupted()) {
				  forNothing += slow();
				  try {
					  Thread.sleep(1_000); 
				  } catch (InterruptedException e) {
					  Thread.currentThread().interrupt();
					  continue;
				  }
				  forNothing += slow(); 
			  }
		  });
		  thread.start();
		  Thread.sleep(1_000);
		  thread.interrupt();
		  System.out.println("end");
	  }
	  
	  public static void ex38() {
		  var nbThreads = 4;
		  var threads = new ArrayList<Thread>();
		  for(var i = 0; i < nbThreads; i++) {
			  threads.add(new Thread(() -> {
				  var counter = 0;
				  while(!Thread.interrupted()) {	 
					  System.out.println("id : " + Thread.currentThread().getId());
					  System.out.println("counter : " + counter);
					  try {
						  Thread.sleep(5_000);
					  } catch (InterruptedException e) {
						  Thread.currentThread().interrupt();
						  continue;
					  }
					  counter++;
				  }
			  }));
		  }
		  for(var thread : threads) {
			  thread.start();
		  }
		  System.out.println("enter a thread id:");
		  try(var scanner = new Scanner(System.in)) {
			  while(scanner.hasNextInt()) {
				  var threadId = scanner.nextInt();
				  for(var thread : threads) {
					  if(thread.getId() == threadId) {
						  thread.interrupt();
					  }
				  }
			  }
		  }
	  }

	  public static void main(String[] args) throws InterruptedException {
		  ex38();
	  }
}
