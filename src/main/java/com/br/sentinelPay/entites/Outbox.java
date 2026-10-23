package com.br.sentinelPay.entites;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "outbox")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Outbox{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID idTransacao;

    private String payload;

    @Enumerated(EnumType.STRING)
    private StatusOutbox status;

    private LocalDateTime dataCriacao;

    @PrePersist
    protected void onCreate(){
        this.dataCriacao = LocalDateTime.now();
    }
}
