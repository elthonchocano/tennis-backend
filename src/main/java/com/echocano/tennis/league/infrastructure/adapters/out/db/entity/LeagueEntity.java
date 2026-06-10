package com.echocano.tennis.league.infrastructure.adapters.out.db.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "leagues", uniqueConstraints = {
        @UniqueConstraint(name = "unique_league_name_season", columnNames = { "name", "season" })
})
public class LeagueEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String season;

    @OneToMany(mappedBy = "league", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MatchEntity> matches = new ArrayList<>();

    @Column(name = "points_per_win", nullable = false)
    private Integer pointsPerWin = 3;

    @Column(name = "points_per_loss", nullable = false)
    private Integer pointsPerLoss = 1;

    @Column(name = "points_per_walkover", nullable = false)
    private Integer pointsPerWalkover = 0;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public List<MatchEntity> getMatches() {
        return matches;
    }

    public void setMatches(List<MatchEntity> matches) {
        this.matches = matches;
    }

    public Integer getPointsPerWin() {
        return pointsPerWin;
    }

    public void setPointsPerWin(Integer pointsPerWin) {
        this.pointsPerWin = pointsPerWin;
    }

    public Integer getPointsPerLoss() {
        return pointsPerLoss;
    }

    public void setPointsPerLoss(Integer pointsPerLoss) {
        this.pointsPerLoss = pointsPerLoss;
    }

    public Integer getPointsPerWalkover() {
        return pointsPerWalkover;
    }

    public void setPointsPerWalkover(Integer pointsPerWalkover) {
        this.pointsPerWalkover = pointsPerWalkover;
    }

}