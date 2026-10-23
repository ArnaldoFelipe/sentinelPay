package com.br.sentinelPay.job;

import com.br.sentinelPay.entites.Outbox;
import com.br.sentinelPay.entites.StatusOutbox;
import com.br.sentinelPay.entites.StatusTransacao;
import com.br.sentinelPay.entites.Transacao;
import com.br.sentinelPay.exception.TransacaoNaoEncontradaException;
import com.br.sentinelPay.repository.OutboxRepository;
import com.br.sentinelPay.repository.TransacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OutboxScheduler {

    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final TransacaoRepository transacaoRepository;

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void processarOutbox(){
        List<Outbox> solicitacoes = outboxRepository.findByStatus(StatusOutbox.PENDENTE);

        for (Outbox evento: solicitacoes){
            kafkaTemplate.send("transacao-solicitada", evento.getPayload());
            evento.setStatus(StatusOutbox.ENVIADO);
            outboxRepository.save(evento);

            Transacao transacao = transacaoRepository.findById(evento.getIdTransacao())
                            .orElseThrow(() -> new TransacaoNaoEncontradaException("Transação não encontrada"));
            transacao.setStatus(StatusTransacao.EM_ANALISE);
            transacaoRepository.save(transacao);
        }
    }
}
