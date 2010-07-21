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
<script type="text/javascript">
	var djConfig = { isDebug: true };
</script>
<script language="JavaScript" type="text/javascript" src="scripts/dojo/dojo.js"></script>
<script type="text/javascript">
	//////////////////////////////////////////////////////////////////
	// Dojo Setup
	//////////////////////////////////////////////////////////////////
	dojo.require("dojo.dnd.*");
	dojo.require("dojo.event.*");
	//////////////////////////////////////////////////////////////////
	// Global Variables
	//////////////////////////////////////////////////////////////////
	// Project category names
	var projectCategoryNames = <%=ScorecardActionsHelper.getInstance().generateProjectCategoriesJSArray()%>;
	// DnD list ID sequence
	var listIDSequence = 0;
	// epsilon
	var EPS = 1e-9;
 	// Input holds the count of groups.
	var groupCountInput;
	//////////////////////////////////////////////////////////////////
	// Misc Routines
	//////////////////////////////////////////////////////////////////
	/*
	 * Check if the value of the input is a number, reset to 0 if not.
	 */
	function checkNumber(input)
	{
    	var value = input.value;
    	if (isNaN(value))
    	{
        	input.value = "0.0";
    	}
    	else if (value < -EPS || value > 100+EPS)
    	{
        	input.value = "0.0";
    	}
	}
	
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
	///////////////////////////////////////////////////////////
	// Drag And Drop Support Functions
	///////////////////////////////////////////////////////////
	/*
	 * Initialize the DnD functionality.
	 */
	function initDnD(){
		// get all UL elements
		var uls = document.getElementsByTagName('ul');
		for (var i = 0; i < uls.length; i++) {
			var dl = uls[i]; 
			makeULReorderable(dl);
		}
		listIDSequence = uls.length;
	}
	
	/*
	 * Reorder the questions.
	 */
	function reorderQuestions(evt) {
			// locate the UL element
			var ul = document.getElementById(evt.dragObject.type);
			// refresh question numbers
			refreshQuestionNumbers(ul);
	}
	
	/*
	 * Make UL reorderable
	 */
	function makeULReorderable(ul) {
			if (ul.id.indexOf('list') != 0) {
				return;
			}
			// create a DropTarget with name dl.id
			var target = new dojo.dnd.HtmlDropTarget(ul, [ul.id]);
			// connect DropTarget.onDrop to function reorderQuestions
			dojo.event.connect(target, 'onDrop', 'reorderQuestions');
			
			// get all LI's under the UL
			var lis = ul.getElementsByTagName('li');
			for(var x = 0; x< lis.length; x++){
				// create a DragSource and assign the target
				new dojo.dnd.HtmlDragSource(lis[x], ul.id);
			}
	}
	
	
	///////////////////////////////////////////////////////
	// Group / Section / Question Management Functions
	///////////////////////////////////////////////////////
	/*
	 * Add a group.
	 */
	function addGroup(btn) {
			var btnRow = btn.parentNode.parentNode;
			var mainTable = btnRow.parentNode;
			var groupNumber = getGroupNumber(btnRow);
			//alert("Groupnumber" + groupNumber);
			// find the sections total row for current group
    	var sectionsTotalRow = document.getElementById("allGroups[" +
        groupNumber + "].sectionTotal").parentNode.parentNode;
      //alert('sectionsTotalRow' + sectionsTotalRow.rowIndex);
      // append a sections total row
    	var sectionsTotalRowClone = sectionsTotalRow.cloneNode(true);
    	sectionsTotalRowClone.getElementsByTagName('input')[0].value = '100.0';
    	mainTable.insertBefore(sectionsTotalRowClone, sectionsTotalRow);
    	mainTable.insertBefore(sectionsTotalRow, sectionsTotalRowClone);
    	// append a new subtotal row
			var subtotalRow = mainTable.rows[btnRow.rowIndex + 4];
			var subtotalRowClone = subtotalRow.cloneNode(true);
			subtotalRowClone.getElementsByTagName('input')[0].value = '100.0';
			mainTable.insertBefore(subtotalRowClone, sectionsTotalRowClone);
			// append an UL row
    	var ulRow = mainTable.rows[btnRow.rowIndex + 3];
    	var ulRowClone = ulRow.cloneNode(true);
    	var ul = ulRowClone.getElementsByTagName('ul')[0];
    	ul.id = 'list' + (listIDSequence++);
    	var lis = ul.getElementsByTagName('li');
    	var lisLength = lis.length;
    	// leave only ONE <LI>
    	while (lisLength > 1) {
    			ul.removeChild(lis[--lisLength]);
    	}
    	makeULReorderable(ul);
    	var li = ul.getElementsByTagName('li')[0];
    	li.getElementsByTagName('textarea')[0].value = 'Description goes here.';
    	li.getElementsByTagName('textarea')[1].value = 'Guideline goes here.';
    	li.getElementsByTagName("select")[0].selectedIndex = 0;
    	li.getElementsByTagName("select")[1].selectedIndex = 0;
    	li.getElementsByTagName("input")[0].value = '100.00';
    	mainTable.insertBefore(ulRowClone, subtotalRowClone);
			
    	// append a questions header row
    	var questionsHeaderRow = mainTable.rows[btnRow.rowIndex + 2];
    	var questionsHeaderRowClone = questionsHeaderRow.cloneNode(true);
    	mainTable.insertBefore(questionsHeaderRowClone, ulRowClone);
    	
    	// append a section header row
    	var sectionHeaderRow = mainTable.rows[btnRow.rowIndex + 1];;
    	var sectionHeaderRowClone = sectionHeaderRow.cloneNode(true);
    	sectionHeaderRowClone.getElementsByTagName('input')[0].value = 'Section name goes here';
    	sectionHeaderRowClone.getElementsByTagName('input')[1].value = '100.0';
    	mainTable.insertBefore(sectionHeaderRowClone, questionsHeaderRowClone);
    	
    	// append a group header row
    	var groupHeaderRowClone = btnRow.cloneNode(true);
    	groupHeaderRowClone.getElementsByTagName('input')[0].value = 'Group name goes here';
    	groupHeaderRowClone.getElementsByTagName('input')[1].value = '100.0';
    	mainTable.insertBefore(groupHeaderRowClone, sectionHeaderRowClone);

    	// Reset question count
    	setQuestionCount(sectionHeaderRowClone, 1);
    	
    	// Reset section count
    	setSectionCount(groupHeaderRowClone, 1);
    	
    	// Reset group numbers
    	var current = groupHeaderRowClone;
    	while (current != null) {
    			// alert(current.rowIndex);
    			//alert(getGroupNumber(current));
    			setGroupNumber(current, ++groupNumber);
    			current = findNextGroupRow(current);
    			
    	}
    	// Increase group count
    	setGroupCount(getGroupCount() + 1);
	}
	
	/*
	 * Remove a group.
	 */
	function removeGroup(btn) {
			var btnRow = btn.parentNode.parentNode;
			var mainTable = btnRow.parentNode;
			var groupNumber = getGroupNumber(btnRow);
			//alert(groupNumber);
			var count = getGroupCount();
			if (count == 1) {
					// only ONE group left, cannot remove
					return;
			} else {
					// Reset group numbers
					var currentGroupNumber = groupNumber;
					var current = findNextGroupRow(btnRow);
					while (current != null) {
							setGroupNumber(current, currentGroupNumber++);
							current = findNextGroupRow(current);
					}
					
					// Remove group
					var sectionsTotalRow = document.getElementById("allGroups[" +
        		groupNumber + "].sectionTotal").parentNode.parentNode;
        	var startIndex = btnRow.rowIndex + 1;
        	var endIndex = sectionsTotalRow.rowIndex;
        	for (var i = startIndex; i <= endIndex; i++) {
        			mainTable.removeChild(mainTable.rows[btnRow.rowIndex + 1]);
        	}
        	mainTable.removeChild(btnRow);
        	// Decrease the group count
        	setGroupCount(count - 1);
        	//alert(getGroupCount());
			}
	}
	
	function getGroupCount() {
			return parseInt(groupCountInput.value);
	}
	
	function setGroupCount(count) {
			groupCountInput.value = count;
	}
	/*
 	 * Locate the next Group row from current group row.
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
	
	function setGroupNumber(row, number) {
			var curGNumber = getGroupNumber(row);
    	var mainTable = row.parentNode;
    	// group-level inputs
    	var elements = row.getElementsByTagName('input');
    	for (var i = 0; i < elements.length; i++) {
    			elements[i].name = elements[i].name.replace('allGroups[' + curGNumber + ']', 'allGroups[' + number + ']');
    	}
    	
      var startIndex;
      var endIndex;
    	// section & question level inputs/textareas/selects
    	var curSectionRow = mainTable.rows[row.rowIndex + 1];
    	while (curSectionRow != null) {
    			startIndex = curSectionRow.rowIndex;
					endIndex = startIndex + 3;
					for (var i = startIndex; i <= endIndex; i++) {
							var current = mainTable.rows[i];
							// TEXTAREA's
							elements = current.getElementsByTagName('textarea');
							for (var j = 0; j < elements.length; j++) {
									elements[j].name = elements[j].name.replace('allGroups[' + curGNumber + ']', 'allGroups[' + number + ']');
									// TODO: FOR DEBUG ONLY
									// elements[j].value = elements[j].name.replace('allGroups[' + curGNumber + ']', 'allGroups[' + number + ']');
							}
							// INPUT's
							elements = current.getElementsByTagName('input');
							for (var j = 0; j < elements.length; j++) {
									if (elements[j].name != null) {
											elements[j].name = elements[j].name.replace('allGroups[' + curGNumber + ']', 'allGroups[' + number + ']');
									}
									if (elements[j].id != null) {
											elements[j].id = elements[j].id.replace('allGroups[' + curGNumber + ']', 'allGroups[' + number + ']');
									}
							}
							// SELECT's
							elements = current.getElementsByTagName('select');
							for (var j = 0; j < elements.length; j++) {
									elements[j].name = elements[j].name.replace('allGroups[' + curGNumber + ']', 'allGroups[' + number + ']');
							}
					}
					curSectionRow = findNextSectionRow(curSectionRow);
    	}
    	var sectionsSubtotalInput = mainTable.rows[endIndex + 1].cells[2].getElementsByTagName('input')[0];
      sectionsSubtotalInput.id = sectionsSubtotalInput.id.replace('allGroups[' + curGNumber + ']', 'allGroups[' + number + ']');
      //alert(curGNumber + '->' + number);
      //sectionsSubtotalInput.value = sectionsSubtotalInput.id.replace('allGroups[' + curGNumber + ']', 'allGroups[' + number + ']');
	}
	/*
	 * Add a section.
	 */
	function addSection(btn) {
			var btnRow = btn.parentNode.parentNode;
			var mainTable = btnRow.parentNode;
			var sectionNumber = getSectionNumber(btnRow);
			var groupHeaderRow = btnRow;
			while (groupHeaderRow.cells[0].className != "forumTitle") {
					groupHeaderRow = mainTable.rows[groupHeaderRow.rowIndex - 1];
			}
			var count = getSectionCount(groupHeaderRow);
			// append a new subtotal row
			var subtotalRow = mainTable.rows[btnRow.rowIndex + 3];
			var subtotalRowClone = subtotalRow.cloneNode(true);
			mainTable.insertBefore(subtotalRowClone, subtotalRow);
    	mainTable.insertBefore(subtotalRow, subtotalRowClone);
    	
    	// append an UL row
    	var ulRow = mainTable.rows[btnRow.rowIndex + 2];
    	var ulRowClone = ulRow.cloneNode(true);
    	var ul = ulRowClone.getElementsByTagName('ul')[0];
    	ul.id = 'list' + (listIDSequence++);
    	var lis = ul.getElementsByTagName('li');
    	var lisLength = lis.length;
    	// leave only ONE <LI>
    	while (lisLength > 1) {
    			ul.removeChild(lis[--lisLength]);
    	}
    	makeULReorderable(ul);
    	var li = ul.getElementsByTagName('li')[0];
    	li.getElementsByTagName('textarea')[0].value = 'Description goes here.';
    	li.getElementsByTagName('textarea')[1].value = 'Guideline goes here.';
    	li.getElementsByTagName("select")[0].selectedIndex = 0;
    	li.getElementsByTagName("select")[1].selectedIndex = 0;
    	li.getElementsByTagName("input")[0].value = '100.00';
    	mainTable.insertBefore(ulRowClone, subtotalRowClone);
    	
    	// append a questions header row
    	var questionsHeaderRow = mainTable.rows[btnRow.rowIndex + 1];
    	var questionsHeaderRowClone = questionsHeaderRow.cloneNode(true);
    	mainTable.insertBefore(questionsHeaderRowClone, ulRowClone);
    	
    	// append a section header row
    	var sectionHeaderRow = btnRow;
    	var sectionHeaderRowClone = sectionHeaderRow.cloneNode(true);
    	sectionHeaderRowClone.getElementsByTagName('input')[0].value = 'Section name goes here';
    	sectionHeaderRowClone.getElementsByTagName('input')[1].value = '100.0';
    	mainTable.insertBefore(sectionHeaderRowClone, questionsHeaderRowClone);

			// Reset section numbers
    	var current = sectionHeaderRowClone;
    	while (current != null) {
    			setSectionNumber(current, ++sectionNumber);
    			current = findNextSectionRow(current);
    	}
    	
    	// Reset question count
    	setQuestionCount(sectionHeaderRowClone, 1);
    	
    	// Increase section count
    	setSectionCount(groupHeaderRow, count + 1);
    	// re-calculate section subtotal
			var groupNumber = getGroupNumber(groupHeaderRow);
			calculateSectionSubtotal(groupNumber);
	}
	
	/*
	 * Remove a section.
	 */
	function removeSection(btn) {
			var sectionHeaderRow = btn.parentNode.parentNode;
			var mainTable = sectionHeaderRow.parentNode;
			var groupHeaderRow = sectionHeaderRow;
			while (groupHeaderRow.cells[0].className != "forumTitle") {
					groupHeaderRow = mainTable.rows[groupHeaderRow.rowIndex - 1];
			}
			var count = getSectionCount(groupHeaderRow);
			if (count == 1) {
					// only ONE section left, cannot remove
					return;	
			} else {
					var sectionNumber = getSectionNumber(sectionHeaderRow);
					// Reset section numbers for next sections
					var current = findNextSectionRow(sectionHeaderRow);
					while (current != null) {
							setSectionNumber(current, sectionNumber++);
							current = findNextSectionRow(current);
					}
					// remove the section
					mainTable.removeChild(mainTable.rows[sectionHeaderRow.rowIndex + 3]);
					mainTable.removeChild(mainTable.rows[sectionHeaderRow.rowIndex + 2]);
					mainTable.removeChild(mainTable.rows[sectionHeaderRow.rowIndex + 1]);
					mainTable.removeChild(sectionHeaderRow);
					// decrease the section count
					setSectionCount(groupHeaderRow, count - 1);
					// re-calculate section subtotal
					var groupNumber = getGroupNumber(groupHeaderRow);
					calculateSectionSubtotal(groupNumber);
			}
	}
	
	/*
	 * Get the section count for a given group.
	 */
	function getSectionCount(groupHeaderRow) {
			var input = groupHeaderRow.cells[1].getElementsByTagName('input')[1];
			return parseInt(input.value);
	}
	
	/*
	 * Set the section count for a given group.
	 */
	function setSectionCount(groupHeaderRow, count) {
			var input = groupHeaderRow.cells[1].getElementsByTagName('input')[1];
			input.value = count;
	}
	/*
	 * Locate the next section row.
	 */
	function findNextSectionRow(current) {
			var mainTable = current.parentNode;
			var next = mainTable.rows[current.rowIndex + 4];
			if (next != null && next.cells.length == 3 && next.cells[0].getElementsByTagName('input').length > 0) {
					return next;
			} else {
					return null;
			}
	}
	
	/*
 	 * Get the section number.
 	 */
	function getSectionNumber(row)
	{
    	var temp;
    	var elements = row.cells[0].getElementsByTagName('input');
    	if (elements.length > 0)
    	{
        	temp = elements[0].name;
    	}
    	else
    	{
        	temp = row.cells[0].getElementsByTagName('textarea')[0].name;
    	}
    	temp = temp.substring(temp.indexOf('allSections['));
    	temp = temp.substring(temp.indexOf('[') + 1, temp.indexOf(']'));
    	return parseInt(temp);
	}
	
	/*
	 * Set the section number
	 */
	function setSectionNumber(row, number) {
			var startIndex = row.rowIndex;
			var endIndex = startIndex + 3;
			var mainTable = row.parentNode;
			for (var i = startIndex; i <= endIndex; i++) {
					var current = mainTable.rows[i];
					// TEXTAREA's
					var elements = current.getElementsByTagName('textarea');
					for (var j = 0; j < elements.length; j++) {
							elements[j].name = elements[j].name.replace(/allSections(\[\d+\])/ , 'allSections[' + number + ']');
							// TODO: FOR DEBUG ONLY
							//elements[j].value = elements[j].name.replace(/allSections(\[\d+\])/ , 'allSections[' + number + ']');
					}
					// INPUT's
					elements = current.getElementsByTagName('input');
					for (var j = 0; j < elements.length; j++) {
							if (elements[j].name != null) {
								elements[j].name = elements[j].name.replace(/allSections(\[\d+\])/ , 'allSections[' + number + ']');
							}
							if (elements[j].id != null) {
									elements[j].id = elements[j].id.replace(/allSections(\[\d+\])/ , 'allSections[' + number + ']');
							}
					}
					// SELECT's
					elements = current.getElementsByTagName('select');
					for (var j = 0; j < elements.length; j++) {
							elements[j].name = elements[j].name.replace(/allSections(\[\d+\])/ , 'allSections[' + number + ']');
					}
					/*
					// UL's
					elements = current.getElementsByTagName('ul');
					for (var j = 0; j < elements.length; j++) {
							elements[j].id = elements[j].id.replace(/allSections(\[\d+\])/ , 'allSections[' + number + ']');
							alert(elements[j].id);
							makeULReorderable(elements[j]);
					}
					*/
			}
	}
	/*
	 * Add a question.
	 */
	function addQuestion(btn) {
			var btnRow = btn.parentNode.parentNode;
			var mainTable = btnRow.parentNode;
			// locate the UL element
			var ul = mainTable.rows[btnRow.rowIndex + 1].cells[0].getElementsByTagName('ul')[0];
			// clone a question <LI> element
			var lis = ul.getElementsByTagName('li');
			var questionClone = lis[lis.length - 1].cloneNode(true);
			questionClone.getElementsByTagName('textarea')[0].value = 'Description goes here.';
    		questionClone.getElementsByTagName('textarea')[1].value = 'Guideline goes here.';
    		questionClone.getElementsByTagName("select")[0].selectedIndex = 0;
    		questionClone.getElementsByTagName("select")[1].selectedIndex = 0;
    		questionClone.getElementsByTagName("input")[0].value = '100.00';
			// set question number
			setQuestionNumber(questionClone, lis.length);
			// create a DragSource and assign the target
			new dojo.dnd.HtmlDragSource(questionClone, ul.id);
			// append to UL
			ul.appendChild(questionClone);
			// increase the question count
			var sectionHeaderRow = mainTable.rows[btnRow.rowIndex - 1];
			setQuestionCount(sectionHeaderRow, getQuestionCount(sectionHeaderRow) + 1);
			// re-calculate the subtotal
			var groupNumber = getGroupNumber(sectionHeaderRow);
			var sectionNumber = getSectionNumber(sectionHeaderRow);
			calculateSubtotal(groupNumber, sectionNumber);
	}
	
	/*
	 * Remove a question.
	 */
	function removeQuestion(btn) {
			var li = btn.parentNode.parentNode.parentNode.parentNode.parentNode;
			var ul = li.parentNode;
			var ulRow = ul.parentNode.parentNode;
			var mainTable = ulRow.parentNode;
			var sectionHeaderRow = mainTable.rows[ulRow.rowIndex - 2];
			var count = getQuestionCount(sectionHeaderRow);
			if (count == 1) {
					// only ONE question left, cannot remove
					return;
			} else {
					// remove the LI element
					ul.removeChild(li);
					// refresh question numbers
					refreshQuestionNumbers(ul);
					// decrease the question count
					setQuestionCount(sectionHeaderRow, count - 1);
					// re-calculate the subtotal
					var groupNumber = getGroupNumber(sectionHeaderRow);
					var sectionNumber = getSectionNumber(sectionHeaderRow);
					calculateSubtotal(groupNumber, sectionNumber);
			}
	}
	
	/*
	 * Refresh question numbers
	 */
	function refreshQuestionNumbers(ul) {
			// reset the question numbers for each LI element
			var lis = ul.getElementsByTagName('li');
			for (var i = 0; i < lis.length; i++) {
					setQuestionNumber(lis[i], i);
			}
	}
	/*
	 * Return the question count for given section.
	 */
	function getQuestionCount(sectionHeaderRow) {
			var input = sectionHeaderRow.cells[1].getElementsByTagName('input')[1];
			return parseInt(input.value);
	}
	
	/*
	 * Set the question count for given section.
	 */
	function setQuestionCount(sectionHeaderRow, count) {
			var input = sectionHeaderRow.cells[1].getElementsByTagName('input')[1];
			input.value = count;
	}
	
	/*
	 * Set the question number.
	 */
	function setQuestionNumber(li, number) {
			// INPUT's
			var elements = li.getElementsByTagName('input');
			for (var i = 0; i < elements.length; i++) {
					elements[i].name = elements[i].name.replace(/allQuestions(\[\d+\])/ , 'allQuestions[' + number + ']');
			}
			// TEXTAREA's
			elements = li.getElementsByTagName('textarea');
			for (var i = 0; i < elements.length; i++) {
					elements[i].name = elements[i].name.replace(/allQuestions(\[\d+\])/ , 'allQuestions[' + number + ']');
					// TODO: FOR DEBUG ONLY
					// elements[i].value = elements[i].name.replace(/allQuestions(\[\d+\])/ , 'allQuestions[' + number + ']');
			}
			// SELECT's
			elements = li.getElementsByTagName('select');
			for (var i = 0; i < elements.length; i++) {
					elements[i].name = elements[i].name.replace(/allQuestions(\[\d+\])/ , 'allQuestions[' + number + ']');
			}
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
	
	// call initDnD while loading the page
	dojo.event.connect(dojo, 'loaded', 'initDnD');
	// call locateGroupCountInput to locate the input element while loading the page
	dojo.event.connect(dojo, 'loaded', 'locateGroupCountInput');
	// call refreshProjectCategories while loading the page
	dojo.event.connect(dojo, 'loaded', 'refreshProjectCategories');
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
               	<% int listIdSequence = 0; %>
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
                        <tr style="background-color:#ffffff" cellpadding="0">
                        	<td colspan="5">
                        		<ul style="margin:0px" id='<%= "list" +  (listIdSequence++)%>'>
                        			<logic:iterate id="curQuestion" indexId="qIdx" name="curSection" property="allQuestions" type="com.topcoder.management.scorecard.data.Question">
                            			<% subtotal += curQuestion.getWeight(); %>
                            			<li style="list-style-type:none;">
                            				<table class="forumBkgd" border="0" cellpadding="0" cellspacing="1">
                            					<logic:messagesPresent property='<%= "scorecard.allGroups[" + gIdx + "].allSections[" + sIdx + "].allQuestions[" + qIdx + "]" %>'>
                                					<tr>
                                    					<td class="errorText" style="height: 100%" colspan="5">
                                        					<html:errors property='<%= "scorecard.allGroups[" + gIdx + "].allSections[" + sIdx + "].allQuestions[" + qIdx + "]" %>'  />
                                    					</td>
                                					</tr>
                            					</logic:messagesPresent>
                            					<tr onMouseOver="javascript:highlightTableRow(this);" style="background-color:#ffffff">
                            						<td class="ForumQuestion" width="62%">
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
                                					<td class="forumTextOdd" width="5%">
                                    					<html:text property='<%= "scorecard.allGroups[" + gIdx + "].allSections[" + sIdx + "].allQuestions[" + qIdx + "].weight" %>' onchange="checkNumber(this);updateQuestionsTotal(this);" style="width:30px; height:16px" styleClass="inputBox" size="20" />
                                					</td>
                                					<td class="forumTextOdd" nowrap width="12%" >
                                    					<html:select property='<%= "scorecard.allGroups[" + gIdx + "].allSections[" + sIdx + "].allQuestions[" + qIdx + "].documentUploadValue" %>' styleClass="inputBox" style="margin-right:3px;margin-left:5px;" size="1">
                                        					<html:option value="N/A">N/A</html:option>
                                        					<html:option value="Required">Required</html:option>
                                        					<html:option value="Optional">Optional</html:option>
                                    					</html:select>
                                					</td>
                                					<td class="forumTextOdd" width="6%" >
                                    					<html:submit onclick="removeQuestion(this);return false;" styleClass="Buttons2">
                                        					<bean:message key="editScorecard.button.remove" />
                                    					</html:submit>
                                					</td>
                                				</tr>
                                			</table>
                                		</li>
                        			</logic:iterate>
                        		</ul>
                        	</td>
                        </tr>
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