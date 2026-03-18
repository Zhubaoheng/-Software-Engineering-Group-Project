<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Module Placeholder</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 24px; background: #f6f7fb; }
        .card { max-width: 720px; background: #fff; border: 1px solid #ddd; border-radius: 12px; padding: 20px; }
    </style>
</head>
<body>
<div class="card">
    <h2><%= request.getAttribute("moduleName") %> module scaffold</h2>
    <p>Route: <code><%= request.getAttribute("modulePath") %></code></p>
    <p>This role entry point is reserved for the next iteration. The MO module is already active in this baseline.</p>
    <p><a href="<%= request.getContextPath() %>/logout">Logout</a></p>
</div>
</body>
</html>
