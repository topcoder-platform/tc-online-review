<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="html" uri="/tags/struts-html" %>
<%@ taglib prefix="bean" uri="/tags/struts-bean" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html xhtml="true">

<head>
    <c:if test="${empty project}">
        <title><bean:message key="global.title.level2"
            arg0='${orfn:getMessage(pageContext, "OnlineReviewApp.title")}'
            arg1='${orfn:getMessage(pageContext, "editProjectLinks.title")}' /></title>
    </c:if>
    <c:if test="${not empty project}">
        <jsp:include page="/includes/project/project_title.jsp" />
    </c:if>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

    <!-- TopCoder CSS -->
    <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/style.css' />" />
    <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/coders.css' />" />
    <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/tcStyles.css' />" />

    <!-- CSS and JS by Petar -->
    <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/or/new_styles.css' />" />
    <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/or/phasetabs.css' />" />
    <script language="JavaScript" type="text/javascript" src="<html:rewrite href='/js/or/jquery.js' />"><!-- @ --></script>
    <script type="text/javascript">

    var ERR_LAST_ROW_CANT_DELETE = '<bean:message key="editProjectLinks.error.LastRow"/>';
    var ERR_PRJ_NOT_SELECTED = '<bean:message key="editProjectLinks.error.ProjectNotSelected"/>';
    var ERR_DUPLICATED_PRJ_SELECTED = '<bean:message key="editProjectLinks.error.DuplicatedProjectsSelected"/>';
    var ERR_LINK_TYPE_NOT_SELECTED = '<bean:message key="editProjectLinks.error.LinkTypeNotSelected"/>';

	function deleteExistLink(obj){
		$(obj).parents("tr").remove();
		refreshTable();
	}
	
	function deleteNewLink(obj) {
		if ($("#newLinks tr.dataline").length == 1) {
			alert(ERR_LAST_ROW_CANT_DELETE);
			return;
		}
		$(obj).parents("tr").remove();
		refreshTable();
	}
	
	
	function addLink(obj) {
		var newLinksRows = $("#newLinks tr.dataline");
		for (var i=0; i<newLinksRows.length; i++){
			var valid = validateRow($(newLinksRows[i]));
			if (!valid){
				return;
			}
		}
		
		var len=$("#newLinks tr").length;
		var newLine = $("#newLinks tr:nth-child(3)").clone();
		$($(newLine).children()[0]).children()[0].value = "";
		$("#newLinks tr:last").before(newLine);
		refreshTable();
	}

	function validateRow(row, allowEmpty){
		var prjIdCtrl = $($(row).children()[1]).children()[0];
		var linkTypeCtrl = $($(row).children()[2]).children()[0];

		if (allowEmpty && prjIdCtrl.value == -1 && linkTypeCtrl.value == -1){
			return true;
		}
		
		if (prjIdCtrl.value == -1){
			alert(ERR_PRJ_NOT_SELECTED);
			prjIdCtrl.focus();
			return false;
		}

		var lines = $("tr.dataline");
		for (var i=0; i<lines.length; i++){
			var otherPrjIdCtrl = $($(lines[i]).children()[1]).children()[0];
			if ((otherPrjIdCtrl != prjIdCtrl) && (otherPrjIdCtrl.value == prjIdCtrl.value)){
				alert(ERR_DUPLICATED_PRJ_SELECTED);
				prjIdCtrl.focus();
				return false;
			}
		}
		
		if (linkTypeCtrl.value == -1){
			alert(ERR_LINK_TYPE_NOT_SELECTED);
			linkTypeCtrl.focus();
			return false;
		}

		return true;
	}
	
	function refreshTable() {
		 var oldLinksSize = $("#tabExistLinks tr.dataline").length;
		 $("#tabExistLinks tr.dataline").each(function(i) {
			$(this).removeClass('dark');
			$(this).removeClass('light');
			$(this).addClass(i%2==0? 'dark':'light');

			$($(this).children()[1]).children()[0].name= "prjIds["+i+"]";
			$($(this).children()[2]).children()[0].name= "lnkTypes["+i+"]";
		})
		
		$("#newLinks tr.dataline").each(function(i) {
			$(this).removeClass('dark');
			$(this).removeClass('light');
			$(this).addClass(i%2==0? 'dark':'light');

			var idx = i + oldLinksSize;
			
			$($(this).children()[1]).children()[0].name = "prjIds["+idx+"]";
			$($(this).children()[2]).children()[0].name = "lnkTypes["+idx+"]";
		})
	}
	
	function changeProjectText(obj) {
		$(obj).parent().prev().children()[0].value = obj.value == -1? "":obj.value;		
	}

	function changeProjectSel(obj){
		var sel = $(obj).parent().next().children()[0];
		var found = false;
		for (var i = 1; i < sel.options.length; i++) {
			if (sel.options[i].value == obj.value){
				found = true;
				sel.options[i].selected = true;
			}
	    }

	    if (!found){
	    	sel.options[0].selected = true;
	    }       
	}
	
	function validateForm() {
		var newLinksRows = $("tr.dataline");
		for (var i=0; i<newLinksRows.length; i++){
			if (!validateRow($(newLinksRows[i]), true)){
				return false;
			}
		}

		return true;
	}
	
	</script>

    
</head>

<body>
<div align="center">
    <DIV class="maxWidthBody" align="left">
		<jsp:include page="/includes/inc_header.jsp" />
        <jsp:include page="/includes/project/project_tabs.jsp" />

	<div id="mainMiddleContent">
		<div class="clearfix"></div>
	
		<DIV id="tabcontentcontainer">
			<div id="contentTitle">
				<h3>${project.allProperties["Project Name"]} <bean:message key="global.version" /> ${project.allProperties["Project Version"]} - ${orfn:getMessage(pageContext, "editProjectLinks.title")}</h3>
			</div>
				
			<html:form action="/actions/EditProjectLink.do?method=saveProjectLinks" onsubmit="return validateForm()">
		    <html:hidden property="pid" value="${project.id}"/>
		    <div id="tabExistLinks">
				<TABLE class="tabLinks" id="existLinks" cellpadding="0">
					<TBODY>
					<TR>
						<TD class="title" colspan="4"><bean:message key="editProjectLinks.caption.EditProjectLinks" /></TD>
					</TR>
					<TR>
						<TD class="header" width="15%"><bean:message key="editProjectLinks.caption.LinkedProjectId" /></TD>
						<TD class="header" width="35%"><bean:message key="editProjectLinks.caption.LinkedProjectName" /></TD>
						<TD class="header" width="30%"><bean:message key="editProjectLinks.caption.LinkType" /></TD>
						<TD class="header" width="20%"><bean:message key="editProjectLinks.caption.Operation" /></TD>
					</TR>
					
					<c:forEach items="${linkInfos}" var="linkInfo" varStatus="idxLinkInfo">
						<TR class='${(idxLinkInfo.index % 2 == 0) ? "light" : "dark"} dataline'>
							<td class="value" nowrap="nowrap">${linkInfo.id}</td>
							<td class="value" nowrap="nowrap">${linkInfo.name}<html:hidden property="prjIds[${idxLinkInfo.index}]" value="${linkInfo.id}"/></td>
							<TD class="value" nowrap="nowrap">
							<html:select property="lnkTypes[${idxLinkInfo.index}]" value="${linkInfo.linkType}" styleClass="inputBox">
								<OPTION value="-1"><bean:message key="editProjectLinks.option.SelectLinkType" /></OPTION>
								<html:options  collection="linkTypes" property="id" labelProperty="type"/>
							</html:select>
							</TD>
							<TD class="value" nowrap="nowrap" align="right">
							<html:img srcKey="btnDelete.img" style="cursor:pointer;" altKey="btnDelete.alt" onclick="deleteExistLink(this)"/>
							</TD>
						</TR>
					</c:forEach>
					<TR>
						<TD class="lastRowTD" colspan="4"><!-- @ --></TD>
					</TR>

					</TBODY>
				</TABLE>
			</div>
			<div id="tabNewLinks">
				<TABLE class="tabLinks" id="newLinks" cellpadding="0">
					<TBODY>
					<TR>
						<TD class="title" colspan="4"><bean:message key="editProjectLinks.caption.AddProjectLinks" /></TD>
					</TR>
					<TR>
						<TD class="header"  width="15%"><bean:message key="editProjectLinks.caption.ProjectId" /></TD>
						<TD class="header"  width="35%"><bean:message key="editProjectLinks.caption.SelectTgtProject" /></TD>
						<TD class="header"  width="30%"><bean:message key="editProjectLinks.caption.LinkType" /></TD>
						<TD class="header"  width="20%"><bean:message key="editProjectLinks.caption.Operation" /></TD>
					</TR>
					<TR class="light dataline">
						<td class="value" nowrap="nowrap"><input type="text" class="input" onkeyup="changeProjectSel(this)"/></td>						
						<td class="value" nowrap="nowrap">
							<html:select property="prjIds[${fn:length(linkInfos)}]"  value="-1" onchange="changeProjectText(this)" styleClass="inputBox">
								<option value="-1"><bean:message key="editProjectLinks.option.SelectTgtProject" /></option>
								<html:options  collection="selectableProjects" property="id" labelProperty='allProperties.Project Name'/>
							</html:select>
						</td>
						<td class="value" nowrap="nowrap">
							<html:select property="lnkTypes[${fn:length(linkInfos)}]" styleClass="inputBox" value="-1">
								<OPTION value="-1" selected="selected"><bean:message key="editProjectLinks.option.SelectLinkType" /></OPTION>
								<html:options  collection="linkTypes" property="id" labelProperty="type"/>	
							</html:select>
						</td>
						<TD class="value" nowrap="nowrap" align="right">
						<html:img srcKey="btnAdd.img" style="cursor:pointer;" altKey="btnAdd.alt" onclick="addLink(this)"/>
						&nbsp;&nbsp;
						<html:img srcKey="btnDelete.img" style="cursor:pointer;" altKey="btnDelete.alt" onclick="deleteNewLink(this)"/>
						</TD>
					</TR>
					
					
					
					<TR>
						<TD class="lastRowTD" colspan="4"><!-- @ --></TD>
					</TR>
					</TBODY>
				</TABLE>
				<BR>
			</div>
			
		 	<DIV class="bottomButtonBar">
		 		<html:image srcKey="btnSave.img" border="0" altKey="btnSave.img"/>
				&nbsp;
				<html:link page="/actions/ViewProjectDetails.do?method=viewProjectDetails&pid=${project.id}">
				 <html:img srcKey="btnCancel.img" style="cursor:pointer;" border="0" altKey="btnCancel.img"/>
				</html:link>
		          </DIV>
		    </div>
    		</html:form>
		</DIV>
	</DIV>
</div>
</div>
</body>
</html:html>
