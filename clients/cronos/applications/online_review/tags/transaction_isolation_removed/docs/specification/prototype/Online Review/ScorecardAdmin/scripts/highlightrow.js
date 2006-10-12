// These variables are for saving the original background colors
var previousElements=new Array();
var previousColors=new Array();
var highlightColor='#ffffcc';
function highlightTableRow(tableRow)
{
  var i=0;
  // Restore color of the previously highlighted row
  for (i; i<previousElements.length; i++)
  {
    var myElement=previousElements[i];
    // hack!!! code to support Netscape 6 and 7
    if (previousColors[i]==0)
    {
      if (myElement.style)
      {
        myElement.style.background=0;
      }
    }
    // end hack
    else
    {
      myElement.style["backgroundColor"]=previousColors[i];
    }
    previousElements[i]=previousColors[i]=0;
  }
  // Highlight every cell on the row
  if (tableRow)
  {
    // The first child will be (or should be) a <TD>
    var tableCell=tableRow.firstChild;
    while (tableCell && tableCell.tagName!="TD")
    {
      tableCell=tableCell.nextSibling;
    }

    var i=0;
    // Loop through every sibling (a sibling of a cell should be a cell)
    // We then highlight every siblings
    while (tableCell)
    {
      // Make sure it's actually a cell (a TD)
      if (tableCell.tagName=="TD")
      {
        previousElements[i]=tableCell;
        // If no style has been assigned, assign it, otherwise Netscape will 
        // behave weird.
        if (!tableCell.style)
        {
          tableCell.style={};
        }
        else
        {
          // If, row color already defined, save it so we can restore the color
          // when the cursor is no longer over the row
          if (tableCell.parentNode.style["backgroundColor"])
          {
            previousColors[i]=tableCell.parentNode.style["backgroundColor"];
          }
          else
            previousColors[i]=tableCell.style["backgroundColor"];
        }
        // Assign the highlight color
        tableCell.style["backgroundColor"]=highlightColor;

        // Optional: alter cursor
        tableCell.style.cursor='move';
        i++;
      }
      // Go to the next cell in the row
      tableCell=tableCell.nextSibling;
    }
    tableRow.onMouseOut="highlightTableRow(0);";
  }
}
