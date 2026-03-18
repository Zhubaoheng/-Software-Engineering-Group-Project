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
<body class="auth-page">
<section class="auth-card">
    <h1>Teaching Assistant Recruitment</h1>
    <p class="muted">Sign in with a seed account to explore the MVP.</p>
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
    <div class="hint">
        Demo accounts: <code>ta01 / ta01</code>, <code>mo01 / mo01</code>, <code>admin01 / admin01</code>
    </div>
</section>
</body>
</html>
