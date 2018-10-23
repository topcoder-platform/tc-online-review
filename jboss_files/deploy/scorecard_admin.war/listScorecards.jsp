<%-- 
   (#)listScorecards.jsp
   ------------------------------------------------------------------
   @copyright Copyright (C) 2006, TopCoder Inc. All Rights Reserved.
   @author TCSDEVELOPER
   @version 1.0
   ------------------------------------------------------------------
   This is the page to display a list of scorecards defined in the
   system, the scorecards are grouped by the project category.
   The representation of the scorecard list content is provided in
   page "listScorecardsContent.jsp".
   ------------------------------------------------------------------
   @param[Request Param] projectTypeId
        project type id, long integer value.1 for "Component", 2 for "Application"
   
--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<tiles:insert page="pageLayout.jsp" flush="true">
    <tiles:put name="content" value="listScorecardsContent.jsp" />
</tiles:insert>