function updateLink() {
}

function deleteLink(obj) {
    var tr = obj.parentNode.parentNode;
    var cell = tr.cells[0];
    var projectId = cell.getElementsByTagName("input")[0].value;
    var projectName = cell.getElementsByTagName("input")[1].value;
    var table = tr.parentNode;
    table.removeChild(tr);
    refreshTable();

    var form = document.getElementById("projectLinksForm");
    var newTargetProjectIdSelect = form.newTargetProjectIdSelect;
    var option = document.createElement("option");
    option.value = projectId;
    option.appendChild(document.createTextNode(projectName));
    var neighbour = null;
    var len = newTargetProjectIdSelect.options.length;
    for (var i = 1; i < len; i++) {
        if (newTargetProjectIdSelect.options[i].text > projectName ) {
            neighbour = newTargetProjectIdSelect.options[i];
            break;
        }
    }

    newTargetProjectIdSelect.insertBefore(option, neighbour);
}


function addLink() {
    var form = document.getElementById("projectLinksForm");
    var newTargetProjectIdSelect = form.newTargetProjectIdSelect;
    var newLinkTypeIdSelect = form.newLinkTypeId;
    var newTargetProjectId = form.newTargetProjectId;

    if (newTargetProjectIdSelect.selectedIndex == 0) {
        alert('Please, select the target project');
    } else if (newLinkTypeIdSelect.selectedIndex == 0) {
        alert('Please, select the link type');
    } else if (projectIdsConsistent(newTargetProjectId, newTargetProjectIdSelect) <= 0) {
        alert('There is no project with such ID');
        newTargetProjectId.focus();
    } else {
        var table = document.getElementById("existLinks");
        var row = table.insertRow(table.rows.length - 1);

        var cell1 = row.insertCell(0);
        cell1.className = 'value';
        cell1.noWrap = 'nowrap';
        cell1.appendChild(document.createTextNode(newTargetProjectIdSelect.options[newTargetProjectIdSelect.selectedIndex].text));
        var hidden = document.createElement("input");
        hidden.type = 'hidden';
        hidden.name = 'targetProjectId';
        hidden.value = newTargetProjectIdSelect.options[newTargetProjectIdSelect.selectedIndex].value;
        cell1.appendChild(hidden);

        hidden = document.createElement("input");
        hidden.type = 'hidden';
        hidden.name = 'targetProjectName';
        hidden.value = newTargetProjectIdSelect.options[newTargetProjectIdSelect.selectedIndex].text;
        cell1.appendChild(hidden);
        newTargetProjectIdSelect.remove(newTargetProjectIdSelect.selectedIndex);

        var cell2 = row.insertCell(1);
        cell2.className = 'value';
        cell2.noWrap = 'nowrap';
        var newSelect = $('#newLinkTypeId').clone();
        newSelect[0].setAttribute("name", "linkTypeId");
        newSelect[0].removeAttribute("id");
        newSelect[0].selectedIndex = newLinkTypeIdSelect.selectedIndex;
        cell2.appendChild(newSelect[0]);

        var cell3 = row.insertCell(2);
        cell3.className = 'value';
        cell3.noWrap = 'nowrap';
        var img = new Image();
        img.border = '0';
        img.alt = 'delete';
        img.src = '/i/or/bttn_delete.gif';
        img.onclick = new Function('deleteLink(this);');
        cell3.appendChild(img);
        
        refreshTable();
    }
}

function projectIdsConsistent(input, select) {
    var len = select.options.length;
    for (var i = 1; i < len; i++) {
        if (select.options[i].value == input.value) {
            return i;
        }
    }
    return -1;
}

function refreshTable() {
    $("#existLinks tr").each(function(i) {
        $(this).removeClass('dark');
        $(this).removeClass('light');
        $(this).addClass(i % 2 == 0 ? 'dark' : 'light');
    })
}

function changeProject(obj) {
    $(obj).parent().prev().children()[0].value = obj.value;
}

function changeProjectById(obj) {
    var form = document.getElementById("projectLinksForm");
    var newTargetProjectIdSelect = form.newTargetProjectIdSelect;
    var newTargetProjectId = form.newTargetProjectId;
    var index = projectIdsConsistent(newTargetProjectId, newTargetProjectIdSelect);
    if (index <= 0) {
        alert('There is no project with such ID');
        newTargetProjectId.focus();
    } else {
        newTargetProjectIdSelect.selectedIndex = index;
    }
}

function createRelatedLink() {

}

function validate_form(obj, flag) {
    return false;
}
