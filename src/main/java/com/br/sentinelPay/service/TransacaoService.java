package com.br.sentinelPay.service;

import com.br.sentinelPay.dto.ResultadoAnaliseDTO;
import com.br.sentinelPay.dto.TransacaoRequest;
import com.br.sentinelPay.dto.TransacaoResponse;
import com.br.sentinelPay.entites.Outbox;
import com.br.sentinelPay.entites.StatusOutbox;
import com.br.sentinelPay.entites.StatusTransacao;
import com.br.sentinelPay.entites.Transacao;
import com.br.sentinelPay.exception.PayLoadJsonException;
import com.br.sentinelPay.exception.TransacaoDuplicadaException;
import com.br.sentinelPay.exception.TransacaoNaoEncontradaException;
import com.br.sentinelPay.mapper.TransacaoMapper;
import com.br.sentinelPay.repository.OutboxRepository;
import com.br.sentinelPay.repository.TransacaoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransacaoService {

    private final TransacaoRepository repository;
    private final TransacaoMapper mapper;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;
    private final StringRedisTemplate redisTemplate;

    @Transactional
    public TransacaoResponse solicitarPagamento(TransacaoRequest request) {
        String chave = gerarChaveIdempotencia(request);

        Boolean travado = redisTemplate.opsForValue().setIfAbsent(chave, "lock", Duration.ofSeconds(30));

        if(Boolean.FALSE.equals(travado)){
            throw new TransacaoDuplicadaException("Transação idêntica já em processamento");
        }

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
            throw new PayLoadJsonException("Erro ao gerar payLoad da outbox");
        }
    }

    @Transactional
    @CacheEvict(value = "transacao", key = "#resultado.idTransacao()")
    public void processarResultado(ResultadoAnaliseDTO resultado){

        Transacao transacao = repository.findById(resultado.idTransacao())
                .orElseThrow(() -> new TransacaoNaoEncontradaException("Transação não encontrada"));
        transacao.setStatus(resultado.statusTransacao());
        repository.save(transacao);
    }

    public Page<TransacaoResponse> listarTransacoes(Pageable pageable){
        Page<Transacao> transacoes = repository.findAll(pageable);
        return transacoes.map(mapper::toResponse);
    }

    private String gerarChaveIdempotencia(TransacaoRequest request){
        return "pagamento:" + request.contaOrigem() + ":" + request.contaDestino() + ":" + request.valor();
    }

    @Cacheable(value = "transacao", key = "#id")
    public TransacaoResponse buscarTransacaoPorId(UUID id){
        Transacao transacao = repository.findById(id)
                .orElseThrow(() -> new TransacaoNaoEncontradaException("Transação não encontrada"));
        return mapper.toResponse(transacao);
    }
}
