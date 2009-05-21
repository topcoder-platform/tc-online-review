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
            arg1='${orfn:getMessage(pageContext, "editProject.title.CreateNew")}' /></title>
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
    <script language="JavaScript" type="text/javascript" src="<html:rewrite href='/js/or/rollovers2.js' />"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript" src="<html:rewrite href='/js/or/dojo.js' />"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript" src="<html:rewrite href='/js/or/util.js' />"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript" src="<html:rewrite href='/js/or/validation_util2.js' />"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript" src="<html:rewrite href='/js/or/validation_edit_project_links.js' />"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript" src="<html:rewrite href='/js/or/parseDate.js' />"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript">
    	var lastLinkIndex = ${fn:length(projectForm.map['new_link_dest_id']) - 1};
    	
    	function newProjectLink() {
    		//Get add link table node
    		var newLinksTable = document.getElementById("newLinks");
        // Get the number of rows in table
        var rowCount = newLinksTable.rows.length;
        // Create a new row into resources table
        var newRow = cloneInputRow(newLinksTable.rows[2]);
    		
        // Rows should vary colors
        var rows = newLinksTable.rows;
        var strLastRowStyle = "dark"; // This variable will remember the style of the last row
        // Find first non-hidden row, starting from the bottom of the table
        for (var i = rows.length - 2; i >= 0; --i) {
            if (rows[i].style["display"] == "none") continue;
            strLastRowStyle = rows[i].className;
            break;
        }
        newRow.className = (strLastRowStyle == "dark") ? "light" : "dark";
    		
    		
        // Increase resource index
        lastLinkIndex++;
        // Rename all the inputs to have a new index
        patchAllChildParamIndexes(newRow, lastLinkIndex);
        // Insert new row into links table
        newLinksTable.tBodies[0].insertBefore(newRow, newLinksTable.rows[rowCount - 1]);    		
    	}
    	
    </script>

<body>
<div align="center">
    <div class="maxWidthBody" align="left">

        <jsp:include page="/includes/inc_header.jsp" />
        <jsp:include page="/includes/project/project_tabs.jsp" />

        <div id="mainMiddleContent">
           <div class="clearfix"/>
           <div id="tabcontentcontainer"> 	
           	 <html:form action="/actions/SaveProjectLinks" onsubmit="return validate_form(this, true);">
           	  <html:hidden property="method" value="saveProjectLinks" />
           	  <html:hidden property="pid" />
           	  		
              <c:if test="${orfn:isErrorsPresent(pageContext.request)}">
                  <table cellpadding="0" cellspacing="0" border="0">
                      <tr><td width="16"><!-- @ --></td><td><!-- @ --></td></tr>
                      <tr>
                          <td colspan="2"><span style="color:red;"><bean:message key="Error.saveReview.ValidationFailed" /></span></td>
                      </tr>
                      <html:errors property="org.apache.struts.action.GLOBAL_MESSAGE" />
                  </table><br />
              </c:if>
           	  		
              <div id="contentTitle">
              		<h3>Project Name version 1.0 - Manage Project Links</h3> 
              </div>           	
      
			        <div id="tabExistLinks">
			        	<table cellpadding="0" id="existLinks" class="tabLinks">
			        		<tbody>
			        		<tr>
			        			<td colspan="4" class="title">Edit Project Links</td>
			        		</tr>
			        		<tr>
			        			<td class="header">Linked Project Name</td>
			        			<td class="header">Link Type</td>
			        			<td class="header">Operation</td>
			        		</tr>
			        		<tr class="light">
			        			<td nowrap="nowrap" class="value">Test Component name 2</td>
			        			<td nowrap="nowrap" class="value">
			        				<select class="inputBox" name="linkType">
			        						<option value="-1">Select Link Type</option>
			        						<option value="1">Conceptualization Spec Review</option>
			        						<option selected="selected" value="2">Conceptualization Round 2</option>
			        						<option value="3">Module Architecture</option>
			        						<option value="4">Spawned Component</option>
			        				</select>
			        			</td>
			        			<td nowrap="nowrap" class="value">
			        				  <html:img srcKey="editProjectLinks.btnDelete.img" border="0" altKey="editProjectLinks.btnDelete.alt" />
			        			</td>
			        		</tr>
			        		<tr>
			        			<td colspan="5" class="lastRowTD"><!-- @ --></td>
			        		</tr>
			        		</tbody>
			        	</table>   
			        </div>
              
			        <div id="tabNewLinks">
			        	<table cellpadding="0" id="newLinks" class="tabLinks">
			        		<tbody>
			        		<tr class="dark">
			        			<td colspan="6" class="title">Add Project Links</td>
			        		</tr>
			        		<tr class="light">
			        			<td class="header">Project ID</td>
			        			<td class="header">Select A Project </td>
			        			<td class="header">Link Type</td>
			        			<td class="header">Operation</td>
			        		</tr>
			        		<c:forEach var="linkIdx" varStatus="linkStatus" begin="0" end="${fn:length(projectLinkForm.map['new_link_dest_id']) - 1}">
			        		<tr class="dark">
			        			<td nowrap="nowrap" class="value">
			        				<html:text property="new_link_dest_id_text[${linkIdx}]" />
				              <div name="project_link_validation_msg" class="error" style="display:none"></div>			        					
			        			</td>				
			        			<td nowrap="nowrap" class="value">
				               <html:select styleClass="inputBox" property="new_link_dest_id[${linkIdx}]" style="width:120px;">
				               	  <html:option key="editProjectLinks.projectTypes.SelectProject" value="-1" />
                          <c:forEach items="${activeProjects}" var="activeProject">				               	                          	   
                          	   <html:option value="${activeProject.id}">${activeProject.name}</html:option>
                          </c:forEach>
				               </html:select>
				               <div name="project_link_validation_msg" class="error" style="display:none"></div>			        					
			        			</td>
			        			<td nowrap="nowrap" class="value">
				               <html:select styleClass="inputBox" property="new_link_type_id[${linkIdx}]" style="width:120px;">
				               	  <html:option key="editProjectLinks.projectTypes.SelectType" value="-1" />
                          <c:forEach items="${projectLinkTypes}" var="projectLinkType">				               	                          	   
                          	   <html:option value="${projectLinkType.id}">${projectLinkType.name}</html:option>
                          </c:forEach>
				               </html:select>
                       <div name="project_link_validation_msg" class="error" style="display:none"></div>			        									               
			        			</td>
			        			<td nowrap="nowrap" class="value">
			        				<c:if test="${linkIdx eq 0}">
			        				  <html:img srcKey="editProjectLinks.btnAdd.img" border="0" 
			        				  	        onclick="javascript:newProjectLink();"
			        				  	     altKey="editProjectLinks.btnAdd.alt"  style="cursor:hand;" />&#160;
			        				</c:if> 	
			        				  <html:img srcKey="editProjectLinks.btnDelete.img" 
			        				  	    style="cursor:hand;${(linkIdx eq 0) ? 'display: none;' : ''}" border="0" 
			        				  	    altKey="editProjectLinks.btnDelete.alt" />
			        			</td>
			        		</tr>
			        	  </forEach>
			        		<tr>
			        			<td colspan="4" class="lastRowTD"><!-- @ --></td>
			        		</tr>
			        		</tbody>
			        	</table>
			        	<br/>
			        </div>

             <div class="bottomButtonBar">
                  <html:image srcKey="btnSaveChanges.img" altKey="btnSaveChanges.alt" border="0"/>&#160;
                  <html:link page="/actions/ViewProjectDetails.do?method=viewProjectDetails&pid=${project.id}"><html:img srcKey="btnCancel.img" altKey="btnCancel.alt" border="0"/></html:link>                            
             </div>
			     </html:form>    
           </div> <!-- //tabconentcontainer -->
        </div><!-- //mainMiddleContent -->
            
        <jsp:include page="/includes/inc_footer.jsp" />  
        	  
     </div><!-- //maxWidthBody -->
</div>      
</body>     
            
</html:html>	      
<            
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             