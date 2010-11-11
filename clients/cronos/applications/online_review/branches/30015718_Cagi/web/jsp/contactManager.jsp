<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="html" uri="/tags/struts-html" %>
<%@ taglib prefix="bean" uri="/tags/struts-bean" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html xhtml="true">

<head>
    <jsp:include page="/includes/project/project_title.jsp">
        <jsp:param name="thirdLevelPageKey" value="contactManager.title" />
    </jsp:include>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

    <!-- TopCoder CSS -->
    <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/style.css' />" />
    <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/coders.css' />" />
    <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/stats.css' />" />
    <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/tcStyles.css' />" />

    <!-- JS from wireframes -->
    <script language="javascript" type="text/javascript"
        src="<html:rewrite href='/js/or/popup.js' />"><!-- @ --></script>
    <script language="javascript" type="text/javascript"
        src="<html:rewrite href='/js/or/expand_collapse.js' />"><!-- @ --></script>

    <!-- CSS and JS by Petar -->
    <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/or/new_styles.css' />" />
    <script language="JavaScript" type="text/javascript"
        src="<html:rewrite href='/js/or/rollovers2.js' />"><!-- @ --></script>
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
                                <td><html:img src="/i/${categoryIconName}" alt="" border="0" /></td>
                                <td><html:img src="/i/${rootCatalogIcon}" alt="${rootCatalogName}" border="0" /></td>
                                <td>
                                    <span class="bodyTitle">${project.allProperties["Project Name"]}</span>
                                    <c:if test='${!(empty project.allProperties["Project Version"])}'>
                                        <font size="4"><bean:message key="global.version" />
                                            ${project.allProperties["Project Version"]}</font>
                                    </c:if>
                                </td>
                            </tr>
                        </table>
                    </div><br />

                    <html:errors />
                    <html:form action="/actions/ContactManager">
                        <html:hidden property="method" value="contactManager" />
                        <html:hidden property="postBack" value="y" />
                        <html:hidden property="pid" value="${project.id}" />
                        <div align="center">
                        <table class="scorecard" id="table1">
                            <tr>
                                <td class="title"><b><bean:message key="contactManager.Contact" /></b></td>
                            </tr>
                            <tr class="light">
                                <td class="Value"><br>
                                    <b><bean:message key="contactManager.Category" /></b>
                                    <html:select property="cat" size="1" style="width:150px;" styleClass="inputBox">
                                        <html:option key="contactManager.Category.no" value="" />
                                        <html:option key="contactManager.Category.Question" value="Question" />
                                        <html:option key="contactManager.Category.Comment" value="Comment" />
                                        <html:option key="contactManager.Category.Complaint" value="Complaint" />
                                        <html:option key="contactManager.Category.Other" value="Other" />
                                    </html:select>
                                    <html:textarea property="msg" cols="20" rows="5" styleClass="inputTextBox" />

                                    <p><!-- @ --></p>
                                    <div align="right">
                                        <html:image srcKey="btnSubmit.img" altKey="btnSubmit.alt" border="0" />&#160;
                                        <a href="javascript:history.go(-1)"><html:img srcKey="btnCancel.img" altKey="btnCancel.alt" border="0" /></a>
                                    </div><br />
                                </td>
                            </tr>
                            <tr>
                                <td class="lastRowTD"><!-- @ --></td>
                            </tr>
                        </table>
                        </div>
                    </html:form>
                </div>
            </div>
        
        <jsp:include page="/includes/inc_footer.jsp" />

    </div>


</body>
</html:html>
