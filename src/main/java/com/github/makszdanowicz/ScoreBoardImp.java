package com.github.makszdanowicz;

import java.util.Collections;
import java.util.List;

public class ScoreBoardImp implements ScoreBoard{
    @Override
    public void startNewMatch(String homeTeam, String guestTeam) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<Match> getSummaryOfMatchesInProgress() {
        return Collections.emptyList();
    }
}
