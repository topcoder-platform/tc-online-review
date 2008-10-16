function getDateString(dateString){
    if (dateString==null || dateString.length < 1)
        return '';
        
    var result = null;
    var returnValue = null;
    
    // This is for mm/dd/yyyy
    var expression = /(\d{1,2})\/(\d{1,2})\/(\d{4})/;
    if (result = expression.exec(dateString)){
        return checkDateValues(result[2], result[1], result[3]);
    }

    // This is for mm/dd/yy
    expression = /(\d{1,2})\/(\d{1,2})\/(\d{2})/;
    if (result = expression.exec(dateString)){
        return checkDateValues(result[2], result[1], result[3]);
    }

    // This is for mm/dd
    expression = /(\d{1,2})\/(\d{1,2})/;
    if (result = expression.exec(dateString)){
        return checkDateValues(result[2], result[1], (new Date()).getYear());
    }

    // This is for mm dd yyyy
    expression = /(\d{1,2})[\s]+(\d{1,2})[\s]+(\d{4})/;
    if (result = expression.exec(dateString)){
        return checkDateValues(result[2], result[1], result[3]);
    }

    // This is for mm dd yy
    expression = /(\d{1,2})[\s]+(\d{1,2})[\s]+(\d{2})/;
    if (result = expression.exec(dateString)){
        return checkDateValues(result[2], result[1], result[3]);
    }
        
    // This is for yyyy-mm-dd
    expression = /(\d{4})-+(\d{1,2})-+(\d{1,2})/;
    if (result = expression.exec(dateString)){
        return checkDateValues(result[3], result[2], result[1]);
    }
    
    // This is for mm-dd-yyyy
    expression = /(\d{1,2})-+(\d{1,2})-+(\d{4})/;
    if (result = expression.exec(dateString)){
        return checkDateValues(result[2], result[1], result[3]);
    }

    // This is for mm-dd-yy
    expression = /(\d{1,2})-+(\d{1,2})-+(\d{2})/;
    if (result = expression.exec(dateString)){
        return checkDateValues(result[2], result[1], result[3]);
    }

    // This is for yyyy.mm.dd
    expression = /(\d{4})\.(\d{1,2})\.(\d{1,2})/;
    if (result = expression.exec(dateString)){
        return checkDateValues(result[3], result[2], result[1]);
    }
    
    // This is for mm.dd.yyyy
    expression = /(\d{1,2})\.(\d{1,2})\.(\d{4})/;
    if (result = expression.exec(dateString)){
        return checkDateValues(result[2], result[1], result[3]);
    }

    // This is for mm.dd.yy
    expression = /(\d{1,2})\.(\d{1,2})\.(\d{2})/;
    if (result = expression.exec(dateString)){
        return checkDateValues(result[2], result[1], result[3]);
    }

    // This is for yyyy mm dd
    expression = /(\d{4})[\s]+(\d{1,2})[\s]+(\d{1,2})/;
    if (result = expression.exec(dateString)){
        return checkDateValues(result[3], result[2], result[1]);
    }

    // This is for mmddyyyy
    expression = /(\d{2})(\d{2})(\d{2,4})/;
    if (result = expression.exec(dateString)){
        return checkDateValues(result[2], result[1], result[3]);
    }

    // This is for mmm dd, yyyy
    expression = /([a-zA-Z]{3,})\.?\s+(\d{1,2})(st|nd|rd|th)?\.?,?\s+(\d{4})/;
    if (result = expression.exec(dateString)){
    
        if(result.length == 3)
            return checkDateValues(result[2], getDayIndex(result[1]), result[3]);
        else
            return checkDateValues(result[2], getDayIndex(result[1]), result[4]);
    }
  
    //For string today
    expression = /tod/i;
    if (result = expression.exec(dateString)){
        var date = new Date();
        return checkDateValues(date.getDate(), date.getMonth()+1, date.getFullYear());
    }
    
    //For string tomorrow
    expression = /tom/i;
    if (result = expression.exec(dateString)){
        var date = new Date();
        date.setDate(date.getDate()+1);
        return checkDateValues(date.getDate(), date.getMonth()+1, date.getFullYear());
    }
    
    //For string yesterday
    expression = /yes/i;
    if (result = expression.exec(dateString)){
        var date = new Date();
        date.setDate(date.getDate()-1);
        return checkDateValues(date.getDate(), date.getMonth()+1, date.getFullYear());
    }        

    //For last weekdays
    expression = /last ((mon.*)|(tue.*)|(wed.*)|(thu.*)|(fri.*)|(sat.*)|(sun.*))/i;
    if (result = expression.exec(dateString)){
        var date = new Date();
        var todayWeekDay = date.getDay();
        var requestedWeekDay = getDayOfWeek(result[1]);
        var subtractDays = todayWeekDay-requestedWeekDay;
        if (subtractDays < 0)
            subtractDays+=6;
        date.setDate(date.getDate()-subtractDays);
        return checkDateValues(date.getDate(), date.getMonth()+1, date.getFullYear());
    } 
    //For next weekdays
    expression = /next ((mon.*)|(tue.*)|(wed.*)|(thu.*)|(fri.*)|(sat.*)|(sun.*))/i;
    if (result = expression.exec(dateString)){
        var date = new Date();
        var todayWeekDay = date.getDay();
        var requestedWeekDay = getDayOfWeek(result[1]);
        var addDays = requestedWeekDay-todayWeekDay;
        if (addDays <= 0)
            addDays+=6;
        date.setDate(date.getDate()+addDays+1);
        return checkDateValues(date.getDate(), date.getMonth()+1, date.getFullYear());
    }     
                
    return 'Invalid';    
}

function getDayOfWeek(weekName){
    var expression = /^sun/i;
    if (expression.exec(weekName))
        return 0;
    expression = /^mon/i;
    if (expression.exec(weekName))
        return 1;
    expression = /^tue/i;
    if (expression.exec(weekName))
        return 2;
    expression = /^wed/i;
    if (expression.exec(weekName))
        return 3;
    expression = /^thu/i;
    if (expression.exec(weekName))
        return 4;
    expression = /^fri/i;
    if (expression.exec(weekName))
        return 5;
    expression = /^sat/i;
    if (expression.exec(weekName))
        return 6;
    return 0;
}

function getDayIndex(name){
    var expression = /^jan/i;
    if (expression.exec(name))
        return 1;
    expression = /^feb/i;
    if (expression.exec(name))
        return 2;
    expression = /^mar/i;
    if (expression.exec(name))
        return 3;
    expression = /^apr/i;
    if (expression.exec(name))
        return 4;
    expression = /^may/i;
    if (expression.exec(name))
        return 5;
    expression = /^jun/i;
    if (expression.exec(name))
        return 6;
    expression = /^jul/i;
    if (expression.exec(name))
        return 7;
    expression = /^aug/i;
    if (expression.exec(name))
        return 8;
    expression = /^sep/i;
    if (expression.exec(name))
        return 9;
    expression = /^oct/i;
    if (expression.exec(name))
        return 10;
    expression = /^nov/i;
    if (expression.exec(name))
        return 11;
    expression = /^dec/i;
    if (expression.exec(name))
        return 12;

    return 1;
}

function checkDateValues(day, month, year){
	var intYear = parseInt(year, 10);
	if (intYear >= 0 && intYear <= 99) {
		intYear = intYear + 2000;
	}
    var d = new Date(intYear, month-1, day);
    var returnMonth = ""+(d.getMonth()+1);
    if (d.getMonth()+1 != month || d.getDate()!=day)
        return "Invalid";
    if (d.getMonth()+1 < 10)
        returnMonth = "0"+returnMonth;
    var returnDay = d.getDate();
    if (d.getDate() < 10)
        returnDay = "0"+ returnDay;
    var returnYear = "" + d.getFullYear();
    return returnMonth + "." + returnDay+ "." + returnYear;

}

function getTimeString(timeString, ampmDropDown){
    if (timeString==null || timeString.length < 1)
        return '';
        
    var result = null;
    var returnValue = null;
    
    var ampmflag = false;

    // This is for hh:mm:ss a|p
    var expression = /(\d{1,2}):(\d{1,2}):(\d{1,2})\s?(a|p)?/;
    if (result = expression.exec(timeString)){
        var hours = parseInt(result[1], 10);
        if (result[4]!=null && result[4].match(/^a/i) && hours > 11) {
            ampmflag = true;
        }
        else if (result[4]!=null && result[4].match(/^p/i) && hours < 12) {
            hours+=12;
        }
	if (result[4]!=null && (result[4].match(/^a/i) || result[4].match(/^p/i))) {
		ampmflag = true;
	}        

        return checkTimeValues(hours, result[2], result[3], ampmDropDown, ampmflag);
    }
    
    // This is for hh:mm a|p
    var expression = /(\d{1,2}):(\d{1,2})\s?(a|p)?/;
    if (result = expression.exec(timeString)){
        var hours = parseInt(result[1], 10);
        if (result[3]!=null && result[3].match(/^a/i) && hours > 11) {
            hours-=12;
        }
        else if (result[3]!=null && result[3].match(/^p/i) && hours < 12) {
            hours+=12;
        }
        if (result[4]!=null && (result[4].match(/^a/i) || result[4].match(/^p/i))) {
                ampmflag = true;
        }

        return checkTimeValues(hours, result[2], 0, ampmDropDown, ampmflag);
    }   
    
    // This is for hh a|p
    var expression = /(\d{1,2})\s?(a|p)?/;
    if (result = expression.exec(timeString)){
        var hours = parseInt(result[1], 10);
        if (result[2]!=null && result[2].match(/^a/i) && hours > 11) {
            hours-=12;
        }
        else if (result[2]!=null && result[2].match(/^p/i) && hours < 12)
            hours+=12;
        if (result[2]!=null && (result[2].match(/^a/i) || result[2].match(/^p/i))) {
                ampmflag = true;
        }

        return checkTimeValues(hours, 0, 0, ampmDropDown, ampmflag);
    }
         
    return 'Invalid';
}

function getAMPMDropDown(parent) {
    var child = parent.firstChild;

    while (child) {
        if (child.name && child.name.length >= "addphase_start_AMPM".length && child.name.substring(0, "addphase_start_AMPM".length) == "addphase_start_AMPM") {
            return child;
        }
        if (child.name && child.name.length >= "phase_start_AMPM".length && child.name.substring(0, "phase_start_AMPM".length) == "phase_start_AMPM") {
            return child;
        }
        if (child.name && child.name.length >= "phase_end_AMPM".length && child.name.substring(0, "phase_end_AMPM".length) == "phase_end_AMPM") {
            return child;
        }

        child = child.nextSibling;
    }
    return null;

}

function checkTimeValues(hours, minutes, seconds, parent, ampmflag){
    var date = new Date();
    date.setHours(hours);
    date.setMinutes(minutes);
    date.setSeconds(seconds);
    var returnHours = "";
    var returnMinutes = "";
    var returnAMPM = "";
    if (date.getHours() <= 11){
        if (date.getHours()==0)
            returnHours = "12";
        else
            returnHours = date.getHours();
        returnAMPM = "AM";
    }
    else if (date.getHours() == 12){
        returnHours = "12";
        returnAMPM = "PM";
    }
    else{
        returnHours = ""+(date.getHours()-12);
        returnAMPM = "PM";
    }
    returnMinutes = ""+date.getMinutes();
    if (returnMinutes.length == 1)
        returnMinutes = "0"+returnMinutes;
    if (minutes == 0)
        returnMinutes = "00";
    
    if (ampmflag) {
	    var ampmDropDown = getAMPMDropDown(parent);

	    for (var i = 0; i < ampmDropDown.options.length; i++) {
        	if (ampmDropDown.options[i].value.toUpperCase() == returnAMPM) {
	            ampmDropDown.selectedIndex = i;
        	    break;
	        }
	    }
    }
    return returnHours+":"+returnMinutes;

}

