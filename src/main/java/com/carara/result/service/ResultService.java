package com.carara.result.service;

import com.carara.result.domain.Result;
import com.carara.result.domain.Winner;
import com.carara.result.infra.message.response.Vote;
import com.carara.result.infra.message.response.VoteOption;
import com.carara.result.infra.repository.ResultRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ResultService {

    ResultRepository resultRepository;

    public Result findById(Long id) {
        Optional<Result> resultOptional = resultRepository.findById(id);
        if (resultOptional.isPresent()) {
            return resultOptional.get();
        }
        throw new EntityNotFoundException("Result not found for id: " + id);
    }

    public Result calculateResult(List<Vote> voteList) {
        if (voteList.isEmpty()) {
            throw new IllegalArgumentException("Vote list is empty");
        }

        Long agendaId = voteList.get(0).getAgendaId();
        Optional<Result> resultByAgenda = resultRepository.findByAgendaId(agendaId);
        if (resultByAgenda.isPresent()) {
            return resultByAgenda.get();
        }

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

        result.setAgendaId(voteList.get(0).getAgendaId());

        return resultRepository.save(result);
    }
}
