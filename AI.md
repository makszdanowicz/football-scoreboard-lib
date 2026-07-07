# AI Tools Usage

## 1. Summary of AI Usage

Google Gemini were used as a design and review assistant throughout the implementation of the Live Football World Cup Scoreboard library.

The AI was used for:

- **Requirements analysis:** reviewing the task description, identifying hidden business rules, edge cases and assumptions that needed to be documented.
- **Design discussions:** validating architectural decisions such as introducing a `ScoreBoard` interface, choosing domain models (`Match`, `MatchId`) and deciding how to store active matches.
- **TDD support:** reviewing test scenarios, checking whether tests correctly represented business requirements, and suggesting improvements.
- **Concurrency analysis:** discussing possible thread-safety risks and evaluating synchronization strategies such as `synchronized`, concurrent collections and `ReentrantReadWriteLock`.
- **Documentation support:** improving the structure and clarity of `README.md` and documenting assumptions, reasoning and trade-offs.

All final design decisions, implementation choices and code changes were reviewed and implemented manually.

---

# 2. Prompt History and Contextual Information

The following prompts represent the main discussions that influenced the implementation.

## Phase 1 - Understanding Requirements and Defining Domain Rules

### Context

I started by providing the full coding exercise description and asking AI to help analyze the requirements before writing code.

### Prompt

> Otrzymałem zadanie do wykonania. Pomóż mi je zrozumieć, rozebrać na części i przygotować się do implementacji.

## Phase 2 - TDD Approach and Initial Design

### Context

Before implementation, I wanted to define the domain model and API contract.

### Prompt

> Chcę wykonać to zadanie w TDD. Pomóż mi najpierw zdefiniować wymagania biznesowe i sprawdzić, na jakie przypadki powinienem zwrócić uwagę przed implementacją.


## Phase 3 - Active Matches Design and Business Validation



### Context



Before implementing the scoreboard logic, I analyzed how to prevent invalid domain states and how active matches should be stored.



The main questions I wanted to validate were:



- How should active matches be stored to allow efficient lookup and updates?

- How can the system prevent a team from participating in multiple matches simultaneously?

- What validation steps should i check?

- What should be used as a unique identifier for a match?



### Prompt



> Przeanalizuj moje założenia projektowe dla aktywnych meczów:
> Chcę przechowywać aktywne mecze w strukturze, która pozwoli szybko znaleźć konkretny mecz podczas aktualizacji wyniku lub zakończenia meczu. Rozważam użycie HashMap.
> Jako klucz chciałbym użyć własnej struktury MatchId zawierającej homeTeam i guestTeam. Czy jest to dobre podejście? Jakie inne opcje można rozważyć?



### Prompt



> Chcę dodać regułę biznesową: jedna drużyna nie może jednocześnie uczestniczyć w dwóch aktywnych meczach.
> Przykład:
> Jeśli istnieje mecz Mexico - Canada, próba rozpoczęcia Mexico - Brazil powinna zakończyć się wyjątkiem.
> Czy użycie Set<String> teamsInPlay do przechowywania aktualnie grających drużyn jest dobrym rozwiązaniem?



### Prompt



> Jakie kroki walidacyjne mam rozważyć (naprzykład sprawdzenie gdy użytkownik próbuje zaktualizować wynik lub zakończyć mecz, czy on istnieje)?



### Outcome



The final design decisions were:



- `Map<MatchId, Match>` is used to store active matches because it provides efficient lookup by match identifier.

- `MatchId` is implemented as a dedicated value object containing:

    - `homeTeam`

    - `guestTeam`

- `Set<String> teamsInPlay` is used to efficiently validate whether a team is already participating in an active match.

- Invalid operations, such as updating or finishing a non-existing match, result in an exception.
---

## Phase 4 - Ordering Strategy: Sequence vs Timestamp

### Context

The requirements specify ordering by the most recently started match when scores are equal.

### Prompt

> Dla sortowania przy remisie: czy powinienem używać Instant startTime, czy lepiej przechowywać własny order? Jakie są zalety i wady obu rozwiązań?

### Outcome

After discussing the trade-offs, I chose:

- internal monotonically increasing `sequence`
- deterministic ordering
- stable unit tests without dependency on system clock

---

## Phase 5 - Thread Safety and Concurrent Access

### Context

I considered that the scoreboard library may be used by multiple threads.

### Prompt

> Tablica wyników jako biblioteka może być aktualizowana przez wiele wątków naraz. Jakie problemy mogą wystąpić i jakie mechanizmy synchronizacji można zastosować?

### Prompt

> Wyjaśnij jak działa ReadWriteLock w Javie i kiedy warto go użyć zamiast synchronized.

### Outcome

This led to introducing:

- `ReentrantReadWriteLock`
- read locks for queries
- write locks for state modifications

---

## Phase 6 - Code Review and Refactoring

### Context

During implementation, I provided my tests and implementation for review.

### Prompt

> Sprawdź moje testy i implementację zgodnie z TDD. Czy kolejność implementacji jest poprawna? Czy są miejsca wymagające refaktoryzacji?

### Outcome

The review helped improve:

- test readability
- API naming
- separation between public contract and implementation details

---

## Phase 7 - Documentation Creation

### Prompt

> Na podstawie wymagań zadania i naszej rozmowy pomóż przygotować README.md. Dokumentacja powinna zawierać assumptions, reasoning, trade-offs oraz instrukcję uruchomienia projektu.

### Outcome

This resulted in creating:

- README.md
- documented assumptions
- architectural decisions
- implementation trade-offs
- build and test instructions

---

# 3. Artifacts That Guided Implementation

The following artifacts and conclusions from AI-assisted discussions influenced the final implementation:

## Domain Model Decisions

The discussion resulted in the following model:

- `MatchId` - identifies a match.
- `Match` - immutable representation of current match state.
- `ScoreBoard` - public API contract.

---

## Sequence Ordering Decision

AI discussion helped evaluate timestamp-based ordering versus deterministic sequence numbers.

The final implementation uses:

```java
private long sequence;
```

because the requirement needs relative ordering, not the actual kickoff time.

---

## Thread Safety Design

The final concurrency approach was influenced by discussion around:

- mutable shared collections,
- race conditions,
- immutable objects,
- locking strategies.

The implementation uses:

```java
private final ReadWriteLock lock =
        new ReentrantReadWriteLock();
```

to protect shared state.

---

## README and Project Documentation

AI assistance was used to structure project documentation according to the requested code review standards:

- assumptions,
- reasoning,
- trade-offs,
- running instructions.
