/**
 * Respond to server JSON object.
 *
 */
function processDeleteCardResponse(status) {
  // Can grab any DIV or SPAN HTML element and can then manipulate its
  // contents dynamically via javascript
	if (status == 200){
		console.log("delete Card Success");
	}else{
		alert("Card Not found");
	}
	// Update computation result
	//document.createForm.result.value = httpResult;
	  
	refreshCardList();
}

function handleDeleteCardClick(k) {
	
	console.log("handlingDeleteCard");
	console.log(k);
  var table = document.getElementById("CardListTable");
  var EventType = table.rows[k].cells[0].innerHTML;
  console.log(EventType);
  var Recipient = table.rows[k].cells[1].innerHTML;
  
  var data = {};
  data["eventtype"] = EventType;
  data["recipient"] = Recipient;
  
  var js = JSON.stringify(data);
  console.log("JS:" + js);
  var xhr = new XMLHttpRequest();
  xhr.open("POST", deletecard_url, true);

  // send the collected data as JSON
  xhr.send(js);

  // This will process results and update HTML as appropriate. 
  xhr.onloadend = function () {
	    console.log(xhr);
	    console.log(xhr.request);
	    if (xhr.readyState == XMLHttpRequest.DONE) {
	      console.log ("XHR:" + xhr.responseText);
	      processDeleteCardResponse(xhr.status);
	    } else {
	      processDeleteCardResponse("N/A");
	    }
	  };
}

