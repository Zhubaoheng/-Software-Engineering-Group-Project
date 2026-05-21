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

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        AuthUser user = WebUtils.currentUser(request);
        if (user != null) {
            response.sendRedirect(WebUtils.defaultLandingPath(request, user));
            return;
        }
        request.setAttribute("flashMessage", WebUtils.consumeFlash(request));
        WebUtils.forward(request, response, "/WEB-INF/jsp/register.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        if (username == null || username.trim().isEmpty()) {
            failBack(request, response, "Username is required.", username);
            return;
        }
        if (password == null || password.isEmpty()) {
            failBack(request, response, "Password is required.", username);
            return;
        }
        if (confirmPassword == null || !confirmPassword.equals(password)) {
            failBack(request, response, "Passwords do not match.", username);
            return;
        }

        try {
            AppContext.AUTH.registerTaUser(username, password);
        } catch (IllegalArgumentException e) {
            failBack(request, response, e.getMessage(), username);
            return;
        }

        WebUtils.setFlash(request, "Account created. Please sign in.");
        response.sendRedirect(request.getContextPath() + "/login");
    }

    private void failBack(HttpServletRequest request, HttpServletResponse response,
                          String message, String username)
            throws ServletException, IOException {
        request.setAttribute("error", message);
        request.setAttribute("username", username);
        WebUtils.forward(request, response, "/WEB-INF/jsp/register.jsp");
    }
}
