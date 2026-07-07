package com.github.makszdanowicz;

import java.util.List;

public interface ScoreBoard {
    void startNewMatch(String homeTeam, String guestTeam);
    void updateScore(MatchId id, int homeScore, int guestScore);
    void finishMatch(MatchId id);
    List<Match> getSummaryOfMatchesInProgress();
}
