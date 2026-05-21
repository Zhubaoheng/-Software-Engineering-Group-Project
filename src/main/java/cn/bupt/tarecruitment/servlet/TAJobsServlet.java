package cn.bupt.tarecruitment.servlet;

import cn.bupt.tarecruitment.context.AppContext;
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
import java.util.List;
import java.util.Map;

@WebServlet("/ta/jobs")
public class TAJobsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        AuthUser user = WebUtils.requireRole(request, response, "TA");
        if (user == null) {
            return;
        }
        List<JobPost> jobs = AppContext.JOBS_SERVICE.listOpenJobs();
        ApplicantProfile profile = AppContext.PROFILES_SERVICE.getProfile(user.getId());
        Map<String, SkillMatch> matches = AppContext.MATCH_SERVICE.matchJobsForApplicant(jobs, profile);
        request.setAttribute("jobs", jobs);
        request.setAttribute("matches", matches);
        request.setAttribute("flashMessage", WebUtils.consumeFlash(request));
        WebUtils.forward(request, response, "/WEB-INF/jsp/ta/jobs.jsp");
    }
}
