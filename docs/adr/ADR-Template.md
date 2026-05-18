# ADR-XXX: <Decision Title>

<!--
# 참고 자료!  
https://github.com/architecture-decision-record/architecture-decision-record
https://techblog.woowahan.com/22151/
https://americanopeople.tistory.com/380
-->

## Status

Proposed

<!--
Possible values:
- Proposed
- Accepted
- Deprecated
- Superseded
- Rejected
-->

---

## Context

Describe the background, requirements, constraints, and technical problems

that led to this architectural decision.

Example:

- Current system limitations

- Functional/non-functional requirements

- Performance considerations

- Scalability concerns

- Operational constraints

- Team or infrastructure constraints

---

## Decision

Clearly describe the architectural decision that was made.

Example:

- Adopt PostgreSQL as the primary relational database

- Use JWT for stateless authentication

- Separate file metadata from binary storage

---

## Rationale

Explain why this decision was made.

Consider including:

- Technical advantages

- Performance characteristics

- Consistency guarantees

- Scalability considerations

- Operational benefits

- Maintainability

- Ecosystem maturity

---

## Consequences

### Positive

Describe the expected benefits and improvements.

Example:

- Improved transactional consistency

- Better query performance

- Simplified operational model

### Negative

Describe drawbacks, limitations, or operational costs.

Example:

- Increased infrastructure complexity

- Vendor-specific dependency

- Additional learning curve

---

## Alternatives Considered

### Alternative 1

Describe the alternative and why it was not selected.

### Alternative 2

Describe the alternative and why it was not selected.

---

## Trade-offs

Describe what was prioritized and what was intentionally sacrificed.

Example:

- Prioritized consistency over simplicity

- Prioritized scalability over operational cost

---

## Assumptions

Document assumptions made during the decision process.

Example:

- Traffic volume will increase over time

- Metadata search requirements may expand

- Multi-region deployment may be required later

---

## Related ADRs

List related architectural decisions if applicable.

Example:

- ADR-002: Use JWT for Stateless Authentication

- ADR-003: Introduce Event-Driven File Processing

---

## References

List official documentation, benchmarks, articles, or internal documents.

Example:

- Official documentation

- Benchmark results

- Architecture diagrams

- RFC documents

---

## Date

YYYY-MM-DD


