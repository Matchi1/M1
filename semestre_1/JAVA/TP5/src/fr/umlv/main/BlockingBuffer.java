package fr.umlv.main;

public interface BlockingBuffer <E> {
	public void put(E element) throws InterruptedException;
	public E take() throws InterruptedException;
}
