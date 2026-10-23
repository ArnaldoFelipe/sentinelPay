package com.br.sentinelPay.exception.dto;

import java.time.LocalDateTime;

public record ErroResponse(
        String mensagem,
        String codigo,
        LocalDateTime timesTamp
) {
}
