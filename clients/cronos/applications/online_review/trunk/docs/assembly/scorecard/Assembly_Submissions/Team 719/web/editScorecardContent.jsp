<%-- 
   (#) editScorecardContent.jsp
   ------------------------------------------------------------------
   @copyright Copyright (C) 2006, TopCoder Inc. All Rights Reserved.
   @author albertwang, flying2hk
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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<script>
var projectCategoryNames = <%=ScorecardActionsHelper.getInstance().generateProjectCategoriesJSArray()%>;
var EPS = 1e-9;
addEvent(window, "load", refreshProjectCategories);
addEvent(window, "load", locateGroupCountInput);
/*
 * Add event.
 */
function addEvent(elm, evType, fn, useCapture)
{
    if (elm.addEventListener)
    {
        elm.addEventListener(evType, fn, useCapture);
        return true;
    }
    else if (elm.attachEvent)
    {
        var r = elm.attachEvent("on" + evType, fn);
        return r;
    }
}

/*
 * Input holds the count of groups.
 */
var groupCountInput;
/*
 * Locate the group count input.
 */
function locateGroupCountInput()
{
    var elements = document.getElementsByTagName("input");
    for (var i = 0; i < elements.length; i++)
    {
        if (elements[i].name == "scorecard.count")
        {
            groupCountInput = elements[i];
            break;
        }
    }
}
    /*
 * Refresh project categories.
 */
function refreshProjectCategories()
{
    var projTypeIndex = document.getElementById("tdProjectTypeSelect")
        .getElementsByTagName("select")[0].selectedIndex;
    var sel = document.getElementById("tdProjectCategorySelect")
        .getElementsByTagName("select")[0];
    var oldVal = sel.value;
    var len = sel.length;

    for (var i = len - 1; i >= 0; i--)
    {
        sel.remove(i);
    }
    for (var i = 0; i < projectCategoryNames[projTypeIndex].length; i++)
    {
        var opt = new Option(projectCategoryNames[projTypeIndex][i],
            projectCategoryNames[projTypeIndex][i]);
        if (oldVal == opt.value)
        {
            opt.selected = "selected";
        }
        var pre = null;
        if (i > 0)
        {
            pre = sel.options[i - 1];
        }
        try
        {
            sel.add(opt, pre); // standards compliant; doesn't work in IE
        }
        catch (ex)
        {
            sel.add(opt); // IE only
        }
    }
}

function checkNumber(input)
{
    var value = input.value;
    if (isNaN(value))
    {
        input.value = "0.0";
    }
    else if (value < EPS || value > 100+EPS)
    {
        input.value = "0.0";
    }
}
    /*
 * Add a group.
 */
function addGroup(btn)
{
    var btnRow = btn.parentNode.parentNode;
    var table = btnRow.parentNode;
    var curGroupNumber = getGroupNumber(btnRow);
    // find the sections total row for current group
    var sectionsTotalRow = document.getElementById("allGroups[" +
        curGroupNumber + "].sectionTotal").parentNode.parentNode;
    // append a sections total row
    var newSectionsTotalRow = sectionsTotalRow.cloneNode(true);
    table.insertBefore(newSectionsTotalRow, sectionsTotalRow);
    table.insertBefore(sectionsTotalRow, newSectionsTotalRow);
    // append a questions total row
    var newQuestionsTotalRow = table.rows[sectionsTotalRow.rowIndex -
        1].cloneNode(true);
    table.insertBefore(newQuestionsTotalRow, newSectionsTotalRow);
    // append a question row
    var newQuestionRow = table.rows[btnRow.rowIndex + 3].cloneNode(true);
    table.insertBefore(newQuestionRow, newQuestionsTotalRow);
    newQuestionRow.getElementsByTagName("textarea")[0].value =
        "Description goes here.";
    newQuestionRow.getElementsByTagName("textarea")[1].value =
        "Guideline goes here.";
    newQuestionRow.getElementsByTagName("select")[0].selectedIndex = 0;
    newQuestionRow.getElementsByTagName("select")[1].selectedIndex = 0;
    newQuestionRow.getElementsByTagName("input")[0].value = "100.00";
    // append a question header row
    var newQuestionHeaderRow = table.rows[btnRow.rowIndex + 2].cloneNode(true);
    table.insertBefore(newQuestionHeaderRow, newQuestionRow);
    // append a section header row
    var newSectionHeaderRow = table.rows[btnRow.rowIndex + 1].cloneNode(true);
    table.insertBefore(newSectionHeaderRow, newQuestionHeaderRow);
    newSectionHeaderRow.getElementsByTagName("input")[0].value =
        "Section name goes here.";
    newSectionHeaderRow.getElementsByTagName("input")[1].value = "100.00";
    // append a group header row
    var newGroupHeaderRow = btnRow.cloneNode(true);
    table.insertBefore(newGroupHeaderRow, newSectionHeaderRow);
    newGroupHeaderRow.getElementsByTagName("input")[0].value =
        "Group name goes here.";
    newGroupHeaderRow.getElementsByTagName("input")[1].value = "100.00";
    // update new group's section count
    newGroupHeaderRow.cells[1].getElementsByTagName("input")[1].value = "1";
    // update new section's question count
    newSectionHeaderRow.cells[1].getElementsByTagName("input")[1].value = "1";
    // update group numbers
    curGroupNumber++;
    setGroupNumber(newGroupHeaderRow, curGroupNumber);
    curGroupNumber++;
    var cur = findNextGroupRow(newGroupHeaderRow);
    while (cur != null)
    {
        setGroupNumber(cur, curGroupNumber);
        curGroupNumber++;
        cur = findNextGroupRow(cur);
    }
    // update group count
    var groupCount = parseInt(groupCountInput.value);
    groupCount++;
    groupCountInput.value = groupCount;
}

/*
 * Remove a group.
 */
function removeGroup(btn)
{
    var btnRow = btn.parentNode.parentNode;
    var table = btnRow.parentNode;
    var btnRowGroupNumber = getGroupNumber(btnRow);
    var curGroupNumber = btnRowGroupNumber;
    curRow = findNextGroupRow(btnRow);
    while (curRow != null)
    {
        setGroupNumber(curRow, curGroupNumber);
        curGroupNumber++;
        curRow = findNextGroupRow(curRow);
    }
    if (curGroupNumber == btnRowGroupNumber && curGroupNumber == 0)
    {
        // the last group, cannot remove
        // NOP
    }
    else
    {
        // update group count
        var groupCount = parseInt(groupCountInput.value);
        groupCount--;
        groupCountInput.value = groupCount;
        // remove sections/questions under this group
        curRow = table.rows[btnRow.rowIndex + 1];
        while (curRow != null && curRow.cells[0].className != "forumTitle")
        {
            table.removeChild(curRow);
            curRow = table.rows[btnRow.rowIndex + 1];
        }
        // remove group row
        table.removeChild(btnRow);
    }
}

/*
 * Get the group number.
 */
function getGroupNumber(row)
{
    var temp;
    var elements = row.cells[0].getElementsByTagName("input");
    if (elements.length > 0)
    {
        temp = elements[0].name;
    }
    else
    {
        temp = row.cells[0].getElementsByTagName("textarea")[0].name;
    }
    temp = temp.substring(temp.indexOf("allGroups["));
    temp = temp.substring(temp.indexOf("[") + 1, temp.indexOf("]"));
    return parseInt(temp);
}
    /*
 * Set the group number.
 */
function setGroupNumber(row, number)
{
    var curGNumber = getGroupNumber(row);
    var afterRowIndex = row.rowIndex;
    var elements = document.getElementsByTagName("input");
    for (var i = 0; i < elements.length; i++)
    {
        if (elements[i].parentNode.parentNode.rowIndex >= afterRowIndex)
        {
            var name = elements[i].name;
            if (name != null && name.indexOf('allGroups[' + curGNumber + ']') >= 0)
            {
                name = name.replace('allGroups[' + curGNumber + ']', 'allGroups[' + number + ']');
                elements[i].name = name;
            }
            else
            {
                var id = elements[i].id;
                if (id.indexOf('allGroups[' + curGNumber + ']') >= 0)
                {
                    id = id.replace('allGroups[' + curGNumber + ']', 'allGroups[' + number + ']');
                    elements[i].id = id;
                }
            }
        }
    }
    elements = document.getElementsByTagName("textarea");
    for (var i = 0; i < elements.length; i++)
    {
        if (elements[i].parentNode.parentNode.rowIndex >= afterRowIndex)
        {
            var name = elements[i].name;
            if (name.indexOf('allGroups[' + curGNumber + ']') >= 0)
            {
                name = name.replace('allGroups[' + curGNumber + ']', 'allGroups[' + number + ']');
                elements[i].name = name;
            }
        }
    }
    elements = document.getElementsByTagName("select");
    for (var i = 0; i < elements.length; i++)
    {
        if (elements[i].parentNode.parentNode.rowIndex >= afterRowIndex)
        {
            var name = elements[i].name;
            if (name.indexOf('allGroups[' + curGNumber + ']') >= 0)
            {
                name = name.replace('allGroups[' + curGNumber + ']',  'allGroups[' + number + ']');
                elements[i].name = name;
            }
        }
    }
}

/*
 * Find the next Group row from current group row.
 */
function findNextGroupRow(curGroupRow)
{
    var table = curGroupRow.parentNode;
    var curRow = table.rows[curGroupRow.rowIndex + 1];

    while (curRow != null && curRow.cells[0].className != "forumTitle")
    {
        curRow = table.rows[curRow.rowIndex + 1];
    }
    if (curRow != null && curRow.cells[0].className == "forumTitle" &&
        curRow.cells.length == 3)
    {
        return curRow;
    }
    else
    {
        return null;
    }
}

/*
 * Add a section.
 */
function addSection(btn)
{
    var btnRow = btn.parentNode.parentNode;
    var table = btnRow.parentNode;
    var curRow = btnRow;
    var qCountText = btnRow.cells[1].getElementsByTagName("input")[1];
    var qCount = parseInt(qCountText.value);
    for (var i = 0; i < qCount + 2; i++)
    {
        curRow = table.rows[curRow.rowIndex + 1];
    }
    // append question weight subtotal row
    var newSubtotalRow = curRow.cloneNode(true);
    table.insertBefore(newSubtotalRow, curRow);
    table.insertBefore(curRow, newSubtotalRow);
    curRow = newSubtotalRow;
    // append one question
    var newQuestionRow = table.rows[btnRow.rowIndex + 2].cloneNode(true);
    newQuestionRow.getElementsByTagName("textarea")[0].value =
        "Description goes here.";
    newQuestionRow.getElementsByTagName("textarea")[1].value =
        "Guideline goes here.";
    newQuestionRow.getElementsByTagName("select")[0].selectedIndex = 0;
    newQuestionRow.getElementsByTagName("select")[1].selectedIndex = 0;
    newQuestionRow.getElementsByTagName("input")[0].value = "100.00";
    table.insertBefore(newQuestionRow, curRow);
    curRow = newQuestionRow;
    // append question header row
    var newQuestionHeaderRow = table.rows[btnRow.rowIndex + 1].cloneNode(true);
    table.insertBefore(newQuestionHeaderRow, curRow);
    curRow = newQuestionHeaderRow;
    // append section header row
    var newSectionRow = btnRow.cloneNode(true);
    newSectionRow.getElementsByTagName("input")[0].value =
        "Section name goes here.";
    newSectionRow.getElementsByTagName("input")[1].value = "100.00";
    table.insertBefore(newSectionRow, curRow);
    // update question count for the new section
    qCountText = newSectionRow.cells[1].getElementsByTagName("input")[1];
    qCountText.value = "1";
    // update section numbers
    var sectionNumber = getSectionNumber(btnRow);
    sectionNumber++;
    setSectionNumber(newSectionRow, sectionNumber);

    var groupNumber = getGroupNumber(newSectionRow);
    // calculate section's question subtotal
    calculateSubtotal(groupNumber, sectionNumber);
    calculateSectionSubtotal(groupNumber);

    sectionNumber++;
    var nextSectionRow = findNextSectionRow(newSectionRow);
    while (nextSectionRow != null)
    {
        setSectionNumber(nextSectionRow, sectionNumber);
        sectionNumber++;
        nextSectionRow = findNextSectionRow(nextSectionRow);
    }

    // update section count
    // find the group row
    curRow = btnRow;
    while (curRow.cells[0].className != "forumTitle")
    {
        curRow = table.rows[curRow.rowIndex - 1];
    }
    var sCountText = curRow.cells[1].getElementsByTagName("input")[1];
    var sCount = sCountText.value;
    sCount++;
    sCountText.value = sCount;
}
    
    /*
 * Find the next Section row from current section row.
 */
function findNextSectionRow(curSectionRow)
{
    var table = curSectionRow.parentNode;
    var qCountText = curSectionRow.cells[1].getElementsByTagName("input")[1];
    var qCount = parseInt(qCountText.value);
    var curRow = curSectionRow;
    for (var i = 0; i < qCount + 2; i++)
    {
        curRow = table.rows[curRow.rowIndex + 1];
    }
    curRow = table.rows[curRow.rowIndex + 1];
    if (curRow.cells.length == 3 && curRow.cells[0].className ==
        "forumTextEven" && curRow.cells[1].getElementsByTagName("input").length
        > 0)
    {
        return curRow;
    }
    else
    {
        return null;
    }
}

/*
 * Remove a section.
 */
function removeSection(btn)
{
    var btnRow = btn.parentNode.parentNode;
    var table = btnRow.parentNode;
    var btnRowSectionNumber = getSectionNumber(btnRow);
    var curSectionNumber = btnRowSectionNumber;
    curRow = findNextSectionRow(btnRow);
    while (curRow != null)
    {
        setSectionNumber(curRow, curSectionNumber);
        curSectionNumber++;
        curRow = findNextSectionRow(curRow);
    }
    if (curSectionNumber == btnRowSectionNumber && curSectionNumber == 0)
    {
        // the last section, cannot remove
        // NOP
    }
    else
    {
        // find the group row
        curRow = btnRow;
        while (curRow.cells[0].className != "forumTitle")
        {
            curRow = table.rows[curRow.rowIndex - 1];
        }
        var sCountText = curRow.cells[1].getElementsByTagName("input")[1];
        var sCount = sCountText.value;
        sCount--;
        sCountText.value = sCount;

        // remove questions under this section
        curRow = table.rows[btnRow.rowIndex + 1];
        while (curRow.cells.length == 5)
        {
            table.removeChild(curRow);
            curRow = table.rows[btnRow.rowIndex + 1];
        }
        // remove subtotal row
        table.removeChild(table.rows[btnRow.rowIndex + 1]);

        var groupNumber = getGroupNumber(btnRow);
        // remove section row
        table.removeChild(btnRow);

        // calculate section subtotal
        calculateSectionSubtotal(groupNumber);
    }
}

/*
 * Get the section number.
 */
function getSectionNumber(row)
{
    var temp;
    var elements = row.cells[0].getElementsByTagName("input");
    if (elements.length > 0)
    {
        temp = elements[0].name;
    }
    else
    {
        temp = row.cells[0].getElementsByTagName("textarea")[0].name;
    }
    temp = temp.substring(temp.indexOf("allSections["));
    temp = temp.substring(temp.indexOf("[") + 1, temp.indexOf("]"));
    return parseInt(temp);
}

/*
 * Set the section number in the given row and its questions.
 */
function setSectionNumber(row, number)
{
    var curNumber = getSectionNumber(row);
    var curGNumber = getGroupNumber(row);
    var afterRowIndex = row.rowIndex;
    var elements = document.getElementsByTagName("input");
    for (var i = 0; i < elements.length; i++)
    {
        if (elements[i].parentNode.parentNode.rowIndex >= afterRowIndex)
        {
            var name = elements[i].name;

            if (name != null && name.indexOf('allGroups[' + curGNumber + '].allSections[' + curNumber + ']') >= 0)
            {
                name = name.replace('allSections[' + curNumber + ']', 'allSections[' + number + ']');
                elements[i].name = name;
            }
            else
            {
                var id = elements[i].id;
                if (id.indexOf('allGroups[' + curGNumber + '].allSections[' +
                    curNumber + ']') >= 0)
                {
                    id = id.replace('allGroups[' + curGNumber + '].allSections[' + curNumber + ']', 'allGroups[' + curGNumber + '].allSections[' + number + ']');
                    elements[i].id = id;
                }
            }
        }
    }
    elements = document.getElementsByTagName("textarea");
    for (var i = 0; i < elements.length; i++)
    {
        if (elements[i].parentNode.parentNode.rowIndex >= afterRowIndex)
        {
            var name = elements[i].name;
            if (name.indexOf('allGroups[' + curGNumber + '].allSections[' +
                curNumber + ']') >= 0)
            {
                name = name.replace('allSections[' + curNumber + ']', 'allSections[' + number + ']');
                elements[i].name = name;
            }
        }
    }
    elements = document.getElementsByTagName("select");
    for (var i = 0; i < elements.length; i++)
    {
        if (elements[i].parentNode.parentNode.rowIndex >= afterRowIndex)
        {
            var name = elements[i].name;
            if (name.indexOf('allGroups[' + curGNumber + '].allSections[' +
                curNumber + ']') >= 0)
            {
                name = name.replace('allSections[' + curNumber + ']', 'allSections[' + number + ']');
                elements[i].name = name;
            }
        }
    }
}
    /*
 * Add a question.
 */
function addQuestion(btn)
{
    var btnRow = btn.parentNode.parentNode;
    var table = btnRow.parentNode;
    var curRow = btnRow;
    var qCountText = table.rows[btnRow.rowIndex -
        1].cells[1].getElementsByTagName("input")[1];
    var qCount = qCountText.value;

    while (curRow != null && curRow.cells.length == 5)
    {
        curRow = table.rows[curRow.rowIndex + 1];
    }
    var lastQuestionRow = table.rows[curRow.rowIndex - 1];
    // copy a new question row
    var newRow = lastQuestionRow.cloneNode(true);

    newRow.getElementsByTagName("textarea")[0].value = "Description goes here.";
    newRow.getElementsByTagName("textarea")[1].value = "Guideline goes here.";
    newRow.getElementsByTagName("select")[0].selectedIndex = 0;
    newRow.getElementsByTagName("select")[1].selectedIndex = 0;
    newRow.getElementsByTagName("input")[0].value = "100.00";
    // insert the new row
    table.insertBefore(newRow, lastQuestionRow);
    table.insertBefore(lastQuestionRow, newRow);
    // increase the question number
    var curNumber = getQuestionNumber(lastQuestionRow);
    curNumber++;
    setQuestionNumber(newRow, curNumber);
    calculateSubtotal(getGroupNumber(newRow), getSectionNumber(newRow));
    qCount++;
    qCountText.value = qCount;
}

/*
 * Remove a question
 */
function removeQuestion(btn)
{
    var btnRow = btn.parentNode.parentNode;
    var table = btnRow.parentNode;
    var curRow = table.rows[btnRow.rowIndex + 1];
    var btnRowQuestionNumber = getQuestionNumber(btnRow);
    var curQuestionNumber = btnRowQuestionNumber;
    while (curRow.cells.length == 5)
    {
        setQuestionNumber(curRow, curQuestionNumber);
        curQuestionNumber++;
        curRow = table.rows[curRow.rowIndex + 1];
    }
    if (curQuestionNumber == btnRowQuestionNumber && curQuestionNumber == 0)
    {
        // the last question, cannot remove
        // NOP
    }
    else
    {
        // find the section row
        curRow = btnRow;
        while (curRow.cells.length == 5)
        {
            curRow = table.rows[curRow.rowIndex - 1];
        }
        var qCountText = curRow.cells[1].getElementsByTagName("input")[1];
        var qCount = qCountText.value;
        qCount--;
        var groupNumber = getGroupNumber(btnRow);
        var sectionNumber = getSectionNumber(btnRow);
        qCountText.value = qCount;
        table.removeChild(btnRow);
        calculateSubtotal(groupNumber, sectionNumber);
    }
}

/*
 * Get the question number in the given row.
 */
function getQuestionNumber(row)
{
    var qTypeSel = row.cells[1].getElementsByTagName("select")[0];
    var temp = qTypeSel.name;
    temp = temp.replace( /[^\d^\[]/g, ''); // [0[1[1
    return temp.substr(temp.lastIndexOf('[') + 1);
}
    /*
 * Set the question number in the given row.
 */
function setQuestionNumber(row, number)
{
    // Cell for Question Text & Guideline
    var cell = row.cells[0];
    // Question text
    var cur = cell.getElementsByTagName("textarea")[0];
    var name = cur.name;
    name = name.replace(/allQuestions(\[\d+\])/ , 'allQuestions[' + number + ']');
    cur.name = name;
    // Question Guideline
    cur = cell.getElementsByTagName("textarea")[1];
    name = cur.name;
    name = name.replace(/allQuestions(\[\d+\])/ , 'allQuestions[' + number + ']');
    cur.name = name;
    // Cell for Question Type
    cell = row.cells[1];
    // Question type
    cur = cell.getElementsByTagName("select")[0];
    name = cur.name;
    name = name.replace(/allQuestions(\[\d+\])/ , 'allQuestions[' + number + ']');
    cur.name = name;
    // Cell for Weight
    cell = row.cells[2];
    // Weight
    cur = cell.getElementsByTagName("input")[0];
    name = cur.name;
    name = name.replace(/allQuestions(\[\d+\])/ , 'allQuestions[' + number + ']');
    cur.name = name;
    // Cell for Document Upload
    cell = row.cells[3];
    // Document Upload
    cur = cell.getElementsByTagName("select")[0];
    name = cur.name;
    name = name.replace(/allQuestions(\[\d+\])/ , 'allQuestions[' + number + ']');
    cur.name = name;
}

/*
 * This will be fired when a question weight is changed, it updates the subtotal of
 * the section to which the question belongs.
 */
function updateQuestionsTotal(input)
{
    var sectionNumber = getSectionNumber(input.parentNode.parentNode);
    var groupNumber = getGroupNumber(input.parentNode.parentNode);
    calculateSubtotal(groupNumber, sectionNumber);
}

/*
 * Calculate and update the subtotal of given section.
 */
function calculateSubtotal(groupNumber, sectionNumber)
{
    var elements = document.getElementsByTagName("input");
    var total = 0;
    for (var i = 0; i < elements.length; i++)
    {
        var name = elements[i].name;
        if (name != null && name.indexOf("scorecard.allGroups[" + groupNumber +
            "].allSections[" + sectionNumber + "].allQuestions[") >= 0 &&
            name.indexOf("weight") >= 0)
        {
            total = total + parseInt(elements[i].value);
        }
    }
    var questionSubtotalInput = document.getElementById("allGroups[" +
        groupNumber + "].allSections[" + sectionNumber + "].questionTotal");
    questionSubtotalInput.value = total;
}

/*
 * This will be fired when a section weight is changed, it updates the subtotal of
 * the group to which the section belongs.
 */
function updateSectionsTotal(input)
{
    var groupNumber = getGroupNumber(input.parentNode.parentNode);
    calculateSectionSubtotal(groupNumber);
}

/*
 * Calculate and update the subtotal of given group
 */
function calculateSectionSubtotal(groupNumber)
{
    var elements = document.getElementsByTagName("input");
    var total = 0;
    for (var i = 0; i < elements.length; i++)
    {
        var name = elements[i].name;
        if (name != null && name.indexOf("scorecard.allGroups[" + groupNumber +
            "].allSections[") >= 0 && name.indexOf("weight") >= 0 &&
            name.indexOf("allQuestions[") < 0)
        {
            total = total + parseInt(elements[i].value);
        }
    }
    var sectionSubtotalInput = document.getElementById("allGroups[" +
        groupNumber + "].sectionTotal");
    sectionSubtotalInput.value = total;
}
</script>
<%
    // cache some lookups
    pageContext.setAttribute("projectTypeNames", ScorecardActionsHelper.getInstance().getProjectTypeNames());
    pageContext.setAttribute("projectCategoryNames", ScorecardActionsHelper.getInstance().getProjectCategoryNames());
    pageContext.setAttribute("scorecardTypeNames", ScorecardActionsHelper.getInstance().getScorecardTypeNames());
    pageContext.setAttribute("scorecardStatusNames", ScorecardActionsHelper.getInstance().getScorecardStatusNames());
    pageContext.setAttribute("questionTypeNames", ScorecardActionsHelper.getInstance().getQuestionTypeNames());
    
%>
<html:form action="/scorecardAdmin.do?actionName=saveScorecard">
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
                        <html:hidden property="scorecard.count" />
                    </td>
                    <td class="forumTextOdd" style="width: 40%" nowrap>
                        <logic:equal value="true" name="scorecardForm" property="scorecardNameEditable">
                            <html:text property="scorecard.name" style="width:300px" styleClass="inputBox" size="1" disabled="false" maxlength="<%= String.valueOf(Constants.NAME_MAXLENGTH) %>"/>
                        </logic:equal>
                        <logic:equal value="false" name="scorecardForm" property="scorecardNameEditable">
                            <html:text property="scorecard.name" style="width:300px" styleClass="inputBox" size="1" disabled="true" maxlength="<%= String.valueOf(Constants.NAME_MAXLENGTH) %>"/>
                        </logic:equal>
                        <logic:equal value="true" name="scorecardForm" property="scorecardVersionEditable">
                            <html:text property="scorecard.version" style="width:50; height:16" styleClass="inputBox" size="1" disabled="false" maxlength="<%= String.valueOf(Constants.VERSION_MAXLENGTH) %>"/>
                        </logic:equal>
                        <logic:equal value="false" name="scorecardForm" property="scorecardVersionEditable">
                            <html:text property="scorecard.version" style="width:50; height:16" styleClass="inputBox" size="1" disabled="true" maxlength="<%= String.valueOf(Constants.VERSION_MAXLENGTH) %>"/>
                        </logic:equal>
                    </td>
                    <td class="forumTextOdd" style="width: 17%" id="tdProjectTypeSelect">
                        <html:select property="projectTypeName" onchange="refreshProjectCategories();">
                            <html:options name="projectTypeNames" labelName="projectTypeNames" />
                        </html:select>
                    </td>
                    <td class="forumTextOdd" style="width: 17%" id="tdProjectCategorySelect">
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
                        <html:text property="minScoreText" style="width:30px; height:16px" styleClass="inputBox" size="20" onchange="checkNumber(this);" />
                    </td>
                    <td class="forumTextOdd"  width="6%">
                        <html:text property="maxScoreText" style="width:30px; height:16px" styleClass="inputBox" size="20" onchange="checkNumber(this);" />
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
                    <% float sectionSubtotal = 0; %>
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
                            <html:text property='<%= "scorecard.allGroups[" + gIdx + "].name"%>' style="width:300px;margin-left:8px;" styleClass="inputBox" size="20" maxlength="<%= String.valueOf(Constants.NAME_MAXLENGTH) %>"/>&nbsp;
                            <strong><bean:message key="global.label.weight"/>&nbsp;</strong> 
                            <html:text property='<%= "scorecard.allGroups[" + gIdx + "].weight"%>' style="width:50px" styleClass="inputBox" size="20" onchange="checkNumber(this);" />
                        </td>
                        <td class="forumTitle">
                            <html:submit title='<%= "btnAddGroup[" + gIdx + "]"%>' onclick="addGroup(this); return false;" styleClass="Buttons" style="float:right">
                                <bean:message key="editScorecard.button.add" />
                            </html:submit>
                            <html:hidden property='<%= "scorecard.allGroups[" + gIdx + "].count" %>'/>
                        </td>
                        <td class="forumTitle">
                            <html:submit onclick="removeGroup(this); return false;" styleClass="Buttons2">
                                <bean:message key="editScorecard.button.remove" />
                            </html:submit>
                            
                        </td>
                    </tr>
                    <logic:iterate id="curSection" indexId="sIdx" name="curGroup" property="allSections" type="com.topcoder.management.scorecard.data.Section">
                        <% float subtotal = 0; %>
                        <% sectionSubtotal += curSection.getWeight(); %>
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
                                <html:text property='<%= "scorecard.allGroups[" + gIdx + "].allSections[" + sIdx + "].name" %>' style="width:300px" styleClass="inputBox" size="20" maxlength="<%= String.valueOf(Constants.NAME_MAXLENGTH) %>"/>&nbsp;
                                <strong><bean:message key="global.label.weight"/>&nbsp;</strong> 
                                <html:text property='<%= "scorecard.allGroups[" + gIdx + "].allSections[" + sIdx + "].weight" %>' style="width:50px" styleClass="inputBox" size="20" onchange="checkNumber(this);updateSectionsTotal(this);" />
                            </td>  
                            <td class="forumTitle">
                                
                                <html:submit title='<%= "btnAddSection[" + gIdx + "][" + sIdx + "]"%>' onclick="addSection(this);return false;" styleClass="Buttons" style="float:right">
                                    <bean:message key="editScorecard.button.add" />
                                </html:submit>
                                <html:hidden property='<%= "scorecard.allGroups[" + gIdx + "].allSections[" + sIdx + "].count" %>'/>
                            </td>
                            <td class="forumTitle">
                                <html:submit onclick="removeSection(this);return false;" styleClass="Buttons2">
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
                                <html:submit title='<%= "btnAddGroup[" + gIdx + "][" + sIdx + "]"%>' onclick="addQuestion(this);return false;" styleClass="Buttons">
                                    <bean:message key="editScorecard.button.add" />
                                </html:submit>
                            </td>
                        </tr>
                        <logic:iterate id="curQuestion" indexId="qIdx" name="curSection" property="allQuestions" type="com.topcoder.management.scorecard.data.Question">
                            <% subtotal += curQuestion.getWeight(); %>
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
                                    <html:textarea property='<%= "scorecard.allGroups[" + gIdx + "].allSections[" + sIdx + "].allQuestions[" + qIdx + "].description" %>' rows="20" cols="20" styleClass="inputBoxQuestion"  />
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
                                    <html:text property='<%= "scorecard.allGroups[" + gIdx + "].allSections[" + sIdx + "].allQuestions[" + qIdx + "].weight" %>' onchange="checkNumber(this);updateQuestionsTotal(this);" style="width:30px; height:16px" styleClass="inputBox" size="20" />
                                </td>
                                <td class="forumTextOdd" nowrap width="12%" >
                                    <html:select property='<%= "scorecard.allGroups[" + gIdx + "].allSections[" + sIdx + "].allQuestions[" + qIdx + "].documentUploadValue" %>' styleClass="inputBox" style="margin-right:3px;margin-left:5px;" size="1">
                                        <html:option value="N/A">N/A</html:option>
                                        <html:option value="Required">Required</html:option>
                                        <html:option value="Optional">Optional</html:option>
                                    </html:select>
                                </td>
                                <td class="forumTextOdd" width="4%" >
                                    <html:submit onclick="removeQuestion(this);return false;" styleClass="Buttons2">
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
                                <input type="text" id='<%= "allGroups[" + gIdx + "].allSections[" + sIdx + "].questionTotal" %>'  value="<%= subtotal %>" style="width:30px; height:16px" name="T139"  class="inputBox" size="20" disabled />
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
                            <input type="text" id='<%= "allGroups[" + gIdx + "].sectionTotal" %>' value='<%= sectionSubtotal %>' style="width:30px; height:16px" class="inputBox" size="20" disabled/>
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
                <html:submit style="width:125px; float:center;" styleClass="Buttons2">
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