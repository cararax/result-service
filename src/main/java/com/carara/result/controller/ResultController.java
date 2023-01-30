package com.carara.result.controller;

import com.carara.result.infra.message.ResultListener;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/results")
@AllArgsConstructor
public class ResultController {
    ResultListener resultListener;

    @GetMapping("/{agendaId}")

    public void calculateResult(@PathVariable String agendaId) throws JsonProcessingException {
        resultListener.listen(Long.valueOf(agendaId));
    }
}
