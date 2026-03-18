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

## Implemented MVP

- Login by role
- TA profile create and edit
- CV file upload and metadata save
- Browse open TA jobs
- View job details
- Apply for a job
- View application status list
- MO create, edit, and close job posts
- MO review applicants and update decisions
- Admin workload dashboard

## Environment Requirements

- JDK `17`
- Maven `3.9+`
- A Servlet 6 compatible container such as `Tomcat 10.1`

## Build

From the project root:

```bash
mvn clean package
```

Generated artifact:

```text
target/ta-recruitment-system-1.0.0-SNAPSHOT.war
```

## How To Start The Project

### Option 1: Deploy To Tomcat 10.1

1. Build the project:

```bash
mvn clean package
```

2. Copy the generated WAR to Tomcat:

```bash
cp target/ta-recruitment-system-1.0.0-SNAPSHOT.war /path/to/tomcat/webapps/
```

3. Start Tomcat.

4. Open the application:

```text
http://localhost:8080/ta-recruitment-system-1.0.0-SNAPSHOT/
```

The app entry page redirects to:

```text
/login
```

### Option 2: Run From IDE With Local Tomcat

1. Import the project as a Maven project.
2. Configure a local `Tomcat 10.1` server.
3. Deploy the WAR or exploded WAR artifact.
4. Start the server and open the deployed context path.

## Important Runtime Data Note

This project stores runtime data using:

```text
System.getProperty("user.dir") + /data
```

That means the active `data/` directory depends on the working directory of the server process.

In practice:

- If the server is started from the project root, the app uses this repository's `data/` folder.
- If the server is started from Tomcat's own folder, the app may create and use `TOMCAT_WORKING_DIR/data/` instead.

If the seeded accounts do not work after deployment, copy the project `data/` folder to the server working directory, or configure the server so that its working directory is the project root.

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
| `/logout` | GET | Clear session and return to login |
| `/role-home` | GET | Generic landing page for logged-in users |

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

## Current Limitations

- No database is used; all persistence is file-based.
- Authentication is simplified for coursework demonstration.
- The system is designed as an MVP prototype and not as a production-ready platform.
