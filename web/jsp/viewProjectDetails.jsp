<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="html" uri="/tags/struts-html" %>
<%@ taglib prefix="bean" uri="/tags/struts-bean" %>
<%@ taglib prefix="tc-webtag" uri="/tags/tc-webtags" %>
<%@ page import="com.cronos.onlinereview.actions.AuthorizationHelper" %>
<%@ page import="com.cronos.onlinereview.actions.Constants" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html xhtml="true">

<head>
	<title><bean:message key="OnlineReviewApp.title" /></title>

	<!-- TopCoder CSS -->
	<link type="text/css" rel="stylesheet" href="<html:rewrite page='/css/style.css' />" />
	<link type="text/css" rel="stylesheet" href="<html:rewrite page='/css/coders.css' />" />
	<link type="text/css" rel="stylesheet" href="<html:rewrite page='/css/stats.css' />" />
	<link type="text/css" rel="stylesheet" href="<html:rewrite page='/css/tcStyles.css' />" />

	<!-- CSS and JS by Petar -->
	<link type="text/css" rel="stylesheet" href="<html:rewrite page='/css/new_styles.css' />" />
	<link type="text/css" rel="stylesheet" href="<html:rewrite page='/css/phasetabs.css' />" />
	<script language="JavaScript" type="text/javascript"
		src="<html:rewrite page='/scripts/rollovers.js' />"><!-- @ --></script>
	<script language="JavaScript" type="text/javascript"
		src="<html:rewrite page='/scripts/dojo.js' />"><!-- @ --></script>
	<script language="JavaScript" type="text/javascript">
		var ajaxSupportUrl = "<html:rewrite page='/ajaxSupport' />";
	</script>
	<script language="JavaScript" type="text/javascript"
		src="<html:rewrite page='/scripts/ajax.js' />"><!-- @ --></script>		
	<script language="JavaScript" type="text/javascript">
		// send the Ajax request
		function setTimelineNotification(pid, chbox) {
			var targetStatus;
			if (chbox.checked == true) {
				// DO NOT be confused here
				// at the very moment the checkbox is clicked, the Inactive scorecard's "checked" status is "on"
				targetStatus = "On";
			} else {
				// at the very moment the checkbox is clicked, the Active scorecard's "checked" status is "off"
				targetStatus = "Off";
			}
			// assemble the request XML
			var content =
				'<?xml version="1.0" ?>' +
				'<request type="SetTimelineNotification">' +
				"<parameters>" +
				'<parameter name="ProjectId">' +
				pid +
				"</parameter>" +
				'<parameter name="Status">' +
				targetStatus +
				"</parameter>" +
				"</parameters>" +
				"</request>";
		
			// Send the AJAX request
			sendRequest(content, 
				function (result, respXML) { 
					// operation succeeded, change the status of corresponding checkbox
					if (chbox.checked) {
						chbox.checked = false;
					} else if (!chbox.checked){
						chbox.checked = true;
					}
					// TODO: Check what is supposed  to be done by the next line of code
					// refresh the filter
					refreshFilter();
				},
				function (result, respXML) { 
					// operation failed, alert the error message to the user
					alert("An error occured while setting the Timeline change notification: " + result);
				}
			);	
		}
	</script>

	<!-- TABS JS -->
<script type="text/javascript">

	// Set tab to intially be selected when page loads:
	// [which tab (1=first tab), ID of tab content to display]:
	var initialtab = [1, "sc1"];

	function cascadedstyle(el, cssproperty, csspropertyNS){
		if (el.currentStyle)
			return el.currentStyle[cssproperty];
		else if (window.getComputedStyle){
			var elstyle = window.getComputedStyle(el, "");
			return elstyle.getPropertyValue(csspropertyNS);
		}
	}

	var previoustab="";

	function expandcontent(cid, aobject){
		if (document.getElementById){
			highlighttab(aobject);
			detectSourceindex(aobject);
			if (previoustab != "")
				document.getElementById(previoustab).style.display = "none";
			document.getElementById(cid).style.display = "block";
			previoustab = cid;
			if (aobject.blur)
				aobject.blur();
			return false;
		}
		else
			return true;
	}

	function highlighttab(aobject) {
		if (typeof tabobjlinks == "undefined")
			collecttablinks();
		for (i = 0; i < tabobjlinks.length; i++)
			tabobjlinks[i].style.backgroundColor = initTabcolor;
		var themecolor = aobject.getAttribute("theme")? aobject.getAttribute("theme") : initTabpostcolor;
		aobject.style.backgroundColor = 
			document.getElementById("tabcontentcontainer").style.backgroundColor = themecolor;
	}

	function collecttablinks() {
		var tabobj = document.getElementById("tablist");
		tabobjlinks = tabobj.getElementsByTagName("A");
	}

	function detectSourceindex(aobject) {
		for (i=0; i < tabobjlinks.length; i++) {
			if (aobject == tabobjlinks[i]) {
				tabsourceindex=i; //source index of tab bar relative to other tabs
				break;
			}
		}
	}

	function do_onload() {
		var cookiename = (typeof persisttype != "undefined" && persisttype == "sitewide") ? "tabcontent" : window.location.pathname;
		var cookiecheck = window.get_cookie && get_cookie(cookiename).indexOf("|") != -1;
		collecttablinks();
		initTabcolor = cascadedstyle(tabobjlinks[1], "backgroundColor", "background-color");
		initTabpostcolor = cascadedstyle(tabobjlinks[0], "backgroundColor", "background-color");
		if (typeof enablepersistence != "undefined" && enablepersistence && cookiecheck) {
			var cookieparse = get_cookie(cookiename).split("|");
			var whichtab = cookieparse[0];
			var tabcontentid = cookieparse[1];
			expandcontent(tabcontentid, tabobjlinks[whichtab]);
		}
		else
			expandcontent(initialtab[1], tabobjlinks[initialtab[0]-1]);
	}

	if (window.addEventListener)
		window.addEventListener("load", do_onload, false);
	else if (window.attachEvent)
		window.attachEvent("onload", do_onload);
	else if (document.getElementById)
		window.onload = do_onload;
</script>
<!-- END TABS JS -->

</head>

<body>
	<jsp:include page="/includes/inc_header.jsp" />
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr valign="top">
			<!-- Left Column Begins-->
			<td width="180">
				<jsp:include page="/includes/inc_leftnav.jsp" />
			</td>
			<!-- Left Column Ends -->

			<!-- Gutter Begins -->
			<td width="15"><html:img page="/i/clear.gif" width="15" height="1" border="0" /></td>
			<!-- Gutter Ends -->

			<!-- Center Column Begins -->
			<td class="bodyText">
				<jsp:include page="/includes/project/project_tabs.jsp" />

				<div id="mainMiddleContent">
					<jsp:include page="/includes/project/project_info.jsp" /><br />
					<jsp:include page="/includes/project/project_myrole.jsp" />
					<jsp:include page="/includes/project/project_timeline.jsp" />
					<jsp:include page="/includes/project/project_phase.jsp" />
					<jsp:include page="/includes/project/project_detail.jsp" />
					<jsp:include page="/includes/project/project_resource.jsp" />

					<div align="right">
						<a href="javascript:history.go(-1)"><html:img srcKey="btnBack.img" altKey="btnBack.alt" border="0" /></a>&#160;
						<%
							if (AuthorizationHelper.hasUserPermission(request, Constants.EDIT_PROJECT_DETAILS_PERM_NAME)) {
						%>
								<a href="EditProject.do?method=editProject&pid=${project.id}"><html:img
									srcKey="viewProjectDetails.btnEdit.img" border="0"
									altKey="viewProjectDetails.btnEdit.alt" /></a>
						<%
							}
						%><br />
					</div><br />
				</div>
				<br /><br />
			</td>
			<!-- Center Column Ends -->

			<!-- Gutter -->
			<td width="15"><html:img page="/i/clear.gif" width="25" height="1" border="0" /></td>
			<!-- Gutter Ends -->

			<!-- Gutter -->
			<td width="2"><html:img page="/i/clear.gif" width="2" height="1" border="0" /></td>
			<!-- Gutter Ends -->
		</tr>
	</table>

	<jsp:include page="/includes/inc_footer.jsp" />
</body>
</html:html>