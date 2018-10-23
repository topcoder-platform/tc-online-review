<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ page import="com.cronos.onlinereview.actions.ScorecardActionsHelper" %>
<!--Header begins-->
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="head">
      <tr valign="top">
        <td height="57" align="left" rowspan="2" style="width: 219px"><img src="images/logoTCSHome.gif" alt="TopCoder Software" width="216" height="57" border="0" /></td>
        <td width="155" height="57" align="left" rowspan="2"><img src="images/homeBannerAdmin.gif" alt="Administration" width="155" height="57" border="0" /></td>
        <td class="headRight">
          <table border="0" cellpadding="0" cellspacing="0" class="adminSearch" align="right">
            <tr valign="middle">
              <td width="24" height="25" valign="bottom"><img src="images/searchHeaderLeft.gif" alt="" width="24" height="25" border="0"></td>
              <td><img src="images/clear.gif" alt="" width="6" border="0" /></td>
                <td class="login">
                        <!-- TODO : change this to user name -->
                        <logic:present name="<%=ScorecardActionsHelper.getInstance().getUserIdSessionAttributeKey() %>">
                            <tc-webtag:handle coderId="<%= ScorecardActionsHelper.getInstance().getUserId(request) %>" context="component" /> is logged in.
                        </logic:present>
                        <logic:notPresent name="<%=ScorecardActionsHelper.getInstance().getUserIdSessionAttributeKey() %>">
                            Please <html:link action="/login">login</html:link>.
                        </logic:notPresent>
                        </td>
                        
              <td><img src="images/clear.gif" alt="" width="10" border="0" /></td>
                <td class="login"><a href="logout.do?method=logout" class="loginLinks">:: Logout</a></td>
                <td><img src="images/clear.gif" alt="" width="10" border="0" /></td>
            </tr>
          </table>
        </td>
        </tr>
      <tr valign="bottom">
        <td class="headRight"><img src="images/homeBannerHead.gif" alt="The Future of Software Development" width="510" height="13" border="0" /></td>
  </tr>
  <tr>
      <td height="4" class="headStripe" colspan="3"><img src="images/clear.gif" alt="" height="4" border="0" /></td>
  </tr>
</table>
<!-- Nav Bar begins -->
<table width="100%" border="0" cellpadding="0" cellspacing="0" >
  <tr>
    <td width="20" class="adminTopNav"><img src="images/clear.gif" alt="" width="20" height="21" border="0"></td>
    <td>
      <table width="3%" border="0" cellpadding="0" cellspacing="0" class="adminTopNav">
        <tr valign="middle">
          <td width="46" align="center" class="adminTopNav"><a onmouseover="document.images['siteAdmin'].src = siteAdminon.src; window.status='Return to Regular Site'; return true;" onmouseout="document.images['siteAdmin'].src = siteAdminoff.src" class="topNavLinks" href="http://software.topcoder.com"><img src="images/siteAdmin_off.gif" alt="RETURN TO SITE" name="siteAdmin" width="84" height="21" border="0" /></a></td>
          <td width="66" align="center" class="adminTopNav"><a onmouseover="document.images['catalogAdmin'].src = catalogAdminon.src; window.status='Catalog Administration'; return true;" onmouseout="document.images['catalogAdmin'].src = catalogAdminoff.src" class="topNavLinks" href="#"><img src="images/catalogAdmin_off.gif" alt="CATALOG ADMIN" name="catalogAdmin" width="97" height="21" border="0" /></a></td>
          <td width="106" align="center" class="adminTopNav"><a onmouseover="document.images['categoryAdmin'].src = categoryAdminon.src; window.status='Category Administration'; return true;" onmouseout="document.images['categoryAdmin'].src = categoryAdminoff.src" class="topNavLinks" href="#"><img src="images/categoryAdmin_off.gif" alt="CATEGORY ADMIN" name="categoryAdmin" width="104" height="21" border="0" /></a></td>
          <td width="156" align="center" class="adminTopNav"><a onmouseover="document.images['userAdmin'].src = userAdminon.src; window.status='User Administration'; return true;" onmouseout="document.images['userAdmin'].src = userAdminoff.src" class="topNavLinks" href="#"><img src="images/userAdmin_off.gif" alt="USER ADMIN" name="userAdmin" width="79" height="21" border="0" /></a></td>
          <td width="106" align="center" class="adminTopNav"><a onmouseover="document.images['listsAdmin'].src = listsAdminon.src; window.status='Lists Administration'; return true;" onmouseout="document.images['listsAdmin'].src = listsAdminoff.src" class="topNavLinks" href="#"><img src="images/listAdmin_off.gif" alt="LISTS ADMIN" name="listsAdmin" width="79" height="21" border="0" /></a></td>
          <td width="106" align="center" class="adminTopNav">
                        <html:link action="/scorecardAdmin?actionName=listScorecards&projectTypeId=1" onmouseover="document.images['scorecardAdmin'].src = scorecardAdminon.src; window.status='Scorecard Administration'; return true;" onmouseout="document.images['scorecardAdmin'].src = scorecardAdminoff.src" styleClass="topNavLinks">
                            <img src="images/scorecardAdmin_off.gif" alt="SCORECARD ADMIN" name="scorecardAdmin" height="21" border="0" />
                        </html:link>
                    </td>
        </tr>
      </table>
    </td>
    <td width="97%" class="adminTopNav"><img src="images/clear.gif" alt="" border="0"></td>
  </tr>
</table>
<!-- Nav Bar ends -->
<!--Header ends -->
