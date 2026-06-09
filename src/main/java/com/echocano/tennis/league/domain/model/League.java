package com.echocano.tennis.league.domain.model;

import java.util.ArrayList;
import java.util.List;

public class League {
    private Long id;
    private String name;
    private String season;
    private List<Match> matches = new ArrayList<>();

    private Integer pointsPerWin = 3;
    private Integer pointsPerLoss = 1;
    private Integer pointsPerWalkover = 0;

    public League() {
    }

    public League(Long id, String name, String season) {
        this.id = id;
        this.name = name;
        this.season = season;
    }

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

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
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