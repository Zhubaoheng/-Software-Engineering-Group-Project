<%@ page import="cn.bupt.tarecruitment.model.AuthUser" %>
<%
    AuthUser user = (AuthUser) session.getAttribute("currentUser");
    String role = user == null ? "" : user.getRole();
    request.setAttribute("pageTitle", "Workspace Overview");
    request.setAttribute("pageMeta", "Welcome to the TA Recruitment System. Use the sidebar to navigate.");
%>
<%@ include file="/WEB-INF/jsp/common/header.jspf" %>

<section class="card">
    <h2>Welcome, <%= user == null ? "Guest" : user.getUsername() %></h2>
    <p>You are signed in as <strong><%= role %></strong>. Use the sidebar navigation to access the available features for your role.</p>
</section>

<% if ("TA".equalsIgnoreCase(role)) { %>
<div class="card-grid">
    <section class="card">
        <h3>My Profile</h3>
        <p>Create and manage your applicant profile, including skills, education, and contact information.</p>
        <a href="<%= request.getContextPath() %>/ta/profile" class="btn">Edit Profile</a>
    </section>
    <section class="card">
        <h3>Browse Jobs</h3>
        <p>View available TA positions and apply for the ones that match your skills.</p>
        <a href="<%= request.getContextPath() %>/ta/jobs" class="btn">View Jobs</a>
    </section>
    <section class="card">
        <h3>My Applications</h3>
        <p>Track the status of your submitted applications.</p>
        <a href="<%= request.getContextPath() %>/ta/applications" class="btn">View Applications</a>
    </section>
</div>
<% } else if ("MO".equalsIgnoreCase(role)) { %>
<div class="card-grid">
    <section class="card">
        <h3>Job Posts</h3>
        <p>Create and manage job postings for your modules.</p>
        <a href="<%= request.getContextPath() %>/mo/jobs" class="btn">Manage Jobs</a>
    </section>
    <section class="card">
        <h3>Review Applicants</h3>
        <p>Review and manage applications for your posted positions.</p>
        <a href="<%= request.getContextPath() %>/mo/applicants" class="btn">View Applicants</a>
    </section>
</div>
<% } else if ("ADMIN".equalsIgnoreCase(role)) { %>
<div class="card-grid">
    <section class="card">
        <h3>Workload Dashboard</h3>
        <p>Monitor TA workload distribution and identify potential overload situations.</p>
        <a href="<%= request.getContextPath() %>/admin/workload" class="btn">View Workload</a>
    </section>
</div>
<% } %>

<%@ include file="/WEB-INF/jsp/common/footer.jspf" %>
