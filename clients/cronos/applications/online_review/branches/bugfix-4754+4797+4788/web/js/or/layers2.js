// Title: Tabbed Layers
// Date: 04-05-2005 


function flipLayer (layername) 
{
// turn all the layers off one by one
document.getElementById('Layer1').style.display = 'none';
document.getElementById('Layer2').style.display = 'none';
document.getElementById('Layer3').style.display = 'none';
document.getElementById('Layer3b').style.display = 'none';
document.getElementById('Layer4').style.display = 'none';
document.getElementById('Layer5').style.display = 'none';
document.getElementById('Layer6').style.display = 'none';
//turn the layer you want to show on
document.getElementById(layername).style.display = 'block';
}