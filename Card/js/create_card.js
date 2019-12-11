/**
 * Respond to server JSON object.
 *
 */
function handleDuplicateCardClick(k){
	console.log("handlingCreateCard");
	if(document.getElementById("newre").value.length<1){
		alert("please enter name for duplicate!");
		return;
	}
	  var table = document.getElementById("CardListTable");
	  var form = document.createForm;
	  var EventType = table.rows[k].cells[0].innerHTML;
	  var Recipient = table.rows[k].cells[1].innerHTML;
	  var Orientation = table.rows[k].cells[2].innerHTML;
	  
	  var data = {};
	  var card={};
	  card["eventtype"] = EventType;
	  card["recipient"] = Recipient;
	  card["orientation"] = Orientation;
	  data["card"] = card;
	 
	  data["newrecipient"] = document.getElementById("newre").value;
	  
	  var js = JSON.stringify(data);
	  console.log("JS:" + js);
	  var xhr = new XMLHttpRequest();
	  xhr.open("POST", duplicate_url, true);

	  // send the collected data as JSON
	  xhr.send(js);

	  // This will process results and update HTML as appropriate. 
	  xhr.onloadend = function () {
		    console.log(xhr);
		    console.log(xhr.request);
		    if (xhr.readyState == XMLHttpRequest.DONE) {
		      console.log ("XHR:" + xhr.responseText);
		      refreshCardList();
		    } else {
		      refreshCardLish();
		    }
		  };
}

function processCreateCardResponse(status) {
  // Can grab any DIV or SPAN HTML element and can then manipulate its
  // contents dynamically via javascript
	if (status == 200){
		console.log("Create Card Success");
	}else if (status == 409){
		console.log("Duplicate Card")
		alert("Duplicate Card!");
	}else if (status == 400){
		alert("Please Check Your input, Recipient cannot be empty");
	}
	// Update computation result
	//document.createForm.result.value = httpResult;
	  
	refreshCardList();
}

function handleCreateCardClick(e) {
	console.log("handlingCreateCard");
  var form = document.createForm;
  var EventType = form.EventType.value;
  var Recipient = form.Recipient.value;
  var Orientation = form.Orientation.value;
  
  var data = {};
  data["eventtype"] = EventType;
  data["recipient"] = Recipient;
  data["orientation"] = Orientation;
  
  var js = JSON.stringify(data);
  console.log("JS:" + js);
  var xhr = new XMLHttpRequest();
  xhr.open("POST", createcard_url, true);

  // send the collected data as JSON
  xhr.send(js);

  // This will process results and update HTML as appropriate. 
  xhr.onloadend = function () {
	    console.log(xhr);
	    console.log(xhr.request);
	    if (xhr.readyState == XMLHttpRequest.DONE) {
	      console.log ("XHR:" + xhr.responseText);
	      processCreateCardResponse(xhr.status);
	    } else {
	      processCreateCardResponse("N/A");
	    }
	  };
}

