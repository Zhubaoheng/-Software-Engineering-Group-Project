# Teaching Assistant Recruitment System

Lightweight Java Servlet/JSP coursework project for the BUPT International School TA recruitment workflow.

## Project Overview

This system replaces spreadsheet-based TA recruitment with a simple web application for three roles:

- `TA Applicant`: maintain profile, upload CV, browse jobs, apply, track status
- `Module Organiser (MO)`: create job posts, review applicants, shortlist/select candidates
- `Admin`: monitor TA workload and identify overload risk

The project follows the coursework constraints:

- Java 17
- Maven WAR project
- Jakarta Servlet / JSP
- JSON file storage only
- No database
- No Spring Boot

## Implemented Features

- Login by role
- New user self-registration at `/register` (creates a TA Applicant account)
- TA profile create and edit
- CV file upload, metadata save, and in-browser CV preview
- Browse open TA jobs
- View job details
- Apply for a job
- View application status list
- MO create, edit, and close job posts
- MO review applicants and update decisions
- Admin workload dashboard
- AI-assisted explainable skill matching on TA job pages and MO applicant pages: each job/applicant shows a match score, matched skills and missing skills, and MO applicant lists are ranked by match
- AI-assisted workload-balancing recommendations on the Admin dashboard: overload detection plus suggested reassignments with a human-readable reason

## Installation & Startup

A step-by-step walk-through. The whole process takes around 5 minutes on a fresh machine.

### Prerequisites

| Software | Tested version | How to check |
| --- | --- | --- |
| JDK | **17** (LTS) | `java -version` should print `openjdk version "17..."` or `java version "17..."`. JDK 18-21 also work; do not use JDK 8 / 11. |
| Maven | **3.9+** | `mvn -version` should print `Apache Maven 3.9...` or newer. |
| Apache Tomcat | **10.1.x** (Servlet 6) | `catalina version` should print `Apache Tomcat/10.1.xx`. Older Tomcat 9.x / 8.x **will not work** — the project uses Jakarta Servlet 6 (the `jakarta.*` package, not `javax.*`). |
| Web browser | any modern | Chrome / Edge / Firefox / Safari all work. |

Quick install hints if you are missing something:

- macOS (Homebrew): `brew install openjdk@17 maven tomcat@10`
- Ubuntu / Debian: `sudo apt install openjdk-17-jdk maven` plus Tomcat 10.1 from the [Apache mirror](https://tomcat.apache.org/download-10.cgi)
- Windows: install the official JDK 17 MSI, the Maven binary zip (add `bin/` to `PATH`), and the Tomcat 10.1 Windows Service Installer

### Step 1 — Get the source code

If you received this as a ZIP, unzip it and `cd` into the project root. Otherwise:

```bash
git clone https://github.com/Zhubaoheng/-Software-Engineering-Group-Project.git
cd -Software-Engineering-Group-Project
```

You should see `pom.xml`, `src/`, and `data/` at the top level. **All commands below are run from this project root unless stated otherwise.**

### Step 2 — Build the WAR

```bash
mvn clean package
```

This compiles every source file, runs the unit-test suite (53 tests should pass), and packages a deployable WAR. Expect to see:

```text
[INFO] Tests run: 53, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

The produced artifact:

```text
target/ta-recruitment-system-1.0.0-SNAPSHOT.war
```

To run just the tests without producing a WAR:

```bash
mvn test
```

To generate the JavaDoc HTML (output goes to `target/site/apidocs/`):

```bash
mvn javadoc:javadoc
```

### Step 3 — Deploy the WAR into Tomcat

Find your Tomcat install. Common locations:

- macOS (Homebrew): `/opt/homebrew/opt/tomcat@10/libexec` (Apple-silicon) or `/usr/local/opt/tomcat@10/libexec` (Intel)
- Linux / manual install: wherever you unpacked Tomcat (`apache-tomcat-10.1.xx/`)
- Windows: `C:\Program Files\Apache Software Foundation\Tomcat 10.1`

That folder is your **`CATALINA_HOME`**. Copy the WAR into its `webapps/` directory:

```bash
# macOS / Linux example
cp target/ta-recruitment-system-1.0.0-SNAPSHOT.war "$CATALINA_HOME/webapps/"
```

```powershell
# Windows PowerShell example
Copy-Item target\ta-recruitment-system-1.0.0-SNAPSHOT.war "$env:CATALINA_HOME\webapps\"
```

If a previous version is already deployed there, delete the old `.war` **and** the unpacked folder of the same name before copying the new WAR:

```bash
rm -f "$CATALINA_HOME/webapps/ta-recruitment-system-1.0.0-SNAPSHOT.war"
rm -rf "$CATALINA_HOME/webapps/ta-recruitment-system-1.0.0-SNAPSHOT"
```

### Step 4 — Start Tomcat ⚠️ MUST run from the project root

The application stores runtime data under `System.getProperty("user.dir") + /data`. That means **Tomcat must be launched with the project root as its working directory**, otherwise the seed accounts won't load and login will fail.

The reliable recipe is: `cd` into the project root first, then call Tomcat's startup script with `CATALINA_HOME` pointed at your Tomcat install.

```bash
# macOS / Linux
cd /path/to/-Software-Engineering-Group-Project
CATALINA_HOME=/opt/homebrew/opt/tomcat@10/libexec \
  "$CATALINA_HOME/bin/catalina.sh" start
```

```powershell
# Windows PowerShell
cd C:\path\to\-Software-Engineering-Group-Project
$env:CATALINA_HOME = "C:\Program Files\Apache Software Foundation\Tomcat 10.1"
& "$env:CATALINA_HOME\bin\catalina.bat" start
```

After startup, watch the log to confirm deployment:

```bash
tail -f "$CATALINA_HOME/logs/catalina.out"   # macOS / Linux
```

You should see a line containing `Deployment of web application archive [...ta-recruitment-system-1.0.0-SNAPSHOT.war] has finished in [...] ms`. Then press `Ctrl-C` to stop tailing — Tomcat keeps running in the background.

### Step 5 — Open the application

Browse to:

```text
http://localhost:8080/ta-recruitment-system-1.0.0-SNAPSHOT/
```

The root redirects to `/login`. Sign in with any of the seeded accounts (see the next section). If login succeeds, your data path is correct.

### Step 6 — Stop Tomcat when you are done

```bash
# macOS / Linux
"$CATALINA_HOME/bin/catalina.sh" stop
```

```powershell
# Windows
& "$env:CATALINA_HOME\bin\catalina.bat" stop
```

### Alternative: run from an IDE (IntelliJ IDEA / Eclipse)

1. Import the project as a **Maven** project (`File → Open` and pick `pom.xml`).
2. Add a **Tomcat 10.1** server in `Run → Edit Configurations`.
3. Deploy the exploded WAR artifact (`ta-recruitment-system-1.0.0-SNAPSHOT:war exploded`).
4. **Set the run-configuration working directory to the project root** (in IntelliJ: the Server tab → "Working directory" field). This is the IDE equivalent of Step 4 above.
5. Start the server. The IDE will open the app in your browser automatically.

### Troubleshooting

- **Login always fails with the seed accounts.**
  Tomcat is not running from the project root, so the app looks at the wrong `data/` folder. Stop Tomcat, repeat Step 4 with `cd` into the project root before starting, or copy the project's `data/` folder into Tomcat's own working directory.
- **Port 8080 already in use.**
  Either stop the other process (`lsof -i :8080` on macOS / Linux, `netstat -ano | findstr :8080` on Windows) or change Tomcat's port in `$CATALINA_HOME/conf/server.xml` (look for `Connector port="8080"`).
- **`BUILD FAILURE` complaining about Java version.**
  You are on JDK < 17. Install JDK 17+ and re-run.
- **CSS / pages look unstyled or 404 on `/assets/...`.**
  An old deployment is being served. Remove both the old `.war` and the unpacked folder under `webapps/`, then copy the new WAR and restart Tomcat.
- **App shows empty profile / job lists after deployment to a different machine.**
  The new working directory's `data/` is empty. Either restart Tomcat from this project's root (which contains the seed `data/`), or copy this project's `data/` folder into the working directory you are launching Tomcat from.

## Seed Accounts

- `ta01 / ta01`
- `ta02 / ta02`
- `ta03 / ta03`
- `mo01 / mo01`
- `admin01 / admin01`

## Recommended Demo Order

### TA Demo

1. Login as `ta01 / ta01`
2. Open `Profile`
3. Update personal details and optionally upload a CV
4. Open `Open Jobs`
5. View a job detail page
6. Apply for a job
7. Open `Applications` to check status

### MO Demo

1. Login as `mo01 / mo01`
2. Open `Job Posts`
3. Create or edit a job
4. Open `Applicants`
5. Review an applicant
6. Change decision to `Shortlisted`, `Selected`, or `Rejected`

### Admin Demo

1. Login as `admin01 / admin01`
2. Open `Workload Board`
3. Review total assigned hours and overload flags

## Main Interfaces And Routes

### Public / Shared

| Route | Method | Purpose |
| --- | --- | --- |
| `/` | GET | Redirects to `/login` |
| `/login` | GET, POST | Login page and authentication |
| `/register` | GET, POST | New user registration form and account creation |
| `/logout` | GET | Clear session and return to login |
| `/role-home` | GET | Generic landing page for logged-in users |
| `/cv/preview` | GET | Stream an uploaded CV file for in-browser preview |

### TA Interfaces

| Route | Method | Purpose |
| --- | --- | --- |
| `/ta/profile` | GET, POST | Create or update applicant profile and CV |
| `/ta/jobs` | GET | Browse open jobs |
| `/ta/job-detail` | GET | View a selected job's details |
| `/ta/apply` | POST | Submit a job application |
| `/ta/applications` | GET | View submitted applications and statuses |

### MO Interfaces

| Route | Method | Purpose |
| --- | --- | --- |
| `/mo/jobs` | GET, POST | List jobs, open form pages, create/edit/close posts |
| `/mo/applicants` | GET | View applicants for a selected job |
| `/mo/applicants/review` | GET, POST | Review one applicant and update decision |

### Admin Interfaces

| Route | Method | Purpose |
| --- | --- | --- |
| `/admin/workload` | GET | Show TA workload summary and overload warning |

## Data Files

Application data is stored in plain JSON files under `data/`.

| File | Function |
| --- | --- |
| `data/users.json` | Login accounts and user roles |
| `data/profiles.json` | TA profile data |
| `data/jobs.json` | Job post records |
| `data/applications.json` | Application records and statuses |
| `data/assignments.json` | Confirmed workload assignments for admin calculation |
| `data/uploads/` | Uploaded CV files |

Missing files and directories are created automatically when the application starts.

## Key Source Files And Responsibilities

### Core Configuration

| File | Function |
| --- | --- |
| `pom.xml` | Maven build configuration |
| `src/main/java/cn/bupt/tarecruitment/context/AppContext.java` | Creates repositories and services |
| `src/main/java/cn/bupt/tarecruitment/util/AppPaths.java` | Defines runtime data file locations |

### Models

| Folder | Function |
| --- | --- |
| `src/main/java/cn/bupt/tarecruitment/model/` | Domain models such as users, jobs, applications, workload rows |

### Data Access

| Folder | Function |
| --- | --- |
| `src/main/java/cn/bupt/tarecruitment/repository/` | JSON-based repositories for reading and writing data |

### Business Logic

| Folder | Function |
| --- | --- |
| `src/main/java/cn/bupt/tarecruitment/service/` | Service interfaces |
| `src/main/java/cn/bupt/tarecruitment/service/impl/` | Core business logic implementations |

### Web Layer

| Folder | Function |
| --- | --- |
| `src/main/java/cn/bupt/tarecruitment/servlet/` | HTTP endpoints for TA, MO, Admin, and login |
| `src/main/webapp/WEB-INF/jsp/` | JSP pages for the user interface |
| `src/main/webapp/assets/app.css` | Shared frontend styling |

## Validation And Business Rules

- A TA must log in before accessing TA pages.
- A duplicate active application for the same job is blocked.
- Closed jobs cannot receive new applications.
- MO decisions update application status.
- Workload is calculated from assignment records.
- Overload is highlighted on the admin dashboard.

## AI-Assisted Features

The system includes two "AI-assisted" features. Both are **rule-based, deterministic, and
explainable** — they call no external model or API, and the same input always produces the
same output. Crucially, both are **advisory only**: the coursework requires that AI output
is never blindly accepted, so the system always leaves the final decision to a human.

### Explainable Skill Matching

Each open job lists a set of required skills, and each TA profile lists the skills the
applicant has. The match service normalises both lists (trimmed, lower-cased), counts how
many required skills are covered, and produces a score (percentage of required skills
covered), a STRONG / MODERATE / WEAK level, and a plain-English explanation naming the
matched and missing skills.

- On TA job pages the score helps an applicant judge fit and see which skills to strengthen.
- On MO applicant pages the applicants are ranked by match score, but the score is shown
  alongside the full profile so the MO can override the ranking. The MO, not the algorithm,
  decides who is shortlisted, selected, or rejected.

### Workload-Balancing Recommendations

The Admin dashboard aggregates assigned hours per TA from the assignment records and flags
any TA above the overload threshold (12 hours). For each overloaded TA the workload service
suggests moving a specific assignment to a specific underloaded TA, and prints a
human-readable reason explaining the hours involved and why the move keeps both TAs within
the limit.

These recommendations are suggestions only. The Admin reviews them and decides whether to
act; the system performs no automatic reassignment.

## Current Limitations

- No database is used; all persistence is file-based.
- Authentication is simplified for coursework demonstration.
- The system is designed as an MVP prototype and not as a production-ready platform.
Application data is stored in the `data/` directory as JSON files. The app creates missing files and upload folders on demand.


## Team Members

tokidosaya010：231224756(Team member)

LHR1105：221171305

colin596：231225085

zhubaoheng: 221170962

lswsb: 2024018006 (Support TA)

RongjiaLiu: 231224608
