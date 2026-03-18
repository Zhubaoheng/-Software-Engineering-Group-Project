package cn.bupt.tarecruitment.servlet;

import cn.bupt.tarecruitment.context.AppContext;
import cn.bupt.tarecruitment.model.AuthUser;
import cn.bupt.tarecruitment.model.JobPost;
import cn.bupt.tarecruitment.util.WebUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@WebServlet("/mo/jobs")
public class MOJobsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        AuthUser user = WebUtils.requireRole(request, response, "MO");
        if (user == null) {
            return;
        }
        String action = request.getParameter("action");
        request.setAttribute("flashMessage", WebUtils.consumeFlash(request));
        List<JobPost> jobs = AppContext.JOBS.findAll().stream()
                .filter(job -> user.getId().equals(job.getOrganiserId()))
                .sorted(Comparator.comparing(JobPost::getDeadline, Comparator.nullsLast(String::compareTo)).reversed())
                .toList();
        request.setAttribute("jobs", jobs);

        if ("new".equalsIgnoreCase(action) || "edit".equalsIgnoreCase(action)) {
            JobPost job = new JobPost();
            if ("edit".equalsIgnoreCase(action)) {
                job = loadOwnedJob(request, response, user);
                if (job == null) {
                    return;
                }
            }
            request.setAttribute("job", job);
            WebUtils.forward(request, response, "/WEB-INF/jsp/mo/job-form.jsp");
            return;
        }

        WebUtils.forward(request, response, "/WEB-INF/jsp/mo/jobs.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        AuthUser user = WebUtils.requireRole(request, response, "MO");
        if (user == null) {
            return;
        }
        String action = request.getParameter("action");
        try {
            if ("close".equalsIgnoreCase(action)) {
                JobPost job = loadOwnedJob(request, response, user);
                if (job == null) {
                    return;
                }
                job.setStatus("CLOSED");
                AppContext.JOBS.save(job);
                WebUtils.setFlash(request, "Job closed.");
                response.sendRedirect(request.getContextPath() + "/mo/jobs");
                return;
            }

            JobPost job = parseJob(request);
            if ("edit".equalsIgnoreCase(action)) {
                JobPost existing = loadOwnedJob(request, response, user);
                if (existing == null) {
                    return;
                }
                job.setJobId(existing.getJobId());
            } else {
                job.setJobId("JOB-" + UUID.randomUUID().toString().substring(0, 8));
            }
            job.setOrganiserId(user.getId());
            AppContext.JOBS.save(job);
            WebUtils.setFlash(request, "Job saved.");
            response.sendRedirect(request.getContextPath() + "/mo/jobs");
        } catch (IllegalArgumentException ex) {
            request.setAttribute("error", ex.getMessage());
            request.setAttribute("job", parseDraft(request));
            request.setAttribute("jobs", AppContext.JOBS.findAll().stream()
                    .filter(job -> user.getId().equals(job.getOrganiserId()))
                    .sorted(Comparator.comparing(JobPost::getDeadline, Comparator.nullsLast(String::compareTo)).reversed())
                    .toList());
            WebUtils.forward(request, response, "/WEB-INF/jsp/mo/job-form.jsp");
        }
    }

    private JobPost loadOwnedJob(HttpServletRequest request, HttpServletResponse response, AuthUser user)
            throws IOException {
        String jobId = request.getParameter("id");
        if (jobId == null || jobId.isBlank()) {
            WebUtils.setFlash(request, "Job not found.");
            response.sendRedirect(request.getContextPath() + "/mo/jobs");
            return null;
        }
        JobPost job = AppContext.JOBS.findById(jobId).orElse(null);
        if (job == null || !user.getId().equals(job.getOrganiserId())) {
            WebUtils.setFlash(request, "You cannot access that job.");
            response.sendRedirect(request.getContextPath() + "/mo/jobs");
            return null;
        }
        return job;
    }

    private JobPost parseJob(HttpServletRequest request) {
        JobPost job = new JobPost();
        job.setModuleCode(trim(request.getParameter("moduleCode")));
        job.setModuleName(trim(request.getParameter("moduleName")));
        job.setDescription(trim(request.getParameter("description")));
        job.setRequiredSkills(WebUtils.splitCsv(request.getParameter("requiredSkills")));
        job.setHoursPerWeek(parsePositiveInt(request.getParameter("hoursPerWeek"), "Hours per week must be a positive number."));
        job.setPositions(parsePositiveInt(request.getParameter("positions"), "Positions must be a positive number."));
        String deadline = trim(request.getParameter("deadline"));
        if (deadline.isBlank()) {
            throw new IllegalArgumentException("Deadline is required.");
        }
        try {
            LocalDate.parse(deadline);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Deadline must use yyyy-MM-dd format.");
        }
        job.setDeadline(deadline);
        String status = trim(request.getParameter("status"));
        job.setStatus("CLOSED".equalsIgnoreCase(status) ? "CLOSED" : "OPEN");
        return job;
    }

    private JobPost parseDraft(HttpServletRequest request) {
        JobPost draft = new JobPost();
        draft.setModuleCode(request.getParameter("moduleCode"));
        draft.setModuleName(request.getParameter("moduleName"));
        draft.setDescription(request.getParameter("description"));
        draft.setRequiredSkills(WebUtils.splitCsv(request.getParameter("requiredSkills")));
        draft.setDeadline(request.getParameter("deadline"));
        draft.setStatus(request.getParameter("status"));
        try {
            draft.setHoursPerWeek(Integer.parseInt(trim(request.getParameter("hoursPerWeek"))));
        } catch (Exception ignored) {
            draft.setHoursPerWeek(0);
        }
        try {
            draft.setPositions(Integer.parseInt(trim(request.getParameter("positions"))));
        } catch (Exception ignored) {
            draft.setPositions(0);
        }
        draft.setJobId(request.getParameter("jobId"));
        return draft;
    }

    private int parsePositiveInt(String value, String message) {
        try {
            int parsed = Integer.parseInt(trim(value));
            if (parsed <= 0) {
                throw new IllegalArgumentException(message);
            }
            return parsed;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(message);
        }
    }

    private String trim(String value) {
        return value == null ? "" : value.trim();
    }
}
