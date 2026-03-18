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

@WebServlet("/ta/job-detail")
public class TAJobDetailServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        AuthUser user = WebUtils.requireRole(request, response, "TA");
        if (user == null) {
            return;
        }
        String jobId = request.getParameter("jobId");
        JobPost job = AppContext.JOBS_SERVICE.findById(jobId).orElse(null);
        if (job == null) {
            WebUtils.setFlash(request, "Job not found.");
            response.sendRedirect(request.getContextPath() + "/ta/jobs");
            return;
        }
        request.setAttribute("job", job);
        request.setAttribute("alreadyApplied", AppContext.APPLICATIONS_SERVICE.hasActiveApplication(user.getId(), jobId));
        request.setAttribute("flashMessage", WebUtils.consumeFlash(request));
        WebUtils.forward(request, response, "/WEB-INF/jsp/ta/job-detail.jsp");
    }
}
