function expcollHandler(srcElement) {
  var rowId = srcElement.id + "r";
  var iconId = srcElement.id + "i";
  var rowElement = document.getElementById(rowId);
  if (rowElement.style.display == "none") {
    rowElement.style.display = "";
    document.getElementById(iconId).src = "/i/or/minus.gif";
  } else {
    rowElement.style.display = "none";
    document.getElementById(iconId).src = "/i/or/plus.gif";
  }
  if (srcElement.blur) {
  	srcElement.blur();
  }
  return false;
}
