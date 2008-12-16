
/*
 * This function returns the first child Element of the specified node, which has the specified name.
 * Note, that the processing is done recursively.
 */
function getChildByName(node, name) {
	for (var element = dojo.dom.firstElement(node); element != null;
			element = dojo.dom.nextElement(element)) {
		// Check if current child has the specified name
		if (element.getAttribute("name") == name) {
			// If yes, return it
			return element;
		} else {
			// Process the element recursively
			var result = getChildByName(element, name);
			if (result != null) {
				// If the element was found in the recursive call, return it
				return result;
			}
		}
	}
	// Null is returned to signal no element found
	return null;
}


/*
 * This function returns all the child Elements of the specified node, which have the specified name.
 * Note, that the processing is done recursively.
 */
function getChildrenByName(node, name) {
	var compoundResult = [];
	for (var element = dojo.dom.firstElement(node); element != null;
			element = dojo.dom.nextElement(element)) {
		// Check if current child has the specified name
		if (element.getAttribute("name") == name) {
			// If yes, append it to compound result
			compoundResult.push(element);
		} else {
			// Process the element recursively
			var result = getChildrenByName(element, name);
			// Append the found elements to compound result
			// TODO: Note that this is not the most efficient way to do it
			compoundResult = compoundResult.concat(result);
		}
	}
	// Return the compound result, empty array is returned if no elements found
	return compoundResult;
}


/*
 * This function returns the first child Element of the specified node, which has the specified name prefix.
 * Note, that the processing is done recursively.
 */
function getChildByNamePrefix(node, namePrefix) {
	for (var element = dojo.dom.firstElement(node); element != null;
			element = dojo.dom.nextElement(element)) {
		// Check if current child has the specified name prefix
		if (element.getAttribute("name") && element.getAttribute("name").indexOf(namePrefix) == 0) {
			// If yes, return it
			return element;
		} else {
			// Process the element recursively
			var result = getChildByNamePrefix(element, namePrefix);
			if (result != null) {
				// If the element was found in the recursive call, return it
				return result;
			}
		}
	}
	// Null is returned to signal no element found
	return null;
}


/*
 * This function returns all the child Elements of the specified node, which have the specified name prefix.
 * Note, that the processing is done recursively.
 */
function getChildrenByNamePrefix(node, namePrefix) {
	var compoundResult = [];
	for (var element = dojo.dom.firstElement(node); element != null;
			element = dojo.dom.nextElement(element)) {
		// Check if current child has the specified name prefix
		if (element.getAttribute("name") && element.getAttribute("name").indexOf(namePrefix) == 0) {
			// If yes, append it to compound result
			compoundResult.push(element);
		} else {
			// Process the element recursively
			var result = getChildrenByNamePrefix(element, namePrefix);
			// Append the found elements to compound result
			// TODO: Note that this is not the most efficient way to do it
			compoundResult = compoundResult.concat(result);
		}
	}
	// Return the compound result, empty array is returned if no elements found
	return compoundResult;
}

/*
 * TODO: Write docs for this function
 */
function patchParamIndex(paramNode, newIndex) {
	if (paramNode.name.indexOf("[") >= 0 ) {
		paramNode.name = paramNode.name.replace(/\[([.0-9])+\]/, "[" + newIndex + "]");
		if (paramNode.outerHTML) {
			var pNode = document.createElement(paramNode.outerHTML.replace(/\[([.0-9])+\]/, "[" + newIndex + "]"));
			while (paramNode.children.length > 0) {
				pNode.appendChild(paramNode.children[0]);
			}
			paramNode.parentNode.replaceChild(pNode, paramNode);
		}
	} else if (paramNode.name.indexOf("(") >= 0 ) {
		paramNode.name = paramNode.name.replace(/\(([.0-9])+\)/, "(" + newIndex + ")");
		if (paramNode.outerHTML) {
			var pNode = document.createElement(paramNode.outerHTML.replace(/\(([.0-9])+\)/, "(" + newIndex + ")"));
			while (paramNode.children.length > 0) {
				pNode.appendChild(paramNode.children[0]);
			}
			paramNode.parentNode.replaceChild(pNode, paramNode);
		}
	}
}

/*
 * TODO: Write docs for this function
 */
function patchAllChildParamIndexes(node, newIndex) {
	var allInputs = node.getElementsByTagName("input");
	for (var i = 0; i < allInputs.length; i++) {
		patchParamIndex(allInputs[i], newIndex);
	}
	var allTextAreas = node.getElementsByTagName("textarea");
	for (var i = 0; i < allTextAreas.length; i++) {
		patchParamIndex(allTextAreas[i], newIndex);
	}
	var allSelects = node.getElementsByTagName("select");
	for (var i = 0; i < allSelects.length; i++) {
		// TODO: Check if the stuff with selectedIndex is really needed
		var selectIndex = allSelects[i].selectedIndex;
		patchParamIndex(allSelects[i], newIndex);
		allSelects[i].selectedIndex = selectIndex;
	}
}

/*
 * TODO: Write docs for this function
 */
function cloneInputRow(rowNode) {
	var clonedNode = rowNode.cloneNode(true);

	var oldSelectNodes = rowNode.getElementsByTagName("select");
	var newSelectNodes = clonedNode.getElementsByTagName("select");
	for (var i = 0; i < oldSelectNodes.length; i++) {
		newSelectNodes[i].value = oldSelectNodes[i].value;
	}

	var oldInputNodes = rowNode.getElementsByTagName("input");
	var newInputNodes = clonedNode.getElementsByTagName("input");
	for (var i = 0; i < oldInputNodes.length; i++) {
		if (oldInputNodes[i].type == "radio") {
			newInputNodes[i].checked = oldInputNodes[i].checked;
			newInputNodes[i].defaultChecked = oldInputNodes[i].checked;
		}
	}

	return clonedNode;
}

/*
 * This function sets the value of the "answer" input with the specified index,
 * to the value composed from values of appropriate "passed_tests" and "all_tests" inputs.
 */
function populateTestCaseAnswer(itemIdx) {
	var answerNode = document.getElementsByName("answer[" + itemIdx + "]")[0];
	var passedNode = getChildByName(answerNode.parentNode, "passed_tests");
	var allNode = getChildByName(answerNode.parentNode, "all_tests");

	answerNode.value = passedNode.value + "/" + allNode.value;
}

/*
 * This function encodes characters used to form HTML/XML/XHTML
 * code replacing them with their corresponding entities
 */
function htmlEncode(textToEncode) {
	var resultingText = "";
	for (var i = 0; i < textToEncode.length; ++i) {
		var ch = textToEncode.charAt(i);
		switch (ch) {
		case '&':
			resultingText += "&amp;";
			break;
		case '<':
			resultingText += "&lt;";
			break;
		case '>':
			resultingText += "&gt;";
			break;
		default:
			resultingText += ch;
		}
	}
	return resultingText;
}

function isWhitespace(ch) {
	return (ch == ' ' || ch == '\t' || ch == '\n' || ch == '\r' || ch == '\xA0');
}

function trimString(textToTrim) {
	var iBegin = 0;
	var iEnd = textToTrim.length;
	
	for (; iBegin < iEnd; ++iBegin)
		if (!isWhitespace(textToTrim.charAt(iBegin)))
			break;
	for (; iEnd > iBegin; --iEnd)
		if (!isWhitespace(textToTrim.charAt(iEnd-1)))
			break;
	if (iBegin == iEnd)
		return "";
	return textToTrim.slice(iBegin, iEnd);
}
