package br.com.app.order.exception;

public class EmptyEventsException extends RuntimeException {
    public EmptyEventsException(String msg) {
        super(msg);
    }
}
