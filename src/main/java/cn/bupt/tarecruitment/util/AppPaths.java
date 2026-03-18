package cn.bupt.tarecruitment.util;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class AppPaths {
    private AppPaths() {
    }

    public static final Path DATA_DIR = Paths.get(System.getProperty("user.dir"), "data");
    public static final Path USERS_FILE = DATA_DIR.resolve("users.json");
    public static final Path PROFILES_FILE = DATA_DIR.resolve("profiles.json");
    public static final Path JOBS_FILE = DATA_DIR.resolve("jobs.json");
    public static final Path APPLICATIONS_FILE = DATA_DIR.resolve("applications.json");
    public static final Path ASSIGNMENTS_FILE = DATA_DIR.resolve("assignments.json");
    public static final Path UPLOADS_DIR = DATA_DIR.resolve("uploads");
}
