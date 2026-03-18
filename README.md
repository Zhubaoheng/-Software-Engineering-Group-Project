# Teaching Assistant Recruitment System

Lightweight Java Servlet/JSP coursework project for the BUPT International School TA recruitment workflow.

## Tech Stack
- Java 17
- Maven WAR
- Jakarta Servlet / JSP
- JSON file storage

## Seed Accounts
- `ta01 / ta01`
- `ta02 / ta02`
- `ta03 / ta03`
- `mo01 / mo01`
- `admin01 / admin01`

## Main Flows Implemented
- TA profile create or edit
- CV upload metadata and file copy
- Browse open jobs and view job details
- Apply for a job and track application status
- MO create, edit, close, and review applicants
- Admin view TA workload dashboard

## Local Run
1. Install Maven 3.9+.
2. From the project root, run `mvn clean package`.
3. Deploy the generated WAR to a Jakarta Servlet 6 compatible container such as Tomcat 10.1.
4. Open `/login` and sign in with `ta01 / ta01`, `mo01 / mo01`, or `admin01 / admin01`.

## Data Files
Application data is stored in the `data/` directory as JSON files. The app creates missing files and upload folders on demand.

lswsb：LHR1105
