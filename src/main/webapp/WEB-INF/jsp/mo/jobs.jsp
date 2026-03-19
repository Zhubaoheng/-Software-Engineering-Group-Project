<%@ page import="java.util.List" %>
<%@ page import="cn.bupt.tarecruitment.model.JobPost" %>
<%@ page import="cn.bupt.tarecruitment.util.WebUtils" %>
<%
    request.setAttribute("pageTitle", "Module Organiser Job Posts");
    request.setAttribute("pageMeta", "Create, edit, and close TA job postings before moving on to applicant review.");
    List<JobPost> jobs = (List<JobPost>) request.getAttribute("jobs");
%>
<%@ include file="/WEB-INF/jsp/common/header.jspf" %>
<section class="section-stack">
    <div class="toolbar">
        <a class="link-button" href="<%= request.getContextPath() %>/mo/jobs?action=new">Create Job Post</a>
        <a class="secondary-link" href="<%= request.getContextPath() %>/mo/applicants">Review Applicants</a>
    </div>
    <section class="card">
        <table>
            <tr>
                <th>Module</th>
                <th>Hours</th>
                <th>Positions</th>
                <th>Deadline</th>
                <th>Status</th>
                <th>Actions</th>
            </tr>
            <% if (jobs != null) {
                   for (JobPost job : jobs) { %>
            <tr>
                <td><strong><%= WebUtils.escapeHtml(job.getModuleCode()) %></strong><br><%= WebUtils.escapeHtml(job.getModuleName()) %></td>
                <td><%= job.getHoursPerWeek() %></td>
                <td><%= job.getPositions() %></td>
                <td><%= WebUtils.escapeHtml(job.getDeadline()) %></td>
                <td><span class="status"><%= WebUtils.escapeHtml(job.getStatus()) %></span></td>
                <td>
                    <a href="<%= request.getContextPath() %>/mo/jobs?action=edit&id=<%= job.getJobId() %>">Edit</a>
                    <form class="inline-form" method="post" action="<%= request.getContextPath() %>/mo/jobs">
                        <input type="hidden" name="action" value="close">
                        <input type="hidden" name="id" value="<%= job.getJobId() %>">
                        <button type="submit">Close</button>
                    </form>
                    <a href="<%= request.getContextPath() %>/mo/applicants?jobId=<%= job.getJobId() %>">Applicants</a>
                </td>
            </tr>
            <%     }
               } %>
        </table>
    </section>
</section>
<%@ include file="/WEB-INF/jsp/common/footer.jspf" %>
