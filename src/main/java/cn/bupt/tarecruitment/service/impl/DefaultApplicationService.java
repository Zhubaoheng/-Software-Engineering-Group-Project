package cn.bupt.tarecruitment.service.impl;

import cn.bupt.tarecruitment.model.ApplicationRecord;
import cn.bupt.tarecruitment.model.JobPost;
import cn.bupt.tarecruitment.repository.ApplicationRepository;
import cn.bupt.tarecruitment.service.ApplicationService;
import cn.bupt.tarecruitment.service.JobService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class DefaultApplicationService implements ApplicationService {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private final ApplicationRepository applicationRepository;
    private final JobService jobService;

    public DefaultApplicationService(ApplicationRepository applicationRepository, JobService jobService) {
        this.applicationRepository = applicationRepository;
        this.jobService = jobService;
    }

    @Override
    public List<ApplicationRecord> listByApplicant(String applicantId) {
        return applicationRepository.findByApplicantId(applicantId).stream()
                .sorted(Comparator.comparing(ApplicationRecord::getSubmittedAt, Comparator.nullsLast(String::compareTo)).reversed())
                .toList();
    }

    @Override
    public ApplicationRecord applyForJob(String applicantId, String jobId) {
        JobPost job = jobService.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Job not found."));
        if (!"OPEN".equalsIgnoreCase(job.getStatus())) {
            throw new IllegalStateException("This job is closed for applications.");
        }
        if (hasActiveApplication(applicantId, jobId)) {
            throw new IllegalStateException("You have already applied for this job.");
        }
        ApplicationRecord application = new ApplicationRecord();
        application.setApplicationId("APP-" + UUID.randomUUID().toString().substring(0, 8));
        application.setApplicantId(applicantId);
        application.setJobId(jobId);
        application.setSubmittedAt(LocalDateTime.now().format(FORMATTER));
        application.setStatus("Submitted");
        applicationRepository.save(application);
        return application;
    }

    @Override
    public boolean hasActiveApplication(String applicantId, String jobId) {
        return applicationRepository.findByApplicantAndJob(applicantId, jobId).stream()
                .anyMatch(application -> !List.of("Rejected", "Withdrawn").contains(normalize(application.getStatus())));
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }
}
