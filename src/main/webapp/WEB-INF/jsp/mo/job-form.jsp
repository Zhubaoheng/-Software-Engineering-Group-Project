<%@ page import="cn.bupt.tarecruitment.model.JobPost" %>
<%@ page import="cn.bupt.tarecruitment.util.WebUtils" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Job Form</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 24px; background: #f6f7fb; }
        .card { max-width: 820px; background: #fff; padding: 20px; border: 1px solid #ddd; border-radius: 12px; }
        input, textarea, select { width: 100%; padding: 8px; margin: 6px 0 14px; }
        .message { background: #fef4e6; padding: 10px; border: 1px solid #ffd59e; margin-bottom: 12px; }
    </style>
</head>
<body>
<div class="card">
    <h2><%= request.getAttribute("job") != null && ((JobPost) request.getAttribute("job")).getJobId() != null ? "Edit Job Post" : "Create Job Post" %></h2>
    <% String message = (String) request.getAttribute("error"); if (message != null) { %>
        <div class="message"><%= WebUtils.escapeHtml(message) %></div>
    <% } %>
    <%
        JobPost job = (JobPost) request.getAttribute("job");
        boolean editing = job != null && job.getJobId() != null;
        String currentStatus = job == null || job.getStatus() == null ? "OPEN" : job.getStatus();
    %>
    <form method="post" action="<%= request.getContextPath() %>/mo/jobs">
        <input type="hidden" name="action" value="<%= editing ? "edit" : "create" %>">
        <input type="hidden" name="jobId" value="<%= editing ? job.getJobId() : "" %>">
        <label>Module Code</label>
        <input name="moduleCode" value="<%= editing ? WebUtils.escapeHtml(job.getModuleCode()) : "" %>" required>
        <label>Module Name</label>
        <input name="moduleName" value="<%= editing ? WebUtils.escapeHtml(job.getModuleName()) : "" %>" required>
        <label>Description</label>
        <textarea name="description" rows="5" required><%= editing ? WebUtils.escapeHtml(job.getDescription()) : "" %></textarea>
        <label>Required Skills (comma separated)</label>
        <input name="requiredSkills" value="<%= editing ? WebUtils.escapeHtml(String.join(", ", job.getRequiredSkills())) : "" %>">
        <label>Hours Per Week</label>
        <input type="number" name="hoursPerWeek" min="1" value="<%= editing ? job.getHoursPerWeek() : 0 %>" required>
        <label>Positions</label>
        <input type="number" name="positions" min="1" value="<%= editing ? job.getPositions() : 1 %>" required>
        <label>Deadline</label>
        <input type="date" name="deadline" value="<%= editing ? WebUtils.escapeHtml(job.getDeadline()) : "" %>" required>
        <label>Status</label>
        <select name="status">
            <option value="OPEN" <%= "OPEN".equalsIgnoreCase(currentStatus) ? "selected" : "" %>>Open</option>
            <option value="CLOSED" <%= "CLOSED".equalsIgnoreCase(currentStatus) ? "selected" : "" %>>Closed</option>
        </select>
        <button type="submit">Save</button>
        <a href="<%= request.getContextPath() %>/mo/jobs">Back</a>
    </form>
</div>
</body>
</html>
