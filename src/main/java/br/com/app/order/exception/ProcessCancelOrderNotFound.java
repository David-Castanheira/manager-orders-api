package br.com.app.order.exception;

public class ProcessCancelOrderNotFound extends RuntimeException {
    public ProcessCancelOrderNotFound(String msg) {
        super(msg);
    }
}
