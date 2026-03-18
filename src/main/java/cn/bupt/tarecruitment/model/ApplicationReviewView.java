package cn.bupt.tarecruitment.model;

public class ApplicationReviewView {
    private ApplicationRecord application;
    private ApplicantProfile profile;
    private JobPost job;

    public ApplicationReviewView() {
    }

    public ApplicationReviewView(ApplicationRecord application, ApplicantProfile profile, JobPost job) {
        this.application = application;
        this.profile = profile;
        this.job = job;
    }

    public ApplicationRecord getApplication() {
        return application;
    }

    public void setApplication(ApplicationRecord application) {
        this.application = application;
    }

    public ApplicantProfile getProfile() {
        return profile;
    }

    public void setProfile(ApplicantProfile profile) {
        this.profile = profile;
    }

    public JobPost getJob() {
        return job;
    }

    public void setJob(JobPost job) {
        this.job = job;
    }
}

