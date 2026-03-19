<%@ page import="java.util.List" %>
<%@ page import="cn.bupt.tarecruitment.model.JobPost" %>
<%@ page import="cn.bupt.tarecruitment.model.ApplicationReviewView" %>
<%@ page import="cn.bupt.tarecruitment.util.WebUtils" %>
<%
    List<ApplicationReviewView> applications = (List<ApplicationReviewView>) request.getAttribute("applications");
    JobPost job = (JobPost) request.getAttribute("job");
    request.setAttribute("pageTitle", "Applicant Review");
    request.setAttribute("pageMeta", applications != null
            ? "Inspect submitted applicants for the selected module and move them through the decision process."
            : "Pick a job post first, then inspect the linked applicant list.");
%>
<%@ include file="/WEB-INF/jsp/common/header.jspf" %>
<section class="section-stack">
    <div class="toolbar">
        <a class="secondary-link" href="<%= request.getContextPath() %>/mo/jobs">Back to job posts</a>
    </div>
    <% if (applications != null) { %>
    <section class="card">
        <h2>Applicants for <%= job == null ? "" : WebUtils.escapeHtml(job.getModuleCode() + " - " + job.getModuleName()) %></h2>
        <table>
            <tr>
                <th>Applicant</th>
                <th>Email</th>
                <th>Skills</th>
                <th>Status</th>
                <th>Action</th>
            </tr>
            <% for (ApplicationReviewView review : applications) { %>
            <tr>
                <td><%= review.getProfile() == null ? "-" : WebUtils.escapeHtml(review.getProfile().getName()) %></td>
                <td><%= review.getProfile() == null ? "-" : WebUtils.escapeHtml(review.getProfile().getEmail()) %></td>
                <td><%= review.getProfile() == null ? "-" : WebUtils.escapeHtml(String.join(", ", review.getProfile().getSkills())) %></td>
                <td><span class="status"><%= WebUtils.escapeHtml(review.getApplication().getStatus()) %></span></td>
                <td><a href="<%= request.getContextPath() %>/mo/applicants/review?applicationId=<%= review.getApplication().getApplicationId() %>">Review</a></td>
            </tr>
            <% } %>
        </table>
    </section>
    <% } else {
           List<JobPost> jobs = (List<JobPost>) request.getAttribute("jobs");
    %>
    <section class="card">
        <h2>Select a job</h2>
        <div class="stack">
            <% if (jobs != null) {
                   for (JobPost listedJob : jobs) { %>
            <div class="info-card">
                <h3><%= WebUtils.escapeHtml(listedJob.getModuleCode()) %> - <%= WebUtils.escapeHtml(listedJob.getModuleName()) %></h3>
                <p class="muted-block">Deadline: <%= WebUtils.escapeHtml(listedJob.getDeadline()) %> · Status: <%= WebUtils.escapeHtml(listedJob.getStatus()) %></p>
                <a class="link-button" href="<%= request.getContextPath() %>/mo/applicants?jobId=<%= listedJob.getJobId() %>">Open applicant list</a>
            </div>
            <%     }
               } %>
        </div>
    </section>
    <% } %>
</section>
<%@ include file="/WEB-INF/jsp/common/footer.jspf" %>
