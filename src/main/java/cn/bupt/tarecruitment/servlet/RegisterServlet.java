package cn.bupt.tarecruitment.servlet;

import cn.bupt.tarecruitment.context.AppContext;
import cn.bupt.tarecruitment.model.ApplicantProfile;
import cn.bupt.tarecruitment.model.AuthUser;
import cn.bupt.tarecruitment.util.WebUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        AuthUser currentUser = WebUtils.currentUser(request);
        if (currentUser != null) {
            response.sendRedirect(WebUtils.defaultLandingPath(request, currentUser));
            return;
        }

        String name = trim(request.getParameter("name"));
        String studentId = trim(request.getParameter("studentId"));
        String email = trim(request.getParameter("email"));
        String username = trim(request.getParameter("username"));
        String password = trim(request.getParameter("password"));
        String confirmPassword = trim(request.getParameter("confirmPassword"));

        List<String> errors = new ArrayList<>();
        if (name.isEmpty()) {
            errors.add("Name is required.");
        }
        if (studentId.isEmpty()) {
            errors.add("Student ID is required.");
        }
        if (email.isEmpty()) {
            errors.add("Email is required.");
        }
        if (username.isEmpty()) {
            errors.add("Username is required.");
        }
        if (password.isEmpty()) {
            errors.add("Password is required.");
        }
        if (!password.equals(confirmPassword)) {
            errors.add("Passwords do not match.");
        }

        if (!errors.isEmpty()) {
            setDraftAttributes(request, name, studentId, email, username, errors);
            WebUtils.forward(request, response, "/WEB-INF/jsp/register.jsp");
            return;
        }

        try {
            AuthUser user = AppContext.AUTH.registerTaUser(username, password);
            ApplicantProfile profile = new ApplicantProfile(user.getId());
            profile.setName(name);
            profile.setStudentId(studentId);
            profile.setEmail(email);
            profile.setSkills(List.of());
            profile.setAvailability("");
            AppContext.PROFILES_SERVICE.saveProfile(profile);

            HttpSession session = request.getSession(true);
            session.setAttribute(WebUtils.SESSION_USER, user);
            WebUtils.setFlash(request, "Account created successfully. Complete your profile to apply for jobs.");
            response.sendRedirect(request.getContextPath() + "/ta/profile");
        } catch (IllegalArgumentException ex) {
            errors.add(ex.getMessage());
            setDraftAttributes(request, name, studentId, email, username, errors);
            WebUtils.forward(request, response, "/WEB-INF/jsp/register.jsp");
        }
    }

    private void setDraftAttributes(HttpServletRequest request, String name, String studentId,
                                    String email, String username, List<String> errors) {
        request.setAttribute("draftName", name);
        request.setAttribute("draftStudentId", studentId);
        request.setAttribute("draftEmail", email);
        request.setAttribute("draftUsername", username);
        request.setAttribute("errors", errors);
    }

    private String trim(String value) {
        return value == null ? "" : value.trim();
    }
}
