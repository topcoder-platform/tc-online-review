<%--
  - Author: TCSASSEMBLER
  - Version: 2.0
  - Copyright (C) 2014 TopCoder Inc., All Rights Reserved.
  -
  - Description: This page displays project links edition page.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="or" uri="/or-tags" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
    <jsp:include page="/includes/project/project_title.jsp" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

    <!-- TopCoder CSS -->
    <link type="text/css" rel="stylesheet" href="/css/style.css" />
    <link type="text/css" rel="stylesheet" href="/css/coders.css" />
    <link type="text/css" rel="stylesheet" href="/css/tcStyles.css" />

    <!-- CSS and JS by Petar -->
    <link type="text/css" rel="stylesheet" href="/css/or/new_styles.css" />
    <link type="text/css" rel="stylesheet" href="/css/or/phasetabs.css" />
    <script language="JavaScript" type="text/javascript" src="/js/or/rollovers2.js"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript" src="/js/or/dojo.js"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript" src="/js/or/util.js"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript" src="/js/or/validation_util.js"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript" src="/js/or/validation_edit_project_links.js"><!-- @ --></script>
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

</head>
<body onload="onLoad();">
<div align="center">
    <div class="maxWidthBody" align="left">

        <jsp:include page="/includes/inc_header.jsp" />
        <jsp:include page="/includes/project/project_tabs.jsp" />

        <div id="mainMiddleContent">
           <div class="clearfix"></div>
           <div id="tabcontentcontainer">     
                <s:form action="SaveProjectLinks" onsubmit="return validate_form(this, true);" namespace="/actions">
                 <input type="hidden" name="pid"  value="<or:fieldvalue field='pid' />" />
                         
              <c:if test="${orfn:isErrorsPresent(pageContext.request)}">
                  <table cellpadding="0" cellspacing="0" border="0">
                      <tr><td width="16"><!-- @ --></td><td><!-- @ --></td></tr>
                      <tr>
                          <td colspan="2"><span style="color:red;"><or:text key="Error.saveReview.ValidationFailed" /></span></td>
                      </tr>
                      <s:actionerror escape="false" />
                  </table><br />
              </c:if>
                         
              <div id="contentTitle">
                    <h3>${project.allProperties["Project Name"]} version ${project.allProperties["Project Version"]} - Manage Project Links</h3>
              </div>

                    <div id="tabNewLinks">
                        <table cellpadding="0" id="newLinks" class="tabLinks">
                            <tbody>
                            <tr class="dark">
                                <td colspan="6" class="title"><or:text key="editProjectLinks.box.editLinks" /></td>
                            </tr>
                            <tr>
                                <td class="header"><or:text key="editProjectLinks.editLink.LinkType" /></td>
                                <td class="header"><or:text key="editProjectLinks.editLink.SelectProject" /></td>
                                
                                <td class="header"><or:text key="editProjectLinks.editLink.Operation" /></td>
                            </tr>
                            <c:forEach var="linkIdx" varStatus="linkStatus" begin="0" end="${fn:length(projectLinkForm.map['link_dest_id']) - 1}">
                            <tr class='${(linkStatus.index % 2 == 0) ? "light" : "dark"}'>
                                <td nowrap="nowrap" class="value">
                                <select class="inputBox" name="link_type_id[${linkIdx}]" 
                                               onchange="onLinkTypeDropDownChange(this);"><c:set var="OR_FIELD_TO_SELECT" value="link_type_id[${linkIdx}]"/>
                                     <option  value="-1"  <or:selected value="-1"/>><or:text key="editProjectLinks.projectTypes.SelectType" /></option>
                                  <c:forEach items="${projectLinkTypes}" var="projectLinkType">                                                                    
                                         <option value="${projectLinkType.id}" <or:selected value="${projectLinkType.id}"/>>${projectLinkType.name}</option>
                                  </c:forEach>
                                </select>
                            <div name="project_link_validation_msg" class="error" style="display:none"></div>
                                </td>

                                <td nowrap="nowrap" class="value">
                            <select class="inputBox" name="link_dest_id[${linkIdx}]"><c:set var="OR_FIELD_TO_SELECT" value="link_dest_id[${linkIdx}]"/>
                                    <option  value="-1"  <or:selected value="-1"/>><or:text key="editProjectLinks.projectTypes.SelectProject" /></option>
                          <c:forEach items="${allProjects}" var="projectElement">
                                <c:if test="${projectElement.id ne project.id}">
                                <option value="${projectElement.id}" <or:selected value="${projectElement.id}"/>>${projectElement.allProperties["Project Name"]} v${projectElement.allProperties["Project Version"]} (${projectElement.projectCategory.name})</option>
                                </c:if>
                          </c:forEach>
                               </select>
                               <div name="project_link_validation_msg" class="error" style="display:none"></div>                                        
                                </td>
                                
                                <td nowrap="nowrap" class="value" align="center">
                                    <c:if test="${linkIdx eq 0}">
                                    <img src="<or:text key='editProjectLinks.btnAdd.img' />" border="0"
                                                onclick="javascript:newProjectLink();"
                                            alt="<or:text key='editProjectLinks.btnAdd.alt' />"  style="cursor:hand;" />
                                    </c:if>
                                    <img src="<or:text key='editProjectLinks.btnDelete.img' />"
                                            style="cursor:hand;${(linkIdx eq 0) ? 'display: none;' : ''}" border="0"
                                            onclick="deleteProjectLink(this.parentNode.parentNode);"
                                            alt="<or:text key='editProjectLinks.btnDelete.alt' />" />
                                    <input type="hidden" name="link_action[${linkIdx}]"  value="<or:fieldvalue field='link_action[${linkIdx}]' />" />
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
                  <input type="image"  src="<or:text key='btnSaveChanges.img' />" alt="<or:text key='btnSaveChanges.alt' />" border="0"/>&#160;
                  <a href="<or:url value='/actions/ViewProjectDetails?pid=${project.id}' />"><img src="<or:text key='btnCancel.img' />" alt="<or:text key='btnCancel.alt' />" border="0"/></a>
             </div>
                 </s:form>    
           </div> <!-- //tabconentcontainer -->
        </div><!-- //mainMiddleContent -->
            
        <jsp:include page="/includes/inc_footer.jsp" />  
              
     </div><!-- //maxWidthBody -->
</div>      
</body>     
            
</html> 
