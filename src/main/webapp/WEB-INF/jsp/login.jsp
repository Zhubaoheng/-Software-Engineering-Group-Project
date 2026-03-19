<%@ page import="cn.bupt.tarecruitment.util.WebUtils" %>
<%
    String error = (String) request.getAttribute("error");
    String flash = (String) request.getAttribute("flashMessage");
    String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - TA Recruitment System</title>
    <link rel="stylesheet" href="<%= contextPath %>/assets/app.css">
</head>
<body class="auth-shell">
<div class="auth-layout">
    <section class="auth-intro">
        <div class="eyebrow">BUPT International School</div>
        <h1>Assist the hiring workflow, not the spreadsheet.</h1>
        <p>
            This demo interface is designed as a calm recruitment workspace. It lets you move through
            profile editing, job browsing, applicant review, and workload monitoring in a single lightweight
            Java Servlet/JSP application.
        </p>
        <div class="feature-list">
            <div class="feature-item">
                <strong>TA flow</strong><br>
                Build a profile, upload a CV, browse open posts, and track application outcomes.
            </div>
            <div class="feature-item">
                <strong>MO flow</strong><br>
                Create posts, inspect applicants, and move candidates from shortlist to final selection.
            </div>
            <div class="feature-item">
                <strong>Admin flow</strong><br>
                Watch assignment pressure and identify overload before confirming more work.
            </div>
        </div>
    </section>

    <section class="auth-card">
        <div class="eyebrow">Sign In</div>
        <h2>Enter the recruitment workspace</h2>
        <p class="muted">Use one of the seeded accounts below to explore the current MVP.</p>
        <% if (flash != null && !flash.isBlank()) { %>
        <div class="flash"><%= WebUtils.escapeHtml(flash) %></div>
        <% } %>
        <% if (error != null && !error.isBlank()) { %>
        <div class="error"><%= WebUtils.escapeHtml(error) %></div>
        <% } %>
        <form method="post" action="<%= contextPath %>/login" class="form">
            <label>Username
                <input type="text" name="username" required>
            </label>
            <label>Password
                <input type="password" name="password" required>
            </label>
            <button type="submit">Sign in</button>
        </form>
        <div class="account-grid">
            <div class="account-chip"><strong>TA</strong><span><code>ta01 / ta01</code></span></div>
            <div class="account-chip"><strong>MO</strong><span><code>mo01 / mo01</code></span></div>
            <div class="account-chip"><strong>Admin</strong><span><code>admin01 / admin01</code></span></div>
        </div>
    </section>
</div>
</body>
</html>
