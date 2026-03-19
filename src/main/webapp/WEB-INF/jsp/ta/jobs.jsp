<%@ page import="java.util.List,cn.bupt.tarecruitment.model.JobPost,cn.bupt.tarecruitment.util.WebUtils" %>
<%
    request.setAttribute("pageTitle", "Open Jobs");
    request.setAttribute("pageMeta", "Browse current TA openings published by Module Organisers and inspect the requirements before applying.");
    List<JobPost> jobs = (List<JobPost>) request.getAttribute("jobs");
%>
<%@ include file="/WEB-INF/jsp/common/header.jspf" %>
<section class="section-stack">
    <div class="info-card">
        <h3>Available Opportunities</h3>
        <p class="muted-block">Each listing shows the module, expected weekly hours, required skills, and deadline. Closed positions stay out of this list to keep the TA view focused.</p>
    </div>
    <section class="card">
        <h2>Live Job Board</h2>
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
</section>
<%@ include file="/WEB-INF/jsp/common/footer.jspf" %>
