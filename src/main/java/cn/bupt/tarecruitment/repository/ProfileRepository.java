package cn.bupt.tarecruitment.repository;

import cn.bupt.tarecruitment.model.ApplicantProfile;
import cn.bupt.tarecruitment.util.AppPaths;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProfileRepository extends JsonRepository<ApplicantProfile> {
    public ProfileRepository() {
        super(AppPaths.PROFILES_FILE, ApplicantProfile.class);
    }

    public List<ApplicantProfile> findAll() {
        return readAll();
    }

    public Optional<ApplicantProfile> findByApplicantId(String applicantId) {
        if (applicantId == null) {
            return Optional.empty();
        }
        return findAll().stream()
                .filter(profile -> applicantId.equals(profile.getApplicantId()))
                .findFirst();
    }

    public void save(ApplicantProfile profile) {
        List<ApplicantProfile> profiles = new ArrayList<>(findAll());
        profiles.removeIf(item -> profile.getApplicantId().equals(item.getApplicantId()));
        profiles.add(profile);
        writeAll(profiles);
    }
}
