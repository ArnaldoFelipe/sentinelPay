package com.br.sentinelPay.exception;

import com.br.sentinelPay.entites.Transacao;
import com.br.sentinelPay.exception.dto.ErroResponse;
import org.springframework.cglib.core.Local;
import org.springframework.data.web.ReactivePageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PayLoadJsonException.class)
    public ResponseEntity<ErroResponse> handlerPayloadJson(PayLoadJsonException ex){
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErroResponse(
                        ex.getMessage(),
                        "ERRO_CONVERSAO_JSON",
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(TransacaoNaoEncontradaException.class)
    public ResponseEntity<ErroResponse> handlerTransacaoNaoEncontrada(TransacaoNaoEncontradaException ex){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErroResponse(
                        ex.getMessage(),
                        "TRANSACAO_NAO_ENCONTRADA",
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(TransacaoDuplicadaException.class)
    public ResponseEntity<ErroResponse> handlerTransacaoDuplicada(TransacaoDuplicadaException ex){
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErroResponse(
                        ex.getMessage(),
                        "TRANSACAO_DUPLICADA",
                        LocalDateTime.now()
                ));
    }
}
