<%-- 
   (#) pageLayout.jsp
   ------------------------------------------------------------------
   @copyright Copyright (C) 2006, TopCoder Inc. All Rights Reserved.
   @author albertwang, flying2hk
   @version 1.0
   ------------------------------------------------------------------
   This is the page layout tile, which defines the default page
   layout for the scorecard admin application.
   It is used in all JSP pages inside this application.
   ------------------------------------------------------------------
--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html:html lang="true">
<head>
    <html:base />
    <title>Scorecard Admin</title>
    <link rel="stylesheet" type="text/css" href="https://software.topcoder.com/includes/tcs_style.css" />
    <link rel="stylesheet" type="text/css" href="styles/new_styles.css" />
    <script language="JavaScript" type="text/javascript" src="scripts/javascriptAdmin.js"></script>
    <script language="JavaScript" type="text/javascript" src="scripts/scorecardFilter.js"></script>
    <script language="JavaScript" type="text/javascript" src="scripts/toggles.js"></script>
    <script language="JavaScript" type="text/javascript" src="scripts/highlightrow.js"></script>
</head>
<body leftmargin="0" rightmargin="0" topmargin="0">
    <tiles:insert page="includes/header.jsp" flush="true"/>
    <!-- Content begins -->
    <div id="contentarea">
        <tiles:insert page="includes/tabs.jsp" flush="true" />
        <tiles:insert attribute="content" />
    </div>
    <!-- End Contentarea Div -->    
    <tiles:insert page="includes/footer.jsp" />
</body>
</html:html>