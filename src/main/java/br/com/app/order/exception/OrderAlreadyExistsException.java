package br.com.app.order.exception;

public class OrderAlreadyExistsException extends RuntimeException {
    public OrderAlreadyExistsException(String msg) {
        super(msg);
    }
}
