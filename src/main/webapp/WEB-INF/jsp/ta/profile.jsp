<%@ page import="cn.bupt.tarecruitment.model.ApplicantProfile,java.util.List,cn.bupt.tarecruitment.util.WebUtils" %>
<%
    request.setAttribute("pageTitle", "Applicant Profile");
    request.setAttribute("pageMeta", "Maintain your personal details, skills, availability, and latest CV before applying for TA posts.");
    ApplicantProfile profile = (ApplicantProfile) request.getAttribute("profile");
    List<String> errors = (List<String>) request.getAttribute("errors");
    String skillsValue = profile != null ? WebUtils.joinCsv(profile.getSkills()) : "";
%>
<%@ include file="/WEB-INF/jsp/common/header.jspf" %>
<section class="section-stack">
    <div class="hero-grid">
        <div class="info-card">
            <h3>Profile Snapshot</h3>
            <div class="meta-row">
                <div class="meta-card">
                    <strong>Applicant ID</strong>
                    <span><%= profile == null ? "-" : WebUtils.escapeHtml(profile.getApplicantId()) %></span>
                </div>
                <div class="meta-card">
                    <strong>Current CV</strong>
                    <span><%= profile != null && profile.getCvFileName() != null ? WebUtils.escapeHtml(profile.getCvFileName()) : "No file yet" %></span>
                </div>
            </div>
        </div>
        <div class="info-card">
            <h3>Demo Guidance</h3>
            <p class="muted-block">Save here first, then move to <strong>Open Jobs</strong> to browse opportunities and submit applications with your latest information.</p>
        </div>
    </div>

    <section class="card">
        <h2>Update Profile</h2>
        <% if (errors != null && !errors.isEmpty()) { %>
        <div class="error">
            <% for (String error : errors) { %>
            <div><%= WebUtils.escapeHtml(error) %></div>
            <% } %>
        </div>
        <% } %>
        <form class="form" method="post" action="<%= request.getContextPath() %>/ta/profile" enctype="multipart/form-data">
            <label>Name
                <input type="text" name="name" value="<%= profile == null ? "" : WebUtils.escapeHtml(profile.getName()) %>" required>
            </label>
            <label>Student ID
                <input type="text" name="studentId" value="<%= profile == null ? "" : WebUtils.escapeHtml(profile.getStudentId()) %>" required>
            </label>
            <label>Email
                <input type="email" name="email" value="<%= profile == null ? "" : WebUtils.escapeHtml(profile.getEmail()) %>" required>
            </label>
            <label>Skills
                <textarea name="skills" rows="4" placeholder="Comma-separated skills"><%= WebUtils.escapeHtml(skillsValue) %></textarea>
            </label>
            <label>Availability
                <textarea name="availability" rows="3" required><%= profile == null ? "" : WebUtils.escapeHtml(profile.getAvailability()) %></textarea>
            </label>
            <label>Upload CV
                <input type="file" name="cvFile" accept=".pdf,.doc,.docx">
            </label>
            <div class="muted">
                Current CV: <%= profile != null && profile.getCvFileName() != null ? WebUtils.escapeHtml(profile.getCvFileName()) : "No CV uploaded yet" %>
            </div>
            <button type="submit">Save Profile</button>
        </form>
    </section>
</section>
<%@ include file="/WEB-INF/jsp/common/footer.jspf" %>
