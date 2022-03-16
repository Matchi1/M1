package fr.uge.net.tcp.ex4;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadData {
    private SocketChannel client;
    private long counter = 0;
    private final ReentrantLock lock = new ReentrantLock();

    public void setSocketChannel(SocketChannel client) {
        lock.lock();
        this.client = client;
        lock.unlock();
    }

    public void tick() {
        lock.lock();
        this.counter = System.currentTimeMillis();
        lock.unlock();
    }

    public void closeIfInactive(int timeout) throws IOException {
        try {
            lock.lock();
            if(client == null) {
                return;
            }
            if(System.currentTimeMillis() - counter > timeout) {
                System.out.println("closed");
                client.close();
                client = null;
            }
        } finally {
            lock.unlock();
        }
    }

    public void close() {
        try {
            lock.lock();
            if (client != null) {
                client.close();
                client = null;
            }
        } catch (IOException ioe) {
            // nothing
        } finally {
            lock.unlock();
        }
    }
}
