package br.com.app.order.exception;

public class ProcessUpdateOrderNotFound extends RuntimeException {
    public ProcessUpdateOrderNotFound(String msg) {
        super(msg);
    }
}
