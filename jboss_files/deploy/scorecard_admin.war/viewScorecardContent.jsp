<%-- 
   (#) viewScorecardContent.jsp
   ------------------------------------------------------------------
   @copyright Copyright (C) 2006, TopCoder Inc. All Rights Reserved.
   @author albertwang, flying2hk
   @version 1.0
   ------------------------------------------------------------------
   This is the content page of "viewScorecard", it displays the details
   of a given scorecard in a readonly manner.
   ------------------------------------------------------------------
   @param scorecardForm [Session Attribute]
        ActionForm containing the scorecard information, it should be
        filled by action "viewScorecard"
--%>
<%@ page language="java" %>
<%@ page import="com.cronos.onlinereview.actions.Constants, com.cronos.onlinereview.actions.ScorecardActionsHelper" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<table width="100%" border="0" cellpadding="0" cellspacing="1" class="forumBkgd">
    <tr>
        <td class="whiteBkgnd" style="height: 100%">
            <table border="0" cellpadding="0" cellspacing="0" width="100%">
                <tr>
                    <td width="50%" nowrap class="tableHeader">
                        <strong><bean:write name="scorecardForm" property="projectTypeName"/> Scorecards</strong>
                    </td>
                    <td width="50%" nowrap class="ExpandCollapse">
                        <a href="javascript:showAll();">
                            <bean:message key="viewScorecard.label.expand_all"/>
                        </a>
                        &nbsp;|&nbsp;
                        <a href="javascript:hideAll();">
                            <bean:message key="viewScorecard.label.collapse_all"/>
                        </a>
                    </td>
                    <td align="right" style="height:35px"></td>
                </tr>
                <tr>
                    <td class="whiteBkgnd" colspan="3"><img src="images/clear.gif" alt="" width="1" height="1" border="0"></td>
                </tr>
            </table>
            <!-- Scorecard details -->
            <table width="100%" border="0" cellpadding="0" cellspacing="1" class="forumBkgd" id="tblScorecardSummary">
                <tr>
                    <td class="forumTextEven" colspan="8">
                        <logic:notEqual value="true" property="scorecard.inUse" name="scorecardForm">
                            <logic:notEqual value="Active" property="scorecard.scorecardStatus.name" name="scorecardForm">
                                <html:form action="/scorecardAdmin?actionName=editScorecard">
                                    <html:hidden property="<%=Constants.PARAM_KEY_SCORECARD_ID %>" value="<%= request.getParameter(Constants.PARAM_KEY_SCORECARD_ID) %>"/>
                                    <html:submit style="width:80px; float:right;" styleClass="Buttons">
                                        <bean:message key="viewScorecard.button.edit"/>
                                    </html:submit>
                                </html:form>
                            </logic:notEqual>
                        </logic:notEqual>
                        
                        <html:form action="/scorecardAdmin?actionName=copyScorecard">
                            <html:hidden property="<%=Constants.PARAM_KEY_SCORECARD_ID %>" value="<%= request.getParameter(Constants.PARAM_KEY_SCORECARD_ID) %>"/>
                            <html:submit property="<%= Constants.PARAM_KEY_SCORECARD_ID %>" style="width:80px; float:right;" styleClass="Buttons2">
                                <bean:message key="viewScorecard.button.copy"/>
                            </html:submit>
                        </html:form>
                        <strong><bean:message key="global.message.scorecard_details"/></strong>
                    </td>
                </tr>
                <tr>
                    <td class="forumTitle">
                        <bean:message key="global.label.id"/>
                    </td>
                    <td class="forumTitle">
                        <bean:message key="global.label.scorecard_name"/>
                    </td>
                    <td class="forumTitle">
                        <bean:message key="global.label.project_type"/>
                    </td>
                    <td class="forumTitle">
                        <bean:message key="global.label.category"/>
                    </td>
                    <td class="forumTitle">
                        <bean:message key="global.label.type"/>
                    </td>
                    <td class="forumTitle">
                        <bean:message key="global.label.min_score"/>
                    </td>
                    <td class="forumTitle">
                        <bean:message key="global.label.max_score"/>
                    </td>
                    <td class="forumTitle">
                        <bean:message key="global.label.status"/>
                    </td>
                </tr>
                <tr>
                    <td class="forumTextOdd" nowrap>
                        <bean:write name="scorecardForm" property="scorecard.id"/>
                    </td>
                    <td class="forumTextOdd">
                        <bean:write name="scorecardForm" property="scorecard.name"/>
                        v
                        <bean:write name="scorecardForm" property="scorecard.version"/>
                    </td>
                    <td class="forumTextOdd">
                        <bean:write name="scorecardForm" property="projectTypeName"/>
                    </td>
                    <td class="forumTextOdd">
                        <bean:write name="scorecardForm" property="projectCategoryName"/>
                    </td>
                    <td class="forumTextOdd">
                        <bean:write name="scorecardForm" property="scorecard.scorecardType.name" />
                    </td>
                    <td class="forumTextOdd">
                        <bean:write name="scorecardForm" property="minScoreText" />
                    </td>
                    <td class="forumTextOdd">
                        <bean:write name="scorecardForm" property="maxScoreText" />
                    </td>
                    <td class="forumTextOdd">
                        <bean:write name="scorecardForm" property="scorecard.scorecardStatus.name" />
                    </td>
                </tr>
                <tr>
                    <td class="forumHeadFoot" colspan="8" height="5">
                        <img src="images/clear.gif" alt="" width="10" height="5" border="0"/>
                    </td>
                </tr>
            </table>
            <table border="0" cellpadding="0" cellspacing="1" class="whiteBkgd" width="100%">
                <tr>
                    <td class="forumTextOdd">
                        <img src="images/clear.gif" alt="" width="1" height="5" border="0" />
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellpadding="0" cellspacing="1" class="forumBkgd" id="table1">
                <logic:iterate id="curGroup" indexId="gIdx" name="scorecardForm" property="scorecard.allGroups">
                    <tr>
                        <td class="forumTitle" colspan="1">
                            <bean:message key="global.label.group"/>&nbsp;
                            <span style="font-weight: 400">
                                <bean:write name="curGroup" property="name" />
                            </span>
                        </td>
                        <td class="forumTitle" colspan="3">
                            <bean:message key="global.label.weight"/>&nbsp;
                            <span style="font-weight: 400">
                                <bean:write name="curGroup" property="weight" />
                            </span>
                        </td>
                    </tr>
                    <logic:iterate id="curSection" indexId="sIdx" name="curGroup" property="allSections">
                        <tr>
                            <td class="forumTextEven">
                                <strong>
                                <bean:message key="global.label.section"/>&nbsp;
                                    <span style="font-weight: 400">
                                        <bean:write name="curSection" property="name" />
                                    </span>
                                </strong>
                            </td>
                            <td class="forumTextEven" colspan="3">
                                <b><bean:message key="global.label.weight"/>:</b><bean:write name="curSection" property="weight" />
                            </td>
                        </tr>
                        <tr>
                            <td class="SectionHeader" nowrap width="86%"><bean:message key="global.label.question"/></td>
                            <td class="SectionHeader" style="width: 8%"><bean:message key="global.label.type"/></td>
                            <td class="SectionHeader" width="2%"><p style="text-align: right"><bean:message key="global.label.weight"/></td>
                            <td class="SectionHeader" width="1%" nowrap><bean:message key="global.label.document_upload"/></td>
                        </tr>
                        <% 
                            String shortQ = "";
                            String longQ = "";
                            String jslnkQ = "";
                        %>
                        <logic:iterate id="curQuestion" indexId="qIdx" name="curSection" property="allQuestions" type="com.topcoder.management.scorecard.data.Question">
                            <%
                                shortQ = "shortQ" + gIdx + "_" + sIdx + "_" + qIdx;
                                longQ = "longQ" + gIdx + "_" + sIdx + "_" + qIdx;
                                jslnkQ = "javascript:toggleDisplay('" + shortQ + "');toggleDisplay('" + longQ + "');";
                            %>
                            <tr>
                                <td class="forumTextOdd" width="86%">
                                    <div class="showText" id="<%= shortQ %>">
                                        <a href="<%= jslnkQ %>" >
                                            <img src="images/plus.gif" alt="open" border="0" />
                                        </a>
                                        <b>Question <%= gIdx.intValue() + 1 %>.<%= sIdx.intValue() + 1 %>.<%= qIdx.intValue() + 1 %></b><br/>
                                        <%= ScorecardActionsHelper.escapeToHTMLString(curQuestion.getDescription()) %>
                                    </div>
                                    <div class="hideText" id="<%= longQ %>">
                                        <a href="<%= jslnkQ %>" >
                                            <img src="images/minus.gif" alt="close" border="0" />
                                        </a>
                                        <b>Question <%= gIdx.intValue() + 1 %>.<%= sIdx.intValue() + 1 %>.<%= qIdx.intValue() + 1 %></b><br/>
                                        <%= ScorecardActionsHelper.escapeToHTMLString(curQuestion.getDescription()) %>
                                        <p/>
                                        <%= ScorecardActionsHelper.escapeToHTMLString(curQuestion.getGuideline()) %>
                                    </div>
                                </td>
                                <td class="forumTextOdd" style="width: 8%" nowrap>
                                    <bean:write name="curQuestion" property="questionType.name" />
                                </td>
                                <td class="forumTextOdd" width="2%">
                                    <bean:write name="curQuestion" property="weight" />
                                </td>
                                <td class="forumTextOdd" width="1%">
                                    <bean:write name="curQuestion" property="documentUploadValue"/>
                                </td>
                            </tr>
                        </logic:iterate>
                    </logic:iterate>
                </logic:iterate>
                <tr>
                    <td class="forumHeadFoot" colspan="4" height="5">
                        <img src="images/clear.gif" alt="" width="10" height="5" border="0" />
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
<form action="javascript:history.back()"> 
    <p align="center">
        <html:submit style="width:125px; float:center;" styleClass="Buttons">
            <bean:message key="global.button.back"/>
        </html:submit>
   
    </p>
 </form>