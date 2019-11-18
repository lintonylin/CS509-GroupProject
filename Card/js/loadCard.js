var EventType ="";
var Recipient ="";
var js;


function GetCard(){
	var res = location.search.toString().substr(1).split("+");		
	EventType = res[0];
	Recipient = decodeURI(res[1]);
	var testc = "This is " +EventType + " card for " + Recipient;
	var head = document.getElementById("head");
	head.innerHTML = testc;
}

function refreshElementList(){
	var select = document.getElementById("elementlist");
	console.log(EventType);
	console.log(Recipient);
	var data = {};
	data["eventtype"] = EventType;
	data["recipient"] = Recipient;
	var js = JSON.stringify(data);
	console.log("JS:" + js);
	var xhr = new XMLHttpRequest();
	xhr.open("post", displaycard_url, true);
	 xhr.send(js);
	 
	xhr.onloadend = function () {
	    console.log(xhr);
	    console.log(xhr.request);
	    if (xhr.readyState == XMLHttpRequest.DONE) {
	    	console.log ("XHR:" + xhr.responseText);
	    	processDisplayCardResponse(xhr.responseText);
	    } else {
	    	processDisplayCardResponse("N/A");
		}
	};	 
}

function processDisplayCardResponse(response) {
	js = JSON.parse(response);
	
	
}


function OnCardLoad(){
	GetCard();
	refreshElementList();
}