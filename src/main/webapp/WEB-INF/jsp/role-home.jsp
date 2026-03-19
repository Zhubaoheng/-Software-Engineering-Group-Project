<%
    request.setAttribute("pageTitle", "Workspace Overview");
    request.setAttribute("pageMeta", "Use the sidebar to move through the current role's available demo pages.");
%>
<%@ include file="/WEB-INF/jsp/common/header.jspf" %>
<section class="card">
    <h2>Role Home</h2>
    <p>This role is scaffolded for the assessment demo. The TA module is implemented first.</p>
    <p>Use the navigation in the sidebar to move through the available pages.</p>
</section>
<%@ include file="/WEB-INF/jsp/common/footer.jspf" %>
