function processDeleteResponse(result) {
  // Can grab any DIV or SPAN HTML element and can then manipulate its
  // contents dynamically via javascript
  console.log("deleted :" + result);
  
  refreshConstantsList();
}

function requestDelete(val) {
   if (confirm("Request to delete " + val)) {
     processDelete(val);
   }
}

function processDelete(val) {
  var data = {};
  data["name"] = val;

  var js = JSON.stringify(data);
  console.log("JS:" + js);
  var xhr = new XMLHttpRequest();
  xhr.open("POST", delete_url, true);

  // send the collected data as JSON
  xhr.send(js);

  // This will process results and update HTML as appropriate. 
  xhr.onloadend = function () {
    console.log(xhr);
    console.log(xhr.request);
    if (xhr.readyState == XMLHttpRequest.DONE) {
      console.log ("XHR:" + xhr.responseText);
      processDeleteResponse(xhr.responseText);
    } else {
      processDeleteResponse("N/A");
    }
  };
}

