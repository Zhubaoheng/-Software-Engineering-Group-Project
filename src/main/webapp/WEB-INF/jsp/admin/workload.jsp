<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="cn.bupt.tarecruitment.model.WorkloadRow" %>
<%@ page import="cn.bupt.tarecruitment.model.WorkloadSummary" %>
<%@ page import="cn.bupt.tarecruitment.util.WebUtils" %>
<%
    request.setAttribute("pageTitle", "TA Workload Dashboard");
    request.setAttribute("pageMeta", "Monitor assignment concentration across teaching assistants and identify overload risk before confirming more selections.");
    WorkloadSummary summary = (WorkloadSummary) request.getAttribute("summary");
    List<WorkloadRow> rows = (List<WorkloadRow>) request.getAttribute("rows");
%>
<%@ include file="/WEB-INF/jsp/common/header.jspf" %>
<section class="section-stack">
    <div class="stats-grid">
        <div class="stat-card">
            <div class="stat-label">Threshold Hours</div>
            <div class="stat-value"><%= summary.getThresholdHours() %></div>
        </div>
        <div class="stat-card">
            <div class="stat-label">Tracked TAs</div>
            <div class="stat-value"><%= summary.getTotalTAs() %></div>
        </div>
        <div class="stat-card">
            <div class="stat-label">Overloaded TAs</div>
            <div class="stat-value"><%= summary.getOverloadedTAs() %></div>
        </div>
        <div class="stat-card">
            <div class="stat-label">Total Assigned Hours</div>
            <div class="stat-value"><%= summary.getTotalHours() %></div>
        </div>
    </div>

    <section class="card">
        <h2>Assignment Pressure</h2>
        <div class="table">
            <table>
                <thead>
                <tr>
                    <th>Applicant ID</th>
                    <th>Name</th>
                    <th>Assignments</th>
                    <th>Total Hours</th>
                    <th>Status</th>
                </tr>
                </thead>
                <tbody>
                <% for (WorkloadRow row : rows) { %>
                <tr>
                    <td><%= WebUtils.escapeHtml(row.getApplicantId()) %></td>
                    <td><%= WebUtils.escapeHtml(row.getApplicantName()) %></td>
                    <td><%= row.getAssignmentCount() %></td>
                    <td><%= row.getTotalHours() %></td>
                    <td>
                        <span class="<%= row.isOverloaded() ? "badge badge-danger" : "badge badge-ok" %>">
                            <%= row.isOverloaded() ? "Overloaded" : "Normal" %>
                        </span>
                    </td>
                </tr>
                <% } %>
                </tbody>
            </table>
        </div>
    </section>
</section>
<%@ include file="/WEB-INF/jsp/common/footer.jspf" %>
