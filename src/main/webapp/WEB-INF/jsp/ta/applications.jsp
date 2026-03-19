<%@ page import="java.util.List,java.util.Map,cn.bupt.tarecruitment.model.ApplicationRecord,cn.bupt.tarecruitment.model.JobPost,cn.bupt.tarecruitment.util.WebUtils" %>
<%
    request.setAttribute("pageTitle", "My Applications");
    request.setAttribute("pageMeta", "Track the recruitment status of every job you have applied for in one place.");
    List<ApplicationRecord> applications = (List<ApplicationRecord>) request.getAttribute("applications");
    Map<String, JobPost> jobsById = (Map<String, JobPost>) request.getAttribute("jobsById");
%>
<%@ include file="/WEB-INF/jsp/common/header.jspf" %>
<section class="section-stack">
    <div class="info-card">
        <h3>Status Meanings</h3>
        <p class="muted-block">Submitted means your application is received. Shortlisted and Selected are set by the Module Organiser. Rejected and Withdrawn remain visible for traceability.</p>
    </div>
    <section class="card">
        <h2>Application Timeline</h2>
        <% if (applications == null || applications.isEmpty()) { %>
        <p>You have not submitted any applications yet.</p>
        <% } else { %>
        <div class="table">
            <table>
                <thead>
                <tr>
                    <th>Job</th>
                    <th>Status</th>
                    <th>Submitted At</th>
                </tr>
                </thead>
                <tbody>
                <% for (ApplicationRecord application : applications) {
                       JobPost job = jobsById == null ? null : jobsById.get(application.getJobId());
                %>
                <tr>
                    <td>
                        <strong><%= job == null ? application.getJobId() : WebUtils.escapeHtml(job.getModuleCode()) %></strong><br>
                        <%= job == null ? "" : WebUtils.escapeHtml(job.getModuleName()) %>
                    </td>
                    <td><span class="status"><%= WebUtils.escapeHtml(application.getStatus()) %></span></td>
                    <td><%= WebUtils.escapeHtml(application.getSubmittedAt()) %></td>
                </tr>
                <% } %>
                </tbody>
            </table>
        </div>
        <% } %>
    </section>
</section>
<%@ include file="/WEB-INF/jsp/common/footer.jspf" %>
