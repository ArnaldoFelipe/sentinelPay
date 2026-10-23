package com.br.sentinelPay.service;

import com.br.sentinelPay.dto.TransacaoRequest;
import com.br.sentinelPay.dto.TransacaoResponse;
import com.br.sentinelPay.entites.Outbox;
import com.br.sentinelPay.entites.StatusOutbox;
import com.br.sentinelPay.entites.StatusTransacao;
import com.br.sentinelPay.entites.Transacao;
import com.br.sentinelPay.mapper.TransacaoMapper;
import com.br.sentinelPay.repository.OutboxRepository;
import com.br.sentinelPay.repository.TransacaoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransacaoService {

    private final TransacaoRepository repository;
    private final TransacaoMapper mapper;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public TransacaoResponse solicitarPagamento(TransacaoRequest request) {
        Transacao transacao = mapper.toEntity(request);
        transacao.setStatus(StatusTransacao.PENDENTE);
        Transacao transacaoSalva = repository.save(transacao);

        resgistrarOutbox(transacaoSalva);

        return mapper.toResponse(transacaoSalva);
    }

    private void resgistrarOutbox(Transacao transacao){
        Outbox outbox = Outbox.builder()
                .idTransacao(transacao.getId())
                .payload(gerarPayloadJson(transacao))
                .status(StatusOutbox.PENDENTE)
                .build();
        outboxRepository.save(outbox);
    }

    private String gerarPayloadJson(Transacao transacao){
        try {
            return  objectMapper.writeValueAsString(transacao);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao gerar payLoad da outbox", e);
        }
    }
}
