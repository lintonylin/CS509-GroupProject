var EventType ="";
var Recipient ="";
var Orientation;
var js;
var front;
var innerleft;
var innerright;
var back;
var card={};

function GetCard(){
	var res = location.search.toString().substr(1).split("+");		
	EventType = decodeURI(res[0]);
	Recipient = decodeURI(res[1]);
	Orientation = res[2];
	var testc = "This is " +EventType + " card for " + Recipient;
	card["eventtype"] = EventType;
	card["recipient"] = Recipient;
	card["orientation"] = Orientation;
	var head = document.getElementById("head");
	head.innerHTML = testc;
}


function refreshImageList(){
	var imagelist = document.getElementById("s3source");
	console.log(imagelist);
	imagelist.options.length=0;
	console.log("JS:" + js);
	var xhr = new XMLHttpRequest();
	xhr.open("get", getImageList_url, true);
	xhr.send();
	xhr.onloadend = function () {
		console.log(xhr);
	    console.log(xhr.request);
	    if (xhr.readyState == XMLHttpRequest.DONE) {
	    	if (xhr.status == 200) {
	    		insertImage(xhr.responseText);
	    	}else{console.log("fail to load imagelist");}
	    }
	};
	console.log(xhr);
}


function insertImage(response){
	var js;
	var imagelist = document.getElementById("s3source");
	js = JSON.parse(response);
	for(var i = 0; i<js.length; i++){
		var name = js[i].substr(49);
		imagelist.options[imagelist.length] = new Option(name, js[i]);
	}
	console.log(js);
}

function refreshElementList(){
	var select = document.getElementById("elementList");
	
	while(select.hasChildNodes()){

		select.removeChild(select.firstChild);

	}
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
	    	if (xhr.status == 200) {
	    		console.log ("XHR:" + xhr.responseText);
	    		processDisplayCardResponse(xhr.responseText);
	    		InsertToSelect(select);
	    	}
	    } else {
	    	processDisplayCardResponse("N/A");
		}
	};
	
	
}

function processDisplayCardResponse(response) {
	var js;
	js = JSON.parse(response);
	front = JSON.parse(js["page0"]);
	innerleft = JSON.parse(js["page1"]);
	innerright = JSON.parse(js["page2"]);
	back = JSON.parse(js["page3"]);

}


function setPage(page,select,pageNo){
	var i;
	for(i = 0; i < page.length; i++){
		var option = document.createElement("option");
		if(page[i]["text_ID"]> 0 ){
			option.text =  "Text_ID : " + page[i]["text_ID"] + "; Type: Text;" + " On Page:" + pageNo;
		}else{
			option.text =  "Image_ID : " + page[i]["image_ID"] + "; Type: Image;" + " On Page:" + pageNo;
		}
		option.value = JSON.stringify(page[i]);
		console.log(option.value);
		select.add(option);
	}
}

function InsertToSelect(select){
	setPage(front,select,0);
	setPage(innerleft,select,1);
	setPage(innerright,select,2);
	setPage(back,select,3);
}




function OnCardLoad(){
	GetCard();
	refreshElementList();
	refreshImageList();
}

function handleShowCardClick(){
	var url = "./ShowCard.html"
	url +=  location.search.toString();
	window.location.href = url;
}