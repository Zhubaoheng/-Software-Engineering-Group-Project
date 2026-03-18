package cn.bupt.tarecruitment.util;

import cn.bupt.tarecruitment.model.AuthUser;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class WebUtils {
    public static final String SESSION_USER = "currentUser";
    public static final String SESSION_FLASH = "flashMessage";

    private WebUtils() {
    }

    public static void forward(HttpServletRequest request, HttpServletResponse response, String jspPath)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(jspPath);
        dispatcher.forward(request, response);
    }

    public static void redirect(HttpServletResponse response, String path) throws IOException {
        response.sendRedirect(path);
    }

    public static AuthUser currentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session == null ? null : (AuthUser) session.getAttribute(SESSION_USER);
    }

    public static AuthUser requireUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        AuthUser user = currentUser(request);
        if (user == null) {
            redirect(response, request.getContextPath() + "/login");
        }
        return user;
    }

    public static AuthUser requireRole(HttpServletRequest request, HttpServletResponse response, String role)
            throws IOException {
        AuthUser user = requireUser(request, response);
        if (user == null) {
            return null;
        }
        if (!role.equalsIgnoreCase(user.getRole())) {
            setFlash(request, "You do not have access to that page.");
            redirect(response, request.getContextPath() + "/login");
            return null;
        }
        return user;
    }

    public static void setFlash(HttpServletRequest request, String message) {
        HttpSession session = request.getSession();
        session.setAttribute(SESSION_FLASH, message);
    }

    public static String consumeFlash(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        Object value = session.getAttribute(SESSION_FLASH);
        if (value != null) {
            session.removeAttribute(SESSION_FLASH);
            return value.toString();
        }
        return null;
    }

    public static List<String> splitCsv(String raw) {
        if (raw == null || raw.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.stream(raw.split("[,\\n]"))
                .map(String::trim)
                .filter(value -> !value.isBlank())
                .toList();
    }

    public static String joinCsv(List<String> values) {
        if (values == null || values.isEmpty()) {
            return "";
        }
        return String.join(", ", values);
    }

    public static String escapeHtml(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}
