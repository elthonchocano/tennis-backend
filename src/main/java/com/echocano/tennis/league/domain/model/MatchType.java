package com.echocano.tennis.league.domain.model;

public enum MatchType {
    SHORT(3, 2),
    LONG(5, 3);

    private final int maxSets;
    private final int minSets;

    MatchType(int maxSets, int minSets) {
        this.maxSets = maxSets;
        this.minSets = minSets;
    }

    public int getMaxSets() { return maxSets; }

    public int getMinSets() {
        return minSets;
    }
}