# Live Football World Cup Scoreboard

## Overview
This repository contains a simple Java library for managing a Live Football World Cup Scoreboard. It allows consumers to track ongoing matches, update scores, finish matches and retrieve a dynamically ordered summary of matches currently in progress.

---

## Requirements Analysis
The library implements the following core operations:
* **Start a new match:** Initializes a match with a 0-0 score and records the start time.
* **Update the score:** Allows updating the score with absolute, non-negative integer values.
* **Finish a match:** Removes the match from the active scoreboard.
* **Get a summary:** Returns all ongoing matches ordered by total score (descending). In case of a tie, the most recently started match is listed first (resolved internally via an exact insertion sequence order).
* **[PLACEHOLDER - Custom Feature]:** *(Describe your 5th feature here later)*

---

## Business Assumptions (v1.0 - Initial Design)
Before implementing the code, the following domain rules and constraints were established:

* **Unique Teams:** A specific team can only play one match at a time. The system will reject attempts to start a match if either the home or away team is already on the active scoreboard.
* **Self-Play Restriction:** A team cannot play against itself (e.g., "Mexico" vs "Mexico" is invalid).
* **Non-negative Scores:** Match scores must always be positive integers or zero.
* **Absolute Score Updates:** The `updateScore` method requires the exact new score rather than an incremental value (+1). This ensures idempotency and guards against network/event duplication in enterprise environments.
* **Case-Insensitive Team Names:** "Mexico" and "MEXICO" are treated as the same entity to prevent duplicates.
* **Deterministic Sorting via Sequence:** To determine the "most recently started match" in the event of a tied score, the system uses a monotonically increasing sequence (insertion order).
---

## How to Run

### Prerequisites
* Java 21 or higher
* Apache Maven 3.8+
* Git

### Installation and Testing
To clone the repository and run the test suite, execute the following commands in your terminal:

```bash
# Clone the repository
git clone git@github.com:makszdanowicz/football-scoreboard-lib.git

# Navigate into the project directory
cd scoreboard-library

# Clean the project and run all unit tests
mvn clean test

# Build the project package (JAR file)
mvn clean install
```

### Usage Example

Below is a simple demonstration of how to integrate and use the Scoreboard library in a Java application.
```java
    // TODO
```

