package com.br.sentinelPay.mapper;

import com.br.sentinelPay.dto.TransacaoRequest;
import com.br.sentinelPay.dto.TransacaoResponse;
import com.br.sentinelPay.entites.Transacao;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransacaoMapper {

    Transacao toEntity(TransacaoRequest request);

    TransacaoResponse toResponse(Transacao transacao);

    List<TransacaoResponse> toResponseList(List<Transacao> transacaos);
}
