# Questions

Here we have 3 questions related to the code base for you to answer. It is not about right or wrong, but more about what's the reasoning behind your decisions.

1. In this code base, we have some different implementation strategies when it comes to database access layer and manipulation. If you would maintain this code base, would you refactor any of those? Why?

**Answer:**
```txt
The code base intentionally shows three persistence styles: active record for Store, a Panache repository for
Product, and ports/adapters for Warehouse and Fulfilment. For new business-heavy modules I would use the
port/adapter boundary because the application use case and validator depend on an interface rather than JPA.
This keeps REST, persistence, and business rules separate and allows the rules to be unit tested with small
in-memory fakes.

I would not rewrite Store and Product solely for consistency; that creates risk without immediate value. I
would migrate incrementally when their business rules grow. At the database boundary, unique constraints are
kept for allocation identity, while application validators express user-friendly business errors. For a larger
production system I would also add versioned schema migrations and concurrency controls for the count-based
allocation rules.

```
----
2. When it comes to API spec and endpoints handlers, we have an Open API yaml file for the `Warehouse` API from which we generate code, but for the other endpoints - `Product` and `Store` - we just coded directly everything. What would be your thoughts about what are the pros and cons of each approach and what would be your choice?

**Answer:**
```txt
Contract-first OpenAPI supplies a reviewable language-neutral contract, generated DTOs and client SDKs, which
is valuable for a shared external API. Its costs are generation configuration and a need to keep the spec
precise. Code-first resources are faster for internal, fast-moving APIs but can drift from consumers unless
their generated specification is reviewed. I would use contract-first for public/cross-team APIs and code-first
with automated specification publication for small internal resources; both should have contract tests in CI.

```
----
3. Given the need to balance thorough testing with time and resource constraints, how would you prioritize and implement tests for this project? Which types of tests would you focus on, and how would you ensure test coverage remains effective over time?

**Answer:**
```txt
I would begin with deterministic unit tests for the dedicated Warehouse and Fulfilment validators: duplicate
codes, location validity, capacity/count limits, stock limits, archive/replacement invariants, allocation
duplicates, and all three bonus limits. These tests are fast and protect the highest-risk business rules.

Next are HTTP integration tests for status codes, persistence, and transaction boundaries. Store changes publish
a CDI event, and an `AFTER_SUCCESS` observer calls the legacy gateway only after the database commit; this should
have focused integration tests for both successful and rolled-back transactions. GitHub Actions runs Maven
verification, publishes the JaCoCo artifact, and enforces at least 80% instruction coverage for the core
Location, Warehouse, and Fulfilment domain rules. Coverage is a guardrail, complemented by code review and
contract tests for critical external APIs.

```
