package cn.bupt.tarecruitment.servlet;

import cn.bupt.tarecruitment.model.AuthUser;
import cn.bupt.tarecruitment.util.WebUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            AuthUser user = (AuthUser) session.getAttribute("currentUser");
            session.invalidate();
            if (user != null) {
                System.out.println("[AUTH] User logged out: " + user.getUsername());
            }
        }
        // Create a new session to carry the flash message
        WebUtils.setFlash(request, "You have been signed out successfully.");
        WebUtils.redirect(response, request.getContextPath() + "/login");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }
}
