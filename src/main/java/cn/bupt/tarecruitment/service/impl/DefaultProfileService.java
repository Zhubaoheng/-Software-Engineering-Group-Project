package cn.bupt.tarecruitment.service.impl;

import cn.bupt.tarecruitment.model.ApplicantProfile;
import cn.bupt.tarecruitment.repository.ProfileRepository;
import cn.bupt.tarecruitment.service.ProfileService;

public class DefaultProfileService implements ProfileService {
    private final ProfileRepository profileRepository;

    public DefaultProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public ApplicantProfile getProfile(String applicantId) {
        return profileRepository.findByApplicantId(applicantId).orElseGet(() -> {
            ApplicantProfile profile = new ApplicantProfile(applicantId);
            profile.setSkills(java.util.List.of());
            return profile;
        });
    }

    @Override
    public ApplicantProfile saveProfile(ApplicantProfile profile) {
        profileRepository.save(profile);
        return profile;
    }
}
