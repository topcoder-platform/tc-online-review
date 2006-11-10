// JavaScript Document

function img_act(v) {
	document[v].src = eval(v + "on.src");
}
function img_inact(v) {
	document[v].src = eval(v + "off.src");
}

tab1off = new Image(119, 36);
tab1off.src = "images/tab_my_open_projects.gif";
tab1on = new Image(119, 36);
tab1on.src = "images/tab_my_open_projects_o.gif";

tab2off = new Image(119, 36);
tab2off.src = "images/tab_all_open_projects.gif";
tab2on = new Image(119, 36);
tab2on.src = "images/tab_all_open_projects_o.gif";

tab3off = new Image(119, 36);
tab3off.src = "images/tab_create_project.gif";
tab3on = new Image(119, 36);
tab3on.src = "images/tab_create_project_o.gif";

tab4off = new Image(119, 36);
tab4off.src = "images/tab_inactive_projects.gif";
tab4on = new Image(119, 36);
tab4on.src = "images/tab_inactive_projects_o.gif";



function showLayer (layername) 
{
document.getElementById(layername).style.display = 'block';
}

function hideLayer (layername) 
{
document.getElementById(layername).style.display = 'none';
}


function swapLayer (layername1,layername2) 
{
document.getElementById(layername1).style.display = 'none';
document.getElementById(layername2).style.display = 'block';
}


function toggleDisplay(objectID){
   var object = document.getElementById(objectID);
   if(object.className == 'showText') object.className = 'hideText';
   else object.className = 'showText';
   return;
}
function showAll(){
   var x = document.getElementsByTagName('div');
   for (var i=0;i<x.length;i++){
      if (x[i].id.substring (0,5) == 'longQ'){
         x[i].className = 'showText';
      }else if(x[i].id.substring (0,6) == 'shortQ'){
         x[i].className = 'hideText';
      }
   }
}
function hideAll(){
   var x = document.getElementsByTagName('div');
   for (var i=0;i<x.length;i++){
      if (x[i].id.substring (0,5) == 'longQ'){
         x[i].className = 'hideText';
      }else if(x[i].id.substring (0,6) == 'shortQ'){
         x[i].className = 'showText';
      }
   }
}


