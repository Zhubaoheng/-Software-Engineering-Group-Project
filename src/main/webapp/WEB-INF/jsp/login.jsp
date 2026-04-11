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
    <section class="auth-showcase">
        <div class="eyebrow">BUPT International School</div>
        <h1>Teaching Assistant Recruitment</h1>
        <p class="auth-showcase-copy">A lightweight recruitment workspace for TA applications, MO review, and workload visibility.</p>
        <div class="team-panel">
            <div class="team-panel-label">Project Team</div>
            <div class="team-list">
                <div class="team-member">
                    <span class="team-handle">zhubaoheng</span>
                    <span class="team-id">221170962</span>
                </div>
                <div class="team-member">
                    <span class="team-handle">colin596</span>
                    <span class="team-id">231225085</span>
                </div>
                <div class="team-member">
                    <span class="team-handle">RongjiaLiu</span>
                    <span class="team-id">231224608</span>
                </div>
                <div class="team-member">
                    <span class="team-handle">LHR1105</span>
                    <span class="team-id">221171305</span>
                </div>
                <div class="team-member">
                    <span class="team-handle">tokidosaya010</span>
                    <span class="team-id">231224756</span>
                </div>
                <div class="team-member">
                    <span class="team-handle">lswsb</span>
                    <span class="team-id">2024018006 (Support TA)</span>
                </div>
            </div>
        </div>
    </section>

    <section class="auth-card">
        <div class="auth-brand">
            <div class="auth-logo">
                <span class="auth-logo-core">TR</span>
            </div>
            <div class="auth-brand-copy">
                <div class="eyebrow">Product Access</div>
                <h2>Sign in to continue</h2>
                <p>Use your assigned account to enter the recruitment workspace.</p>
            </div>
        </div>
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
        <div class="auth-actions">
            <a class="secondary-link" href="<%= contextPath %>/register">Create TA account</a>
        </div>
        <div class="auth-note">
            <span class="subtle-badge">Demo access</span>
            <p>TA <code>ta01 / ta01</code> | MO <code>mo01 / mo01</code> | Admin <code>admin01 / admin01</code></p>
        </div>
    </section>
</div>
</body>
</html>
