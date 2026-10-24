package com.br.sentinelPay.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Table(name = "mensagem_morta")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MensagemMorta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String payload;

    private String topicoOrigem;

    @Column(columnDefinition = "TEXT")
    private String mensagemErro;

    private LocalDateTime dataFalha;
}
