
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
