package cn.bupt.tarecruitment.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public final class Flash {
    private static final String KEY = "flashMessage";

    private Flash() {
    }

    public static void set(HttpSession session, String message) {
        if (session != null) {
            session.setAttribute(KEY, message);
        }
    }

    public static String consume(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        Object value = session.getAttribute(KEY);
        if (value == null) {
            return null;
        }
        session.removeAttribute(KEY);
        return String.valueOf(value);
    }
}

