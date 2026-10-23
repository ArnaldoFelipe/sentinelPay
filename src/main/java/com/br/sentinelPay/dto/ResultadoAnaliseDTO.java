package com.br.sentinelPay.dto;

import com.br.sentinelPay.entites.StatusTransacao;

import java.util.UUID;

public record ResultadoAnaliseDTO(
        UUID idTransacao,
        StatusTransacao statusTransacao
) {
}
