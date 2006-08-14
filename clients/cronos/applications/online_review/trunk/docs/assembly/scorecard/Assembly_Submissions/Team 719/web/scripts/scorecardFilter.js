var CHECKBOX_CELL_IDX = 2;

function refreshFilter() {
	var tbl = document.getElementById("scorecardTable");
	var sel = document.getElementById("filterSelect");
	var filterVal = sel.options[sel.selectedIndex].value;
	var rows = tbl.getElementsByTagName("tr");
	var displayIdx = 0;

	for (var i = 0; i < rows.length; i++) {
		var row = rows[i];
		var cells = row.getElementsByTagName("td");
		if (cells.length <= CHECKBOX_CELL_IDX) {
			displayIdx = 0;
			continue;
		}
		var cb = cells[CHECKBOX_CELL_IDX].getElementsByTagName("input");
		if (cb.length != 1 || cb[0].type != "checkbox") {
			displayIdx = 0;
			continue;
		}
		var mask = (cb[0].checked) ? 1 : 2;
		if (filterVal == mask || filterVal == 0) {
			// show the row
			row.style.display = "";
			// properly color the row
			var className = "forumText" + ((++displayIdx % 2 == 0) ? "Even" : "Odd");
			for (var j = 0; j < cells.length; j++) {
				cells[j].className = className;
			}
		}
		else {
			// hide the row
			row.style.display = "none";
		}
	}
}
