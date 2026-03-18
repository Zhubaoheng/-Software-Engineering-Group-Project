package cn.bupt.tarecruitment.repository;

import cn.bupt.tarecruitment.model.JobPost;
import cn.bupt.tarecruitment.util.AppPaths;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JobRepository extends JsonRepository<JobPost> {
    public JobRepository() {
        super(AppPaths.JOBS_FILE, JobPost.class);
    }

    public List<JobPost> findAll() {
        return readAll();
    }

    public Optional<JobPost> findById(String jobId) {
        if (jobId == null) {
            return Optional.empty();
        }
        return findAll().stream()
                .filter(job -> jobId.equals(job.getJobId()))
                .findFirst();
    }

    public List<JobPost> findOpenJobs() {
        return findAll().stream()
                .filter(job -> "OPEN".equalsIgnoreCase(job.getStatus()))
                .sorted(Comparator.comparing(JobPost::getDeadline, Comparator.nullsLast(String::compareTo)))
                .collect(Collectors.toList());
    }

    public void save(JobPost jobPost) {
        List<JobPost> jobs = new ArrayList<>(findAll());
        jobs.removeIf(item -> jobPost.getJobId().equals(item.getJobId()));
        jobs.add(jobPost);
        writeAll(jobs);
    }
}
