<%@ page import="java.util.List" %>
<%@ page import="cn.bupt.tarecruitment.model.JobPost" %>
<%@ page import="cn.bupt.tarecruitment.util.WebUtils" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>MO Jobs</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 24px; background: #f6f7fb; }
        table { width: 100%; border-collapse: collapse; background: #fff; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; vertical-align: top; }
        .toolbar { margin: 0 0 16px; }
        .message { background: #eef7ff; padding: 10px; margin-bottom: 12px; border: 1px solid #b5dcff; }
        a, button { margin-right: 8px; }
    </style>
</head>
<body>
<% String message = (String) request.getAttribute("flashMessage"); if (message != null) { %>
    <div class="message"><%= WebUtils.escapeHtml(message) %></div>
<% } %>
<div class="toolbar">
    <a href="<%= request.getContextPath() %>/mo/jobs?action=new">Create Job Post</a>
    <a href="<%= request.getContextPath() %>/logout">Logout</a>
</div>
<table>
    <tr>
        <th>Module</th>
        <th>Hours</th>
        <th>Positions</th>
        <th>Deadline</th>
        <th>Status</th>
        <th>Actions</th>
    </tr>
    <%
        List<JobPost> jobs = (List<JobPost>) request.getAttribute("jobs");
        if (jobs != null) {
            for (JobPost job : jobs) {
    %>
    <tr>
        <td><strong><%= WebUtils.escapeHtml(job.getModuleCode()) %></strong><br><%= WebUtils.escapeHtml(job.getModuleName()) %></td>
        <td><%= job.getHoursPerWeek() %></td>
        <td><%= job.getPositions() %></td>
        <td><%= WebUtils.escapeHtml(job.getDeadline()) %></td>
        <td><%= WebUtils.escapeHtml(job.getStatus()) %></td>
        <td>
            <a href="<%= request.getContextPath() %>/mo/jobs?action=edit&id=<%= job.getJobId() %>">Edit</a>
            <form method="post" action="<%= request.getContextPath() %>/mo/jobs" style="display:inline;">
                <input type="hidden" name="action" value="close">
                <input type="hidden" name="id" value="<%= job.getJobId() %>">
                <button type="submit">Close</button>
            </form>
            <a href="<%= request.getContextPath() %>/mo/applicants?jobId=<%= job.getJobId() %>">Applicants</a>
        </td>
    </tr>
    <%      }
        }
    %>
</table>
</body>
</html>
