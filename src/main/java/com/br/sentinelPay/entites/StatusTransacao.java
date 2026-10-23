package com.br.sentinelPay.entites;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;


public enum StatusTransacao {
    PENDENTE,
    EM_ANALISE,
    APROVADA,
    RECUSADA
}
