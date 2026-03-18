package cn.bupt.tarecruitment.service;

import cn.bupt.tarecruitment.model.ApplicantProfile;

public interface ProfileService {
    ApplicantProfile getProfile(String applicantId);

    ApplicantProfile saveProfile(ApplicantProfile profile);
}
