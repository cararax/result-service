package com.carara.result.infra.loader;

import com.carara.result.domain.Result;
import com.carara.result.domain.Winner;
import com.carara.result.infra.repository.ResultRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
@Log4j2(topic = "DataLoader")
public class DataLoader implements CommandLineRunner {

    ResultRepository resultRepository;

    @Override
    public void run(String... args) {
        if (resultRepository.count() == 0) {
            loadAssociateData();
        }
    }

    public void loadAssociateData() {
        List<Result> resultList = List.of(
                new Result(1L, Winner.YES),
                new Result(2L, Winner.NO),
                new Result(3L, Winner.DRAW));
        resultRepository.saveAll(resultList);
        log.info("Results loaded to database");
    }
}
