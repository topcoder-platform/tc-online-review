<%--
  - Author: duxiaoyang
  - Version: 1.0
  - Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
  -
  - This JSP file is used to generate the title and expand/collapse links above the review result table.
--%>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="or" uri="/or-tags" %>

<table width="100%" cellpadding="0" cellspacing="0" border="0" style="margin:15px 0px">
    <tr>
        <td><h3 class="scoreBoard__title" >${tableTitle}</h3></td>
        <c:if test="${not noExpandCollapse}">
            <td align="right">
                <a class="expand__all" href="javascript:showAll();"><or:text key="global.expandAll" /></a>
                <a class="collapse__all" href="javascript:hideAll();"><or:text key="global.collapseAll" /></a>
            </td>
        </c:if>
    </tr>
</table>