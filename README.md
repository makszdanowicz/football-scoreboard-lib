# Live Football World Cup Scoreboard

## Table of Contents

- [Overview](#overview)
- [Assumptions](#assumptions)
- [Reasoning](#reasoning)
- [Trade-offs](#trade-offs)
- [How to Run](#how-to-run)
    - [Prerequisites](#prerequisites)
    - [Installation and Testing](#installation-and-testing)
    - [Usage Example](#usage-example)

## Overview
This repository contains a Java library for managing a Live Football World Cup Scoreboard. It allows consumers to track ongoing matches, update scores, finish matches, retrieve a dynamically ordered matches currently in progress and retrieve a list of matches that have been finished.

This project was developed strictly using **Test-Driven Development (TDD)** principles and follows a Design-First approach.

## Assumptions
Before and during the implementation, the following domain rules and business constraints were established:

* **Unique Teams:** a specific team can only play one match at a time. The system actively rejects attempts to start a match if either the home or guest team is already present on the active scoreboard.
* **Self-Play Restriction:** a team cannot play against itself (e.g., "Mexico" vs "Mexico" is invalid).
* **Non-negative Scores:** match scores must always be positive integers or zero. Initial matches start strictly at 0-0.
* **Case-Insensitive Team Names:** "Mexico" and "MEXICO" are treated as the same entity internally to prevent duplicate entries and validation bypasses.
* **In-Memory Volatility:** as a library, the state is kept in memory. It is assumed that the client application is responsible for any persistent storage if required across system restarts.
* **API Abstraction**: the public ScoreBoard interface separates the API contract from its implementation. The current implementation (ScoreBoardImpl) stores data in memory, but alternative implementations (for example, backed by a relational database, NoSQL database, or distributed cache) could be introduced without changing the public API.

## Reasoning
The following design decisions were made to ensure the library is robust, enterprise-ready and maintainable:

* **Absolute Score Updates over Incremental:** the `updateScore` method requires the exact new score (e.g., 2-1) rather than an incremental value (e.g., +1 goal). This ensures idempotency. In distributed enterprise environments (like sports betting platforms), if a network message is duplicated, an absolute update prevents phantom goals.
* **Immutability and Encapsulation:** the `Match` and `MatchId` models are implemented as Java `record`s, making them deeply immutable. When returning collections (e.g., `getFinishedMatches()`), defensive copies (`List.copyOf()`) are used. This guarantees that consumers cannot accidentally modify or corrupt the library's internal state.
* **Custom Feature - Finished Matches Archive:** I added `getFinishedMatches()` as the additional operation. *Reasoning:* In real-world sports data platforms, matches do not simply vanish after the final whistle. Retaining historical matches is crucial for downstream processing, such as post-match reporting, statistical analysis and settling betting markets.

## Trade-offs

* **Sequence Number vs. Timestamp:**  
  The requirements only specify that when two matches have the same total score, the **most recently started match** should appear first. They do not require storing the actual kickoff time.

  Instead of relying on `Instant.now()`, I use an internal monotonically increasing `sequence` number assigned when a match is started. This provides deterministic ordering, simplifies the implementation, and makes unit tests fully predictable without depending on the system clock.

  If recording the actual kickoff time became a business requirement in the future, the model could be extended with a `startTime` field without affecting the sorting logic.

* **ReentrantReadWriteLock vs. Synchronized / Concurrent Collections:**  
  The scoreboard maintains multiple related mutable data structures (`activeMatches`, `teamsInPlay`, `finishedMatches`, and `sequence`) that must remain consistent when accessed concurrently.

  Instead of using a simple `synchronized` approach or individual concurrent collections, I implemented `ReentrantReadWriteLock`. This introduces some additional code complexity, but it provides a better fit for a read-heavy workload.

  Multiple readers can access the scoreboard summary concurrently, while write operations (`startMatch`, `updateScore`, `finishMatch`) obtain exclusive access to guarantee state consistency.

## How to Run

### Prerequisites
* Java 21 or higher
* Git

> **Note:** Apache Maven does not need to be installed separately — this project uses the Maven Wrapper (`mvnw`), which automatically downloads the correct Maven version.

### Installation and Testing
To clone the repository and run the test suite, execute the following commands in your terminal:

```bash
# Clone the repository
git clone git@github.com:makszdanowicz/football-scoreboard-lib.git

# Navigate into the project directory
cd football-scoreboard-lib

# Clean the project and run all unit tests
./mvnw clean test

# Build the project package and install it to your local Maven repository
./mvnw clean install
```

On Windows (Command Prompt / PowerShell), use `mvnw.cmd` instead of `./mvnw`:

```bash
mvnw.cmd clean test
mvnw.cmd clean install
```

### Usage Example

Below is a simple demonstration of how to integrate and use the Scoreboard library in a Java application.

```java
import com.github.makszdanowicz.ScoreBoard;
import com.github.makszdanowicz.ScoreBoardImpl;
import com.github.makszdanowicz.Match;

import java.util.List;

public class ScoreBoardDemo {
  public static void main(String[] args) {
    // 1. Initialize the scoreboard
    ScoreBoard scoreBoard = new ScoreBoardImpl();

    // 2. Start new matches
    scoreBoard.startNewMatch("Mexico", "Canada");
    scoreBoard.startNewMatch("Spain", "Brazil");
    scoreBoard.startNewMatch("Germany", "France");

    // 3. Update scores (using exact new scores)
    scoreBoard.updateScore("Mexico", "Canada", 0, 5);
    scoreBoard.updateScore("Spain", "Brazil", 10, 2);
    scoreBoard.updateScore("Germany", "France", 2, 2);

    // 4. Retrieve and display the summary of ongoing matches
    // Expected order: Spain-Brazil (12), Mexico-Canada (5), Germany-France (4)
    System.out.println("--- Matches In Progress ---");
    List<Match> inProgress = scoreBoard.getSummaryOfMatchesInProgress();
    inProgress.forEach(match -> System.out.println(
            match.id().homeTeam() + " " + match.homeScore() + " - " +
                    match.guestScore() + " " + match.id().guestTeam()
    ));

    // 5. Finish a match (removes it from active, adds to finished)
    scoreBoard.finishMatch("Mexico", "Canada");

    // 6. Retrieve the historical archive (Custom Feature)
    System.out.println("\n--- Finished Matches Archive ---");
    List<Match> finishedMatches = scoreBoard.getFinishedMatches();
    finishedMatches.forEach(match -> System.out.println(
            match.id().homeTeam() + " vs " + match.id().guestTeam() +
                    " ended with final score " + match.homeScore() + "-" + match.guestScore()
    ));
  }
}
```

