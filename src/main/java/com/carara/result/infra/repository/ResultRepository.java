package com.carara.result.infra.repository;

import com.carara.result.domain.Result;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResultRepository extends JpaRepository<Result, Long> {
    Optional<Result> findByAgendaId(Long agendaId);

}