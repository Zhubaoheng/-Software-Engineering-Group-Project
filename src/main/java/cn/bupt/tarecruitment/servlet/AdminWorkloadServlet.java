package cn.bupt.tarecruitment.servlet;

import cn.bupt.tarecruitment.model.AuthUser;
import cn.bupt.tarecruitment.service.WorkloadService;
import cn.bupt.tarecruitment.service.impl.DefaultWorkloadService;
import cn.bupt.tarecruitment.util.WebUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Servlet that renders the Admin workload dashboard, showing the workload
 * summary, per-TA rows and advisory rebalancing recommendations.
 */
@WebServlet("/admin/workload")
public class AdminWorkloadServlet extends HttpServlet {
    private final WorkloadService workloadService = new DefaultWorkloadService();

    /**
     * Renders the workload dashboard for the signed-in Admin.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @throws ServletException if forwarding to the JSP fails
     * @throws IOException      if an I/O error occurs while handling the request
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        AuthUser user = WebUtils.requireRole(request, response, "ADMIN");
        if (user == null) {
            return;
        }
        request.setAttribute("summary", workloadService.buildSummary());
        request.setAttribute("rows", workloadService.buildRows());
        request.setAttribute("recommendations", workloadService.recommendRebalancing());
        request.setAttribute("flashMessage", WebUtils.consumeFlash(request));
        WebUtils.forward(request, response, "/WEB-INF/jsp/admin/workload.jsp");
    }
}
