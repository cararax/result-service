package com.carara.result.domain;

import com.carara.result.infra.message.response.VoteOption;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.util.*;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "result", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"agendaId", "id"})})
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Positive
    @Column(name = "agendaId", unique = true, nullable = false)
    private Long agendaId;

    @NotNull
    @Column(name = "winner", nullable = false)
    private Winner winner;

// todo: adicionar em um dto
//    private List<VoteScore> voteScore = new ArrayList<>();

    @NotNull
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Result result = (Result) o;
        return id != null && Objects.equals(id, result.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}





