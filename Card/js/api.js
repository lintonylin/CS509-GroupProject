// all access driven through BASE. Must end with a SLASH
var base_url = "https://i614j0n966.execute-api.us-east-1.amazonaws.com/beta/"; 

var add_url    = base_url + "calculator";   // POST
var createcard_url = base_url + "createCard";     // POST
var deletecard_url = base_url + "deleteCard";    // DELETE
var cardlist_url   = base_url + "cardList";    // GET
var elementlist_url = base_url +"elementList";
var displaycard_url = base_url + "displayCard";
var addtextelement_url = base_url + "addTextItem";

var addimageelement_url = base_url + "addImageItem";
var deleteelement_url = base_url + "deleteVisualElement";
var edittextelement_url = base_url + "editTextElement";
var editimageelement_url = base_url + "editImageElement";

var getImageList_url = base_url +"imageList";