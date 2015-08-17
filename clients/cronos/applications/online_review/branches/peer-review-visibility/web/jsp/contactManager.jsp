<%--
  - Author: TCSASSEMBLER
  - Version: 2.0
  - Copyright (C) 2014 TopCoder Inc., All Rights Reserved.
  -
  - Description: This page provides the view contact the project manager.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="or" uri="/or-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
    <jsp:include page="/includes/project/project_title.jsp">
        <jsp:param name="thirdLevelPageKey" value="contactManager.title" />
    </jsp:include>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

    <!-- TopCoder CSS -->
    <link type="text/css" rel="stylesheet" href="/css/style.css" />
    <link type="text/css" rel="stylesheet" href="/css/coders.css" />
    <link type="text/css" rel="stylesheet" href="/css/stats.css" />
    <link type="text/css" rel="stylesheet" href="/css/tcStyles.css" />

    <!-- JS from wireframes -->
    <script language="javascript" type="text/javascript"
        src="/js/or/popup.js"><!-- @ --></script>
    <script language="javascript" type="text/javascript"
        src="/js/or/expand_collapse.js"><!-- @ --></script>

    <!-- CSS and JS by Petar -->
    <link type="text/css" rel="stylesheet" href="/css/or/new_styles.css" />
    <script language="JavaScript" type="text/javascript"
        src="/js/or/rollovers2.js"><!-- @ --></script>
</head>

<body>
<div align="center">
    
    <div class="maxWidthBody" align="left">

        <jsp:include page="/includes/inc_header.jsp" />
        
        <jsp:include page="/includes/project/project_tabs.jsp" />
        
            <div id="mainMiddleContent">
                <div style="position: relative; width: 100%;">
                        <table cellspacing="0" cellpadding="0" border="0">
                            <tr valign="middle">
                                <td><img src="/i/${categoryIconName}" alt="" border="0" /></td>
                                <td><img src="/i/${rootCatalogIcon}" alt="${rootCatalogName}" border="0" /></td>
                                <td>
                                    <span class="bodyTitle">${project.allProperties["Project Name"]}</span>
                                    <c:if test='${!(empty project.allProperties["Project Version"])}'>
                                        <font size="4"><or:text key="global.version" />
                                            ${project.allProperties["Project Version"]}</font>
                                    </c:if>
                                </td>
                            </tr>
                        </table>
                    </div><br />

                    <s:actionerror  escape="false"/>
                    <s:form action="ContactManager" namespace="/actions">
                        <input type="hidden" name="postBack" value="y" />
                        <input type="hidden" name="pid" value="${project.id}" />
                        <div align="center">
                        <table class="scorecard" id="table1">
                            <tr>
                                <td class="title"><b><or:text key="contactManager.Contact" /></b></td>
                            </tr>
                            <tr class="light">
                                <td class="Value"><br>
                                    <b><or:text key="contactManager.Category" /></b>
                                    <select name="cat" size="1" style="width:150px;" class="inputBox"><c:set var="OR_FIELD_TO_SELECT" value="cat"/>
                                        <option  value=""  <or:selected value=""/>><or:text key="contactManager.Category.no" /></option>
                                        <option  value="Question"  <or:selected value="Question"/>><or:text key="contactManager.Category.Question" /></option>
                                        <option  value="Comment"  <or:selected value="Comment"/>><or:text key="contactManager.Category.Comment" /></option>
                                        <option  value="Complaint"  <or:selected value="Complaint"/>><or:text key="contactManager.Category.Complaint" /></option>
                                        <option  value="Other"  <or:selected value="Other"/>><or:text key="contactManager.Category.Other" /></option>
                                    </select>
                                    <textarea name="msg" cols="20" rows="5" class="inputTextBox" ><or:fieldvalue field="msg" /></textarea>

                                    <p><!-- @ --></p>
                                    <div align="right">
                                        <input type="image"  src="<or:text key='btnSubmit.img' />" alt="<or:text key='btnSubmit.alt' />" border="0" />&#160;
                                        <a href="<or:url value='/actions/ViewProjectDetails?pid=${project.id}' />">
                                            <img src="<or:text key='btnCancel.img' />" alt="<or:text key='btnCancel.alt' />" border="0" />
                                        </a>
                                    </div><br />
                                </td>
                            </tr>
                            <tr>
                                <td class="lastRowTD"><!-- @ --></td>
                            </tr>
                        </table>
                        </div>
                    </s:form>
                </div>
            </div>
        
        <jsp:include page="/includes/inc_footer.jsp" />

    </div>


</body>
</html>
