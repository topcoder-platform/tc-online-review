var emptyString = /^\s*$/;
var float_test = /^\d*\.{0,1}\d*$/;

function trim(str)
{
	return str.replace(/^\s+|\s+$/g, '');
};

function isAllDigits(argvalue) {
    argvalue = argvalue.toString();
    if (argvalue.length == 0) {
        return false;
    }
    for (var n = 0; n < argvalue.length; ++n) {
        if ((argvalue.substring(n, n + 1) < "0") || (argvalue.substring(n, n + 1) > "9")) {
            return false;
        }
    }
    return true;
}

function isFloat(argvalue) {
    argvalue = argvalue.toString();

    var fValue = parseFloat(argvalue);
    if (isNaN(fValue)) {
        return false;
    }
    
    return float_test.test(argvalue);
}

function isInteger(argvalue) {
    argvalue = argvalue.toString();
    
    var iValue = parseInt(argvalue, 10);
    if (isNaN(iValue) || !(iValue >= -2147483648 && iValue <= 2147483647)) {
	    return false;
	}

    return true;
}

function isDateString(str) {
    if (str == null || (str = trim(str)).length == 0)
        return false;
        
    var result = null;
    
    // for mm.dd.yy
    expression = /^(\d{1,2})\.(\d{1,2})\.((\d{2})|(\d{4}))$/;
    if (result = expression.exec(str)) {
    	if (result[3].length == 4) {
    		year = parseInt(result[3], 10);
    	} else {
    		year = 2000 + parseInt(result[3], 10);
    	}
    	month = parseInt(result[1], 10) - 1;
    	day = parseInt(result[2], 10);
    	
    	var d = new Date();
		d.setFullYear(year, month, day);
    	return d.getFullYear() == year && d.getMonth() == month && d.getDate() == day;
    }
    
	return false;
}

function isTimeString(str) {
    if (str == null || (str = trim(str)).length == 0)
        return false;
        
    var result = null;
    
    // for hh:mm
    var expression = /^(\d{1,2}):(\d{1,2})$/;
    if (result = expression.exec(str)) {
        var hour = parseInt(result[1], 10);
        var minute = parseInt(result[2], 10);
        
        return 1 <= hour && hour <= 12 && 0 <= minute && minute < 60;
    }
    
    return false;
}

function isTimeSpanString(str) {
    if (str == null || (str = trim(str)).length == 0)
        return false;
        
    var result = null;
    
    // for hh
    var expression = /^(\d+)$/;
    if (result = expression.exec(str)) {
        var hour = parseInt(result[1], 10);
        return 0 <= hour;
    }
    
    // for hh:mm
    var expression = /^(\d+):(\d{1,2})$/;
    if (result = expression.exec(str)) {
        var hour = parseInt(result[1], 10);
        var minute = parseInt(result[2], 10);
        return 0 <= hour && 0 <= minute && minute < 60;
    }
    
    return false;
}
