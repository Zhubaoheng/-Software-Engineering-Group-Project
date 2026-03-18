package cn.bupt.tarecruitment.service.impl;

import cn.bupt.tarecruitment.model.JobPost;
import cn.bupt.tarecruitment.repository.JobRepository;
import cn.bupt.tarecruitment.service.JobService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DefaultJobService implements JobService {
    private final JobRepository jobRepository;

    public DefaultJobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Override
    public List<JobPost> listOpenJobs() {
        return jobRepository.findOpenJobs();
    }

    @Override
    public Optional<JobPost> findById(String jobId) {
        return jobRepository.findById(jobId);
    }

    @Override
    public Map<String, JobPost> mapById() {
        return jobRepository.findAll().stream()
                .collect(Collectors.toMap(JobPost::getJobId, Function.identity(), (left, right) -> left));
    }
}
