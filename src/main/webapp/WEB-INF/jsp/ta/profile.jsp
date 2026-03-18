<%@ page import="cn.bupt.tarecruitment.model.ApplicantProfile,java.util.List,cn.bupt.tarecruitment.util.WebUtils" %>
<%@ include file="/WEB-INF/jsp/common/header.jspf" %>
<%
    ApplicantProfile profile = (ApplicantProfile) request.getAttribute("profile");
    List<String> errors = (List<String>) request.getAttribute("errors");
    String skillsValue = profile != null ? WebUtils.joinCsv(profile.getSkills()) : "";
%>
<section class="card">
    <h1>Applicant Profile</h1>
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
<%@ include file="/WEB-INF/jsp/common/footer.jspf" %>
