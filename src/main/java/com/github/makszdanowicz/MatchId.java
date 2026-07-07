package com.github.makszdanowicz;

public record MatchId(String homeTeam, String guestTeam) {
    public MatchId {
        if (homeTeam == null || guestTeam == null || homeTeam.isBlank()  || guestTeam.isBlank()) {
            throw new IllegalArgumentException("Team names can't be empty!");
        }

        if (homeTeam.equalsIgnoreCase(guestTeam)) {
            throw new IllegalArgumentException("A team cannot play against itself");
        }
    }
}