<%@ page import="cn.bupt.tarecruitment.model.JobPost,cn.bupt.tarecruitment.util.WebUtils" %>
<%@ include file="/WEB-INF/jsp/common/header.jspf" %>
<%
    JobPost job = (JobPost) request.getAttribute("job");
    Boolean alreadyApplied = (Boolean) request.getAttribute("alreadyApplied");
%>
<section class="card">
    <% if (job == null) { %>
    <p>Job not found.</p>
    <% } else { %>
    <h1><%= WebUtils.escapeHtml(job.getModuleCode()) %> - <%= WebUtils.escapeHtml(job.getModuleName()) %></h1>
    <p class="muted">Organiser: <%= WebUtils.escapeHtml(job.getOrganiserId()) %></p>
    <p><%= WebUtils.escapeHtml(job.getDescription()) %></p>
    <ul class="detail-list">
        <li><strong>Required Skills:</strong> <%= WebUtils.escapeHtml(WebUtils.joinCsv(job.getRequiredSkills())) %></li>
        <li><strong>Hours/Week:</strong> <%= job.getHoursPerWeek() %></li>
        <li><strong>Positions:</strong> <%= job.getPositions() %></li>
        <li><strong>Deadline:</strong> <%= WebUtils.escapeHtml(job.getDeadline()) %></li>
    </ul>
    <form method="post" action="<%= request.getContextPath() %>/ta/apply">
        <input type="hidden" name="jobId" value="<%= WebUtils.escapeHtml(job.getJobId()) %>">
        <button type="submit" <%= Boolean.TRUE.equals(alreadyApplied) ? "disabled" : "" %>>
            <%= Boolean.TRUE.equals(alreadyApplied) ? "Already Applied" : "Apply for this job" %>
        </button>
    </form>
    <% } %>
</section>
<%@ include file="/WEB-INF/jsp/common/footer.jspf" %>
