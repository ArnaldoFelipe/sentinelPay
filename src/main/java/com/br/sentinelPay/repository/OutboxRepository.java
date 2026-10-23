package com.br.sentinelPay.repository;

import com.br.sentinelPay.entites.Outbox;
import com.br.sentinelPay.entites.StatusOutbox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OutboxRepository extends JpaRepository<Outbox, UUID> {

    List<Outbox> findByStatus(StatusOutbox status);
}
