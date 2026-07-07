package com.github.makszdanowicz;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ScoreBoardImplTest {

    private ScoreBoard scoreBoard;

    @BeforeEach
    void setUp() {
        scoreBoard = new ScoreBoardImpl();
    }

    @Test
    void shouldStartNewMatchWithZeroScope() {
        // given
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
        String homeTeam = "Mexico";
        String firstOpponent = "Canada";
        String secondOpponent = "Brazil";
        scoreBoard.startNewMatch(homeTeam, firstOpponent);

        // when + then
        assertThrows(IllegalStateException.class, () -> scoreBoard.startNewMatch(homeTeam, secondOpponent));
    }

    @Test
    void shouldThrowExceptionWhenTeamPlaysAgainstItself() {
        // when + then
        assertThrows(
                IllegalArgumentException.class,
                () -> scoreBoard.startNewMatch("Mexico", "Mexico")
        );
    }

    @Test
    void shouldUpdateScore() {
        // given
        String homeTeam = "Mexico";
        String guestTeam = "Canada";
        scoreBoard.startNewMatch(homeTeam, guestTeam);
        Match existingMatch = scoreBoard.getSummaryOfMatchesInProgress().getFirst();
        int newHomeScore = 1, newGuestScore = 0;

        // when
        scoreBoard.updateScore(homeTeam, guestTeam, newHomeScore, newGuestScore);

        // then
        List<Match> matches = scoreBoard.getSummaryOfMatchesInProgress();
        assertEquals(1, matches.size());
        assertEquals(existingMatch.id(), matches.getFirst().id());
        assertEquals(newHomeScore, matches.getFirst().homeScore());
        assertEquals(newGuestScore, matches.getFirst().guestScore());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingScoreOfNonExistentMatch() {
        // given
        String notExistHomeName = "notExist1";
        String notExistGuestName = "notExist2";
        MatchId matchId = new MatchId(notExistHomeName, notExistGuestName);

        // when + then
        assertThrows(IllegalArgumentException.class,
                () -> scoreBoard.updateScore(notExistHomeName, notExistGuestName, 1, 1));
    }

    @Test
    void shouldThrowExceptionWhenScoreIsNegative() {
        // given
        String homeTeam = "Mexico";
        String guestTeam = "Canada";
        scoreBoard.startNewMatch(homeTeam, guestTeam);
        Match existingMatch = scoreBoard.getSummaryOfMatchesInProgress().getFirst();
        int newHomeScore = 0, newGuestScore = -1;

        // when + then
        assertThrows(IllegalArgumentException.class,
                () -> scoreBoard.updateScore(homeTeam, guestTeam, newHomeScore, newGuestScore));
    }

    @Test
    void shouldFinishMatch() {
        // given
        String homeTeam = "Mexico";
        String guestTeam = "Canada";
        scoreBoard.startNewMatch(homeTeam, guestTeam);
        Match existingMatch = scoreBoard.getSummaryOfMatchesInProgress().getFirst();

        // when
        scoreBoard.finishMatch(homeTeam, guestTeam);

        // then
        assertTrue(scoreBoard.getSummaryOfMatchesInProgress().isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenFinishingNonExistentMatch() {
        // given
        String notExistingTeam = "notExist";

        // when + then
        assertThrows(IllegalArgumentException.class,
                () -> scoreBoard.finishMatch(notExistingTeam, notExistingTeam));
    }

    @Test
    void shouldReturnEmptySummaryWhenNoMatchesInProgress() {
        // when
        List<Match> summary = scoreBoard.getSummaryOfMatchesInProgress();

        // then
        assertTrue(summary.isEmpty());
    }

    @Test
    void shouldReturnSummaryOrderedByTotalScoreAndInsertionOrder() {
        // given
        MatchId m1 = new MatchId("Mexico", "Canada");
        MatchId m2 = new MatchId("Spain", "Brazil");
        MatchId m3 = new MatchId("Germany", "France");
        MatchId m4 = new MatchId("Uruguay", "Italy");
        MatchId m5 = new MatchId("Argentina", "Australia");

        scoreBoard.startNewMatch(m1.homeTeam(), m1.guestTeam());
        scoreBoard.startNewMatch(m2.homeTeam(), m2.guestTeam());
        scoreBoard.startNewMatch(m3.homeTeam(), m3.guestTeam());
        scoreBoard.startNewMatch(m4.homeTeam(), m4.guestTeam());
        scoreBoard.startNewMatch(m5.homeTeam(), m5.guestTeam());

        scoreBoard.updateScore(m1.homeTeam(), m1.guestTeam(), 0, 5);
        scoreBoard.updateScore(m2.homeTeam(), m2.guestTeam(), 10, 2);
        scoreBoard.updateScore(m3.homeTeam(), m3.guestTeam(), 2, 2);
        scoreBoard.updateScore(m4.homeTeam(), m4.guestTeam(), 6, 6);
        scoreBoard.updateScore(m5.homeTeam(), m5.guestTeam(), 3, 1);

        // when
        List<Match> summary = scoreBoard.getSummaryOfMatchesInProgress();

        // then
        assertEquals(5, summary.size());
        assertEquals(m4, summary.getFirst().id());
        assertEquals(m2, summary.get(1).id());
        assertEquals(m1, summary.get(2).id());
        assertEquals(m5, summary.get(3).id());
        assertEquals(m3, summary.get(4).id());
    }

    @Test
    void shouldReturnFinishedMatches() {
        // given
        String homeTeam = "Poland", guestTeam = "Sweden";
        scoreBoard.startNewMatch(homeTeam, guestTeam);
        scoreBoard.updateScore(homeTeam, guestTeam, 2, 1);

        // when
        scoreBoard.finishMatch(homeTeam, guestTeam);

        // then
        List<Match> finishedMatches = scoreBoard.getFinishedMatches();
        assertEquals(1, finishedMatches.size());
        assertEquals(homeTeam, finishedMatches.getFirst().id().homeTeam());
        assertEquals(2, finishedMatches.getFirst().homeScore());
        assertEquals(guestTeam, finishedMatches.getFirst().id().guestTeam());
        assertEquals(1, finishedMatches.getFirst().guestScore());
        assertTrue(scoreBoard.getSummaryOfMatchesInProgress().isEmpty());
    }
}
