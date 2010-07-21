<%-- 
   (#) editScorecard.jsp
   ------------------------------------------------------------------
   @copyright Copyright (C) 2006, TopCoder Inc. All Rights Reserved.
   @author TCSDEVELOPER
   @version 1.0
   ------------------------------------------------------------------
   This is the page to display the details of a given scorecard in
   editable manner, the user can edit specific areas of the scorecard.
   The representation of the scorecard details content is provided in
   page "editScorecardContent.jsp".
   ------------------------------------------------------------------
   @param scorecardForm [Session Attribute]
        ActionForm containing the scorecard information, it should be
        filled by action "viewScorecard"
--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>

<tiles:insert page="pageLayout.jsp" flush="true">
    <tiles:put name="content" value="editScorecardContent.jsp" />
</tiles:insert>