package cn.bupt.tarecruitment.service;

import cn.bupt.tarecruitment.model.JobPost;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface JobService {
    List<JobPost> listOpenJobs();

    Optional<JobPost> findById(String jobId);

    Map<String, JobPost> mapById();
}
