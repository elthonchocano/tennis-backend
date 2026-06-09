package com.echocano.tennis.league.infrastructure.adapters.out.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "set_results")
public class SetResultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    private MatchEntity match;

    @Column(name = "set_number", nullable = false)
    private Integer setNumber;

    @Column(name = "team1_games", nullable = false)
    private Integer team1Games;

    @Column(name = "team2_games", nullable = false)
    private Integer team2Games;

    @Column(name = "team1_tiebreak_points")
    private Integer team1TieBreakPoints;

    @Column(name = "team2_tiebreak_points")
    private Integer team2TieBreakPoints;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MatchEntity getMatch() {
        return match;
    }

    public void setMatch(MatchEntity match) {
        this.match = match;
    }

    public Integer getSetNumber() {
        return setNumber;
    }

    public void setSetNumber(Integer setNumber) {
        this.setNumber = setNumber;
    }

    public Integer getTeam1Games() {
        return team1Games;
    }

    public void setTeam1Games(Integer team1Games) {
        this.team1Games = team1Games;
    }

    public Integer getTeam2Games() {
        return team2Games;
    }

    public void setTeam2Games(Integer team2Games) {
        this.team2Games = team2Games;
    }

    public Integer getTeam1TieBreakPoints() {
        return team1TieBreakPoints;
    }

    public void setTeam1TieBreakPoints(Integer points) {
        this.team1TieBreakPoints = points;
    }

    public Integer getTeam2TieBreakPoints() {
        return team2TieBreakPoints;
    }

    public void setTeam2TieBreakPoints(Integer points) {
        this.team2TieBreakPoints = points;
    }
}