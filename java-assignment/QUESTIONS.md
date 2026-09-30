# Questions

Here we have 3 questions related to the code base for you to answer. It is not about right or wrong, but more about what's the reasoning behind your decisions.

1. In this code base, we have some different implementation strategies when it comes to database access layer and manipulation. If you would maintain this code base, would you refactor any of those? Why?

**Answer:**
```txt
I would standardize the persistence boundary. Products use a repository, Stores use active record, and
Warehouses use a port/adaptor. The port/adaptor approach best isolates business rules from JPA and makes them
easy to unit test, so I would migrate the other resources incrementally rather than rewrite everything at once.
I would also add database constraints and migrations: application validation alone cannot prevent concurrent
requests from violating business identifiers and lifecycle rules.

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
I would begin with deterministic unit tests for duplicate code, location validity, capacity/count limits, stock
limits, archive, and replacement invariants. Next are HTTP integration tests for status codes, persistence and
transaction boundaries, followed by a few end-to-end tests for the legacy integration. CI publishes JaCoCo and
enforces 80% instruction coverage for location and warehouse core rules. Coverage is a guardrail, supplemented
by review of untested risk paths and contract tests for critical flows.

```
