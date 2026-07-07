package com.github.makszdanowicz;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScoreBoardImpTest {

    @Test
    void shouldStartNewMatchWithZeroScope() {
        // given
        ScoreBoard scoreBoard = new ScoreBoardImp();
        String homeTeam = "Mexico";
        String guestTeam = "Canada";

        // when
        scoreBoard.startNewMatch(homeTeam, guestTeam);

        // then
        List<Match> matches = scoreBoard.getSummaryOfMatchesInProgress();
        assertEquals(1, matches.size());
        assertEquals(homeTeam, matches.getFirst().id().homeTeam());
        assertEquals(guestTeam, matches.getFirst().id().guestTeam());
        assertEquals(0, matches.getFirst().homeScore());
        assertEquals(0, matches.getFirst().guestScore());
    }
}
