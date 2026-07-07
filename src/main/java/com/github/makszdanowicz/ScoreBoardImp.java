package com.github.makszdanowicz;

import java.util.*;

public class ScoreBoardImp implements ScoreBoard{

    private final Map<MatchId, Match> activeMatches = new HashMap<>();
    private final Set<String> teamsInPlay = new HashSet<>();
    private long sequence = 0;

    @Override
    public void startNewMatch(String homeTeam, String guestTeam) {
        // Standardizing names to lowercase for safe comparison
        String normalizedHomeTeam = homeTeam.toLowerCase();
        String normalizedGuestTeam = guestTeam.toLowerCase();

        if (teamsInPlay.contains(normalizedGuestTeam) || teamsInPlay.contains(normalizedHomeTeam)) {
            throw new IllegalStateException("One or both teams are already playing a match");
        }

        MatchId matchId = new MatchId(homeTeam, guestTeam);
        Match match = new Match(matchId, sequence++);
        activeMatches.put(matchId, match);
        teamsInPlay.add(normalizedHomeTeam);
        teamsInPlay.add(normalizedGuestTeam);
    }

    @Override
    public void updateScore(MatchId id, int homeScore, int guestScore) {
        Match currentMatch = activeMatches.get(id);
        Match updatedMatch = new Match(currentMatch.id(), homeScore, guestScore, currentMatch.insertionOrder());
        activeMatches.put(id, updatedMatch);
    }

    @Override
    public List<Match> getSummaryOfMatchesInProgress() {
        return new ArrayList<>(activeMatches.values());
    }
}
