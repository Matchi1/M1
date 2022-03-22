package fr.uge.net.tcp.ex3;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class StringReader implements Reader<String> {

    private enum State {
        DONE, WAITING, ERROR
    };

    private final static int BUFFER_SIZE = 1024;
    private State state = State.WAITING;
    private final ByteBuffer internalBuffer = ByteBuffer.allocate(BUFFER_SIZE); // write-mode
    private final static Charset UTF8 = StandardCharsets.UTF_8;
    private int size;
    private final StringBuilder value = new StringBuilder();

    @Override
    public ProcessStatus process(ByteBuffer buffer) {
        if (state == State.DONE || state == State.ERROR) {
            throw new IllegalStateException();
        }
        buffer.flip();
        if(size == 0) {
            size = buffer.getInt() - Integer.BYTES;
        }
        try {
            size -= Integer.min(buffer.remaining(), internalBuffer.remaining());
            if (buffer.remaining() <= internalBuffer.remaining()) {
                internalBuffer.put(buffer);
            } else {
                var oldLimit = buffer.limit();
                buffer.limit(internalBuffer.remaining());
                internalBuffer.put(buffer);
                buffer.limit(oldLimit);
            }
        } finally {
            buffer.compact();
        }

        if (size > 0 && internalBuffer.hasRemaining()) {
            return ProcessStatus.REFILL;
        }

        internalBuffer.flip();
        value.append(UTF8.decode(internalBuffer));

        if (size > 0) {
            reset();
            return ProcessStatus.REFILL;
        }
        state = State.DONE;
        return ProcessStatus.DONE;
    }

    @Override
    public String get() {
        if (state != State.DONE) {
            throw new IllegalStateException();
        }
        return value.toString();
    }

    @Override
    public void reset() {
        state = State.WAITING;
        internalBuffer.clear();
    }
}
