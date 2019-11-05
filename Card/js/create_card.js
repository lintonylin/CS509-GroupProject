/**
 * Respond to server JSON object.
 *
 */
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

