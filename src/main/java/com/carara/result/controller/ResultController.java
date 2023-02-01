package com.carara.result.controller;

import com.carara.result.domain.Result;
import com.carara.result.infra.message.VoteListener;
import com.carara.result.service.ResultService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/results")
@AllArgsConstructor
@Log4j2(topic = "ResultController")
public class ResultController {
    VoteListener resultListener;
    ResultService resultService;

    @GetMapping("/{agendaId}")
    public ResponseEntity<Object> getResult(@PathVariable String agendaId) {
        Result resultById = resultService.findById(Long.valueOf(agendaId));
        log.info("Result for agenda: " + agendaId + " is: " + resultById.toString());
        return ResponseEntity.status(HttpStatus.OK)
                .body(resultById);
    }

}
