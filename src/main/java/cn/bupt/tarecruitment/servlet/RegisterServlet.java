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

/**
 * Servlet that handles self-registration of new teaching assistant accounts.
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    /**
     * Renders the registration form, or redirects an already signed-in user
     * to their default landing page.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @throws ServletException if forwarding to the JSP fails
     * @throws IOException      if an I/O error occurs while handling the request
     */
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

    /**
     * Validates the submitted credentials and creates a new TA account.
     * On any validation or registration error the form is redisplayed with a
     * message; on success the user is redirected to the login page.
     *
     * @param request  the HTTP request, expecting {@code username},
     *                 {@code password} and {@code confirmPassword} parameters
     * @param response the HTTP response
     * @throws ServletException if forwarding to the JSP fails
     * @throws IOException      if an I/O error occurs while handling the request
     */
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
