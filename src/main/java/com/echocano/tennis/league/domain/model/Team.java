package com.echocano.tennis.league.domain.model;

public class Team {
    private Long id;
    private Player player1;
    private Player player2;
    private String teamName;

    public Team() {
    }

    public boolean isDoubles() {
        return player2 != null;
    }

    public void validate() {
        if (player1 == null) {
            throw new IllegalArgumentException("Player 1 is required for every team.");
        }
        if (player2 != null && player1.getId() != null && player1.getId().equals(player2.getId())) {
            throw new IllegalArgumentException("A doubles team cannot consist of the same player twice.");
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Team other = (Team) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}