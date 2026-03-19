<%@ page import="cn.bupt.tarecruitment.model.ApplicationReviewView" %>
<%@ page import="cn.bupt.tarecruitment.util.WebUtils" %>
<%
    request.setAttribute("pageTitle", "Applicant Detail");
    request.setAttribute("pageMeta", "Review profile information, CV metadata, and current status before making a decision.");
    ApplicationReviewView review = (ApplicationReviewView) request.getAttribute("review");
%>
<%@ include file="/WEB-INF/jsp/common/header.jspf" %>
<section class="split-grid">
    <div class="card">
        <div class="eyebrow">Module</div>
        <h2><%= WebUtils.escapeHtml(review.getJob().getModuleCode()) %> - <%= WebUtils.escapeHtml(review.getJob().getModuleName()) %></h2>
        <div class="stack">
            <div class="meta-card">
                <strong>Applicant</strong>
                <span><%= review.getProfile() == null ? "-" : WebUtils.escapeHtml(review.getProfile().getName()) %></span>
            </div>
            <div class="meta-card">
                <strong>Email</strong>
                <span><%= review.getProfile() == null ? "-" : WebUtils.escapeHtml(review.getProfile().getEmail()) %></span>
            </div>
            <div class="meta-card">
                <strong>Availability</strong>
                <span><%= review.getProfile() == null ? "-" : WebUtils.escapeHtml(review.getProfile().getAvailability()) %></span>
            </div>
            <div class="meta-card">
                <strong>CV</strong>
                <span><%= review.getProfile() == null ? "-" : WebUtils.escapeHtml(review.getProfile().getCvFileName()) %></span>
            </div>
        </div>
    </div>

    <div class="card">
        <% String error = (String) request.getAttribute("error"); if (error != null) { %>
        <div class="error"><%= WebUtils.escapeHtml(error) %></div>
        <% } %>
        <h2>Decision Workspace</h2>
        <p class="muted-block"><strong>Skills:</strong> <%= review.getProfile() == null ? "-" : WebUtils.escapeHtml(String.join(", ", review.getProfile().getSkills())) %></p>
        <p><span class="status"><%= WebUtils.escapeHtml(review.getApplication().getStatus()) %></span></p>
        <form method="post" action="<%= request.getContextPath() %>/mo/applicants/review" class="stack">
            <input type="hidden" name="applicationId" value="<%= review.getApplication().getApplicationId() %>">
            <div class="toolbar">
                <button type="submit" name="decision" value="Shortlisted">Shortlist</button>
                <button type="submit" name="decision" value="Selected">Select</button>
                <button type="submit" name="decision" value="Rejected">Reject</button>
            </div>
        </form>
        <a class="secondary-link" href="<%= request.getContextPath() %>/mo/applicants?jobId=<%= review.getJob().getJobId() %>">Back to applicants</a>
    </div>
</section>
<%@ include file="/WEB-INF/jsp/common/footer.jspf" %>
