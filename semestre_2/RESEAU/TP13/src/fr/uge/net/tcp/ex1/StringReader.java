package fr.uge.net.tcp.ex1;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class StringReader implements Reader<String> {

    private enum State {
        DONE, WAITING_SIZE, WAITING_STRING, ERROR
    };

    private final static int BUFFER_SIZE = 1024;
    private State state = State.WAITING_SIZE;
    private final ByteBuffer internalBuffer = ByteBuffer.allocate(BUFFER_SIZE); // write-mode
    private final static Charset UTF8 = StandardCharsets.UTF_8;
    private int size;
    private StringBuilder value = new StringBuilder();
    private final IntReader reader = new IntReader();

    @Override
    public ProcessStatus process(ByteBuffer buffer) {
        if (state == State.DONE || state == State.ERROR) {
            throw new IllegalStateException();
        }
        if(state == State.WAITING_SIZE) {
            var ret = reader.process(buffer);
            if(ret != ProcessStatus.DONE) {
                return ret;
            }
            size = reader.get();
            if(size < 0 || size > BUFFER_SIZE) {
                return ProcessStatus.ERROR;
            }
            reader.reset();
            state = State.WAITING_STRING;
        }

        if(state == State.WAITING_STRING) {
            try {
                buffer.flip();
                if (!buffer.hasRemaining()) {
                    return ProcessStatus.REFILL;
                }
                var length = Integer.min(size, internalBuffer.remaining());
                length = Integer.min(length, buffer.remaining());
                var limit = buffer.position() + length;
                var oldLimit = buffer.limit();
                buffer.limit(limit);
                internalBuffer.put(buffer);
                buffer.limit(oldLimit);
                size -= length;
            } finally {
                buffer.compact();
            }
        }

        if (size > 0 && internalBuffer.hasRemaining()) {
            return ProcessStatus.REFILL;
        }

        internalBuffer.flip();
        value.append(UTF8.decode(internalBuffer));
        internalBuffer.clear();

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
        state = State.WAITING_SIZE;
        value = new StringBuilder();
        internalBuffer.clear();
    }
}
