<%--
  - Author: TCSASSEMBLER
  - Version: 2.0
  - Copyright (C) 2013-2014 TopCoder Inc., All Rights Reserved.
  -
  - Description: This page fragment displays the form input elements group for editing the project prizes.
--%>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="or" uri="/or-tags" %>

<div class="projectDetails">
    <div class="projectDetails__sectionHeader">
        <div class="projectDetails__title">
            Prizes
        </div>
    </div>
            <!-- The tabs -->
        <div class="projectDetails__tabs">
            <div class='projectDetails__tab projectDetails__tab--active'>
                <a href="javascript:void(0)" onclick="return showPrizeTab('contest-prizes-table', this);">
                    <or:text key="editProject.ProjectDetails.ContestPrizes" />
                </a>
            </div>
            <div class='projectDetails__tab'>
                <a href="javascript:void(0)" onclick="return showPrizeTab('checkpoint-prizes-table', this);">
                    <or:text key="editProject.ProjectDetails.CheckpointPrizes" />
                </a>
            </div>
        </div>
        <div style="display: none">
            <ul id="tablist">
                <li class="current"><a id="contest-prizes-link" onclick="return showPrizeTab('contest-prizes-table', this);" href="javascript:void(0)"><or:text key="editProject.ProjectDetails.ContestPrizes" /></a></li>
                <li><a id="checkpoint-prizes-link" onclick="return showPrizeTab('checkpoint-prizes-table', this);" href="javascript:void(0)"><or:text key="editProject.ProjectDetails.CheckpointPrizes" /></a></li>
            </ul>
            <div style="clear:both;"></div>
        </div>
            <div class="projectDetails__sectionBody">


<c:set var="typeId" value="${projectForm.map['project_type']}"/>
<c:set var="contestPrizesLength" value="${fn:length(projectForm.map['contest_prizes_amount'])}"/>
<table id="contest-prizes-table" class="editPrizeTable" width="100%" cellpadding="0" cellspacing="0" style="border-collapse:collapse;">
<tbody class="editPrizeTable__body">
    <tr style="display: none;">
        <td class="title" colspan="4"><or:text key="editProject.ProjectDetails.ContestPrizes" /></td>
    </tr>
    <tr class="editPrizeTable__header">
        <td class="header" width="32%"><or:text key="editProject.ProjectDetails.Prize.Place" /></td>
        <td class="header"><or:text key="editProject.ProjectDetails.Prize.Amount" /></td>
        <td class="header" colspan="2"><or:text key="editProject.ProjectDetails.Prize.NoOfPrizes" /></td>
    </tr>
    <tr style="display:none;">
        <td class="value"></td>
        <td class="value">
            <label>${"$"}</label>
            <input class="edit__inputBoxDuration" type="text" name="contest_prizes_amount_dump"/>
            <div name="prize_amount_validation_msg" style="display: none" class="error"></div>
        </td>
        <td class="value">
            <div class="selectCustom">
                <select class="inputBox" <c:if test="${not newProject and typeId ne 3}">disabled="disabled" </c:if> name="contest_prizes_num_dump">
                    <c:forEach begin="1" end="10" var="index">
                        <option value="${index}">${index}</option>
                    </c:forEach>
                </select>
            </div>
        </td>
        <td class="center">
            <a href="#" class="deletePayment" onclick="removePrize(this);return false;"><or:text key='editProject.Prizes.DeletePrize.alt' /></a>
        </td>
    </tr>
    <c:if test="${contestPrizesLength > 0}">
        <c:forEach begin="0" end="${contestPrizesLength - 1}" var="prizeIdx" varStatus="vs">
            <tr <c:if test="${vs.index % 2 eq 0}">class="light"</c:if> <c:if test="${vs.index % 2 eq 1}">class="dark"</c:if> >
                <td class="value">${prizeIdx + 1}</td>
                <td class="value">
                    <label>${"$"}</label>
                    <input type="text" class="edit__inputBoxDuration" name="contest_prizes_amount[${prizeIdx}]" <c:if test="${not canEditContestPrize}">disabled="disabled"</c:if> value="<or:fieldvalue field='contest_prizes_amount[${prizeIdx}]' />" />
                    <div name="prize_amount_validation_msg" style="display: none" class="error"></div>
                    <div class="error"><s:fielderror escape="false"><s:param>contest_prizes_amount[${prizeIdx}]</s:param></s:fielderror></div>
                </td>
                <td class="value">
                <div class="selectCustom">
                    <select class="inputBox" name="contest_prizes_num[${prizeIdx}]" <c:if test="${not canEditContestPrize or typeId ne 3}">disabled="disabled"</c:if>><c:set var="OR_FIELD_TO_SELECT" value="contest_prizes_num[${prizeIdx}]"/>
                        <c:forEach begin="1" end="10" var="index">
                            <option  value="${index}"  <or:selected value="${index}"/>>${index}</option>
                        </c:forEach>
                    </select>
                    </div>
                    <div class="error"><s:fielderror escape="false"><s:param>contest_prizes_num[${prizeIdx}]</s:param></s:fielderror></div>
                </td>
                <td class="center">
                    <c:if test="${prizeIdx eq contestPrizesLength - 1 and canEditContestPrize}">
                        <a href="#" class="deletePayment" onclick="removePrize(this);return false;"><or:text key='editProject.Prizes.DeletePrize.alt' /></a>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
    </c:if>
    <c:if test="${canEditContestPrize}">
        <tr <c:if test="${contestPrizesLength % 2 eq 0}">class="light"</c:if><c:if test="${contestPrizesLength % 2 eq 1}">class="dark"</c:if> >
            <td class="value" colspan="4">
            <a href="#" class="addPayment" onclick="addPrize(this, 'contest_prizes');return false;"><or:text key='editProject.Prizes.AddPrize.alt' /></a>
        </td>
        </tr>
    </c:if>
    <tr style="display: none;">
        <td class="lastRowTD" colspan="4"><!-- @ --></td>
    </tr>
    </tbody>
</table>
<c:set var="checkpointPrizesLength" value="${fn:length(projectForm.map['checkpoint_prizes_amount'])}" />
<table id="checkpoint-prizes-table" class="editPrizeTable" width="100%" cellpadding="0" cellspacing="0" style="border-collapse:collapse;display:none;">
<tbody class="editPrizeTable__body">
    <tr style="display: none;">
        <td class="title" colspan="4"><or:text key="editProject.ProjectDetails.CheckpointPrizes" /></td>
    </tr>
    <tr class="editPrizeTable__header">
        <td class="header" width="32%"><or:text key="editProject.ProjectDetails.Prize.Place" /></td>
        <td class="header"><or:text key="editProject.ProjectDetails.Prize.Amount" /></td>
        <td class="header" colspan="2"><or:text key="editProject.ProjectDetails.Prize.NoOfPrizes" /></td>
    </tr>
    <tr style="display:none;">
        <td class="value"></td>
        <td class="value">
            <label>${"$"}</label>
            <input class="edit__inputBoxDuration" type="text" name="checkpoint_prizes_amount_dump"/>
            <div name="prize_amount_validation_msg" style="display: none" class="error"></div>
        </td>
        <td class="value">
        <div class="selectCustom">
            <select class="inputBox" name="checkpoint_prizes_num_dump">
                <c:forEach begin="1" end="10" var="index">
                    <option value="${index}">${index}</option>
                </c:forEach>
            </select>
            </div>
        </td>
        <td class="center">
            <a class="deletePayment" href="#" onclick="removePrize(this);return false;"><or:text key='editProject.Prizes.DeletePrize.alt' /></a>
        </td>
    </tr>
    <c:if test="${checkpointPrizesLength > 0}">
        <c:forEach begin="0" end="${checkpointPrizesLength - 1}" var="prizeIdx" varStatus="vs">
            <tr <c:if test="${vs.index % 2 eq 0}">class="light"</c:if> <c:if test="${vs.index % 2 eq 1}">class="dark"</c:if> >
                <td class="value">${prizeIdx + 1}</td>
                <td class="value">
                    <label>${"$"}</label>
                    <input type="text" class="edit__inputBoxDuration" name="checkpoint_prizes_amount[${prizeIdx}]" <c:if test="${not canEditCheckpointPrize}">disabled="disabled"</c:if> value="<or:fieldvalue field='checkpoint_prizes_amount[${prizeIdx}]' />" />
                    <div class="error"><s:fielderror escape="false"><s:param>checkpoint_prizes_amount[${prizeIdx}]</s:param></s:fielderror></div>
                    <div name="prize_amount_validation_msg" style="display: none" class="error"></div>
                </td>
                <td class="value">
                    <div class="selectCustom">
                        <select class="inputBox" name="checkpoint_prizes_num[${prizeIdx}]" <c:if test="${not canEditCheckpointPrize}">disabled="disabled"</c:if> ><c:set var="OR_FIELD_TO_SELECT" value="checkpoint_prizes_num[${prizeIdx}]"/>
                            <c:forEach begin="1" end="10" var="index">
                                <option  value="${index}"  <or:selected value="${index}"/>>${index}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="error"><s:fielderror escape="false"><s:param>checkpoint_prizes_num[${prizeIdx}]</s:param></s:fielderror></div>
                </td>
                <td class="center">
                    <c:if test="${prizeIdx eq checkpointPrizesLength - 1 and canEditCheckpointPrize}">
                        <a class="deletePayment" href="#" onclick="removePrize(this);return false;"><or:text key='editProject.Prizes.DeletePrize.alt' /></a>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
    </c:if>
    <c:if test="${canEditCheckpointPrize}">
        <tr <c:if test="${checkpointPrizesLength % 2 eq 0}">class="light"</c:if><c:if test="${checkpointPrizesLength % 2 eq 1}">class="dark"</c:if> >
            <td class="value" colspan="4">
            <a class="addPayment" href="#" onclick="addPrize(this, 'checkpoint_prizes');return false;"><or:text key='editProject.Prizes.AddPrize.alt' /></a>
        </td>
        </tr>
    </c:if>
    <tr style="display: none;"
        <td class="lastRowTD" colspan="4"><!-- @ --></td>
    </tr>
</tbody>
</table>
</div>
</div>
