var CHECKBOX_CELL_IDX = 2;
var STATUS_TEXT = ["", "active", "inactive"];
function refreshFilter() {
  var tables = document.getElementsByTagName('table');
  for (var i = 0; i < tables.length; i++) {
    var id = tables[i].id;
    if (tables[i].id.indexOf('sortable') == 0) {
        var sel = document.getElementById('filterSelect');
        var filterVal = sel.options[sel.selectedIndex].value;
        var rows = tables[i].tBodies[0].rows;
        var displayIdx = 0;
        var scorecardCategory = id.substring(id.indexOf('_') + 1);
        for (var j = 0; j < rows.length; j++) {
            var row = rows[j];
            var cells = row.cells;
            var cb = cells[CHECKBOX_CELL_IDX].getElementsByTagName('input');
            var mask = (cb[0].checked) ? 1 : 2;
            if (filterVal == mask || filterVal == 0) {
                // show the row
                row.style.display = '';
                // properly color the row
                var className = 'forumText' + ((++displayIdx % 2 == 0) ? 'Even' : 'Odd');
                for (var k = 0; k < cells.length; k++) {
                    cells[k].className = className;
                }
            } else {
                // hide
                row.style.display = 'none';
            }
        }
        if (displayIdx == 0) {
            tables[i].tFoot.rows[0].cells[0].firstChild.nodeValue = "There are currently no " + STATUS_TEXT[filterVal] + " " + scorecardCategory + " Scorecards in the System.";  
            tables[i].tFoot.rows[0].style.display = '';
        } else {
            tables[i].tFoot.rows[0].style.display = 'none';
        }
    }
  }
}