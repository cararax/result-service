package com.carara.result.controller;

import com.carara.result.domain.Result;
import com.carara.result.infra.message.VoteListener;
import com.carara.result.service.ResultService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/results")
@AllArgsConstructor
public class ResultController {
    VoteListener resultListener;
    ResultService resultService;

    @GetMapping("/{agendaId}")
    public ResponseEntity<Object> getResult(@PathVariable String agendaId) {
        Result resultById = resultService.findById(Long.valueOf(agendaId));
        return ResponseEntity.status(HttpStatus.OK)
                .body(resultById);
    }

}
