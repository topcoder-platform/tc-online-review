function checkallclicked(me, list) {
	if (me.checked == true) {
		for (i = 0; i < list.length; i++) {
			list[i].checked = true;
		}
	}
}

function singleclicked(me, checkall) {
	if (me.checked == false) {
		checkall.checked = false;
	}
}
