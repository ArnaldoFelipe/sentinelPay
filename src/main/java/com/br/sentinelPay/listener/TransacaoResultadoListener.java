package com.br.sentinelPay.listener;

import com.br.sentinelPay.dto.ResultadoAnaliseDTO;
import com.br.sentinelPay.exception.PayLoadJsonException;
import com.br.sentinelPay.service.TransacaoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransacaoResultadoListener {

    private final TransacaoService transacaoService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "transacao-analisada", groupId = "sentinel-group")
    public void receberResultado(String payload){
        try {
            ResultadoAnaliseDTO dto = objectMapper.readValue(payload, ResultadoAnaliseDTO.class);
            transacaoService.processarResultado(dto);
        } catch (JsonProcessingException e){
            throw new PayLoadJsonException("Erro na conversão do payload");
        }
    }
}
