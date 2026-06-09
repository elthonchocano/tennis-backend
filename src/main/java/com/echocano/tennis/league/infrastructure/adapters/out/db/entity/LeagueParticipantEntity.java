package com.echocano.tennis.league.infrastructure.adapters.out.db.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "league_participants", uniqueConstraints = @UniqueConstraint(columnNames = { "league_id", "team_id" }))
public class LeagueParticipantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "league_id", nullable = false)
    private LeagueEntity league;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private TeamEntity team;

    @Column(name = "matches_played")
    private Integer matchesPlayed = 0;

    @Column(name = "matches_won")
    private Integer matchesWon = 0;

    @Column(name = "sets_won")
    private Integer setsWon = 0;

    @Column(name = "sets_lost")
    private Integer setsLost = 0;

    @Column(name = "matches_lost", nullable = false)
    private Integer matchesLost = 0;

    @Column(name = "losses_by_walkover", nullable = false)
    private Integer lossesByWalkover = 0;

    @Column(name = "points", nullable = false)
    private Integer points = 0;

    // Getters y Setters estándar...
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LeagueEntity getLeague() {
        return league;
    }

    public void setLeague(LeagueEntity league) {
        this.league = league;
    }

    public TeamEntity getTeam() {
        return team;
    }

    public void setTeam(TeamEntity team) {
        this.team = team;
    }

    public Integer getMatchesPlayed() {
        return matchesPlayed;
    }

    public void setMatchesPlayed(Integer matchesPlayed) {
        this.matchesPlayed = matchesPlayed;
    }

    public Integer getMatchesWon() {
        return matchesWon;
    }

    public void setMatchesWon(Integer matchesWon) {
        this.matchesWon = matchesWon;
    }

    public Integer getSetsWon() {
        return setsWon;
    }

    public void setSetsWon(Integer setsWon) {
        this.setsWon = setsWon;
    }

    public Integer getSetsLost() {
        return setsLost;
    }

    public void setSetsLost(Integer setsLost) {
        this.setsLost = setsLost;
    }

    public Integer getMatchesLost() {
        return matchesLost;
    }

    public void setMatchesLost(Integer matchesLost) {
        this.matchesLost = matchesLost;
    }

    public Integer getLossesByWalkover() {
        return lossesByWalkover;
    }

    public void setLossesByWalkover(Integer lossesByWalkover) {
        this.lossesByWalkover = lossesByWalkover;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

}