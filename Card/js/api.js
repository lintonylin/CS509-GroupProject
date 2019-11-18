// all access driven through BASE. Must end with a SLASH
var base_url = "https://i614j0n966.execute-api.us-east-1.amazonaws.com/beta/"; 

var add_url    = base_url + "calculator";   // POST
var createcard_url = base_url + "createCard";     // POST
var deletecard_url = base_url + "deleteCard";    // DELETE
var cardlist_url   = base_url + "cardList";    // GET
var elementlist_url = base_url +"elementList";
var displaycard_url = base_url + "displayCard";
var addtextelement_url = base_url + "addTextItem";
var deleteelement_url = base_url + "deleteVisualElement";