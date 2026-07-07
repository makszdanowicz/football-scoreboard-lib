package com.github.makszdanowicz;

import java.util.*;

public class ScoreBoardImp implements ScoreBoard{

    private final Map<MatchId, Match> activeMatches = new HashMap<>();
    private final Set<String> teamsInPlay = new HashSet<>();
    private final List<Match> finishedMatches = new ArrayList<>();
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
    public void updateScore(String homeTeam, String guestTeam, int homeScore, int guestScore) {
        MatchId matchId = new MatchId(homeTeam, guestTeam);
        Match currentMatch = activeMatches.get(matchId);
        if (currentMatch == null) {
            throw new IllegalArgumentException("Match not found");
        }
        Match updatedMatch = new Match(currentMatch.id(), homeScore, guestScore, currentMatch.insertionOrder());
        activeMatches.put(matchId, updatedMatch);
    }

    @Override
    public void finishMatch(String homeTeam, String guestTeam) {
        MatchId matchId = new MatchId(homeTeam, guestTeam);
        Match removedMatch = activeMatches.remove(matchId);
        if (removedMatch != null) {
            teamsInPlay.remove(homeTeam.toLowerCase());
            teamsInPlay.remove(guestTeam.toLowerCase());
            finishedMatches.add(removedMatch);
        } else {
            throw new IllegalArgumentException("Match not found");
        }
    }


    @Override
    public List<Match> getSummaryOfMatchesInProgress() {
        return activeMatches.values().stream()
                .sorted(Comparator.comparingInt(Match::getTotalScore).reversed()
                        .thenComparing(Match::insertionOrder, Comparator.reverseOrder()))
                .toList();

    }

    @Override
    public List<Match> getFinishedMatches() {
        return List.copyOf(finishedMatches);
    }
}
