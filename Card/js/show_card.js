var Canvas;
var ctx;
var width;
var height;
function SetCanvas(){
	Canvas = document.getElementById('myCanvas');
    console.log(Orientation);
    if(Orientation=="LandScape"){
      console.log("redraw");
      Canvas.width = 900;
      Canvas.height = 600;
    }
    var i;
    for(i=0;i<front.length;i++){
    	drawtext(front[i]);
    }

}
//function draw(){
//    ctx.beginPath();
//    ctx.moveTo(50,50);
//    ctx.lineTo(50+height,);
//    ctx.lineTo(50,150);
//    ctx.closePath();
//}
function drawtext(info){
	console.log(info);
	 ctx = Canvas.getContext('2d');
	 ctx.font ="24pt " + info["font"];
	 ctx.strokeText(info['text'], info['left_corner'], info['top']);
//	 ctx.fillText("Hello Canvas",info['top'], info['left_corner']);
}
function showCardInit(){
    GetCard();
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