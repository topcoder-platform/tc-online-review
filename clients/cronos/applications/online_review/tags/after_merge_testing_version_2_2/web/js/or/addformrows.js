var counter = [];

function moreFields(readId, writeId)
{
	if (!counter[readId])
	{
		counter[writeId] = 0;
	}
	counter[readId]++;
	var newFields = document.getElementById(readId).cloneNode(true);
	newFields.id = '';
	newFields.style.display = 'block';
	var newField = newFields.childNodes;
	for (var i=0;i<newField.length;i++)
	{
		var theName = newField[i].name
		if (theName)
			newField[i].name = theName + counter[readId];
	}
	var insertHere = document.getElementById(writeId);
	insertHere.parentNode.insertBefore(newFields,insertHere);
}

function init()
{
	moreFields('CandidateTemplate', 'Candidate');	
}
