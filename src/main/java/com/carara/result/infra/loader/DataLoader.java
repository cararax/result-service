package com.carara.result.infra.loader;

import com.carara.result.domain.Result;
import com.carara.result.domain.Winner;
import com.carara.result.infra.repository.ResultRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class DataLoader implements CommandLineRunner {

    ResultRepository resultRepository;

    @Override
    public void run(String... args) throws Exception {
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
    }
}
