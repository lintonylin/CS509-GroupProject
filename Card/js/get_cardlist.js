/**
 * Refresh constant list from server
 *
 *    GET list_url
 *    RESPONSE  list of [name, value] constants 
 */
function refreshCardList() {
   var xhr = new XMLHttpRequest();
   xhr.open("GET", cardlist_url, true);
   console.log(xhr);
   xhr.send();
   var table = document.getElementById("CardListTable");
   while(table.rows.length >1){
	   table.deleteRow(-1);
   }
   console.log("sent");

  // This will process results and update HTML as appropriate. 
  xhr.onloadend = function () {
    if (xhr.readyState == XMLHttpRequest.DONE) {
      console.log ("XHR:" + xhr.responseText);
      processCardListResponse(xhr.responseText);
    } else {
      processCardListResponse("N/A");
    }
  };
}

/**
 * Respond to server JSON object.
 *
 * Replace the contents of 'constantList' with a <br>-separated list of name,value pairs.
 */
function processCardListResponse(result) {
  // Can grab any DIV or SPAN HTML element and can then manipulate its contents dynamically via javascript
  var js = JSON.parse(result);
//  var constList = document.getElementById('constantList');
  var table = document.getElementById("CardListTable");
  for(var i = 0; i <js.length; i++){
	  var rownode = table.insertRow();
	  
	  var eventnode = rownode.insertCell();
	  var recinode = rownode.insertCell();
	  var orinode = rownode.insertCell();
	  var delenode = rownode.insertCell();
	  var btn = document.createElement("BUTTON");
	  btn.innerHTML ="Delete";
	  var btn2 = document.createElement("BUTTON");
	  btn2.innerHTML = "Edit";
	  

	  const y = i;
	  console.log(y);
	  btn.onclick = function(){
		  handleDeleteCardClick(y+1);
	  };
	  btn2.onclick = function(){
		  handleEditCardClick(y+1);
	  };
	  eventnode.innerHTML = js[i]['eventtype'];
	  recinode.innerHTML = js[i]['recipient'];
	  orinode.innerHTML = js[i]['orientation'];
	  delenode.append(btn);
	  delenode.append(btn2);
  }

}

