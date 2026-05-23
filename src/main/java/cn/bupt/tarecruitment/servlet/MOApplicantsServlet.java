package cn.bupt.tarecruitment.servlet;

import cn.bupt.tarecruitment.context.AppContext;
import cn.bupt.tarecruitment.model.ApplicationRecord;
import cn.bupt.tarecruitment.model.ApplicationReviewView;
import cn.bupt.tarecruitment.model.ApplicantProfile;
import cn.bupt.tarecruitment.model.AuthUser;
import cn.bupt.tarecruitment.model.JobPost;
import cn.bupt.tarecruitment.model.SkillMatch;
import cn.bupt.tarecruitment.util.WebUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Servlet for a Module Organiser to review applicants. Without a {@code jobId}
 * it lists the organiser's own jobs; with one it lists that job's applicants
 * ranked by their advisory skill-match score.
 */
@WebServlet("/mo/applicants")
public class MOApplicantsServlet extends HttpServlet {
    /**
     * Lists the organiser's jobs, or the applicants of a selected job ranked by
     * skill match. Redirects to the jobs page if the job is not owned by the
     * signed-in organiser.
     *
     * @param request  the HTTP request, optionally with a {@code jobId} parameter
     * @param response the HTTP response
     * @throws ServletException if forwarding to the JSP fails
     * @throws IOException      if an I/O error occurs while handling the request
     */
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

        // Compute an explainable skill match per applicant, then rank applicants
        // by descending match score so the best-matched candidates appear first.
        // The match is advisory only; the MO still makes the hiring decision.
        Map<String, SkillMatch> matches = new LinkedHashMap<>();
        List<ApplicationReviewView> reviews = AppContext.APPLICATIONS.findByJobId(jobId).stream()
                .map(application -> toView(application, job))
                .peek(view -> matches.put(
                        view.getApplication().getApplicationId(),
                        AppContext.MATCH_SERVICE.match(job, view.getProfile())))
                .sorted(Comparator
                        .comparingInt((ApplicationReviewView view) ->
                                matches.get(view.getApplication().getApplicationId()).getScorePercent())
                        .reversed()
                        .thenComparing(view -> view.getProfile() == null ? "" : view.getProfile().getName(),
                                String.CASE_INSENSITIVE_ORDER))
                .toList();
        request.setAttribute("job", job);
        request.setAttribute("applications", reviews);
        request.setAttribute("matches", matches);
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
