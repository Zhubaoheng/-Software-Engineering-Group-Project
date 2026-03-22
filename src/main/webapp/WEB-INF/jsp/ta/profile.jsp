<%@ page import="cn.bupt.tarecruitment.model.ApplicantProfile,java.util.List,cn.bupt.tarecruitment.util.WebUtils" %>
<%
    request.setAttribute("pageTitle", "Applicant Profile");
    request.setAttribute("pageMeta", "Fill in a concise, resume-style profile with your education, TA focus, strengths, and latest CV before applying.");
    ApplicantProfile profile = (ApplicantProfile) request.getAttribute("profile");
    List<String> errors = (List<String>) request.getAttribute("errors");
    String skillsValue = profile != null ? WebUtils.joinCsv(profile.getSkills()) : "";
    int completionCount = 0;
    if (profile != null) {
        if (profile.getName() != null && !profile.getName().isBlank()) completionCount++;
        if (profile.getStudentId() != null && !profile.getStudentId().isBlank()) completionCount++;
        if (profile.getEmail() != null && !profile.getEmail().isBlank()) completionCount++;
        if (profile.getPhone() != null && !profile.getPhone().isBlank()) completionCount++;
        if (profile.getMajor() != null && !profile.getMajor().isBlank()) completionCount++;
        if (profile.getGrade() != null && !profile.getGrade().isBlank()) completionCount++;
        if (profile.getPreferredRole() != null && !profile.getPreferredRole().isBlank()) completionCount++;
        if (skillsValue != null && !skillsValue.isBlank()) completionCount++;
        if (profile.getAvailability() != null && !profile.getAvailability().isBlank()) completionCount++;
        if (profile.getSelfIntroduction() != null && !profile.getSelfIntroduction().isBlank()) completionCount++;
        if (profile.getProjectExperience() != null && !profile.getProjectExperience().isBlank()) completionCount++;
        if (profile.getCvFileName() != null && !profile.getCvFileName().isBlank()) completionCount++;
    }
    boolean readyToApply = completionCount >= 9;
%>
<%@ include file="/WEB-INF/jsp/common/header.jspf" %>
<section class="section-stack">
    <div class="profile-layout">
        <section class="card profile-hero">
            <div class="profile-hero-copy">
                <div class="eyebrow">Resume Builder</div>
                <h2>Build a focused TA profile</h2>
                <p class="muted-block">Use the same structure recruiters expect: core contact details, education, target direction, strengths, and one or two strong project highlights.</p>
            </div>
            <div class="profile-progress-card">
                <div class="profile-progress-label">Profile completeness</div>
                <div class="profile-progress-value"><%= completionCount %>/12</div>
                <span class="<%= readyToApply ? "badge badge-ok" : "badge badge-warn" %>"><%= readyToApply ? "Ready to apply" : "Needs more detail" %></span>
            </div>
        </section>

        <aside class="card profile-side-panel">
            <h3>Quick Snapshot</h3>
            <div class="profile-summary-list">
                <div class="meta-card">
                    <strong>Applicant ID</strong>
                    <span><%= profile == null ? "-" : WebUtils.escapeHtml(profile.getApplicantId()) %></span>
                </div>
                <div class="meta-card">
                    <strong>Target</strong>
                    <span><%= profile == null || profile.getPreferredRole() == null || profile.getPreferredRole().isBlank() ? "Add preferred TA direction" : WebUtils.escapeHtml(profile.getPreferredRole()) %></span>
                </div>
                <div class="meta-card">
                    <strong>Current CV</strong>
                    <span><%= profile != null && profile.getCvFileName() != null ? WebUtils.escapeHtml(profile.getCvFileName()) : "No file uploaded" %></span>
                </div>
            </div>
            <% if (profile != null && profile.getCvFileName() != null && !profile.getCvFileName().isBlank()) { %>
            <div class="profile-inline-actions">
                <a class="secondary-link" href="<%= request.getContextPath() %>/cv/preview" target="_blank" rel="noopener">Preview current CV</a>
            </div>
            <% } %>
            <div class="profile-tip-box">
                <div class="sidebar-label">Suggested Content</div>
                <ul class="bullet-list compact-list">
                    <li>Keep your self introduction to 3 to 5 lines.</li>
                    <li>List only the skills relevant to tutoring, labs, or marking.</li>
                    <li>Highlight one strong course project instead of many weak ones.</li>
                </ul>
            </div>
        </aside>
    </div>

    <% if (errors != null && !errors.isEmpty()) { %>
    <div class="error">
        <% for (String error : errors) { %>
        <div><%= WebUtils.escapeHtml(error) %></div>
        <% } %>
    </div>
    <% } %>

    <form class="profile-form" method="post" action="<%= request.getContextPath() %>/ta/profile" enctype="multipart/form-data">
        <section class="card profile-section">
            <div class="profile-section-head">
                <div>
                    <div class="eyebrow">Section 01</div>
                    <h2>Basic Information</h2>
                </div>
                <p class="muted-block">These are the fields recruiters usually scan first when opening a resume.</p>
            </div>
            <div class="form-grid">
                <label>Name
                    <input type="text" name="name" value="<%= profile == null ? "" : WebUtils.escapeHtml(profile.getName()) %>" placeholder="e.g. Alex Chen" required>
                </label>
                <label>Student ID
                    <input type="text" name="studentId" value="<%= profile == null ? "" : WebUtils.escapeHtml(profile.getStudentId()) %>" placeholder="University student number" required>
                </label>
                <label>Email
                    <input type="email" name="email" value="<%= profile == null ? "" : WebUtils.escapeHtml(profile.getEmail()) %>" placeholder="name@bupt.edu.cn" required>
                </label>
                <label>Phone
                    <input type="text" name="phone" value="<%= profile == null ? "" : WebUtils.escapeHtml(profile.getPhone()) %>" placeholder="Optional but recommended">
                </label>
            </div>
        </section>

        <section class="card profile-section">
            <div class="profile-section-head">
                <div>
                    <div class="eyebrow">Section 02</div>
                    <h2>Education And TA Preference</h2>
                </div>
                <p class="muted-block">Enough information for module owners to quickly judge course fit, without making the form too long.</p>
            </div>
            <div class="form-grid">
                <label>Major
                    <input type="text" name="major" value="<%= profile == null ? "" : WebUtils.escapeHtml(profile.getMajor()) %>" placeholder="e.g. Software Engineering">
                </label>
                <label>Grade / Year
                    <input type="text" name="grade" value="<%= profile == null ? "" : WebUtils.escapeHtml(profile.getGrade()) %>" placeholder="e.g. Year 3 / MSc">
                </label>
                <label class="span-2">Preferred TA Direction
                    <input type="text" name="preferredRole" value="<%= profile == null ? "" : WebUtils.escapeHtml(profile.getPreferredRole()) %>" placeholder="e.g. Java lab TA, marking support, tutorial support">
                </label>
                <label class="span-2">Availability
                    <textarea name="availability" rows="3" placeholder="When can you support classes, office hours, or marking?" required><%= profile == null ? "" : WebUtils.escapeHtml(profile.getAvailability()) %></textarea>
                </label>
            </div>
        </section>

        <section class="card profile-section">
            <div class="profile-section-head">
                <div>
                    <div class="eyebrow">Section 03</div>
                    <h2>Strengths And Highlights</h2>
                </div>
                <p class="muted-block">Mirror the most common sections in a campus recruitment resume: skills, short summary, and representative project experience.</p>
            </div>
            <div class="form-grid">
                <label class="span-2">Skills
                    <textarea name="skills" rows="3" placeholder="Comma-separated, e.g. Java, Git, Unit Testing, Communication"><%= WebUtils.escapeHtml(skillsValue) %></textarea>
                </label>
                <label class="span-2">Self Introduction
                    <textarea name="selfIntroduction" rows="4" placeholder="Summarise your strengths, tutoring experience, and why you are suitable for a TA role."><%= profile == null ? "" : WebUtils.escapeHtml(profile.getSelfIntroduction()) %></textarea>
                </label>
                <label class="span-2">Project / Course Highlights
                    <textarea name="projectExperience" rows="5" placeholder="Describe 1 to 2 relevant projects or courses, your role, and the outcome."><%= profile == null ? "" : WebUtils.escapeHtml(profile.getProjectExperience()) %></textarea>
                </label>
            </div>
        </section>

        <section class="card profile-section">
            <div class="profile-section-head">
                <div>
                    <div class="eyebrow">Section 04</div>
                    <h2>CV Attachment</h2>
                </div>
                <p class="muted-block">Upload the latest version so module owners review the same content you see here.</p>
            </div>
            <div class="upload-row">
                <label class="upload-field">Upload CV
                    <input type="file" name="cvFile" accept=".pdf,.doc,.docx">
                </label>
                <div class="upload-meta">
                    <span class="sidebar-label">Current file</span>
                    <div><%= profile != null && profile.getCvFileName() != null ? WebUtils.escapeHtml(profile.getCvFileName()) : "No CV uploaded yet" %></div>
                    <% if (profile != null && profile.getCvFileName() != null && !profile.getCvFileName().isBlank()) { %>
                    <div class="upload-actions">
                        <a class="secondary-link" href="<%= request.getContextPath() %>/cv/preview" target="_blank" rel="noopener">Open preview</a>
                    </div>
                    <% } %>
                </div>
            </div>
            <div class="profile-form-actions">
                <button type="submit">Save Profile</button>
                <div class="muted">After saving, go to Open Jobs and apply with this version of your profile.</div>
            </div>
        </section>
    </form>
</section>
<%@ include file="/WEB-INF/jsp/common/footer.jspf" %>
