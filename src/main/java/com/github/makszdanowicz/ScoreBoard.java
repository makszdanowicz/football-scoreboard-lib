package com.github.makszdanowicz;

import java.util.List;

public interface ScoreBoard {
    void startNewMatch(String homeTeam, String guestTeam);
    void updateScore(String homeTeam, String guestTeam, int homeScore, int guestScore);
    void finishMatch(String homeTeam, String guestTeam);
    List<Match> getSummaryOfMatchesInProgress();
    List<Match> getFinishedMatches();
}
