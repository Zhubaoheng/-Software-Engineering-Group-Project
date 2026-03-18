package cn.bupt.tarecruitment.servlet;

import cn.bupt.tarecruitment.context.AppContext;
import cn.bupt.tarecruitment.model.ApplicationRecord;
import cn.bupt.tarecruitment.model.ApplicationReviewView;
import cn.bupt.tarecruitment.model.ApplicantProfile;
import cn.bupt.tarecruitment.model.AuthUser;
import cn.bupt.tarecruitment.model.JobPost;
import cn.bupt.tarecruitment.util.WebUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

@WebServlet("/mo/applicants")
public class MOApplicantsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        AuthUser user = WebUtils.requireRole(request, response, "MO");
        if (user == null) {
            return;
        }
        request.setAttribute("flashMessage", WebUtils.consumeFlash(request));
        String jobId = request.getParameter("jobId");

        if (jobId == null || jobId.isBlank()) {
            request.setAttribute("jobs", ownedJobs(user.getId()));
            WebUtils.forward(request, response, "/WEB-INF/jsp/mo/applicants.jsp");
            return;
        }

        JobPost job = ownedJobs(user.getId()).stream()
                .filter(item -> jobId.equals(item.getJobId()))
                .findFirst()
                .orElse(null);
        if (job == null) {
            WebUtils.setFlash(request, "Job not found.");
            response.sendRedirect(request.getContextPath() + "/mo/jobs");
            return;
        }

        List<ApplicationReviewView> reviews = AppContext.APPLICATIONS.findByJobId(jobId).stream()
                .map(application -> toView(application, job))
                .sorted(Comparator.comparing(view -> view.getProfile() == null ? "" : view.getProfile().getName(), String.CASE_INSENSITIVE_ORDER))
                .toList();
        request.setAttribute("job", job);
        request.setAttribute("applications", reviews);
        WebUtils.forward(request, response, "/WEB-INF/jsp/mo/applicants.jsp");
    }

    private List<JobPost> ownedJobs(String organiserId) {
        return AppContext.JOBS.findAll().stream()
                .filter(job -> organiserId.equals(job.getOrganiserId()))
                .sorted(Comparator.comparing(JobPost::getDeadline, Comparator.nullsLast(String::compareTo)).reversed())
                .toList();
    }

    private ApplicationReviewView toView(ApplicationRecord application, JobPost job) {
        ApplicantProfile profile = AppContext.PROFILES.findByApplicantId(application.getApplicantId()).orElse(null);
        return new ApplicationReviewView(application, profile, job);
    }
}
