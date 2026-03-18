<%@ page import="java.util.List" %>
<%@ page import="cn.bupt.tarecruitment.model.JobPost" %>
<%@ page import="cn.bupt.tarecruitment.model.ApplicationReviewView" %>
<%@ page import="cn.bupt.tarecruitment.util.WebUtils" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Applicants</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 24px; background: #f6f7fb; }
        table { width: 100%; border-collapse: collapse; background: #fff; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; vertical-align: top; }
        .message { background: #eef7ff; padding: 10px; margin-bottom: 12px; border: 1px solid #b5dcff; }
    </style>
</head>
<body>
<% String message = (String) request.getAttribute("flashMessage"); if (message != null) { %>
    <div class="message"><%= WebUtils.escapeHtml(message) %></div>
<% } %>
<a href="<%= request.getContextPath() %>/mo/jobs">Back to jobs</a>
<%
    List<ApplicationReviewView> applications = (List<ApplicationReviewView>) request.getAttribute("applications");
    if (applications != null) {
        JobPost job = (JobPost) request.getAttribute("job");
%>
<h2>Applicants for <%= job == null ? "" : WebUtils.escapeHtml(job.getModuleCode() + " - " + job.getModuleName()) %></h2>
<table>
    <tr>
        <th>Applicant</th>
        <th>Email</th>
        <th>Skills</th>
        <th>Status</th>
        <th>Action</th>
    </tr>
    <%
        for (ApplicationReviewView review : applications) {
    %>
    <tr>
        <td><%= review.getProfile() == null ? "-" : WebUtils.escapeHtml(review.getProfile().getName()) %></td>
        <td><%= review.getProfile() == null ? "-" : WebUtils.escapeHtml(review.getProfile().getEmail()) %></td>
        <td><%= review.getProfile() == null ? "-" : WebUtils.escapeHtml(String.join(", ", review.getProfile().getSkills())) %></td>
        <td><%= WebUtils.escapeHtml(review.getApplication().getStatus()) %></td>
        <td><a href="<%= request.getContextPath() %>/mo/applicants/review?applicationId=<%= review.getApplication().getApplicationId() %>">Review</a></td>
    </tr>
    <% } %>
</table>
<%
    } else {
        List<JobPost> jobs = (List<JobPost>) request.getAttribute("jobs");
%>
<h2>Select a job</h2>
<ul>
    <%
        if (jobs != null) {
            for (JobPost job : jobs) {
    %>
    <li>
        <a href="<%= request.getContextPath() %>/mo/applicants?jobId=<%= job.getJobId() %>">
            <%= WebUtils.escapeHtml(job.getModuleCode()) %> - <%= WebUtils.escapeHtml(job.getModuleName()) %>
        </a>
    </li>
    <%      }
        }
    %>
</ul>
<% } %>
</body>
</html>
