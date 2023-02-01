package com.carara.result.service;

import com.carara.result.domain.Result;
import com.carara.result.domain.Winner;
import com.carara.result.infra.message.response.Vote;
import com.carara.result.infra.message.response.VoteOption;
import com.carara.result.infra.repository.ResultRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Log4j2(topic = "ResultService")
public class ResultService {

    ResultRepository resultRepository;

    public Result findById(Long id) {
        Optional<Result> resultOptional = resultRepository.findById(id);
        if (resultOptional.isPresent()) {
            log.info("Result found for id: " + id);
            return resultOptional.get();
        }
        log.error("Result not found for id: " + id);
        throw new EntityNotFoundException("Result not found for id: " + id);
    }

    public Result calculateResult(List<Vote> voteList) {
        log.info("Calculating result for vote list: " + voteList);
        checkIfVoteListIsNotEmpty(voteList);

        Long agendaId = voteList.get(0).getAgendaId();
        Result resultByAgenda = checkIfAlreadyExistResultForAgendaId(agendaId);
        if (resultByAgenda != null) {
            log.info("Result already calculated for agenda id: " + agendaId);
            return resultByAgenda;
        }
        Result result = calculateAndSetWinner(voteList);
        result.setAgendaId(voteList.get(0).getAgendaId());
        log.info("Saving result : " + result.getWinner().toString()+ " for agenda id: " + result.getAgendaId());
        return resultRepository.save(result);
    }

    private Result calculateAndSetWinner(List<Vote> voteList) {
        log.info("Calculating winner");
        Map<VoteOption, Long> scoreOfVotes = voteList.stream()
                .collect(Collectors.groupingBy(Vote::getVoteOption, Collectors.counting()));
        Long yesVotes = scoreOfVotes.getOrDefault(VoteOption.YES, 0L);
        Long noVotes = scoreOfVotes.getOrDefault(VoteOption.NO, 0L);
        Result result = new Result();
        if (yesVotes > noVotes) {
            result.setWinner(Winner.YES);
        } else if (noVotes > yesVotes) {
            result.setWinner(Winner.NO);
        } else {
            result.setWinner(Winner.DRAW);
        }
        return result;
    }

    private Result checkIfAlreadyExistResultForAgendaId(Long agendaId) {
        Optional<Result> resultByAgenda = resultRepository.findByAgendaId(agendaId);
        return resultByAgenda.orElse(null);
    }

    private void checkIfVoteListIsNotEmpty(List<Vote> voteList) {
        if (voteList.isEmpty()) {
            log.error("Vote list is empty");
            throw new IllegalArgumentException("Vote list is empty");
        }
    }
}
