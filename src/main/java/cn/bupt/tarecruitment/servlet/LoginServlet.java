package cn.bupt.tarecruitment.servlet;

import cn.bupt.tarecruitment.context.AppContext;
import cn.bupt.tarecruitment.model.AuthUser;
import cn.bupt.tarecruitment.util.WebUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        AuthUser user = WebUtils.currentUser(request);
        if (user != null) {
            response.sendRedirect(WebUtils.resolveLoginSuccessRedirect(request, user));
            return;
        }
        request.setAttribute("flashMessage", WebUtils.consumeFlash(request));
        WebUtils.forward(request, response, "/WEB-INF/jsp/login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        AppContext.AUTH.login(username, password).ifPresentOrElse(user -> {
            HttpSession session = request.getSession(true);
            session.setAttribute(WebUtils.SESSION_USER, user);
            try {
                response.sendRedirect(WebUtils.resolveLoginSuccessRedirect(request, user));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, () -> {
            try {
                request.setAttribute("error", "Invalid username or password.");
                WebUtils.forward(request, response, "/WEB-INF/jsp/login.jsp");
            } catch (ServletException | IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
