<%@ page import="cn.bupt.tarecruitment.model.ApplicationReviewView" %>
<%@ page import="cn.bupt.tarecruitment.util.WebUtils" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Applicant Review</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 24px; background: #f6f7fb; }
        .card { background: #fff; border: 1px solid #ddd; border-radius: 12px; padding: 20px; max-width: 900px; }
        .message { background: #eef7ff; padding: 10px; margin-bottom: 12px; border: 1px solid #b5dcff; }
        button { margin-right: 8px; }
    </style>
</head>
<body>
<% String message = (String) request.getAttribute("flashMessage"); if (message != null) { %>
    <div class="message"><%= WebUtils.escapeHtml(message) %></div>
<% } %>
<% String error = (String) request.getAttribute("error"); if (error != null) { %>
    <div class="message"><%= WebUtils.escapeHtml(error) %></div>
<% } %>
<%
    ApplicationReviewView review = (ApplicationReviewView) request.getAttribute("review");
%>
<div class="card">
    <h2><%= WebUtils.escapeHtml(review.getJob().getModuleCode()) %> - <%= WebUtils.escapeHtml(review.getJob().getModuleName()) %></h2>
    <p><strong>Applicant:</strong> <%= review.getProfile() == null ? "-" : WebUtils.escapeHtml(review.getProfile().getName()) %></p>
    <p><strong>Email:</strong> <%= review.getProfile() == null ? "-" : WebUtils.escapeHtml(review.getProfile().getEmail()) %></p>
    <p><strong>Skills:</strong> <%= review.getProfile() == null ? "-" : WebUtils.escapeHtml(String.join(", ", review.getProfile().getSkills())) %></p>
    <p><strong>Availability:</strong> <%= review.getProfile() == null ? "-" : WebUtils.escapeHtml(review.getProfile().getAvailability()) %></p>
    <p><strong>CV:</strong> <%= review.getProfile() == null ? "-" : WebUtils.escapeHtml(review.getProfile().getCvFileName()) %></p>
    <p><strong>Status:</strong> <%= WebUtils.escapeHtml(review.getApplication().getStatus()) %></p>
    <form method="post" action="<%= request.getContextPath() %>/mo/applicants/review">
        <input type="hidden" name="applicationId" value="<%= review.getApplication().getApplicationId() %>">
        <button type="submit" name="decision" value="Shortlisted">Shortlist</button>
        <button type="submit" name="decision" value="Selected">Select</button>
        <button type="submit" name="decision" value="Rejected">Reject</button>
    </form>
    <p><a href="<%= request.getContextPath() %>/mo/applicants?jobId=<%= review.getJob().getJobId() %>">Back</a></p>
</div>
</body>
</html>
