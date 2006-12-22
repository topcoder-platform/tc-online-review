// create the request object
function createXMLHttpRequest() {
	var xmlHttp;
	if (window.ActiveXObject) {
		xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
	} else if (window.XMLHttpRequest) {
		xmlHttp = new XMLHttpRequest();
	}
	return xmlHttp;
}

/**
 * TODO: Document it
 */
function sendRequest(requestContent, successHandler, errorHandler) {
	// create the Ajax request
	var myRequest = createXMLHttpRequest();

	// set the callback function
	// TODO: Check for errors not handled by Ajax Support
	myRequest.onreadystatechange = function() {
		if (myRequest.readyState == 4 && myRequest.status == 200) {
			// the response is ready
			var respXML = myRequest.responseXML;
			// retrieve the result
			var result = respXML.getElementsByTagName("result")[0].getAttribute("status");
			if (result == "Success") {
				if (successHandler) {
					successHandler(result, respXML);
				}
			} else {
				if (errorHandler) {
					errorHandler(result, respXML);
				}
			}
		}
	};

	// send the request
	myRequest.open("POST", ajaxSupportUrl, true);
	myRequest.setRequestHeader("Content-Type", "text/xml;charset=UTF-8");
	myRequest.send(requestContent);
}
