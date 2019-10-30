function processCreateResponse(result) {
  // Can grab any DIV or SPAN HTML element and can then manipulate its
  // contents dynamically via javascript
  console.log("result:" + result);
  var js = JSON.parse(result);

  var httpResult = js["response"];

  // Update computation result
  document.createForm.result.value = httpResult;
  
  refreshConstantsList();
}


function handleCreateClick(e) {
  var form = document.createForm;
  var arg1 = form.name.value;
  var arg2 = form.value.value;

  var data = {};
  data["name"] = arg1;
  
  // parse arg2 to make sure a float but then convert to base64 using javascript after turning
  // back into a string.
  data["base64EncodedValue"] =  window.btoa("" + parseFloat(arg2));

  var js = JSON.stringify(data);
  console.log("JS:" + js);
  var xhr = new XMLHttpRequest();
  xhr.open("POST", create_url, true);

  // send the collected data as JSON
  xhr.send(js);

  // This will process results and update HTML as appropriate. 
  xhr.onloadend = function () {
    console.log(xhr);
    console.log(xhr.request);
    if (xhr.readyState == XMLHttpRequest.DONE) {
      console.log ("XHR:" + xhr.responseText);
      processCreateResponse(xhr.responseText);
    } else {
      processCreateResponse("N/A");
    }
  };
}
