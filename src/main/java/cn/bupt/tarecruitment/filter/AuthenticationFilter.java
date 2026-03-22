package cn.bupt.tarecruitment.filter;

import cn.bupt.tarecruitment.util.WebUtils;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Set;

@WebFilter("/*")
public class AuthenticationFilter implements Filter {
    private static final Set<String> PUBLIC_PATHS = Set.of(
            "",
            "/",
            "/index.jsp",
            "/login",
            "/register"
    );

    @Override
    public void doFilter(jakarta.servlet.ServletRequest request, jakarta.servlet.ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String servletPath = httpRequest.getServletPath();

        if (isPublicPath(servletPath) || WebUtils.currentUser(httpRequest) != null) {
            chain.doFilter(request, response);
            return;
        }

        if ("GET".equalsIgnoreCase(httpRequest.getMethod())) {
            WebUtils.rememberPostLoginRedirect(httpRequest);
        }
        WebUtils.setFlash(httpRequest, "Please sign in to continue.");
        httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
    }

    private boolean isPublicPath(String servletPath) {
        if (servletPath == null) {
            return false;
        }
        return PUBLIC_PATHS.contains(servletPath) || servletPath.startsWith("/assets/");
    }
}
