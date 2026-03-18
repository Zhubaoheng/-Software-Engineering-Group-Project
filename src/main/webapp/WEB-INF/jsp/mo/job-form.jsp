<%@ page import="cn.bupt.tarecruitment.model.JobPost" %>
<%@ page import="cn.bupt.tarecruitment.util.WebUtils" %>
<%
    JobPost job = (JobPost) request.getAttribute("job");
    boolean editing = job != null && job.getJobId() != null;
    request.setAttribute("pageTitle", editing ? "Edit Job Post" : "Create Job Post");
    request.setAttribute("pageMeta", "Define the module, expected skills, workload, deadline, and availability before publishing the post.");
    String currentStatus = job == null || job.getStatus() == null ? "OPEN" : job.getStatus();
%>
<%@ include file="/WEB-INF/jsp/common/header.jspf" %>
<section class="card">
    <h2><%= editing ? "Edit Job Post" : "Create Job Post" %></h2>
    <% String message = (String) request.getAttribute("error"); if (message != null) { %>
        <div class="error"><%= WebUtils.escapeHtml(message) %></div>
    <% } %>
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
        <div class="toolbar">
            <button type="submit">Save</button>
            <a class="secondary-link" href="<%= request.getContextPath() %>/mo/jobs">Back</a>
        </div>
    </form>
</section>
<%@ include file="/WEB-INF/jsp/common/footer.jspf" %>
