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
	console.log(info.text_ID);
	console.log(info);
	setForm(0);
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

function Show(){
	
    document.getElementById('shade').classList.remove('hide');
    document.getElementById('modal').classList.remove('hide');
    document.getElementById('addtext').classList.add('hide');
    document.getElementById('delete').classList.add('hide');
    document.getElementById('edit').classList.add('hide');
}

function Hide(){
	var i
    for(i = 0; i < noneditable.length; i++) {
    	noneditable[i].classList.remove('hide');
     }
	
	document.getElementById('shade').classList.add('hide');
    document.getElementById('modal').classList.add('hide');
    document.getElementById('addtext').classList.remove('hide');
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
//		TODO:mute image selection
	}
	if(n == 0){
	    var i
	    for(i = 0; i < noneditable.length; i++) {
	    	noneditable[i].classList.add('hide');
	     }
	    document.getElementById('Id').classList.remove('hide');
	    document.getElementById('Id').innerHTML= info.text_ID;
	    document.getElementById('t').setAttribute('value',info.top);
	    document.getElementById('l').setAttribute('value',info.left_corner);
	    document.getElementById('h').setAttribute('value',info.height);
	    document.getElementById('w').setAttribute('value',info.width);
	    document.getElementById('text').setAttribute('value',info.text);
	    document.getElementById('add').disabled = true;
	    document.getElementById('font').value = info.font;
	    document.getElementById('update').onclick = function(){
	    	editElementRequest();
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

function editElementRequest(){
	var data={};
	var data={};
	var element={};
	element["card"]= card;
	element["eid"] = parseInt(document.getElementById('Id').value);
	var position={};
	position['top'] = parseInt(document.getElementById('t').value);
//	TODO: Modify variable name 'left';
	position['left'] = parseInt(document.getElementById('l').value);
	position['height'] = parseInt(document.getElementById('h').value);
	position['width'] = parseInt(document.getElementById('w').value);
	data["element"] = element;
	data["position"] = position;
	data["font"] = document.getElementById('font').value;
	data["text"] = document.getElementById('text').value;
	
	var xhr = new XMLHttpRequest();
	xhr.open("post", editelement_url, true);
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