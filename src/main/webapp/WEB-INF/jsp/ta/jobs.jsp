<%@ page import="java.util.List,cn.bupt.tarecruitment.model.JobPost,cn.bupt.tarecruitment.util.WebUtils" %>
<%@ include file="/WEB-INF/jsp/common/header.jspf" %>
<%
    List<JobPost> jobs = (List<JobPost>) request.getAttribute("jobs");
%>
<section class="card">
    <h1>Open Jobs</h1>
    <% if (jobs == null || jobs.isEmpty()) { %>
    <p>No open jobs are available yet.</p>
    <% } else { %>
    <div class="table">
        <table>
            <thead>
            <tr>
                <th>Module</th>
                <th>Skills</th>
                <th>Hours/Week</th>
                <th>Deadline</th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            <% for (JobPost job : jobs) { %>
            <tr>
                <td>
                    <strong><%= WebUtils.escapeHtml(job.getModuleCode()) %></strong><br>
                    <%= WebUtils.escapeHtml(job.getModuleName()) %>
                </td>
                <td><%= WebUtils.escapeHtml(WebUtils.joinCsv(job.getRequiredSkills())) %></td>
                <td><%= job.getHoursPerWeek() %></td>
                <td><%= WebUtils.escapeHtml(job.getDeadline()) %></td>
                <td><a class="link-button" href="<%= request.getContextPath() %>/ta/job-detail?jobId=<%= WebUtils.escapeHtml(job.getJobId()) %>">View</a></td>
            </tr>
            <% } %>
            </tbody>
        </table>
    </div>
    <% } %>
</section>
<%@ include file="/WEB-INF/jsp/common/footer.jspf" %>
