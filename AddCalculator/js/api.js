// all access driven through BASE. Must end with a SLASH
var base_url = "https://1i4ixzmyig.execute-api.us-east-1.amazonaws.com/epsilon/"; 

var add_url    = base_url + "calculator";   // POST
var create_url = base_url + "constant";     // POST
var delete_url = base_url + "constants";    // DELETE
var list_url   = base_url + "constants";    // GET
