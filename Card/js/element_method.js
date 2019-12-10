var textItem = document.getElementsByClassName('text');
var noneditable = document.getElementsByClassName('ne');
var info;


function deleteElement(){
	var info  = getInfoFromSelect();
	if(info == -1){
		alert("please select an element");
		return;
	}
	if(info.page == 3){
		alert("Cannot edit back page.")
		return;
	}
	deleteElementRequest(info.text_ID);
//	TODO: send request;
}

function editElement(){
	info  = getInfoFromSelect();
	if(info == -1){
		alert("please select an element");
		return;
	}
	if(info.page == 3){
		alert("Cannot edit back page.")
		return;
	}
	if(info.text_ID>0){
		console.log(info.text_ID);
		console.log(info);
		setForm(0);
	}
	else{
		console.log(info.s3ID.substr(49));
		console.log(info);
		setForm(1);
	}

	Show();
	//	TODO: send request;
}

function getInfoFromSelect(){
	var select = document.getElementById("elementList");
	if(select.selectedIndex==-1){
		return -1;
	}
	var info = JSON.parse(select.value);
	return info;
}

function addTextElement(){
	setForm(2); //0 for update text, 1 for update image, 2 for add text 3 for add image
	//	TODO: mute image selections;
	Show();
}
function addImageElement(){
	setForm(3); //0 for update text, 1 for update image, 2 for add text 3 for add image
	//	TODO: mute image selections;
	Show();
}

function Show(){
    document.getElementById('shade').classList.remove('hide');
    document.getElementById('modal').classList.remove('hide');
    document.getElementById('addtext').classList.add('hide');
    document.getElementById('addimage').classList.add('hide');
    document.getElementById('delete').classList.add('hide');
    document.getElementById('edit').classList.add('hide');
}

function Hide(){
	var i
    for(i = 0; i < noneditable.length; i++) {
    	noneditable[i].classList.remove('hide');
     }
	for(i = 0; i < textItem.length; i++) {
    	textItem[i].classList.remove('hide');
     }
	document.getElementById('modal').reset();
	document.getElementById('shade').classList.add('hide');
    document.getElementById('modal').classList.add('hide');
    document.getElementById('addtext').classList.remove('hide');
    document.getElementById('addimage').classList.remove('hide');
    document.getElementById('delete').classList.remove('hide');
    document.getElementById('edit').classList.remove('hide');
    
    
    document.getElementById('Id').setAttribute('value',0);
    document.getElementById('Id').classList.add('hide');
    document.getElementById('t').setAttribute('value',0);
    document.getElementById('l').setAttribute('value',0);
    document.getElementById('h').setAttribute('value',0);
    document.getElementById('w').setAttribute('value',0);
    document.getElementById('text').setAttribute('value','');
    document.getElementById('add').disabled = false;
    document.getElementById('update').disabled = false;
}

function setForm(n){
	if(n == 2){
		document.getElementById('update').disabled = true;
		document.getElementById('add').onclick = function(){
			addTextRequest();
		};
	if(n == 3){
		document.getElementById('update').disabled = true;
		document.getElementById('type').innerHTML= "Image";
		for(i = 0; i < textItem.length; i++) {
	    	textItem[i].classList.add('hide');
		}

		document.getElementById('add').onclick = function(){
			addImageRequest();
		}
	};
//		TODO:mute image selection
	}
	if(n == 0){
	    var i
	    for(i = 0; i < noneditable.length; i++) {
	    	noneditable[i].classList.add('hide');
	     }
	    document.getElementById('Id').classList.remove('hide');
	    if(info.text_ID>0){
	    	document.getElementById('Id').innerHTML= info.text_ID;
		    document.getElementById('type').innerHTML= "Text";
	    }else{
	    	document.getElementById('Id').innerHTML= info.image_ID;
	    	document.getElementById('type').innerHTML= "Image";
	    }
	    document.getElementById('t').setAttribute('value',info.top);
	    document.getElementById('l').setAttribute('value',info.left_corner);
	    document.getElementById('h').setAttribute('value',info.height);
	    document.getElementById('w').setAttribute('value',info.width);
	    document.getElementById('text').setAttribute('value',info.text);
	    document.getElementById('add').disabled = true;
	    document.getElementById('font').value = info.font;
	    document.getElementById('update').onclick = function(){
	    	editElementRequest(1);
		};
	    
	}
	if(n == 1){
	    var i
	    for(i = 0; i < noneditable.length; i++) {
	    	noneditable[i].classList.add('hide');
	     }
	    document.getElementById('Id').classList.remove('hide');
	    if(info.text_ID>0){
	    	document.getElementById('Id').innerHTML= info.text_ID;
		    document.getElementById('type').innerHTML= "Text";
	    }else{
	    	document.getElementById('Id').innerHTML= info.image_ID;
	    	document.getElementById('type').innerHTML= "Image";
	    }
	    document.getElementById('t').setAttribute('value',info.top);
	    document.getElementById('l').setAttribute('value',info.left_corner);
	    document.getElementById('h').setAttribute('value',info.height);
	    document.getElementById('w').setAttribute('value',info.width);
	    for(i = 0; i < textItem.length; i++) {
	    	textItem[i].classList.add('hide');
	     }

	    document.getElementById('add').disabled = true;
	    document.getElementById('update').onclick = function(){
	    	editElementRequest(2);
		};   
	}
}

function addTextRequest(){
	var data ={};
	var position={};
	position['top'] = parseInt(document.getElementById('t').value);
//	TODO: Modify variable name 'left';
	position['left'] = parseInt(document.getElementById('l').value);
	position['height'] = parseInt(document.getElementById('h').value);
	position['width'] = parseInt(document.getElementById('w').value);
	data["card"] = card;
	data["position"] = position; 
	data["text"] = document.getElementById('text').value;
	data["page"] = parseInt(document.getElementById('page').value);
	data["font"] = document.getElementById('font').value;
	var js = JSON.stringify(data);
	console.log(js);
	
	var xhr = new XMLHttpRequest();
	xhr.open("post", addtextelement_url, true);
	xhr.send(js);
	xhr.onloadend = function () {
		    if (xhr.readyState == XMLHttpRequest.DONE) {
		      console.log ("XHR:" + xhr.responseText);
		      Hide();
		      refreshElementList();
		    } else {
		    	
		    }
		  };
}

function addImageRequest(){
	var data ={};
	var position={};
	position['top'] = parseInt(document.getElementById('t').value);
//	TODO: Modify variable name 'left';
	position['left'] = parseInt(document.getElementById('l').value);
	position['height'] = parseInt(document.getElementById('h').value);
	position['width'] = parseInt(document.getElementById('w').value);
	data["card"] = card;
	data["position"] = position; 
	data["text"] = document.getElementById('text').value;
	data["page"] = parseInt(document.getElementById('page').value);
	data["font"] = document.getElementById('font').value;
	var js = JSON.stringify(data);
	console.log(js);
	
	var xhr = new XMLHttpRequest();
	xhr.open("post", addimageelement_url, true);
	xhr.send(js);
	xhr.onloadend = function () {
		    if (xhr.readyState == XMLHttpRequest.DONE) {
		      console.log ("XHR:" + xhr.responseText);
		      Hide();
		      refreshElementList();
		    } else {
		    	
		    }
		  };
}

function deleteElementRequest(id){
	var data={};
	data["card"] = card;
	data["eid"] = parseInt(id);
	var js = JSON.stringify(data);
	var xhr = new XMLHttpRequest();
	xhr.open("post",deleteelement_url, true);
	xhr.send(js);
	xhr.onloadend = function () {
	    if (xhr.readyState == XMLHttpRequest.DONE) {
	      console.log ("XHR:" + xhr.responseText);
	      refreshElementList();
	    } else {
	    	
	    }
	  };
}

function editElementRequest(i){
	var data={};
	var element={};
	var type;
	type =document.getElementById("type").innerHTML;
	data["card"] = card;
	var position={};
	position['top'] = parseInt(document.getElementById('t').value);
	position['left'] = parseInt(document.getElementById('l').value);
	position['height'] = parseInt(document.getElementById('h').value);
	position['width'] = parseInt(document.getElementById('w').value);
	data["eid"] = parseInt(document.getElementById('Id').innerHTML);
	data["position"] = position;
	var url;
	if(i == 1){
		data["font"] = document.getElementById('font').value;
		data["text"] = document.getElementById('text').value;
		url = edittextelement_url;
	}else{
		url = editimageelement_url;
	}
	var js = JSON.stringify(data);
	console.log(js);
	var xhr = new XMLHttpRequest();
	xhr.open("post", url, true);
	xhr.send(js);
	xhr.onloadend = function () {
		    if (xhr.readyState == XMLHttpRequest.DONE) {
		      console.log ("XHR:" + xhr.responseText);
		      Hide();
		      refreshElementList();
		    } else {
		    	
		    }
		  };
	
}