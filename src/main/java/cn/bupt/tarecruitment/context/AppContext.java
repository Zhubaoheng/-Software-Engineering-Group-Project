package cn.bupt.tarecruitment.context;

import cn.bupt.tarecruitment.repository.ApplicationRepository;
import cn.bupt.tarecruitment.repository.AssignmentRepository;
import cn.bupt.tarecruitment.repository.JobRepository;
import cn.bupt.tarecruitment.repository.ProfileRepository;
import cn.bupt.tarecruitment.repository.UserRepository;
import cn.bupt.tarecruitment.service.ApplicationService;
import cn.bupt.tarecruitment.service.AuthService;
import cn.bupt.tarecruitment.service.JobService;
import cn.bupt.tarecruitment.service.ProfileService;
import cn.bupt.tarecruitment.service.impl.DefaultApplicationService;
import cn.bupt.tarecruitment.service.impl.DefaultAuthService;
import cn.bupt.tarecruitment.service.impl.DefaultJobService;
import cn.bupt.tarecruitment.service.impl.DefaultProfileService;
import cn.bupt.tarecruitment.util.AppPaths;
import cn.bupt.tarecruitment.util.JsonUtils;

public final class AppContext {
    public static final UserRepository USERS = new UserRepository();
    public static final ProfileRepository PROFILES = new ProfileRepository();
    public static final JobRepository JOBS = new JobRepository();
    public static final ApplicationRepository APPLICATIONS = new ApplicationRepository();
    public static final AssignmentRepository ASSIGNMENTS = new AssignmentRepository();

    public static final AuthService AUTH = new DefaultAuthService(USERS);
    public static final ProfileService PROFILES_SERVICE = new DefaultProfileService(PROFILES);
    public static final JobService JOBS_SERVICE = new DefaultJobService(JOBS);
    public static final ApplicationService APPLICATIONS_SERVICE =
            new DefaultApplicationService(APPLICATIONS, JOBS_SERVICE);

    static {
        JsonUtils.ensureFile(AppPaths.USERS_FILE);
        JsonUtils.ensureFile(AppPaths.PROFILES_FILE);
        JsonUtils.ensureFile(AppPaths.JOBS_FILE);
        JsonUtils.ensureFile(AppPaths.APPLICATIONS_FILE);
        JsonUtils.ensureFile(AppPaths.ASSIGNMENTS_FILE);
        JsonUtils.ensureDir(AppPaths.UPLOADS_DIR);
    }

    private AppContext() {
    }
}
