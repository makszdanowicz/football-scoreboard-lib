package com.github.makszdanowicz;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @Test
    void shouldThrowExceptionWhenTeamIsAlreadyPlaying() {
        // given
        ScoreBoard scoreBoard = new ScoreBoardImp();
        String homeTeam = "Mexico";
        String firstOpponent = "Canada";
        String secondOpponent = "Brazil";
        scoreBoard.startNewMatch(homeTeam, firstOpponent);

        // when + then
        assertThrows(IllegalStateException.class, () -> scoreBoard.startNewMatch(homeTeam, secondOpponent));
    }

    @Test
    void shouldUpdateScore() {
        // given
        ScoreBoard scoreBoard = new ScoreBoardImp();
        String homeTeam = "Mexico";
        String guestTeam = "Canada";
        scoreBoard.startNewMatch(homeTeam, guestTeam);
        Match existingMatch = scoreBoard.getSummaryOfMatchesInProgress().getFirst();
        int newHomeScore = 1, newGuestScore = 0;

        // when
        scoreBoard.updateScore(existingMatch.id(), newHomeScore, newGuestScore);

        // then
        List<Match> matches = scoreBoard.getSummaryOfMatchesInProgress();
        assertEquals(1, matches.size());
        assertEquals(homeTeam, matches.getFirst().id().homeTeam());
        assertEquals(guestTeam, matches.getFirst().id().guestTeam());
        assertEquals(newHomeScore, matches.getFirst().homeScore());
        assertEquals(newGuestScore, matches.getFirst().guestScore());
    }
}
