package fr.uge.net.tcp.ex1;

import java.nio.ByteBuffer;

public class MessageReader implements Reader<Message>{

    private enum State {
        DONE, WAITING_LOGIN, WAITING_MESSAGE, ERROR
    }

    private State state = State.WAITING_LOGIN;
    private final StringReader reader = new StringReader();
    private String username;
    private String content;
    private Message message;

    @Override
    public ProcessStatus process(ByteBuffer bb) {
        if (state == State.DONE || state == State.ERROR) {
            throw new IllegalStateException();
        }
        if(state == State.WAITING_LOGIN) {
            var status = reader.process(bb);
            if (status != ProcessStatus.DONE) {
                return status;
            }
            username = reader.get();
            reader.reset();
            state = State.WAITING_MESSAGE;
        }

        if(state == State.WAITING_MESSAGE) {
            var status = reader.process(bb);
            if (status != ProcessStatus.DONE) {
                return status;
            }
            content = reader.get();
        }

        message = new Message(username, content);
        state = State.DONE;
        return ProcessStatus.DONE;
    }

    @Override
    public Message get() {
        if (state != State.DONE) {
            throw new IllegalStateException();
        }
        return message;
    }

    @Override
    public void reset() {
        state = State.WAITING_LOGIN;
        reader.reset();
        username = "";
        message = null;
    }
}
