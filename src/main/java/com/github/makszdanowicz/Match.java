package com.github.makszdanowicz;

public record Match(MatchId id, int homeScore, int guestScore, long insertionOrder) {

    public Match {
        if (homeScore < 0 || guestScore < 0) {
            throw new IllegalArgumentException("The scores must be non negative!");
        }
    }

    public Match(MatchId id, long insertionOrder) {
        this(id, 0, 0, insertionOrder);
    }

    public int getTotalScore() {
        return homeScore + guestScore;
    }
}
