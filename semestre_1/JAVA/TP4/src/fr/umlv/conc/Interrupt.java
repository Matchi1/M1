package fr.umlv.conc;

public class Interrupt {
	public static void main(String[] args) throws InterruptedException {
	    var thread = new Thread(() -> {
	        while(true){  
	            try {
					Thread.sleep(1_000);
				} catch (InterruptedException e) {
					return;
				}
	        }
	    });
	    thread.start();
	    Thread.sleep(1_000);
	    thread.interrupt();
	    System.out.println("end");
	}
}
