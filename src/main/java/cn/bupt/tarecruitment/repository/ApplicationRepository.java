package cn.bupt.tarecruitment.repository;

import cn.bupt.tarecruitment.model.ApplicationRecord;
import cn.bupt.tarecruitment.util.AppPaths;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ApplicationRepository extends JsonRepository<ApplicationRecord> {
    public ApplicationRepository() {
        super(AppPaths.APPLICATIONS_FILE, ApplicationRecord.class);
    }

    public List<ApplicationRecord> findAll() {
        return readAll();
    }

    public Optional<ApplicationRecord> findById(String applicationId) {
        if (applicationId == null) {
            return Optional.empty();
        }
        return findAll().stream()
                .filter(application -> applicationId.equals(application.getApplicationId()))
                .findFirst();
    }

    public List<ApplicationRecord> findByApplicantId(String applicantId) {
        return findAll().stream()
                .filter(application -> applicantId != null && applicantId.equals(application.getApplicantId()))
                .collect(Collectors.toList());
    }

    public List<ApplicationRecord> findByJobId(String jobId) {
        return findAll().stream()
                .filter(application -> jobId != null && jobId.equals(application.getJobId()))
                .collect(Collectors.toList());
    }

    public List<ApplicationRecord> findByApplicantAndJob(String applicantId, String jobId) {
        return findAll().stream()
                .filter(application -> applicantId != null && jobId != null
                        && applicantId.equals(application.getApplicantId())
                        && jobId.equals(application.getJobId()))
                .collect(Collectors.toList());
    }

    public void save(ApplicationRecord applicationRecord) {
        List<ApplicationRecord> applications = new ArrayList<>(findAll());
        applications.removeIf(item -> applicationRecord.getApplicationId().equals(item.getApplicationId()));
        applications.add(applicationRecord);
        writeAll(applications);
    }
}
