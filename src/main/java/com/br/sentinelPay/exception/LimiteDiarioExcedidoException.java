package com.br.sentinelPay.exception;

public class LimiteDiarioExcedidoException extends RuntimeException {
    public LimiteDiarioExcedidoException(String message) {
        super(message);
    }
}
