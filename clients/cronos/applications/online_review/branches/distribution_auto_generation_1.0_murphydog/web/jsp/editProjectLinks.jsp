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
    <jsp:include page="/includes/project/project_title.jsp" />
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
    <script language="JavaScript" type="text/javascript">
    	/**
    	 * Initializes some parameters.
    	 */
    	function initParameters() {
    		 // Initiates the lastLinkIndex
    	   lastLinkIndex = ${fn:length(projectLinkForm.map['link_dest_id']) - 1};
    	   
    	   // Initiates all possible options
    	   projectOptions = new Array();    	
    	   // Initiates option values
    	   projectOptions.push(new Option('${orfn:getMessage(pageContext, "editProjectLinks.projectTypes.SelectProject")}','-1'));
         <c:forEach items="${allProjects}" var="projectElement">
         <c:set var="str" value="${projectElement.allProperties[\"Project Name\"]} v${projectElement.allProperties[\"Project Version\"]} (${projectElement.projectCategory.name}) (${projectElement.projectStatus.name})"/>
         <c:set var="repl" value="${fn:replace(str, \"'\", \"\")}"/>
            <c:if test="${projectElement.id ne project.id}">
                projectOptions.push(new Option('${repl}','${projectElement.id}'));
            </c:if>
         </c:forEach>

      }    	

      /**
       * Callback function. It is called after page loading.
       */
    	function onLoad() {
    		   initParameters();

    	     // reset drop downs
    	     resetDropDowns();    	
    	}
    </script>

<body onload="onLoad();">
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
                    <h3>${project.allProperties["Project Name"]} version ${project.allProperties["Project Version"]} - Manage Project Links</h3>
              </div>

                    <div id="tabNewLinks">
                        <table cellpadding="0" id="newLinks" class="tabLinks">
                            <tbody>
                            <tr class="dark">
                                <td colspan="6" class="title"><bean:message key="editProjectLinks.box.editLinks" /></td>
                            </tr>
                            <tr>
								<td class="header"><bean:message key="editProjectLinks.editLink.LinkType" /></td>
                                <td class="header"><bean:message key="editProjectLinks.editLink.SelectProject" /></td>
                                
                                <td class="header"><bean:message key="editProjectLinks.editLink.Operation" /></td>
                            </tr>
                            <c:forEach var="linkIdx" varStatus="linkStatus" begin="0" end="${fn:length(projectLinkForm.map['link_dest_id']) - 1}">
                            <tr class='${(linkStatus.index % 2 == 0) ? "light" : "dark"}'>
								<td nowrap="nowrap" class="value">
				               <html:select styleClass="inputBox" property="link_type_id[${linkIdx}]" 
				               	            onchange="onLinkTypeDropDownChange(this);">
				               	  <html:option key="editProjectLinks.projectTypes.SelectType" value="-1" />
                          <c:forEach items="${projectLinkTypes}" var="projectLinkType">				               	                          	   
                          	   <html:option value="${projectLinkType.id}">${projectLinkType.name}</html:option>
                          </c:forEach>
                            </html:select>
							<div name="project_link_validation_msg" class="error" style="display:none"></div>
                                </td>

                                <td nowrap="nowrap" class="value">
                            <html:select styleClass="inputBox" property="link_dest_id[${linkIdx}]">
                                    <html:option key="editProjectLinks.projectTypes.SelectProject" value="-1" />
                          <c:forEach items="${allProjects}" var="projectElement">
                                <c:if test="${projectElement.id ne project.id}">
                                <html:option value="${projectElement.id}">${projectElement.allProperties["Project Name"]} v${projectElement.allProperties["Project Version"]} (${projectElement.projectCategory.name})</html:option>
                                </c:if>
                          </c:forEach>
				               </html:select>
				               <div name="project_link_validation_msg" class="error" style="display:none"></div>			        					
			        			</td>
			        			
                                <td nowrap="nowrap" class="value" align="center">
                                    <c:if test="${linkIdx eq 0}">
                                    <html:img srcKey="editProjectLinks.btnAdd.img" border="0"
                                                onclick="javascript:newProjectLink();"
                                            altKey="editProjectLinks.btnAdd.alt"  style="cursor:hand;" />
                                    </c:if>
                                    <html:img srcKey="editProjectLinks.btnDelete.img"
                                            style="cursor:hand;${(linkIdx eq 0) ? 'display: none;' : ''}" border="0"
                                            onclick="deleteProjectLink(this.parentNode.parentNode);"
                                            altKey="editProjectLinks.btnDelete.alt" />
                                    <html:hidden property="link_action[${linkIdx}]" />
                                </td>
                            </tr>
                        </c:forEach>
                            <tr>
                                <td colspan="3" class="lastRowTD"><!-- @ --></td>
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
