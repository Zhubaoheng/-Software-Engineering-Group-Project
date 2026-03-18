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

@WebServlet("/admin/workload")
public class AdminWorkloadServlet extends HttpServlet {
    private final WorkloadService workloadService = new DefaultWorkloadService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        AuthUser user = WebUtils.requireRole(request, response, "ADMIN");
        if (user == null) {
            return;
        }
        request.setAttribute("summary", workloadService.buildSummary());
        request.setAttribute("rows", workloadService.buildRows());
        request.setAttribute("flashMessage", WebUtils.consumeFlash(request));
        WebUtils.forward(request, response, "/WEB-INF/jsp/admin/workload.jsp");
    }
}
