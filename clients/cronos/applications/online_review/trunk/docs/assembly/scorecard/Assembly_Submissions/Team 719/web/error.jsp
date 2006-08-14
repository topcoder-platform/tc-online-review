<%-- 
   (#) error.jsp
   ------------------------------------------------------------------
   @copyright Copyright (C) 2006, TopCoder Inc. All Rights Reserved.
   @author TCSDEVELOPER
   @version 1.0
   ------------------------------------------------------------------
   This is the page which is displayed to the user when a general error
   (non-form-validation errors, e.g. server-side exception) occurs.
   The representation of the error messages is provided in page
   "errorContent.jsp".
   ------------------------------------------------------------------
--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>

<tiles:insert page="pageLayout.jsp" flush="true">
    <tiles:put name="content" value="errorContent.jsp" />
</tiles:insert>