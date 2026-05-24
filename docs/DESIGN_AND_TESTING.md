# Design and Testing Notes

Supporting notes for the coursework report on the Teaching Assistant (TA) Recruitment
System. They describe the architecture, the main design decisions and trade-offs, the
design of the AI-assisted features, and the testing strategy. They are kept factual and
aligned with the actual code.

## 1. Layered Architecture

The system uses a conventional layered web architecture. A request flows through clearly
separated layers, each with a single responsibility:

```
HTTP request
   -> AuthenticationFilter   (cross-cutting access control)
   -> Servlet                (web layer: parse request, call service, forward to JSP)
   -> Service                (business logic, rules, AI-assisted calculations)
   -> Repository              (data access, JSON serialisation)
   -> JSON file               (persistence under data/)
```

- **AuthenticationFilter** is a `@WebFilter("/*")` that intercepts every request. It allows
  a small set of public paths (`/`, `/login`, `/register`, static assets) and redirects any
  other unauthenticated request to the login page, remembering the original GET target so
  the user returns there after signing in. This keeps access control in one place rather
  than scattered across servlets.
- **Servlets** form the web layer. Each servlet handles one route (for example
  `TAProfileServlet`, `MOJobsServlet`, `AdminWorkloadServlet`), reads request parameters,
  calls a service, places results as request attributes, and forwards to a JSP. Servlets
  contain no business rules.
- **Services** hold the business logic and the AI-assisted calculations
  (`DefaultMatchService`, `DefaultWorkloadService`, and the application/job/profile/auth
  services). They are plain Java classes with no servlet dependencies, which makes them
  directly unit-testable.
- **Repositories** wrap reading and writing the JSON files via Jackson, exposing typed
  collections to the services.
- **JSP pages** under `WEB-INF/jsp/` render the HTML and are reached only by server-side
  forwards, so they cannot be requested directly.

## 2. Key Design Decisions and Trade-offs

All three decisions below are driven by the coursework constraints (Java 17, Maven WAR,
Jakarta Servlet/JSP, JSON file storage, no database, no Spring).

- **JSON files instead of a database.** Persistence is plain JSON files under `data/`,
  read and written with Jackson. *Benefit:* zero setup, easy to inspect, easy to seed, and
  simple to demonstrate. *Trade-off:* no transactions, no concurrent-write safety, and no
  query language; the system is an MVP prototype, not production software, and this is
  acceptable at coursework scale.
- **Manual dependency injection via `AppContext`.** A single `AppContext` class constructs
  the repositories and services and exposes them as `static final` fields, wiring
  dependencies through constructors. *Benefit:* one obvious place that shows how the system
  is assembled, with no framework to learn. *Trade-off:* wiring is manual and global rather
  than container-managed; acceptable given the small, fixed object graph. Services that
  take their dependencies as constructor parameters can still be tested with substitutes.
- **Plain Servlet/JSP with no framework.** The web layer uses Jakarta Servlet annotations
  and JSP only. *Benefit:* the request lifecycle is fully visible and the codebase stays
  small and easy to defend at a viva. *Trade-off:* more boilerplate than a framework like
  Spring MVC would need, but the coursework explicitly rules out Spring Boot.

## 3. AI-Assisted Feature Design

The system has two AI-assisted features. Both are **rule-based, deterministic, and
explainable**, and both are presented as **advisory** — the coursework requires that AI
output is never blindly accepted, so a human always makes the final decision.

### 3.1 Explainable Skill Matching (`DefaultMatchService`)

The matcher compares a job's required skills against a TA profile's skills:

1. Every skill is normalised — trimmed and lower-cased.
2. A required skill is considered *covered* when an applicant skill is equal to it, or when
   one fully contains the other at word level (for example "software engineering" covers
   "software"). This tolerates minor wording differences without fuzzy guessing.
3. The score is the percentage of required skills covered, rounded to an integer.
4. The score maps to a STRONG (>= 75%), MODERATE (>= 40%), or WEAK level.
5. The service also produces a plain-English explanation naming how many skills are covered
   and which skills are missing.

A job with no required skills is treated as a full (100%) match; a profile with no skills
yet scores 0 with a prompt to complete the profile. On TA pages this guides the applicant;
on MO pages the applicant list is **ranked** by score, but the score is shown next to the
full profile so the MO can override it. The MO decides shortlist/select/reject.

### 3.2 Workload-Balancing Recommendations (`DefaultWorkloadService`)

The workload service aggregates assigned hours per TA from the assignment records and flags
anyone above the threshold (12 hours). To suggest a rebalance it:

1. Sorts TAs by total hours, descending, and processes the overloaded ones first.
2. For each overloaded TA, picks the single largest assignment as the candidate to move,
   because moving it frees the most hours.
3. Finds the receiver — the underloaded TA with the lowest current total whose total stays
   within the threshold after taking the moved assignment.
4. Emits a recommendation with a human-readable reason stating the hours involved and why
   the move keeps both TAs within the limit.
5. Updates working totals so successive suggestions remain mutually consistent.

If no safe receiver exists, no suggestion is made for that TA. The system performs no
automatic reassignment; the Admin reviews each suggestion and chooses whether to act.

### 3.3 Why a Transparent Rule-Based Approach

A transparent rule-based design was chosen deliberately over an opaque machine-learning
model or an external LLM API:

- **Explainability.** Every score and recommendation comes with the exact reason behind
  it, which is essential when the output influences a hiring or workload decision.
- **No external API dependency.** The features run entirely offline, with no API key, no
  cost, and no network call — the system builds and demonstrates reliably anywhere.
- **Defensible at a viva.** The behaviour is deterministic, so it can be reproduced and
  walked through line by line, and it is straightforward to unit test.
- **Appropriate scale.** With small, structured skill and assignment data, simple rules
  are accurate enough; a trained model would add opacity and complexity without benefit.

## 4. Testing Strategy

The project has a JUnit 5 test suite of **53 tests**. Testing focuses on the layers where
logic lives:

- **Unit tests for pure logic.** The services contain the rules and the AI-assisted
  calculations and have no servlet dependencies, so they are tested directly. Coverage
  includes skill-match scoring and levels (full match, no skills, partial match,
  substring/word-level coverage), workload aggregation and overload flagging, the
  rebalancing algorithm, and validation rules such as blocking duplicate active
  applications and applications to closed jobs.
- **Read-only tests against seed data.** Additional tests load the seeded JSON files and
  assert structural expectations — the seed accounts exist, profiles and jobs parse
  correctly, and identifiers are consistent — so the demo data stays valid as the project
  evolves. These tests do not mutate the data files.
- **Deterministic by design.** Because the AI-assisted features are rule-based, their
  outputs are exactly predictable, which makes assertions precise and stable.

Run the suite with `mvn test`; `mvn clean package` also runs it as part of the build.

## 5. Use of Generative AI in Development

Generative AI tools were used to assist development in an advisory capacity: drafting and
refining documentation, suggesting JSP layout and CSS, proposing test cases, and reviewing
code for clarity. All AI suggestions were read, tested, and adapted by the team before
being accepted, and the team remains responsible for the final code and its correctness.
This mirrors how the system itself treats AI output — as guidance for a human decision,
never as an automatic verdict.
