<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="cn.bupt.tarecruitment.model.WorkloadRow" %>
<%@ page import="cn.bupt.tarecruitment.model.WorkloadSummary" %>
<%
    WorkloadSummary summary = (WorkloadSummary) request.getAttribute("summary");
    List<WorkloadRow> rows = (List<WorkloadRow>) request.getAttribute("rows");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>TA Workload Dashboard</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 32px; background: #f7f7fb; color: #1f2937; }
        h1 { margin-bottom: 8px; }
        .summary { display: grid; grid-template-columns: repeat(auto-fit, minmax(160px, 1fr)); gap: 12px; margin: 20px 0; }
        .card { background: white; border-radius: 12px; padding: 16px; box-shadow: 0 4px 18px rgba(15, 23, 42, 0.08); }
        .card strong { display: block; font-size: 1.6rem; margin-top: 6px; }
        table { width: 100%; border-collapse: collapse; background: white; border-radius: 12px; overflow: hidden; box-shadow: 0 4px 18px rgba(15, 23, 42, 0.08); }
        th, td { padding: 12px 14px; text-align: left; border-bottom: 1px solid #e5e7eb; }
        th { background: #111827; color: white; }
        tr.overload { background: #fff1f2; }
        .badge { display: inline-block; padding: 4px 8px; border-radius: 999px; font-size: 0.85rem; }
        .ok { background: #dcfce7; color: #166534; }
        .warn { background: #fee2e2; color: #991b1b; }
    </style>
</head>
<body>
<h1>TA Workload Dashboard</h1>
<p>Threshold: <%= summary.getThresholdHours() %> hours</p>

<div class="summary">
    <div class="card">Total TAs<strong><%= summary.getTotalTAs() %></strong></div>
    <div class="card">Overloaded TAs<strong><%= summary.getOverloadedTAs() %></strong></div>
    <div class="card">Assignments<strong><%= summary.getTotalAssignments() %></strong></div>
    <div class="card">Total Hours<strong><%= summary.getTotalHours() %></strong></div>
</div>

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
    <%
        for (WorkloadRow row : rows) {
    %>
    <tr class="<%= row.isOverloaded() ? "overload" : "" %>">
        <td><%= row.getApplicantId() %></td>
        <td><%= row.getApplicantName() %></td>
        <td><%= row.getAssignmentCount() %></td>
        <td><%= row.getTotalHours() %></td>
        <td>
            <span class="badge <%= row.isOverloaded() ? "warn" : "ok" %>">
                <%= row.isOverloaded() ? "Overloaded" : "Normal" %>
            </span>
        </td>
    </tr>
    <%
        }
    %>
    </tbody>
</table>
</body>
</html>
