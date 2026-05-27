# 🧩 ActivityMaster Core

[![CI](https://github.com/Activity-Master/Core/actions/workflows/maven-verify.yml/badge.svg)](https://github.com/Activity-Master/Core/actions/workflows/maven-verify.yml)
[![Issues](https://img.shields.io/github/issues/Activity-Master/Core)](https://github.com/Activity-Master/Core/issues)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)](https://github.com/Activity-Master/Core/pulls)
[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![JDK](https://img.shields.io/badge/JDK-25%2B-0A7?logo=java)](https://openjdk.org/projects/jdk/25/)
[![Build](https://img.shields.io/badge/Build-Maven-C71A36?logo=apachemaven)](https://maven.apache.org/)

<!-- Tech icons row -->
![Vert.x](https://img.shields.io/badge/Vert.x-5-4B9?logo=eclipsevertdotx&logoColor=white)
![Hibernate Reactive](https://img.shields.io/badge/Hibernate-Reactive_7-59666C?logo=hibernate)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15+-4169E1?logo=postgresql&logoColor=white)
![Guice](https://img.shields.io/badge/Guice-7-2F4F4F)
![GuicedEE](https://img.shields.io/badge/GuicedEE-Core-0A7)

Open-source implementation of the Functional Service Data Model (FSDM) services — Enterprise, Address, Events, Arrangements, ResourceItem, Classification, and the supporting security/ActiveFlag infrastructure. Built on **Java 25+**, **Vert.x 5**, **GuicedEE**, **Hibernate Reactive 7**, and **PostgreSQL**, the library exposes Guice client interfaces so downstream applications can manage the canonical warehouse schema without a dedicated desktop client.

Built on [Vert.x 5](https://vertx.io/) · [Google Guice](https://github.com/google/guice) · [Hibernate Reactive](https://hibernate.org/reactive/) · [Mutiny](https://smallrye.io/smallrye-mutiny/) · JPMS module `com.guicedee.activitymaster` · Java 25+

## ✨ Features

- **Canonical FSDM domain services** — Enterprise, Address, Events, Arrangements, ResourceItem, Classification
- **Reactive persistence** — Hibernate Reactive 7 + PostgreSQL via Vert.x reactive SQL clients
- **Vert.x 5 integration** — async workflows, event-bus messaging, and verticle deployment
- **GuicedEE DI bootstrap** — lifecycle hooks, post-startup actions, and ServiceLoader-driven module discovery
- **Security token propagation** — `SecurityToken` metadata on every service call with ActiveFlag row-state enforcement
- **Enterprise lifecycle** — `createNewEnterprise` → `loadUpdates` → `startNewEnterprise` bootstrapping order with `ISystemUpdate`/`@SortedUpdate` classification loading and `IPasswordsService` admin registration

## 📦 Installation

```xml
<dependency>
  <groupId>com.activity-master</groupId>
  <artifactId>activity-master</artifactId>
</dependency>
```

<details>
<summary>Gradle (Kotlin DSL)</summary>

```kotlin
implementation("com.activity-master:activity-master")
```
</details>

## 🚀 Quick Start

```bash
cp .env.example .env   # update DB credentials + toggles
mvn -B clean verify    # compilation + tests (uses Testcontainers)
```

Consume services via the Activity Master Client module in your host app.

## ⚙️ Configuration

### Environment Variables

Copy `.env.example` to `.env` for local development. Keep secrets out of version control.

| Variable | Purpose | Default |
|---|---|---|
| `DB_URL` | Database JDBC URL | — |
| `DB_USER` | Database username | — |
| `DB_PASS` | Database password | — |
| `JWT_TEST_TOKEN` | JWT token for test harness | — |
| `TEST_DB_CONTAINER_IMAGE` | Testcontainers Postgres image | `postgres:latest` |
| `OAUTH2_ISSUER_URL` | OAuth2 issuer URL | — |
| `JWKS_URI` | JSON Web Key Set URI | — |
| `ENVIRONMENT` | Runtime environment | `dev` |

CI secrets (`SONA_USERNAME`, `SONA_PASSWORD`, `GPG_PRIVATE_KEY`, `GPG_PASSPHRASE`, `GITHUB_ACTOR`, `GITHUB_TOKEN`) are managed via GitHub Actions repository/environment secrets.

## 🧩 JPMS & SPI

Module name: **`com.guicedee.activitymaster`**

JPMS-friendly with ServiceLoader discovery for DI and lifecycle hooks.

```java
module my.app {
    requires com.guicedee.activitymaster;
    requires com.guicedee.client;
    requires com.guicedee.persistence;

    opens my.app.entities to org.hibernate.orm.core, com.google.guice, com.entityassist;
    opens my.app.services to com.google.guice;

    provides com.guicedee.client.services.lifecycle.IGuiceModule
        with my.app.AppModule;
}
```

## 🧪 Testing

The test harness uses Testcontainers to spin up a PostgreSQL instance automatically. Tests enforce the enterprise lifecycle order:

1. `createNewEnterprise` — bootstrap the enterprise
2. `loadUpdates` — classifications and types loaded via `ISystemUpdate`/`@SortedUpdate`
3. `startNewEnterprise` — admin user registered through `IPasswordsService`, post-startup actions execute

```bash
# Run all tests
mvn -B clean verify

# Skip integration tests
mvn -B clean verify -DskipITs
```

CI via `.github/workflows/maven-verify.yml` installs Java 25, sets the required secrets, and executes `mvn -B verify`.

## 🧰 Troubleshooting & Best Practices

- Verify `.env` is present for local runs and matches the required variables
- Every service propagates `SecurityToken` metadata and respects ActiveFlag row status — changes in capabilities must describe how value-level access is enforced
- Keep transactions short; chain `Uni` calls and reuse a single session within `withTransaction`
- Use Testcontainers for local integration testing to avoid external database dependencies

## 🤝 Contributing

Issues and pull requests are welcome.

- Follow existing code style and patterns
- Include tests for new features
- Update documentation for behavior changes
- Ensure JPMS `module-info.java` is correct
- Run `mvn -B clean verify` before submitting a PR

## 📄 License

[Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0)

## Repository & Links

- GitHub: https://github.com/Activity-Master/Core
- Issues: https://github.com/Activity-Master/Core/issues
- Pull Requests: https://github.com/Activity-Master/Core/pulls
