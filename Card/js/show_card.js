var canvas = document.getElementsByClassName("canvas");
var ctxf;
var ctxl;
var ctxr;
var ctxb;
var width;
var height;
var base64;
function SetCanvas(){
	 var i;
    console.log(Orientation);
    if(Orientation=="Landscape"){
      console.log("redraw");
      for(i=0;i<canvas.length;i++){
      	canvas[i].width =900;
      	canvas[i].height = 600;
      }
      
    }
	ctxf = canvas[0].getContext('2d');
	ctxl = canvas[1].getContext('2d');

	ctxr = canvas[2].getContext('2d');

	ctxb = canvas[3].getContext('2d');


    
    
    for(i=0;i<front.length;i++){
    	if(front[i].text_ID>0){
        	drawtext(front[i],ctxf);
    	}else{
    		drawimage(front[i],ctxl);
    	}
    }
    
    for(i=0;i<innerleft.length;i++){
    	if(innerleft[i].text_ID>0){
        	drawtext(innerleft[i],ctxl);
    	}else{
    		drawimage(innerleft[i],ctxl);
    	}
    }
    for(i=0;i<innerright.length;i++){
    	if(innerright[i].text_ID>0){
        	drawtext(innerright[i],ctxr);
    	}else{
    		drawimage(innerright[i],ctxr);
    	}
    }
    for(i=0;i<back.length;i++){
    	if(back[i].text_ID>0){
        	drawtext(back[i],ctxb);
    	}else{
    		drawimage(back[i],ctxb);
    	}
    }

}
//function draw(){
//    ctx.beginPath();
//    ctx.moveTo(50,50);
//    ctx.lineTo(50+height,);
//    ctx.lineTo(50,150);
//    ctx.closePath();
//}
function drawtext(info,ctx){
	console.log(info);
	 ctx.font ="24pt " + info["font"];
	 ctx.strokeText(info['text'], info['left_corner'], info['top']);
//	 ctx.fillText("Hello Canvas",info['top'], info['left_corner']);
}

function drawimage(info,ctx){
	console.log(info);
	var img =new Image();
	
	img.onload= function(){
		ctx.drawImage(img,info['left_corner'], info['top'],info['height'],info['width']);
	};
	
	img.onerror =function(){
		img.src = "https://cs509norwichfans.s3.amazonaws.com/images/error.png";
		console.log("Fail to upload");
	}
    //getbase64(info["s3ID"].substr(49));
    
	img.src=info["s3ID"];
	console.log(img.src);
}

function getbase64(url){
	
	var data={};
	data["url"] = url;
	var js = JSON.stringify(data);
	var xhr = new XMLHttpRequest();
	xhr.open("post", getimage64_url, true);
	xhr.send(js);
	
	 
	xhr.onloadend = function () {
	    console.log(xhr);
	    console.log(xhr.request);
	    if (xhr.readyState == XMLHttpRequest.DONE) {
	    	base64 = xhr.responseText;
	    } else {
		}
	};
	return "";
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