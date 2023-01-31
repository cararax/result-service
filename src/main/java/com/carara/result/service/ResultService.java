package com.carara.result.service;

import com.carara.result.domain.Result;
import com.carara.result.domain.Winner;
import com.carara.result.infra.message.response.Vote;
import com.carara.result.infra.message.response.VoteOption;
import com.carara.result.infra.repository.ResultRepository;
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

    public Result calculateResult(List<Vote> voteList) {

        Long agendaId = voteList.get(0).getAgendaId();
        Optional<Result> resultByAgenda = resultRepository.findByAgendaId(agendaId);
        if(resultByAgenda.isPresent()){
            return resultByAgenda.get();
        }

        Map<VoteOption, Long> scoreOfVotes = voteList.stream()
                .collect(Collectors.groupingBy(Vote::getVoteOption, Collectors.counting()));

        Long yesVotes = scoreOfVotes.get(VoteOption.YES);
        Long noVotes = scoreOfVotes.get(VoteOption.NO);

        Result result = new Result();

        if (yesVotes > noVotes) {
            result.setWinner(Winner.YES);
        }else if (noVotes > yesVotes) {
            result.setWinner(Winner.NO);
        }else {
            result.setWinner(Winner.DRAW);
        }

        result.setAgendaId(voteList.get(0).getAgendaId());

        return resultRepository.save(result);
    }
}
