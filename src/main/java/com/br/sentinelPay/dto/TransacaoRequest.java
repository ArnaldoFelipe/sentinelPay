package com.br.sentinelPay.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;


public record TransacaoRequest(
        @NotNull(message = "A Conta de origem deve esta preenchida")
        @NotBlank(message = "A Conta de origem deve esta preenchida")
        String contaOrigem,

        @NotNull(message = "A Conta de destino deve esta preenchida")
        @NotBlank(message = "A Conta de destino deve esta preenchida")
        String contaDestino,

        @Positive(message = "O valor deve ser maior que zero, e não pode ser negativo")
        BigDecimal valor
) {
}
