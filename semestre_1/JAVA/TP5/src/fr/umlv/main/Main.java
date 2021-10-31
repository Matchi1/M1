package fr.umlv.main;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
	public static void ex1_3() {
		/*
		var thread = new Thread(() -> {
			while(true) {
				System.out.println("hello");
				try {
					Thread.currentThread().sleep(1_000);
				} catch (InterruptedException e) {
					System.out.println(e);
				}
			}
		});
		thread.start();
		*/
		
		var delays = new ArrayList<Integer>();
		var threads = new ArrayList<Thread>();
		delays.add(1_000);
		delays.add(4_000);
		for (var delay : delays) {
			threads.add(new Thread(() -> {
				while(true) {
					System.out.println("hello " + Thread.currentThread().getId());
					try {
						Thread.currentThread().sleep(delay);
					} catch (InterruptedException e) {
						System.out.println(e);
					}
				}
			}));
		}
		for(var thread : threads) {
			thread.start();
		}
	}

	public static Runnable createConsumer(long id, BlockingQueue<String> queue, long delay){
		return () -> {
			try {
				while(true) {
					var message = queue.take();
					System.out.println(message + " " + id);
					Thread.currentThread().sleep(delay);
				}
			} catch (InterruptedException e) {
				return;
			}
		};
	}
	
	public static Runnable createProducer(long id, BlockingQueue<String> queue, long delay) {
		return () -> {
			try {
				while(true) {
					Thread.currentThread().sleep(delay);
					queue.put("hello " + id);
					System.out.println("produced " + id);
				}
			} catch (InterruptedException e) {
				return;
			}
		};
	}
	
	public static Runnable createConsumerSync(long id, BlockingBuffer<String> queue, long delay){
		return () -> {
			try {
				while(true) {
					var message = queue.take();
					System.out.println(message + " " + id);
					Thread.currentThread().sleep(delay);
				}
			} catch (InterruptedException e) {
				return;
			}
		};
	}
	
	public static Runnable createProducerSync(long id, BlockingBuffer<String> queue, long delay) {
		return () -> {
			try {
				while(true) {
					Thread.currentThread().sleep(delay);
					queue.put("hello " + id);
					System.out.println("produced " + id);
				}
			} catch (InterruptedException e) {
				return;
			}
		};
	}
	
	public static void ex1_4() {
		var queue = new ArrayBlockingQueue<String>(10);
		var producer = new Thread(createProducer(Thread.currentThread().getId(), queue, 1_000));
		var consumer = new Thread(createConsumer(Thread.currentThread().getId(), queue, 1_000)); 
		producer.start();
		consumer.start();
	}
	
	public static void ex1_5() {
		var queue = new ArrayBlockingQueue<String>(10);
		var producer1 = new Thread(createProducer(0, queue, 1_000));
		var producer2 = new Thread(createProducer(1, queue, 1_000));
		var consumer1 = new Thread(createConsumer(2, queue, 2_000));
		var consumer2 = new Thread(createConsumer(3, queue, 3_000)); 
		var consumer3 = new Thread(createConsumer(4, queue, 5_000)); 
		producer1.start();
		producer2.start();
		consumer1.start();
		consumer2.start();
		consumer3.start();
	}
	
	public static void ex2_1() {
		var queue = new SynchronizedBlockingBuffer<String>(10);
		var producer1 = new Thread(createProducerSync(0, queue, 1_000));
		var producer2 = new Thread(createProducerSync(1, queue, 1_000));
		var consumer1 = new Thread(createConsumerSync(2, queue, 2_000));
		var consumer2 = new Thread(createConsumerSync(3, queue, 3_000)); 
		var consumer3 = new Thread(createConsumerSync(4, queue, 5_000)); 
		producer1.start();
		producer2.start();
		consumer1.start();
		consumer2.start();
		consumer3.start();
	}
	
	public static void ex2_4() {
		var queue = new LockedBlockingBuffer<String>(10);
		var producer1 = new Thread(createProducerSync(0, queue, 1_000));
		var producer2 = new Thread(createProducerSync(1, queue, 1_000));
		var consumer1 = new Thread(createConsumerSync(2, queue, 2_000));
		var consumer2 = new Thread(createConsumerSync(3, queue, 3_000)); 
		var consumer3 = new Thread(createConsumerSync(4, queue, 5_000)); 
		producer1.start();
		producer2.start();
		consumer1.start();
		consumer2.start();
		consumer3.start();
	}
	
	public static void main(String[] args) {
		ex2_4();
	}
}
