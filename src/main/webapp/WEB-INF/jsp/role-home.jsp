<%@ page import="cn.bupt.tarecruitment.model.AuthUser" %>
<%@ page import="cn.bupt.tarecruitment.util.WebUtils" %>
<%
    AuthUser user = (AuthUser) session.getAttribute("currentUser");
    String role = user == null ? "" : user.getRole();
    String username = user == null ? "Guest" : user.getUsername();
    String ctx = request.getContextPath();
    request.setAttribute("pageTitle", "Workspace Overview");
    request.setAttribute("pageMeta", "Your role-aware home for the TA Recruitment System. Pick a feature below to get started.");
%>
<%@ include file="/WEB-INF/jsp/common/header.jspf" %>

<section class="section-stack">
    <section class="card">
        <div class="feature-eyebrow">Dashboard</div>
        <h2>Welcome back, <%= WebUtils.escapeHtml(username) %></h2>
        <p class="muted-block">
            You are signed in as
            <strong><%= role.isEmpty() ? "an unrecognised role" : WebUtils.escapeHtml(role.toUpperCase()) %></strong>.
            The cards below open the features available to you. The sidebar offers the same quick links on every page.
        </p>
    </section>

    <% if ("TA".equalsIgnoreCase(role)) { %>
    <div class="card-grid">
        <section class="card feature-card">
            <div class="feature-eyebrow">Step 1</div>
            <h3>My Profile</h3>
            <p>Maintain your applicant profile &mdash; skills, education, contact details &mdash; and upload your CV so Module Organisers can assess you.</p>
            <a class="btn" href="<%= ctx %>/ta/profile">Edit Profile</a>
        </section>
        <section class="card feature-card">
            <div class="feature-eyebrow">Step 2</div>
            <h3>Open Jobs</h3>
            <p>Browse current TA openings, review required skills and weekly hours, and see how well each post matches your profile before applying.</p>
            <a class="btn" href="<%= ctx %>/ta/jobs">Browse Jobs</a>
        </section>
        <section class="card feature-card">
            <div class="feature-eyebrow">Step 3</div>
            <h3>My Applications</h3>
            <p>Track every application you have submitted and follow its decision status through shortlisting and selection.</p>
            <a class="btn" href="<%= ctx %>/ta/applications">View Applications</a>
        </section>
    </div>
    <% } else if ("MO".equalsIgnoreCase(role)) { %>
    <div class="card-grid">
        <section class="card feature-card">
            <div class="feature-eyebrow">Manage</div>
            <h3>Job Posts</h3>
            <p>Create, edit, and close TA job posts for your modules, setting required skills, weekly hours, and application deadlines.</p>
            <a class="btn" href="<%= ctx %>/mo/jobs">Manage Job Posts</a>
        </section>
        <section class="card feature-card">
            <div class="feature-eyebrow">Review</div>
            <h3>Applicants</h3>
            <p>Review applicants for each post with AI-assisted skill-match insights, then shortlist, select, or reject candidates.</p>
            <a class="btn" href="<%= ctx %>/mo/applicants">Review Applicants</a>
        </section>
    </div>
    <% } else if ("ADMIN".equalsIgnoreCase(role)) { %>
    <div class="card-grid">
        <section class="card feature-card">
            <div class="feature-eyebrow">Monitor</div>
            <h3>Workload Board</h3>
            <p>Monitor assigned hours across teaching assistants, spot overload risk early, and review AI rebalancing recommendations.</p>
            <a class="btn" href="<%= ctx %>/admin/workload">Open Workload Board</a>
        </section>
    </div>
    <% } else { %>
    <section class="card">
        <h3>No role features available</h3>
        <p class="muted-block">Your account does not have a recognised role. Please log out and sign in with a valid TA, MO, or Admin account.</p>
        <a class="secondary-link" href="<%= ctx %>/logout">Log Out</a>
    </section>
    <% } %>
</section>

<%@ include file="/WEB-INF/jsp/common/footer.jspf" %>
