<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<!-- Tabs -->
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr valign="top">
        <logic:equal value="Component" name="scorecardForm" property="projectTypeName">
            <td width="1%" class="normal"><a href="scorecardAdmin.do?actionName=listScorecards&amp;projectTypeId=1"><img src="images/tabComponent_on.gif" alt="Component" border="0"></a></td>
            <td width="1%" class="normal"><a href="scorecardAdmin.do?actionName=listScorecards&amp;projectTypeId=2"><img src="images/tabApplication_off.gif" alt="Application" border="0"></a></td>
            <td width="1%" class="normal"><a href="scorecardAdmin.do?actionName=listScorecards&amp;projectTypeId=3"><img src="images/tabStudio_off.gif" alt="Studio" border="0"></a></td>
            <td width="1%" class="normal">&nbsp;</td>
            <td width="1%" class="normal">&nbsp;</td>
            <td width="98%" align="right">&nbsp;</td>
        </logic:equal>
        <logic:equal value="Application" name="scorecardForm" property="projectTypeName">
            <td width="1%" class="normal"><a href="scorecardAdmin.do?actionName=listScorecards&amp;projectTypeId=1"><img src="images/tabComponent_off.gif" alt="Component" border="0"></a></td>
            <td width="1%" class="normal"><a href="scorecardAdmin.do?actionName=listScorecards&amp;projectTypeId=2"><img src="images/tabApplication_on.gif" alt="Application" border="0"></a></td>
            <td width="1%" class="normal"><a href="scorecardAdmin.do?actionName=listScorecards&amp;projectTypeId=3"><img src="images/tabStudio_off.gif" alt="Studio" border="0"></a></td>
            <td width="1%" class="normal">&nbsp;</td>
            <td width="1%" class="normal">&nbsp;</td>
            <td width="98%" align="right">&nbsp;</td>
        </logic:equal>
        <logic:equal value="Studio" name="scorecardForm" property="projectTypeName">
            <td width="1%" class="normal"><a href="scorecardAdmin.do?actionName=listScorecards&amp;projectTypeId=1"><img src="images/tabComponent_off.gif" alt="Component" border="0"></a></td>
            <td width="1%" class="normal"><a href="scorecardAdmin.do?actionName=listScorecards&amp;projectTypeId=2"><img src="images/tabApplication_off.gif" alt="Application" border="0"></a></td>
            <td width="1%" class="normal"><a href="scorecardAdmin.do?actionName=listScorecards&amp;projectTypeId=3"><img src="images/tabStudio_on.gif" alt="Studio" border="0"></a></td>
            <td width="1%" class="normal">&nbsp;</td>
            <td width="1%" class="normal">&nbsp;</td>
            <td width="98%" align="right">&nbsp;</td>
        </logic:equal>
    </tr>
    <tr valign="top">
        <td colspan="5" class="forumHeadFoot"><img src="images/clear.gif" alt="" width="10" height="7" border="0"></td>
    </tr>
</table>
<!-- Tabs end -->
