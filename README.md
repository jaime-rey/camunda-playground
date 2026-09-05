# camunda-playground

Learning module for Camunda 7 embedded in Spring Boot. Covers BPMN
processes (service tasks, user tasks, exclusive gateways, embedded
subprocesses, boundary timer events) and DMN decision tables (`FIRST`
and `UNIQUE` hit policies).

## Stack

- Java 21
- Spring Boot 3.5.x
- Camunda Platform 7.22 (BPM + DMN + REST + Web apps)
- H2 in-memory
- JUnit 5 + camunda-bpm-assert + AssertJ

## Run

```bash
./mvnw spring-boot:run
```

Then open `http://localhost:8080/camunda` (default cred: `admin` / `admin`).

## Tests

```bash
./mvnw verify
```

Unit-style DMN tests use `DecisionService.evaluateDecisionTableByKey(...)`.
Process tests walk the BPMN task by task using `camunda-bpm-assert`
(`assertThat(pi).isWaitingAt(...)`, `hasPassed(...)`, `complete(task(), ...)`).
Timer-driven paths advance `ClockUtil` and execute the pending job
through `ManagementService`.

## Processes

- `credit-request.bpmn` — credit card request flow (subprocess with
  timer, DMN-driven scoring, manual review path).
- `credit-approval.dmn` — approval decision table.
- `card-tier.dmn` — post-approval tier assignment (`FIRST` hit policy).
