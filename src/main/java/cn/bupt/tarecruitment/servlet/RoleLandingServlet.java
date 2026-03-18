package cn.bupt.tarecruitment.servlet;

import cn.bupt.tarecruitment.util.WebUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/role-home")
public class RoleLandingServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (WebUtils.currentUser(request) == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        request.setAttribute("flashMessage", WebUtils.consumeFlash(request));
        WebUtils.forward(request, response, "/WEB-INF/jsp/role-home.jsp");
    }
}
