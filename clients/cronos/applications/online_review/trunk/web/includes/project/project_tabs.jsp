<%@ page language="java" isELIgnored="false" %>
<%@ page import="com.cronos.onlinereview.actions.AuthorizationHelper" %>
<%@ page import="com.cronos.onlinereview.actions.Constants" %>
<%
	int activeTab = 0;
	if (request.getAttribute("projectTabIndex") != null) {
	    activeTab = ((Integer)request.getAttribute("projectTabIndex")).intValue();
	}
%>
	<%-- MAIN TABS HERE --%>
	<div id="mainTabs"><%

		// "My projects" tab. Visible for everybody except public viewers
		if (AuthorizationHelper.hasUserPermission(request, Constants.VIEW_MY_PROJECTS_PERM_NAME)) {
			if (activeTab == 1) {
		%><img src="../i/tab_my_open_projects_on.gif" width="119" height="36" border="0" name="tab1"><%
			} else {
				%><a href="ListProjects.do?method=listProjects&scope=my"
				onMouseOver="img_act('tab1')" onMouseOut="img_inact('tab1')"><img
				src="../i/tab_my_open_projects.gif" width="119" height="36" border="0" name="tab1"></a><%
			}
		}

		// "All Projects" tab
		if (AuthorizationHelper.hasUserPermission(request, Constants.VIEW_PROJECTS_PERM_NAME)) {
			if (activeTab == 2) {
	%><img src="../i/tab_all_open_projects_on.gif" width="119" height="36" name="tab2"><%
			} else {
				%><a href="ListProjects.do?method=listProjects&scope=all"
				onMouseOver="img_act('tab2')" onMouseOut="img_inact('tab2')"><img
				src="../i/tab_all_open_projects.gif" width="119" height="36" border="0" name="tab2"></a><%
			}
		}

		// "Inactive Projects" tab. Visible only for managers
		if (AuthorizationHelper.hasUserPermission(request, Constants.VIEW_PROJECTS_INACTIVE_PERM_NAME)) {
			if (activeTab == 4) {
		%><img src="../i/tab_inactive_projects_on.gif" width="119" height="36" border="0" name="tab4"><%
			} else {
				%><a href="ListProjects.do?method=listProjects&scope=inactive"
				onMouseOver="img_act('tab4')" onMouseOut="img_inact('tab4')"><img
				src="../i/tab_inactive_projects.gif" width="119"height="36" border="0" name="tab4"></a><%
			}
		}

		// "Create Project" tab. Visible only for managers
		if (AuthorizationHelper.hasUserPermission(request, Constants.CREATE_PROJECT_PERM_NAME)) {
			if (activeTab == 3) {
		%><img src="../i/tab_create_project_on.gif" width="119" height="36" border="0" name="tab3"><br><%
			} else {
		%><a href="NewProject.do?method=newProject"
				onMouseOver="img_act('tab3')" onMouseOut="img_inact('tab3')"><img
				src="../i/tab_create_project.gif" width="119" height="36" border="0" name="tab3"></a><br><%
			}
		}
	%></div>
