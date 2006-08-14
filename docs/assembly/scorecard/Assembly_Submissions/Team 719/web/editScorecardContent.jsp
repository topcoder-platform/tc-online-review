<%-- 
   (#) editScorecardContent.jsp
   ------------------------------------------------------------------
   @copyright Copyright (C) 2006, TopCoder Inc. All Rights Reserved.
   @author TCSDEVELOPER
   @version 1.0
   ------------------------------------------------------------------
   This is the content page of "editScorecard.jsp", it displays the
   details of a given scorecard in editable manner, the user can edit
   specific areas of the scorecard.
   ------------------------------------------------------------------
   @param scorecardForm [Session Attribute]
        ActionForm containing the scorecard information, it should be
        filled by action "viewScorecard"
--%>
<%@ page language="java" %>
<%@ page import="com.cronos.onlinereview.actions.Constants, com.cronos.onlinereview.actions.ScorecardActionsHelper" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<script>
    function set(form, op, gIdx, sIdx, qIdx) {
        form.operation.value = op;
        form.groupIndex.value = gIdx;
        form.sectionIndex.value = sIdx;
        form.questionIndex.value = qIdx;
    }
</script>

<html:form action="/scorecardAdmin.do?actionName=saveScorecard">
<html:hidden property="groupIndex" />
<html:hidden property="sectionIndex" />
<html:hidden property="questionIndex" />
<html:hidden property="operation" />
<%
    // cache some lookups
    pageContext.setAttribute("projectTypeNames", ScorecardActionsHelper.getInstance().getProjectTypeNames());
    pageContext.setAttribute("projectCategoryNames", ScorecardActionsHelper.getInstance().getProjectCategoryNames());
    pageContext.setAttribute("scorecardTypeNames", ScorecardActionsHelper.getInstance().getScorecardTypeNames());
    pageContext.setAttribute("scorecardStatusNames", ScorecardActionsHelper.getInstance().getScorecardStatusNames());
    pageContext.setAttribute("questionTypeNames", ScorecardActionsHelper.getInstance().getQuestionTypeNames());
%>
<table width="100%" border="0" cellpadding="0" cellspacing="1" class="forumBkgd">
    <tr>
        <td class="whiteBkgnd" style="height: 100%">
            <table border="0" cellpadding="0" cellspacing="0" width="100%">
                <tr>
                    <td width="100%" nowrap class="tableHeader">
                        <strong><bean:write name="scorecardForm" property="projectTypeName"/> Scorecards</strong>
                    </td>
                    <td align="right" style="height:35px"></td>
                </tr>
                <tr>
                    <td class="whiteBkgnd" colspan="3">
                        <img src="images/clear.gif" alt="" width="1" height="1" border="0">
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellpadding="0" cellspacing="1" class="forumBkgd">
                <tr>
                    <td class="forumTextEven" colspan="8">
                    <strong><bean:message key="global.message.scorecard_details"/></strong></td>
                </tr>
                <logic:messagesPresent property="scorecard">
                    <tr>
                        <td class="errorText" style="height: 100%" colspan="8">
                            <html:errors property="scorecard"  />
                        </td>
                    </tr>
                </logic:messagesPresent>
                <tr>
                    <td class="forumTitle" width="6%">
                        <bean:message key="global.label.id"/>
                    </td>
                    <td class="forumTitle" style="width: 40%">
                        <bean:message key="global.label.scorecard_name"/>
                    </td>
                    <td class="forumTitle" style="width: 17%">
                        <bean:message key="global.label.project_type"/>
                    </td>
                    <td class="forumTitle" style="width: 17%">
                        <bean:message key="global.label.category"/>
                    </td>
                    <td class="forumTitle" width="7%">
                        <bean:message key="global.label.type"/>
                    </td>
                    <td class="forumTitle" width="6%">
                        <bean:message key="global.label.min_score"/>
                    </td>
                    <td class="forumTitle" width="6%">
                        <bean:message key="global.label.max_score"/>
                    </td>
                    <td class="forumTitle" width="14%">
                        <bean:message key="global.label.status"/>
                    </td>
                </tr>
                <tr>
                    <td class="forumTextOdd" nowrap width="6%">
                        <logic:equal value="-1" name="scorecardForm" property="scorecard.id">
                            <bean:message key="editScorecard.label.id_not_assigned"/>
                        </logic:equal>
                        <logic:notEqual value="-1" name="scorecardForm" property="scorecard.id">
                            <bean:write name="scorecardForm" property="scorecard.id"/>
                        </logic:notEqual>
                    </td>
                    <td class="forumTextOdd" style="width: 40%" nowrap>
                        <logic:equal value="true" name="scorecardForm" property="scorecardNameEditable">
                            <html:text property="scorecard.name" style="width:300px" styleClass="inputBox" size="1" disabled="false"/>
                        </logic:equal>
                        <logic:equal value="false" name="scorecardForm" property="scorecardNameEditable">
                            <html:text property="scorecard.name" style="width:300px" styleClass="inputBox" size="1" disabled="true" />
                        </logic:equal>
                        <logic:equal value="true" name="scorecardForm" property="scorecardVersionEditable">
                            <html:text property="scorecard.version" style="width:50; height:16" styleClass="inputBox" size="1" disabled="false" />
                        </logic:equal>
                        <logic:equal value="false" name="scorecardForm" property="scorecardVersionEditable">
                            <html:text property="scorecard.version" style="width:50; height:16" styleClass="inputBox" size="1" disabled="true" />
                        </logic:equal>
                    </td>
                    <td class="forumTextOdd" style="width: 17%">
                        <html:select property="projectTypeName">
                            <html:options name="projectTypeNames" labelName="projectTypeNames" />
                        </html:select>
                    </td>
                    <td class="forumTextOdd" style="width: 17%">
                        <html:select property="projectCategoryName">
                            <html:options name="projectCategoryNames" labelName="projectCategoryNames" />
                        </html:select>
                    </td>
                    <td class="forumTextOdd"  width="7%">
                        <html:select property="scorecard.scorecardType.name">
                            <html:options name="scorecardTypeNames" labelName="scorecardTypeNames" />
                        </html:select>
                    </td>
                    <td class="forumTextOdd"  width="6%">
                        <html:text property="minScoreText" style="width:30px; height:16px" styleClass="inputBox" size="20" onkeyup="if(isNaN(value))execCommand('undo')" />
                    </td>
                    <td class="forumTextOdd"  width="6%">
                        <html:text property="maxScoreText" style="width:30px; height:16px" styleClass="inputBox" size="20" onkeyup="if(isNaN(value))execCommand('undo')" />
                    </td>
                    <td class="forumTextOdd"  width="14%">
                        <html:select property="scorecard.scorecardStatus.name">
                            <html:options name="scorecardStatusNames" labelName="scorecardStatusNames" />
                        </html:select>
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
                        <img src="images/clear.gif" alt="" width="1" height="5" border="0">
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellpadding="0" cellspacing="1" class="forumBkgd" id="table1" onMouseOut="javascript:highlightTableRow(0);">
                <logic:iterate id="curGroup" indexId="gIdx" name="scorecardForm" property="scorecard.allGroups">
                    <logic:messagesPresent property='<%= "scorecard.allGroups[" + gIdx + "]" %>'>
                        <tr>
                            <td class="errorText" style="height: 100%" colspan="5">
                                <html:errors property='<%= "scorecard.allGroups[" + gIdx + "]" %>'  />
                            </td>
                        </tr>
                    </logic:messagesPresent>
                    <tr>
                        <td class="forumTitle" colspan="3">
                            <bean:message key="global.label.group"/>&nbsp;
                            <html:text property='<%= "scorecard.allGroups[" + gIdx + "].name"%>' style="width:300px;margin-left:8px;" styleClass="inputBox" size="20" />&nbsp;
                            <strong><bean:message key="global.label.weight"/>&nbsp;</strong> 
                            <html:text property='<%= "scorecard.allGroups[" + gIdx + "].weight"%>' style="width:50px" styleClass="inputBox" size="20" onkeyup="if(isNaN(value))execCommand('undo')" />
                        </td>
                        <td class="forumTitle">
                            <% String doAddGroup = "'doAddGroup'"; %>
                            <html:submit onclick='<%= "set(this.form," + doAddGroup + "," + gIdx + ", -1, -1)" %>' styleClass="Buttons" style="float:right">
                                <bean:message key="editScorecard.button.add" />
                            </html:submit>
                        </td>
                        <td class="forumTitle">
                            <% String doRemoveGroup = "'doRemoveGroup'"; %>
                            <html:submit onclick='<%= "set(this.form," + doRemoveGroup + "," + gIdx + ", -1, -1)" %>' styleClass="Buttons2">
                                <bean:message key="editScorecard.button.remove" />
                            </html:submit>
                        </td>
                    </tr>
                    <logic:iterate id="curSection" indexId="sIdx" name="curGroup" property="allSections">
                        <logic:messagesPresent property='<%= "scorecard.allGroups[" + gIdx + "].allSections[" + sIdx + "]" %>'>
                            <tr>
                                <td class="errorText" style="height: 100%" colspan="5">
                                    <html:errors property='<%= "scorecard.allGroups[" + gIdx + "].allSections[" + sIdx + "]" %>'  />
                                </td>
                            </tr>
                        </logic:messagesPresent>
                        
                        <tr>
                            <td class="forumTextEven" colspan="3">
                                <strong><bean:message key="global.label.section"/>&nbsp;</strong>
                                <html:text property='<%= "scorecard.allGroups[" + gIdx + "].allSections[" + sIdx + "].name" %>' style="width:300px" styleClass="inputBox" size="20" />&nbsp;
                                <strong><bean:message key="global.label.weight"/>&nbsp;</strong> 
                                <html:text property='<%= "scorecard.allGroups[" + gIdx + "].allSections[" + sIdx + "].weight" %>' style="width:50px" styleClass="inputBox" size="20" onkeyup="if(isNaN(value))execCommand('undo')" />
                            </td>  
                            <td class="forumTitle">
                                <% String doAddSection = "'doAddSection'"; %>
                                <html:submit onclick='<%= "set(this.form," + doAddSection + "," + gIdx + "," + sIdx + ", -1)" %>' styleClass="Buttons" style="float:right">
                                    <bean:message key="editScorecard.button.add" />
                                </html:submit>
                            </td>
                            <td class="forumTitle">
                                <% String doRemoveSection = "'doRemoveSection'"; %>
                                <html:submit onclick='<%= "set(this.form," + doRemoveSection + "," + gIdx + "," + sIdx + ", -1)" %>' styleClass="Buttons2">
                                    <bean:message key="editScorecard.button.remove" />
                                </html:submit>
                            </td>
                        </tr>
                        <tr>
                            <td class="SectionHeaderQ" width="66%">
                                <bean:message key="global.label.question" />
                            </td>
                            <td class="SectionHeader" width="11%">
                                <bean:message key="global.label.type" />
                            </td>
                            <td class="SectionHeader" width="3%">
                                <bean:message key="global.label.weight" />
                            </td>
                            <td class="SectionHeader" nowrap width="12%" >
                                <bean:message key="global.label.document_upload" />
                            </td>
                            <td class="SectionHeader" width="4%" >
                                <% String doAddQuestion = "'doAddQuestion'"; %>
                                <html:submit onclick='<%= "set(this.form," + doAddQuestion + "," + gIdx + "," + sIdx + ", -1)" %>' styleClass="Buttons">
                                    <bean:message key="editScorecard.button.add" />
                                </html:submit>
                            </td>
                        </tr>
                        <logic:iterate id="curQuestion" indexId="qIdx" name="curSection" property="allQuestions">
                            <logic:messagesPresent property='<%= "scorecard.allGroups[" + gIdx + "].allSections[" + sIdx + "].allQuestions[" + qIdx + "]" %>'>
                                <tr>
                                    <td class="errorText" style="height: 100%" colspan="5">
                                        <html:errors property='<%= "scorecard.allGroups[" + gIdx + "].allSections[" + sIdx + "].allQuestions[" + qIdx + "]" %>'  />
                                    </td>
                                </tr>
                            </logic:messagesPresent>
                            
                            <tr onMouseOver="javascript:highlightTableRow(this);" style="background-color:#ffffff">
                                <td class="ForumQuestion" width="66%">
                                    <bean:message key="global.label.question_text"/>
                                    <br/>
                                    <html:textarea property='<%= "scorecard.allGroups[" + gIdx + "].allSections[" + sIdx + "].allQuestions[" + qIdx + "].description" %>' rows="20" cols="20" styleClass="inputBoxQuestion" />
                                    <br/>
                                    <bean:message key="global.label.question_guideline"/>
                                    <br/>
                                    <html:textarea property='<%= "scorecard.allGroups[" + gIdx + "].allSections[" + sIdx + "].allQuestions[" + qIdx + "].guideline" %>' rows="20" cols="20" styleClass="inputBoxGuideline" />
                                    <br/>
                                </td>
                                <td class="forumTextOdd" width="11%">
                                    <html:select property='<%= "scorecard.allGroups[" + gIdx + "].allSections[" + sIdx + "].allQuestions[" + qIdx + "].questionType.name" %>' style="margin-right:3px;margin-left:5px;" styleClass="inputBox" size="1">
                                        <html:options name="questionTypeNames" labelName="questionTypeNames" />
                                    </html:select>
                                </td>
                                <td class="forumTextOdd" width="3%">
                                    <html:text property='<%= "scorecard.allGroups[" + gIdx + "].allSections[" + sIdx + "].allQuestions[" + qIdx + "].weight" %>' style="width:30px; height:16px" styleClass="inputBox" size="20" onkeyup="if(isNaN(value))execCommand('undo')" />
                                </td>
                                <td class="forumTextOdd" nowrap width="12%" >
                                    <html:select property='<%= "scorecard.allGroups[" + gIdx + "].allSections[" + sIdx + "].allQuestions[" + qIdx + "].documentUploadValue" %>' styleClass="inputBox" style="margin-right:3px;margin-left:5px;" size="1">
                                        <html:option value="N/A">N/A</html:option>
                                        <html:option value="Required">Required</html:option>
                                        <html:option value="Optional">Optional</html:option>
                                    </html:select>
                                </td>
                                <td class="forumTextOdd" width="4%" >
                                    <% String doRemoveQuestion = "'doRemoveQuestion'"; %>
                                    <html:submit onclick='<%= "set(this.form," + doRemoveQuestion + "," + gIdx + "," + sIdx + "," + qIdx + ")" %>' styleClass="Buttons2">
                                        <bean:message key="editScorecard.button.remove" />
                                    </html:submit>
                                </td>
                            </tr>
                        </logic:iterate>
                        <tr>
                            <td class="ForumQuestion" width="66%">&nbsp;</td>
                            <td class="forumTextOdd" width="11%">
                                <p style="text-align: right">
                                    <strong><bean:message key="editScorecard.label.question_subtotal"/></strong>
                                </p>
                            </td>
                            <td class="forumTextOdd" width="19%" colspan="3">
                                <input type="text" style="width:30px; height:16px" name="T139"  class="inputBox" size="20" disabled value="100"/>
                                &nbsp;
                                <bean:message key="editScorecard.message.question_subtotal" />
                            </td>
                        </tr>
                    </logic:iterate>
                    <tr>
                        <td class="forumTextEven" width="66%">&nbsp;</td>
                        <td class="forumTextEven" width="11%">
                            <p style="text-align: right">
                                <strong><bean:message key="editScorecard.label.section_subtotal"/></strong>
                            </p>
                      </td>
                        <td class="forumTextEven" width="19%" colspan="3">
                            <input type="text" style="width:30px; height:16px" name="T141"  class="inputBox" size="20" disabled value="100"/>
                            &nbsp;
                            <bean:message key="editScorecard.message.section_subtotal" />
                        </td>
                    </tr>
                </logic:iterate>
                <tr>
                    <td class="forumHeadFoot" colspan="5" height="5">
                        <img src="images/clear.gif" alt="" width="10" height="5" border="0"/>
                    </td>
                </tr>
            </table>
            <p align="center">
                <html:submit onclick='<%= "set(this.form, 'doFinish', -1, -1, -1)" %>' style="width:125px; float:center;" styleClass="Buttons2">
                    <logic:equal value="true" name="scorecardForm" property="newlyCreated">
                        <bean:message key="editScorecard.button.save_new" />
                    </logic:equal>
                    <logic:equal value="false" name="scorecardForm" property="newlyCreated">
                        <bean:message key="editScorecard.button.save_changes" />
                    </logic:equal>
                </html:submit>
                <br/>
            </p>
        </td>
    </tr>
</table>           
</html:form>                    