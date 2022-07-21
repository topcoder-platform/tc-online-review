<%--
  - Author: TCSASSEMBLER
  - Version: 2.0
  - Copyright: Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
  -
  - Description: The default index page.
--%>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<c:redirect url="https://${pageContext.request.getServerName()}${(not orfn:isUserLoggedIn(pageContext.request))?'/review/actions/Login':'/review/actions/ListProjects'}" />

<%--

Redirect default requests to Login or ListProjects action.

--%>
