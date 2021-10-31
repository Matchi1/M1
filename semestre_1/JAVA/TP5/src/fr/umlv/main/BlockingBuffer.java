package fr.umlv.main;

public interface BlockingBuffer <E> {
	void put(E element) throws InterruptedException;
	E take() throws InterruptedException;
}
