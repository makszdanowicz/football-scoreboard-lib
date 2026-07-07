package com.github.makszdanowicz;

import java.util.*;

public class ScoreBoardImp implements ScoreBoard{

    private final Map<MatchId, Match> activeMatches = new HashMap<>();

    private long sequence = 0;

    @Override
    public void startNewMatch(String homeTeam, String guestTeam) {
        MatchId matchId = new MatchId(homeTeam, guestTeam);
        Match match = new Match(matchId, sequence++);
        activeMatches.put(matchId, match);
    }

    @Override
    public List<Match> getSummaryOfMatchesInProgress() {
        return new ArrayList<>(activeMatches.values());
    }
}
