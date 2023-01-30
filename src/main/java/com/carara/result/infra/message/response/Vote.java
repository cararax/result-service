package com.carara.result.infra.message.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
//@Table(name = "vote", uniqueConstraints = {
//        @UniqueConstraint(columnNames = {"id", "associateId", "agendaId"})})
public class Vote implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @JsonIgnore
    private Long id;

    @Positive
    @Column(name = "associateId", nullable = false)
    private Long associateId;

    @Positive
    @Column(name = "agendaId", nullable = false)
    private Long agendaId;

    @NotNull
    @Column(name = "voteOption", nullable = false)
    private VoteOption voteOption;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @NotNull
    @Column(name = "voteTime", nullable = false)
    private LocalDateTime voteTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Vote vote = (Vote) o;
        return id != null && Objects.equals(id, vote.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
