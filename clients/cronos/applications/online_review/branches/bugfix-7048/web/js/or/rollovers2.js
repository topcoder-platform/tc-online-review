// JavaScript Document

function img_act(v) {
	document[v].src = eval(v + "on.src");
}
function img_inact(v) {
	document[v].src = eval(v + "off.src");
}

tab1off = new Image(119, 36);
tab1off.src = "/i/or/tab_my_open_projects.gif";
tab1on = new Image(119, 36);
tab1on.src = "/i/or/tab_my_open_projects_o.gif";

tab2off = new Image(119, 36);
tab2off.src = "/i/or/tab_all_open_projects.gif";
tab2on = new Image(119, 36);
tab2on.src = "/i/or/tab_all_open_projects_o.gif";

tab3off = new Image(119, 36);
tab3off.src = "/i/or/tab_create_project.gif";
tab3on = new Image(119, 36);
tab3on.src = "/i/or/tab_create_project_o.gif";

tab4off = new Image(119, 36);
tab4off.src = "/i/or/tab_inactive_projects.gif";
tab4on = new Image(119, 36);
tab4on.src = "/i/or/tab_inactive_projects_o.gif";


function showLayer(layername) {
	document.getElementById(layername).style.display = "block";
}
function hideLayer(layername) {
	document.getElementById(layername).style.display = "none";
}
function swapLayer(layername1, layername2) {
	document.getElementById(layername1).style.display = "none";
	document.getElementById(layername2).style.display = "block";
}

function toggleDisplay(objectID){
	var object = document.getElementById(objectID);
	if (object.className == "showText") {
		object.className = "hideText";
	} else {
		object.className = "showText";
	}
}
function showAll() {
	var x = document.getElementsByTagName("div");
	for (var i=0;i<x.length;i++) {
		if (x[i].id.indexOf("longQ") == 0 || x[i].id.indexOf("longR") == 0) {
			x[i].className = "showText";
		} else if (x[i].id.indexOf("shortQ") == 0) {
			x[i].className = "hideText";
		} else if (x[i].id.indexOf("shortR") == 0 && document.getElementById(x[i].id.replace(/shortR_/, "longR_")) != null) {
			x[i].className = "hideText";
		}
	}
}
function hideAll() {
	var x = document.getElementsByTagName("div");
	for (var i=0;i<x.length;i++) {
		if (x[i].id.indexOf("longQ") == 0 || x[i].id.indexOf("longR") == 0) {
			x[i].className = "hideText";
		} else if (x[i].id.indexOf("shortQ") == 0 || x[i].id.indexOf("shortR") == 0) {
			x[i].className = "showText";
		}
	}
}


