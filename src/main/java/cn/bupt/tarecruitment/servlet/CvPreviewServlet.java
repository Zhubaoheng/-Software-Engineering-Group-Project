package cn.bupt.tarecruitment.servlet;

import cn.bupt.tarecruitment.context.AppContext;
import cn.bupt.tarecruitment.model.ApplicantProfile;
import cn.bupt.tarecruitment.model.AuthUser;
import cn.bupt.tarecruitment.util.AppPaths;
import cn.bupt.tarecruitment.util.WebUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@WebServlet("/cv/preview")
public class CvPreviewServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        AuthUser user = WebUtils.currentUser(request);
        if (user == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // Determine which applicant's CV to show
        String applicantId = request.getParameter("applicantId");
        if (applicantId == null || applicantId.isBlank()) {
            applicantId = user.getId();
        }

        ApplicantProfile profile = AppContext.PROFILES_SERVICE.getProfile(applicantId);
        if (profile == null || profile.getCvFileName() == null || profile.getCvFileName().isBlank()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "No CV file found for this applicant.");
            return;
        }

        Path cvFile = AppPaths.UPLOADS_DIR.resolve(applicantId).resolve(profile.getCvFileName());
        if (!Files.exists(cvFile) || !Files.isRegularFile(cvFile)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "CV file not found on disk.");
            return;
        }

        // Determine content type
        String fileName = profile.getCvFileName().toLowerCase();
        String contentType = "application/octet-stream";
        if (fileName.endsWith(".pdf")) {
            contentType = "application/pdf";
        } else if (fileName.endsWith(".doc")) {
            contentType = "application/msword";
        } else if (fileName.endsWith(".docx")) {
            contentType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        } else if (fileName.endsWith(".png")) {
            contentType = "image/png";
        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            contentType = "image/jpeg";
        }

        response.setContentType(contentType);
        response.setHeader("Content-Disposition", "inline; filename=\"" + profile.getCvFileName() + "\"");
        response.setContentLengthLong(Files.size(cvFile));

        try (OutputStream out = response.getOutputStream()) {
            Files.copy(cvFile, out);
        }
    }
}
