package cn.bupt.tarecruitment.service;

import cn.bupt.tarecruitment.model.ApplicationRecord;

import java.util.List;

public interface ApplicationService {
    List<ApplicationRecord> listByApplicant(String applicantId);

    ApplicationRecord applyForJob(String applicantId, String jobId);

    boolean hasActiveApplication(String applicantId, String jobId);
}
