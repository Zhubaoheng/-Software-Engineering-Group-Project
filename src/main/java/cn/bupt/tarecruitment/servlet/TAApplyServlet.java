package cn.bupt.tarecruitment.servlet;

import cn.bupt.tarecruitment.context.AppContext;
import cn.bupt.tarecruitment.model.AuthUser;
import cn.bupt.tarecruitment.util.WebUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/ta/apply")
public class TAApplyServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        AuthUser user = WebUtils.requireRole(request, response, "TA");
        if (user == null) {
            return;
        }
        String jobId = request.getParameter("jobId");
        try {
            AppContext.APPLICATIONS_SERVICE.applyForJob(user.getId(), jobId);
            WebUtils.setFlash(request, "Application submitted successfully.");
            response.sendRedirect(request.getContextPath() + "/ta/applications");
        } catch (IllegalArgumentException | IllegalStateException ex) {
            WebUtils.setFlash(request, ex.getMessage());
            response.sendRedirect(request.getContextPath() + "/ta/job-detail?jobId=" + jobId);
        }
    }
}
