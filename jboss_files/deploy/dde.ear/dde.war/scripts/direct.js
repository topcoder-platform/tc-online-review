
// General init document for scripting purpose, it is
// called when the document has been completely loaded 
// by browser

initDocument = function() {
	initTabsPanel();
	
	window.scrollTo(0,0);
}

/* -----------------------
 * TABS PANEL
 * --------------------- */
 
// tabs based Panel initialization
var arrTabsButton = [];
var arrTabsContent = [];

initTabsPanel = function() {
	if ( typeof tabsBar != 'undefined' || typeof tabsBar != "undefined" || typeof(defaultTab) != "undefined" ) {
		if ( typeof tabsBar != 'undefined' && tabsBar.length > 0) {
			if ( typeof defaultTab == 'number' || typeof defaultTab == 'undefined' ) {
				if ( typeof defaultTab == 'undefined' )
					defaultTab = 0;
					
					btn = getTabsButton();
					if ( btn ) container = getTabsContainer();
					if ( container ) 
						initTabs();
					else
						warn('Tabs Error');
			} else {
				warn("Default tabs are not correctly defined!");
			}
		} else {
			warn("Tabs are not correctly installed!");
		}
	}
}

function getTabsButton() {
	// Look for each tabs element to see any UL element inside them
	result = false;
	for (i=0; i<tabsBar.length; i++) {
		navRoot = getElement(tabsBar[i]);
		if ( navRoot ) {
			for (i=0; i<navRoot.childNodes.length; i++) {
				node = navRoot.childNodes[i];
				if (node.nodeName=="UL") {
					
					for (j=0; j<node.childNodes.length; j++) {
						if ( node.childNodes[j].nodeName == "LI" ) {
							liNode = node.childNodes[j];
							arrTabsButton.push(liNode);
						}
					}
				}
			}
			if ( arrTabsButton.length > 0 ) result = true;
		} else {
			warn("Get Tabs button error");
		}
	}
	return result;
}

function getTabsContainer() {
	// Look for each tabs element to see any UL element inside them
	result = false;
	for (i=0; i<tabsPanel.length; i++) {
		navRoot = getElement(tabsPanel[i]);
		if ( navRoot ) {
			for (i=0; i<navRoot.childNodes.length; i++) {
				node = navRoot.childNodes[i];
				
				if ( (node.nodeName == "DIV" ) && ( hasClassName(node, "tabs_content" ) ) ) {
					arrTabsContent.push(node);
				}
			}
			if ( arrTabsContent.length > 0 ) result = true;
		} else {
			warn("Get Tabs panel content error");
		}
	}
	return result;
}

function initTabs() {
	result = false;
	if ( ( arrTabsButton.length ) > 0 && ( arrTabsContent.length > 0 ) ) {
		// check whether counted tabs element are correct
		if ( arrTabsButton.length == arrTabsContent.length ) {
			
			// init default tab content to be displayed
			for (i=0; i<arrTabsButton.length; i++) {
				arrTabsButton[i].className = arrTabsButton[i].className.replace(" on", "");
			}
			arrTabsButton[defaultTab].className += " on";
			
			// init default tab content to be displayed
			for (i=0; i<arrTabsContent.length; i++) {
				arrTabsContent[i].style.display = "none";
			}
			arrTabsContent[defaultTab].style.display = "block";
			
			// init each tab button mouse click
			for (i=0; i<arrTabsButton.length; i++) {
				
				arrTabsButton[i].onclick = function(e) {
					var self = this;
					var target =0;
					
					//reset tab active class, and display the active
					counter = 0;
					for (j=0; j<arrTabsButton.length; j++) {
						arrTabsButton[j].className = arrTabsButton[j].className.replace(" on", "");
					}
					self.className += " on";
					
					//reset content display
					for (j=0; j<arrTabsContent.length; j++) {
						arrTabsContent[j].style.display = "none"
					}
					idx = getIndex(self, arrTabsButton);
					arrTabsContent[idx].style.display = "block";
					self.blur();
					
					return stopDefault(e);
				}
			}
			
			result = true;
		} else {
			warn("Tabs Error! The amount of Tabs Button and Tabs Container isn't same!");
		}
	}
	return result;
}

function jumpTabs(thisTabNo, targetTabsNo) {
	//reset tab active class, and display the target as active
	arrTabsButton[thisTabNo].className = arrTabsButton[thisTabNo].className.replace(" on", "");
	arrTabsButton[targetTabsNo].className += " on";
	
	//reset current content display, and display the target content
	arrTabsContent[thisTabNo].style.display = "none";
	arrTabsContent[targetTabsNo].style.display = "block";
	
	// move scrollbar to top left (so page showing on top)
	window.scrollTo(0,0);
}


/* ------------------------------------------
Generic function
-------------------------------------------- */

function getIndex(elm, arrElm) {
	for (i=0; i<arrElm.length; i++) {
		if ( elm == arrElm[i] )
			return i;
	}
	return false;
}

function pageHeight() {
	return document.body.scrollHeight;
}

function pageWidth() {
	return document.body.scrollWidth;
}

// Stop default click event
// From: Pro JS Technique, by John Resign, Published by Apress
function stopDefault( e ) {
	// Prevent the default browser action (W3C)
	if ( e && e.preventDefault ) {
		e.preventDefault();
	}
	// A shortcut for stoping the browser action in IE
	else
		window.event.returnValue = false;
	return false;
}

function warn(msg) {
	alert("Warning! Please report to the adminsitrator:\n\n" + msg);
}

/* DOM function */
function getElement(elem)
{
	if (elem && typeof elem == "string")
		return document.getElementById(elem);
	return elem;
}

function hasClassName(elm, className)
{
	if (!elm || !className || !elm.className || elm.className.search(new RegExp("\\b" + className + "\\b")) == -1)
		return false;
	return true;
}

function changeElement(elem1, elem2)
{
	elem1.style.display = "none";
	elem2.style.display = "inline";
	
	if ( elem1.nodeName == "INPUT" )
	{
		elem2.focus();
	}
	return false;
}

