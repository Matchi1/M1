package fr.umlv.conc;

public class VolatileBogus {
	private volatile boolean stop;

	public void runCounter() {
		var localCounter = 0;
		for(;;) {
			if (stop) {
				break;
			}
			localCounter++;
		}
		System.out.println(localCounter);
	}

	public void stop() {
		stop = true;
	}

	public static void main(String[] args) throws InterruptedException {
		var bogus = new VolatileBogus();
		var thread = new Thread(bogus::runCounter);
		thread.start();
		Thread.sleep(100);
		bogus.stop();
		thread.join();
	}
}
