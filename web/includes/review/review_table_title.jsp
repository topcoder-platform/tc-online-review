<%--
  - Author: duxiaoyang
  - Version: 1.0
  - Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
  -
  - This JSP file is used to generate the title and expand/collapse links above the review result table.
--%>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="bean" uri="/tags/struts-bean" %>

<table width="100%" cellpadding="0" cellspacing="0" border="0" style="margin:15px 0px">
    <tr>
        <td><h3 style="margin:0">${tableTitle}</h3></td>
        <c:if test="${not noExpandCollapse}">
            <td align="right" valign="top">
                <a href="javascript:showAll();"><bean:message key="global.expandAll" /></a> |
                <a href="javascript:hideAll();"><bean:message key="global.collapseAll" /></a>
            </td>
        </c:if>
    </tr>
</table>