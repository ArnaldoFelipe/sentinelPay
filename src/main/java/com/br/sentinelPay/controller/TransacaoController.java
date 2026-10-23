package com.br.sentinelPay.controller;

import com.br.sentinelPay.dto.TransacaoRequest;
import com.br.sentinelPay.dto.TransacaoResponse;
import com.br.sentinelPay.service.TransacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transacoes")
@RequiredArgsConstructor
public class TransacaoController {

    private final TransacaoService service;

    @PostMapping
    public ResponseEntity<TransacaoResponse> solicitarPagamento(@RequestBody @Valid TransacaoRequest request){
        TransacaoResponse transacao = service.solicitarPagamento(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(transacao);
    }

    @GetMapping
    public ResponseEntity<Page<TransacaoResponse>> listarTransacoes(@PageableDefault(
                                                                    size = 10,
                                                                    sort = "dataCriacao",
                                                                    direction = Sort.Direction.DESC) Pageable pageable){
        return ResponseEntity.ok(service.listarTransacoes(pageable));
    }
}
