# Smoke Test Cases

These cases use the runtime seed data in `data/`.

## Case 1: Admin workload overload flag
- Open `/admin/workload`.
- Sign in with `admin01 / admin01`.
- Expected: `Alex Chen` appears as overloaded because assigned hours are `14`, which is above the default threshold of `12`.
- Expected: `Bella Li` and `Chris Wang` appear with `0` assigned hours and are normal.

## Case 2: Existing selection status is visible
- Sign in with `mo01 / mo01` and open `/mo/applicants`.
- Expected: `job-001` shows `Alex Chen` as `Selected` and `Bella Li` as `Shortlisted`.
- Expected: `job-002` shows `Alex Chen` as `Selected` and `Chris Wang` as `Under Review`.

## Case 3: Job availability is represented
- Sign in with `ta01 / ta01` and open `/ta/jobs`.
- Expected: `job-001` and `job-002` are visible as open jobs.
- Expected: `job-003` is not shown in the open-job list because its status is `CLOSED`.
