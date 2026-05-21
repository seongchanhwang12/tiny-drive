# AGENTS.md

## Core Invariants

- Preserve aggregate boundaries at all costs
- Do NOT cross aggregates directly or introduce bidirectional aggregate coupling
- `parentId` hierarchy is the absolute source of truth
- Do NOT implement path-based identity logic
- Preserve domain behavior and aggregate consistency
- Avoid unnecessary infrastructure coupling in core business logic
- Prefer explicit separation between business rules and persistence concerns
- Preserve aggregate consistency over query convenience
- Upload lifecycle must remain asynchronous
- Binary data and metadata must remain separated
- Never couple upload completion to request transactions
- Prefer explicit domain modeling over generic abstraction or anemic models

---

## Global Forbidden Rules

Do NOT:

- Introduce Open Session In View (OSIV)
- Introduce uncontrolled fetch joins or new N+1 risks
- Expose JPA entities to API layer
- Define transactions outside application layer
- Create unnecessary long-running or nested transactions
- Access repositories from controllers
- Use field injection
- Introduce mutable shared static state
- Load large binaries fully into memory
- Block request threads with file processing
- Trust client-provided metadata
- Log JWTs or sensitive upload metadata
- Store secrets in source code
- Introduce hidden session state or authentication coupling

---

## Verification Pipeline

Use Gradle Wrapper only.

### Build

```bash
./gradlew build
```

### Test

```bash
./gradlew test
```

### Run

```bash
./gradlew bootRun
```

---

Execute sequentially. Any failure blocks further work or merge.

### 1. Domain Verification

```bash
./gradlew test --tests "*Domain*"
```

Validate:

- Aggregate invariants
- Domain rules
- Value object behavior

---

### 2. Repository Verification

```bash
./gradlew test --tests "*Repository*"
```

Validate:

- JPA mappings
- Query correctness
- N+1 risks

---

### 3. Application Verification

```bash
./gradlew test --tests "*Service*"
```

Validate:

- Use case orchestration
- Transaction boundaries
- Event flow consistency

---

### 4. Full Verification

```bash
./gradlew clean build dependencies
```

Required:

- Zero failing tests
- Zero compilation failures
- Zero unchecked dependency changes
- Zero transaction boundary regressions

---

## Merge Blocking Conditions

A change is invalid if it:

- Breaks aggregate boundaries
- Introduces bidirectional aggregate coupling
- Introduces new N+1 risks
- Makes upload lifecycle synchronous
- Couples upload completion to request transactions
- Expands transaction scope unnecessarily
- Introduces framework dependency into domain layer
- Exposes persistence models externally
- Introduces hidden state or session coupling
- Violates asynchronous processing guarantees

---

## Test Conventions

### General

- Prefer readable and behavior-focused tests.
- Follow BDD-style test structure: given / when / then.
- Use `@DisplayName` to describe business behavior.

### Mockito

- Prefer BDDMockito APIs over classic Mockito APIs.
- Use:
    - `given()` instead of `when()`
    - `then().should()` instead of `verify()`

Example:

``` java
given(userRepository.existsByEmail(email))
    .willReturn(true);

then(userRepository)
    .should()
    .save(any(User.class));
 ```

--- 

## Agent Behavioral Constraints

Before every modification:

1. Explicitly identify:
    - Target Aggregate
    - Target Use Case
    - Transaction Boundary

2. Verify the change complies with:
    - Core Invariants
    - Global Forbidden Rules
    - Merge Blocking Conditions

3. Prefer:
    - Small cohesive changes
    - Explicit domain behavior
    - Fail-fast validation
    - Architectural consistency over short-term convenience

4. Reject solutions that:
    - Increase hidden coupling
    - Blur aggregate ownership
    - Mix orchestration with domain behavior
    - Trade maintainability for premature optimization
    - Introduce implicit architectural behavior

## Communication Rules

- Use Korean for explanations, reasoning, analysis, and architectural discussions
- Keep code, APIs, class names, method names, and technical identifiers in English
- Preserve original technical terminology when translation reduces precision
- Prefer concise technical explanations over conversational wording
- Prioritize architectural clarity and technical correctness over natural language fluency