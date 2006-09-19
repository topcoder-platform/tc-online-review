<%-- 
   (#)listScorecardsContent.jsp
   ------------------------------------------------------------------
   @copyright Copyright (C) 2006, TopCoder Inc. All Rights Reserved.
   @author albertwang, flying2hk
   @version 1.0
   ------------------------------------------------------------------
   This is the content page of "listScorecards.jsp", it displays a
   list of scorecards defined in the system, the scorecards are grouped
   by the project category.
   ------------------------------------------------------------------
   @param[Request Param] projectTypeId
        project type id, long integer value.1 for "Component", 2 for "Application"
        Optional, if missing, 1 is the default value
   @param[Request Attribute] scorecardList
        of type ScorecardListBean, holding the scorecards information to display,
        should be provided by the "listScorecards" action.
   
--%>
<%@ page language="java" %>
<%@ page import="com.cronos.onlinereview.actions.Constants, com.cronos.onlinereview.actions.ScorecardActionsHelper" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<script type='text/javascript' src='scripts/common.js'></script>
<script type='text/javascript' src='scripts/css.js'></script>
<script type='text/javascript' src='scripts/standardista-table-sorting.js'></script>
<script>
    // create the request object
    function createXMLHttpRequest() {
        var xmlHttp;
        if (window.ActiveXObject) {
            xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
        } else if (window.XMLHttpRequest) {
            xmlHttp = new XMLHttpRequest();
        }
        return xmlHttp;
    }
    // send the Ajax request
    function sendRequest(url, sid, chbox) {
        // create the Ajax request
        var myRequest = createXMLHttpRequest();
        var targetStatus;
        if (chbox.checked) {
            // DON'T be confused here
            // at the very moment the checkbox is clicked, the Inactive scorecard's "checked" status is "on"
            targetStatus = 'Active';
        } else {
            // at the very moment the checkbox is clicked, the Active scorecard's "checked" status is "off"
            targetStatus = 'Inactive';
        }
        // assemble the request XML
        var content = '<?xml version="1.0" ?>'+
                      '<request type="SetScorecardStatus">'+
                      '<parameters>'+
                      '<parameter name="ScorecardId">'+
                      sid+
                      '</parameter>'+
                      '<parameter name="Status">'+
                      targetStatus+
                      '</parameter>'+
                      '</parameters>'+
                      '</request>';
        // set the callback function
        myRequest.onreadystatechange = function() {
            if(myRequest.readyState == 4 && myRequest.status == 200){
                // the response is ready
                var respXML = myRequest.responseXML;
                // retrieve the result
                var result = respXML.getElementsByTagName('result')[0].getAttribute('status');
                if (result == 'Success') {
                    // operation succeeded, change the status of corresponding checkbox
                    if (chbox.checked) {
                        chbox.checked = false;
                        chbox.defaultChecked = false;
                        chbox.parentNode.setAttribute('standardistaTableSortingInnerText', 'Inactive');
                    } else if (!chbox.checked){
                        chbox.checked = true;
                        chbox.defaultChecked = true;
                        chbox.parentNode.setAttribute('standardistaTableSortingInnerText', 'Active');
                    }
                    // refresh the filter
                    refreshFilter();
                } else {
                    // operation failed, alert the error message to the user
                    alert('Error occured while setting the scorecard status : ' + result);
                }
            }
        };
        // send the request
        myRequest.open("POST", url, true);
        myRequest.setRequestHeader("Content-Type", "text/xml");
        myRequest.send(content);
    }
</script>
<table width="100%" border="0" cellpadding="0" cellspacing="1" class="forumBkgd">
    <tr>
        <td class="whiteBkgnd" style="height: 100%">
            <table border="0" cellpadding="0" cellspacing="0" width="100%">
                <tr>
                    <td width="100%" nowrap class="tableHeader">
                        <strong><bean:write name="<%= Constants.ATTR_KEY_SCORECARD_LIST %>" property="projectTypeName"/> Scorecards</strong>
                    </td>
                    <td align="right">&nbsp;</td>
                </tr>
                <tr>
                    <td class="whiteBkgnd" colspan="3"><img src="images/clear.gif" alt="" width="1" height="1" border="0"/></td>
                </tr>
            </table>
            <!-- Scorecards -->
            <table id="scorecardTable" width="100%" border="0" cellpadding="0" cellspacing="1" class="forumBkgd" >
                <tr valign="top">
                    <td class="forumTextEven" colspan="4">
                        <html:form action="/scorecardAdmin?actionName=newScorecard">
                            <html:hidden property="projectTypeId" name="<%= Constants.ATTR_KEY_SCORECARD_LIST %>"/>
                            <html:submit style="width:125px; float:right;" styleClass="Buttons2" >
                                <bean:message key="listScorecards.button.create"/>
                            </html:submit>
                        </html:form>
                        <strong><bean:message key="listScorecards.filterSelect.title" /></strong>
                        <select id="filterSelect" value="Active Only" class="inputBox" style="margin-right:5px;margin-left:5px;" name="status" size="1" onchange="refreshFilter()">
                            <option value="0"><bean:message key="listScorecards.filterSelect.options.any"/></option>
                            <option value="1"><bean:message key="listScorecards.filterSelect.options.active"/></option>
                            <option value="2"><bean:message key="listScorecards.filterSelect.options.inactive"/></option>
                        </select>&nbsp;
                    </td>
                </tr>
            </table>
            <logic:iterate id="curGroup" name="<%= Constants.ATTR_KEY_SCORECARD_LIST %>" property="scorecardGroups" type="com.cronos.onlinereview.actions.ScorecardGroupBean">
                <table id='<%= "sortable_" + curGroup.getProjectCategory().getName()  %>' width="100%" border="0" cellpadding="0" cellspacing="1" class="forumBkgd">
                    <thead>
                        <tr valign="top">
                            <td class="forumTitle">
                                <span style="text-decoration: underline"><bean:write name="curGroup" property="projectCategory.name"/></span>
                            </td>                           
                            <td width="50%" class="forumTitle" >
                                <span style="text-decoration: underline"><bean:message key="global.label.type" /></span>
                            </td>
                            <td class="forumTitle" style="text-align: center" width="60">
                                <span style="text-decoration: underline"><bean:message key="global.label.active" /></span>
                            </td>
                        </tr>
                    </thead>
                    <tfoot>
                        <logic:notEmpty name="curGroup" property="scorecards">
                            <tr style="display:none;">
                                <td class="forumTextOdd" nowrap colspan="3">
                                    <bean:message key="listScorecards.message.noscorecards" arg0="<%=curGroup.getProjectCategory().getName() %>"/>
                                </td>
                            </tr>
                        </logic:notEmpty>
                        <logic:empty name="curGroup" property="scorecards">
                            <tr>
                                <td class="forumTextOdd" nowrap colspan="3">
                                    <bean:message key="listScorecards.message.noscorecards" arg0="<%=curGroup.getProjectCategory().getName() %>"/>
                                </td>
                            </tr>
                        </logic:empty>
                    </tfoot>
                    <tbody>
                        <!--Begin lists -->
                        <logic:iterate id="curScorecard" indexId="sIdx" name="curGroup" property="scorecards" type="com.topcoder.management.scorecard.data.Scorecard">
                            <% String rowClass = (sIdx.intValue() % 2 == 0) ? "forumTextOdd" : "forumTextEven"; %>
                            <tr valign="top">
                                <!-- element.innerText is not supported by Firefox as IE, so here just add extended attribute to cache the name -->
                                <td class="<%=rowClass%>" standardistaTableSortingInnerText='<%= curScorecard.getName() + " v " + curScorecard.getVersion() %>'>
                                    <logic:equal value="1" name="curScorecard" property="category">
                                        <!-- "Design" icon, iconStatusSpecSm.gif -->
                                        <img src="images/iconStatusSpecSm.gif" />
                                    </logic:equal>
                                    <logic:equal value="2" name="curScorecard" property="category">
                                        <!-- "Development" icon, iconStatusDevSm.gif -->
                                        <img src="images/iconStatusDevSm.gif" />
                                    </logic:equal>
                                    <logic:equal value="3" name="curScorecard" property="category">
                                        <!-- "Security" icon, iconStatusSecSm.gif -->
                                        <img src="images/iconStatusSecSm.gif" />
                                    </logic:equal>
                                    <logic:equal value="4" name="curScorecard" property="category">
                                        <!-- "Process" icon, iconStatusProcSm.gif -->
                                        <img src="images/iconStatusProcSm.gif" />
                                    </logic:equal>
                                    <logic:equal value="5" name="curScorecard" property="category">
                                        <!-- "Testing Competition" icon, IconTestingSm.gif-->
                                        <img src="images/IconTestingSm.gif" />
                                    </logic:equal>
                                    <logic:equal value="6" name="curScorecard" property="category">
                                        <!-- "Specification" icon, IconSpecSm.gif-->
                                        <img src="images/IconSpecSm.gif" />
                                    </logic:equal>
                                    <logic:equal value="7" name="curScorecard" property="category">
                                        <!-- "Architecture" icon, IconArchSm.gif-->
                                        <img src="images/IconArchSm.gif" />
                                    </logic:equal>
                                    <logic:equal value="8" name="curScorecard" property="category">
                                        <!-- "Component Production" icon, IconComponentSm.gif-->
                                        <img src="images/IconComponentSm.gif" />
                                    </logic:equal>
                                    <logic:equal value="9" name="curScorecard" property="category">
                                        <!-- "Quality Assurance" icon, IconQASm.gif-->
                                        <img src="images/IconQASm.gif" />
                                    </logic:equal>
                                    <logic:equal value="10" name="curScorecard" property="category">
                                        <!-- "Deployment" icon, IconDeploySm.gif-->
                                        <img src="images/IconDeploySm.gif" />
                                    </logic:equal>
                                    <logic:equal value="11" name="curScorecard" property="category">
                                        <!-- "Security" icon, iconStatusSecSm.gif-->
                                        <img src="images/iconStatusSecSm.gif" />
                                    </logic:equal>
                                    <logic:equal value="12" name="curScorecard" property="category">
                                        <!-- "Process" icon, iconStatusProcSm.gif-->
                                        <img src="images/iconStatusProcSm.gif" />
                                    </logic:equal>
                                    <logic:equal value="13" name="curScorecard" property="category">
                                        <!-- "Testing Competition" icon, IconTestingSm.gif-->
                                        <img src="images/IconTestingSm.gif" />
                                    </logic:equal>
                                    <logic:equal value="14" name="curScorecard" property="category">
                                        <!-- "Assembly Competition" icon, IconAssemblySm.gif-->
                                        <img src="images/IconAssemblySm.gif" />
                                    </logic:equal>
                                    <strong>
                                        <html:link action="scorecardAdmin?actionName=viewScorecard" paramId="sid" paramName="curScorecard" paramProperty="id">
                                            <bean:write name="curScorecard" property="name" />
                                            <bean:message key="global.label.v" />
                                            <bean:write name="curScorecard" property="version" />
                                        </html:link>
                                    </strong>
                                </td>
                                <td class="<%=rowClass%>" >
                                    <bean:write name="curScorecard" property="scorecardType.name"/>
                                </td>
                                <td class="<%=rowClass%>" style="text-align: center" standardistaTableSortingInnerText="<%= curScorecard.getScorecardStatus().getName() %>">
                                    <bean:define id="scorecardId" name="curScorecard" property="id" />
                                    <bean:define id="ajaxURL" value="<%= response.encodeURL("ajaxSupport") %>"/> 
                                    <logic:equal value="1" name="curScorecard" property="scorecardStatus.id">
                                        <logic:equal value="true" name="curScorecard" property="inUse">
                                            <input id="<%= "scorecardStatus" + scorecardId %>" type="checkbox" onclick="<%="sendRequest('" + ajaxURL +  "', '" + scorecardId + "', this); return false;" %>" checked disabled/>
                                        </logic:equal>
                                        <logic:equal value="false" name="curScorecard" property="inUse">
                                            <input id="<%= "scorecardStatus" + scorecardId %>" type="checkbox" onclick="<%="sendRequest('" + ajaxURL +  "', '" + scorecardId + "', this); return false;" %>" checked/>
                                        </logic:equal>
                                    </logic:equal>
                                    <logic:equal value="2" name="curScorecard" property="scorecardStatus.id">
                                        <!-- <input id="Checkbox13" type="checkbox" onclick="refreshFilter()"/> -->
                                        <input id="<%= "scorecardStatus" + scorecardId %>" type="checkbox" onclick="<%="sendRequest('" + ajaxURL +  "', '" + scorecardId + "', this); return false;" %>" />
                                    </logic:equal>
                                </td>
                            </tr>
                        </logic:iterate>
                    </tbody>
                </table>
            </logic:iterate>
            <table width="100%" border="0" cellpadding="0" cellspacing="1" class="forumBkgd">    
                <tr>
                    <td class="forumHeadFoot" colspan="3" height="5">
                        <img src="images/clear.gif" alt="" width="10" height="5" border="0"/>
                    </td>
                </tr>
            </table>
            <!-- End lists -->
            <!-- Spacer -->
            <table border="0" cellpadding="0" cellspacing="1" class="whiteBkgd" width="100%">
                <tr>
                    <td class="forumTextOdd"><img src="images/clear.gif" alt="" width="1" height="5" border="0" /></td>
                </tr>
            </table>
        </td>
    </tr>
</table>
            