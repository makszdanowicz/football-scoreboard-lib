package com.github.makszdanowicz;

import java.util.List;

public interface ScoreBoard {
    void startNewMatch(String homeTeam, String guestTeam);
    List<Match> getSummaryOfMatchesInProgress();
}
