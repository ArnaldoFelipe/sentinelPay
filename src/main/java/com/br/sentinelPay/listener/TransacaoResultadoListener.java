package com.br.sentinelPay.listener;

import com.br.sentinelPay.dto.ResultadoAnaliseDTO;
import com.br.sentinelPay.entites.MensagemMorta;
import com.br.sentinelPay.exception.PayLoadJsonException;
import com.br.sentinelPay.repository.MensagemMortaRepository;
import com.br.sentinelPay.service.TransacaoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TransacaoResultadoListener {

    private final TransacaoService transacaoService;
    private final ObjectMapper objectMapper;
    private final MensagemMortaRepository mensagemMortaRepository;

    @RetryableTopic(
            attempts = "3",
            backoff = @Backoff(delay = 1000, multiplier = 2.0)
    )
    @KafkaListener(topics = "transacao-analisada", groupId = "sentinel-group")
    public void receberResultado(String payload){
        try {
            ResultadoAnaliseDTO dto = objectMapper.readValue(payload, ResultadoAnaliseDTO.class);
            transacaoService.processarResultado(dto);
        } catch (JsonProcessingException e){
            throw new PayLoadJsonException("Erro na conversão do payload");
        }
    }

    @DltHandler
    public void processarMensagemMorta(String payload,
                                       @Header(KafkaHeaders.RECEIVED_TOPIC)String topicoOrigem,
                                       @Header(KafkaHeaders.EXCEPTION_MESSAGE)String mensagemErro){

        MensagemMorta mensagemMorta = MensagemMorta.builder()
                .payload(payload)
                .topicoOrigem(topicoOrigem)
                .mensagemErro(mensagemErro)
                .dataFalha(LocalDateTime.now())
                .build();

        mensagemMortaRepository.save(mensagemMorta);
    }
}
