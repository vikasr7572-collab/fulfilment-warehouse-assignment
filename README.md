# Fulfilment Warehouse Assignment

[![Build and test](https://github.com/vikasr7572-collab/fulfilment-warehouse-assignment/actions/workflows/ci.yml/badge.svg)](https://github.com/vikasr7572-collab/fulfilment-warehouse-assignment/actions/workflows/ci.yml)

A Quarkus-based Java application for managing stores, products, warehouses, and fulfilment allocations. This repository contains the completed coding assessment, supporting case study, automated tests, and GitHub Actions quality checks.

## Highlights

- REST endpoints for stores, products, and warehouse lifecycle operations.
- Warehouse validation for unique business-unit codes, locations, capacities, stock, archiving, and replacement.
- Transaction-safe Store integration: the legacy-system notification runs only after a successful database commit.
- Bonus fulfilment allocation feature enforcing the requested store, product, and warehouse limits.
- Embedded H2 for local development and tests; PostgreSQL Docker Compose configuration for the production profile.
- 12 automated tests and a JaCoCo coverage gate enforced by GitHub Actions.

## Run locally

From the `java-assignment` directory:

```powershell
.\mvnw.cmd verify
.\mvnw.cmd quarkus:dev
```

The application starts at `http://localhost:8080`. Full endpoint and Docker instructions are in the [application README](java-assignment/README.md).

## Documentation

- [Assignment requirements](java-assignment/CODE_ASSIGNMENT.md)
- [Technical questions and answers](java-assignment/QUESTIONS.md)
- [Case study](case-study/CASE_STUDY.md)
- [CI workflow](.github/workflows/ci.yml)

