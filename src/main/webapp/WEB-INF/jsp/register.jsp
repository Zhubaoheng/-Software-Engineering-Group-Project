<%@ page import="cn.bupt.tarecruitment.util.WebUtils" %>
<%
    String error = (String) request.getAttribute("error");
    String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register - TA Recruitment System</title>
    <link rel="stylesheet" href="<%= contextPath %>/assets/app.css">
</head>
<body class="auth-shell">
<div class="auth-layout">
    <section class="auth-intro">
        <div class="eyebrow">BUPT International School</div>
        <h1>Join the recruitment workspace</h1>
        <p>
            Create an account to access the Teaching Assistant recruitment system.
            Choose your role during registration to get started with the appropriate workflow.
        </p>
        <div class="feature-list">
            <div class="feature-item">
                <strong>TA Applicant</strong><br>
                Create a profile, upload your CV, browse available positions, and submit applications.
            </div>
            <div class="feature-item">
                <strong>Module Organiser</strong><br>
                Post job openings, review applicants, and manage the selection process.
            </div>
        </div>
    </section>

    <section class="auth-card">
        <div class="eyebrow">Create Account</div>
        <h2>Register a new account</h2>
        <p class="muted">Fill in the form below to create your account and start using the system.</p>
        <% if (error != null && !error.isBlank()) { %>
        <div class="error"><%= WebUtils.escapeHtml(error) %></div>
        <% } %>
        <form method="post" action="<%= contextPath %>/register" class="form">
            <label>Username
                <input type="text" name="username" required minlength="3" maxlength="30"
                       placeholder="Choose a username">
            </label>
            <label>Password
                <input type="password" name="password" required minlength="4"
                       placeholder="Choose a password">
            </label>
            <label>Confirm Password
                <input type="password" name="confirmPassword" required minlength="4"
                       placeholder="Re-enter your password">
            </label>
            <label>Role
                <select name="role" required>
                    <option value="">-- Select your role --</option>
                    <option value="TA">TA Applicant</option>
                    <option value="MO">Module Organiser</option>
                </select>
            </label>
            <button type="submit">Create Account</button>
        </form>
        <p class="muted" style="margin-top: 1rem; text-align: center;">
            Already have an account? <a href="<%= contextPath %>/login">Sign in</a>
        </p>
    </section>
</div>
</body>
</html>
