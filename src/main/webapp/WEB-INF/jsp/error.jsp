<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head><title>Error</title></head>
<body>
<h2>Something went wrong</h2>
<p><%= request.getAttribute("message") == null ? "Unexpected error." : request.getAttribute("message") %></p>
</body>
</html>

