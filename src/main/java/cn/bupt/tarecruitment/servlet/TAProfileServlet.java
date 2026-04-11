package cn.bupt.tarecruitment.servlet;

import cn.bupt.tarecruitment.context.AppContext;
import cn.bupt.tarecruitment.model.ApplicantProfile;
import cn.bupt.tarecruitment.model.AuthUser;
import cn.bupt.tarecruitment.util.AppPaths;
import cn.bupt.tarecruitment.util.WebUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/ta/profile")
@MultipartConfig
public class TAProfileServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        AuthUser user = WebUtils.requireRole(request, response, "TA");
        if (user == null) {
            return;
        }
        request.setAttribute("profile", AppContext.PROFILES_SERVICE.getProfile(user.getId()));
        request.setAttribute("flashMessage", WebUtils.consumeFlash(request));
        WebUtils.forward(request, response, "/WEB-INF/jsp/ta/profile.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        AuthUser user = WebUtils.requireRole(request, response, "TA");
        if (user == null) {
            return;
        }
        ApplicantProfile existing = AppContext.PROFILES_SERVICE.getProfile(user.getId());
        List<String> errors = new ArrayList<>();

        String name = trim(request.getParameter("name"));
        String studentId = trim(request.getParameter("studentId"));
        String email = trim(request.getParameter("email"));
        String phone = trim(request.getParameter("phone"));
        String major = trim(request.getParameter("major"));
        String grade = trim(request.getParameter("grade"));
        String preferredRole = trim(request.getParameter("preferredRole"));
        String availability = trim(request.getParameter("availability"));
        String selfIntroduction = trim(request.getParameter("selfIntroduction"));
        String projectExperience = trim(request.getParameter("projectExperience"));
        String skillsRaw = request.getParameter("skills");

        if (name.isEmpty()) {
            errors.add("Name is required.");
        }
        if (studentId.isEmpty()) {
            errors.add("Student ID is required.");
        }
        if (email.isEmpty()) {
            errors.add("Email is required.");
        }
        if (availability.isEmpty()) {
            errors.add("Availability is required.");
        }

        if (!errors.isEmpty()) {
            ApplicantProfile draft = buildProfile(existing, user.getId(), name, studentId, email, phone, major, grade,
                    preferredRole, availability, selfIntroduction, projectExperience, skillsRaw);
            request.setAttribute("profile", draft);
            request.setAttribute("errors", errors);
            WebUtils.forward(request, response, "/WEB-INF/jsp/ta/profile.jsp");
            return;
        }

        ApplicantProfile profile = buildProfile(existing, user.getId(), name, studentId, email, phone, major, grade,
                preferredRole, availability, selfIntroduction, projectExperience, skillsRaw);
        Part cvPart = request.getPart("cvFile");
        String uploadedFileName = saveCvFile(user.getId(), cvPart);
        if (uploadedFileName != null) {
            profile.setCvFileName(uploadedFileName);
        }
        AppContext.PROFILES_SERVICE.saveProfile(profile);
        WebUtils.setFlash(request, "Profile saved successfully.");
        response.sendRedirect(request.getContextPath() + "/ta/profile");
    }

    private ApplicantProfile buildProfile(ApplicantProfile existing, String applicantId, String name, String studentId,
                                          String email, String phone, String major, String grade, String preferredRole,
                                          String availability, String selfIntroduction, String projectExperience,
                                          String skillsRaw) {
        ApplicantProfile profile = existing == null ? new ApplicantProfile(applicantId) : existing;
        profile.setApplicantId(applicantId);
        profile.setName(name);
        profile.setStudentId(studentId);
        profile.setEmail(email);
        profile.setPhone(phone);
        profile.setMajor(major);
        profile.setGrade(grade);
        profile.setPreferredRole(preferredRole);
        profile.setAvailability(availability);
        profile.setSelfIntroduction(selfIntroduction);
        profile.setProjectExperience(projectExperience);
        profile.setSkills(WebUtils.splitCsv(skillsRaw));
        return profile;
    }

    private String saveCvFile(String applicantId, Part cvPart) throws IOException {
        if (cvPart == null || cvPart.getSize() == 0) {
            return null;
        }
        String submittedFileName = cvPart.getSubmittedFileName();
        if (submittedFileName == null || submittedFileName.isBlank()) {
            return null;
        }
        String originalFileName = Path.of(submittedFileName).getFileName().toString();
        if (originalFileName.isBlank()) {
            return null;
        }
        Path applicantDir = AppPaths.UPLOADS_DIR.resolve(applicantId);
        Files.createDirectories(applicantDir);
        Path target = applicantDir.resolve(originalFileName);
        try (InputStream input = cvPart.getInputStream()) {
            Files.copy(input, target, StandardCopyOption.REPLACE_EXISTING);
        }
        return originalFileName;
    }

    private String trim(String value) {
        return value == null ? "" : value.trim();
    }
}
