package com.br.sentinelPay.dto;

import com.br.sentinelPay.entites.StatusTransacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransacaoResponse(
        UUID id,
        String contaOrigem,
        String contaDestino,
        BigDecimal valor,
        StatusTransacao status,
        LocalDateTime dataCriacao
) {
}
