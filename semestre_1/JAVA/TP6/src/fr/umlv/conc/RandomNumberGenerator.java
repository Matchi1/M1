package fr.umlv.conc;

import java.util.HashSet;
import java.util.concurrent.atomic.AtomicLong;

public class RandomNumberGenerator {
	private volatile long x;
	private static AtomicLong reference = new AtomicLong();

	public RandomNumberGenerator(long seed) {
		if (seed == 0) {
			throw new IllegalArgumentException("seed == 0");
		}
		x = seed;
	}

	public long f(long x) {
		x ^= x >>> 12;
		x ^= x << 25;
		x ^= x >>> 27;
		return x;
	}
	
	public long next() {  // Marsaglia's XorShift
		for(;;) {
			reference.compareAndSet(this.x, f(x));
			return x * 2685821657736338717L;
		}
	}

	public static void main(String[] args) throws InterruptedException {
		var set0 = new HashSet<Long>();
		var set1 = new HashSet<Long>();
		var set2 = new HashSet<Long>();
		var rng0 = new RandomNumberGenerator(1);
		var rng = new RandomNumberGenerator(1);

		for (var i = 0; i < 10_000; i++) {
			set0.add(rng0.next());
		}

		var t = new Thread(() -> {
			for (var i = 0; i < 5_000; i++) {
				set1.add(rng.next());
			}
		});
		t.start();
		for (var i = 0; i < 5_000; i++) {
			set2.add(rng.next());
		}
		t.join();

		System.out.println("set1: " + set1.size() + ", set2: " + set2.size());

		set1.addAll(set2);
		System.out.println("union (should be 10 000): " + set1.size());

		System.out.println("intersection (should be true): " + set0.containsAll(set1));

		set0.removeAll(set1);
		System.out.println("intersection (should be 0): " + set0.size());
	}
}
