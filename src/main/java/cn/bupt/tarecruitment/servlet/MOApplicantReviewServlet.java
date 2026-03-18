package cn.bupt.tarecruitment.servlet;

import cn.bupt.tarecruitment.context.AppContext;
import cn.bupt.tarecruitment.model.ApplicationRecord;
import cn.bupt.tarecruitment.model.ApplicationReviewView;
import cn.bupt.tarecruitment.model.ApplicationStatus;
import cn.bupt.tarecruitment.model.AssignmentRecord;
import cn.bupt.tarecruitment.model.AuthUser;
import cn.bupt.tarecruitment.model.JobPost;
import cn.bupt.tarecruitment.util.WebUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@WebServlet("/mo/applicants/review")
public class MOApplicantReviewServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        AuthUser user = WebUtils.requireRole(request, response, "MO");
        if (user == null) {
            return;
        }
        request.setAttribute("flashMessage", WebUtils.consumeFlash(request));
        ApplicationReviewView review = loadReview(request, response, user);
        if (review == null) {
            return;
        }
        request.setAttribute("review", review);
        WebUtils.forward(request, response, "/WEB-INF/jsp/mo/applicant-detail.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        AuthUser user = WebUtils.requireRole(request, response, "MO");
        if (user == null) {
            return;
        }
        String applicationId = request.getParameter("applicationId");
        String decision = request.getParameter("decision");
        try {
            ApplicationReviewView review = loadReviewByApplicationId(applicationId, user);
            if (review == null) {
                WebUtils.setFlash(request, "Application not found.");
                response.sendRedirect(request.getContextPath() + "/mo/jobs");
                return;
            }
            if ("Selected".equalsIgnoreCase(decision)) {
                selectApplicant(review);
                WebUtils.setFlash(request, "Applicant selected.");
            } else if ("Shortlisted".equalsIgnoreCase(decision) || "Rejected".equalsIgnoreCase(decision)) {
                review.getApplication().setStatus(capitalise(decision));
                AppContext.APPLICATIONS.save(review.getApplication());
                WebUtils.setFlash(request, "Application updated.");
            } else {
                throw new IllegalArgumentException("Unsupported decision.");
            }
            response.sendRedirect(request.getContextPath() + "/mo/applicants/review?applicationId=" + applicationId);
        } catch (IllegalArgumentException ex) {
            request.setAttribute("error", ex.getMessage());
            ApplicationReviewView review = loadReviewByApplicationId(applicationId, user);
            if (review == null) {
                return;
            }
            request.setAttribute("review", review);
            WebUtils.forward(request, response, "/WEB-INF/jsp/mo/applicant-detail.jsp");
        }
    }

    private ApplicationReviewView loadReview(HttpServletRequest request, HttpServletResponse response, AuthUser user)
            throws IOException {
        String applicationId = request.getParameter("applicationId");
        ApplicationReviewView review = loadReviewByApplicationId(applicationId, user);
        if (review == null) {
            WebUtils.setFlash(request, "Application not found.");
            response.sendRedirect(request.getContextPath() + "/mo/jobs");
        }
        return review;
    }

    private ApplicationReviewView loadReviewByApplicationId(String applicationId, AuthUser user) {
        ApplicationRecord application = AppContext.APPLICATIONS.findById(applicationId).orElse(null);
        if (application == null) {
            return null;
        }
        JobPost job = AppContext.JOBS.findById(application.getJobId()).orElse(null);
        if (job == null || !user.getId().equals(job.getOrganiserId())) {
            return null;
        }
        return new ApplicationReviewView(
                application,
                AppContext.PROFILES.findByApplicantId(application.getApplicantId()).orElse(null),
                job
        );
    }

    private void selectApplicant(ApplicationReviewView review) {
        long selectedCount = AppContext.APPLICATIONS.findByJobId(review.getJob().getJobId()).stream()
                .filter(application -> "Selected".equalsIgnoreCase(application.getStatus()))
                .count();
        if (!"Selected".equalsIgnoreCase(review.getApplication().getStatus())
                && selectedCount >= review.getJob().getPositions()) {
            throw new IllegalArgumentException("Selected applicants already reached the position limit.");
        }
        review.getApplication().setStatus("Selected");
        AppContext.APPLICATIONS.save(review.getApplication());

        boolean assignmentExists = AppContext.ASSIGNMENTS.findAll().stream()
                .anyMatch(record -> review.getJob().getJobId().equals(record.getJobId())
                        && review.getApplication().getApplicantId().equals(record.getApplicantId()));
        if (!assignmentExists) {
            AppContext.ASSIGNMENTS.save(new AssignmentRecord(
                    "ASG-" + UUID.randomUUID().toString().substring(0, 8),
                    review.getJob().getJobId(),
                    review.getApplication().getApplicantId(),
                    review.getJob().getHoursPerWeek(),
                    LocalDateTime.now().toString()
            ));
        }
    }

    private String capitalise(String value) {
        if (value == null || value.isBlank()) {
            return value;
        }
        String lower = value.toLowerCase();
        return Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
    }
}
