<%@ page import="cn.bupt.tarecruitment.model.JobPost,cn.bupt.tarecruitment.util.WebUtils" %>
<%
    request.setAttribute("pageTitle", "Job Details");
    request.setAttribute("pageMeta", "Review the full scope of a TA job before deciding whether to submit an application.");
    JobPost job = (JobPost) request.getAttribute("job");
    Boolean alreadyApplied = (Boolean) request.getAttribute("alreadyApplied");
%>
<%@ include file="/WEB-INF/jsp/common/header.jspf" %>
<section class="card">
    <% if (job == null) { %>
    <p>Job not found.</p>
    <% } else { %>
    <div class="eyebrow">Module Detail</div>
    <h2><%= WebUtils.escapeHtml(job.getModuleCode()) %> - <%= WebUtils.escapeHtml(job.getModuleName()) %></h2>
    <p class="muted">Organiser: <%= WebUtils.escapeHtml(job.getOrganiserId()) %></p>
    <p class="muted-block"><%= WebUtils.escapeHtml(job.getDescription()) %></p>
    <div class="meta-row">
        <div class="meta-card">
            <strong>Hours / Week</strong>
            <span><%= job.getHoursPerWeek() %></span>
        </div>
        <div class="meta-card">
            <strong>Positions</strong>
            <span><%= job.getPositions() %></span>
        </div>
        <div class="meta-card">
            <strong>Deadline</strong>
            <span><%= WebUtils.escapeHtml(job.getDeadline()) %></span>
        </div>
    </div>
    <ul class="detail-list">
        <li><strong>Required Skills:</strong> <%= WebUtils.escapeHtml(WebUtils.joinCsv(job.getRequiredSkills())) %></li>
    </ul>
    <div class="toolbar">
        <form method="post" action="<%= request.getContextPath() %>/ta/apply" class="inline-form">
            <input type="hidden" name="jobId" value="<%= WebUtils.escapeHtml(job.getJobId()) %>">
            <button type="submit" <%= Boolean.TRUE.equals(alreadyApplied) ? "disabled" : "" %>>
                <%= Boolean.TRUE.equals(alreadyApplied) ? "Already Applied" : "Apply for this job" %>
            </button>
        </form>
        <a class="secondary-link" href="<%= request.getContextPath() %>/ta/jobs">Back to job board</a>
    </div>
    <% } %>
</section>
<%@ include file="/WEB-INF/jsp/common/footer.jspf" %>
