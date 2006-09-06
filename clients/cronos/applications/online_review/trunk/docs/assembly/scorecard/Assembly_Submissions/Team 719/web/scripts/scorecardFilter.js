var CHECKBOX_CELL_IDX = 2;
var STATUS_TEXT = ["", "active", "inactive"];
function refreshFilter() {
  var tbl = document.getElementById("scorecardTable");
  var sel = document.getElementById("filterSelect");
  var filterVal = sel.options[sel.selectedIndex].value;
  var rows = tbl.getElementsByTagName("tr");
  var displayIdx = 0;
    var count = 0;
  var scorecardCategory = "";
  for (var i = 1; i < rows.length - 1; i++) {
    var row = rows[i];
    var cells = row.getElementsByTagName("td");
    if (cells.length <= CHECKBOX_CELL_IDX) {
      displayIdx = 0;
            if (count == 0) {
                 row.style.display = "";
                 cells[0].innerText = "There are currently no " + STATUS_TEXT[filterVal] + " " + scorecardCategory + " Scorecards in the System.";  
            } else {
                 row.style.display = "none";
            }
            count = 0;
      continue;
    }
    var cb = cells[CHECKBOX_CELL_IDX].getElementsByTagName("input");
    
    if (cb.length != 1 || cb[0].type != "checkbox") {
      displayIdx = 0;
      scorecardCategory = cells[0].firstChild.innerText;
            count = 0;
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
      count++;
    }
    else {
      // hide
      row.style.display = "none";
    }
  }
}
