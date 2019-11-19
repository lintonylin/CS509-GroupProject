var Canvas;
var ctx;
var width;
var height;
function SetCanvas(){
    console.log(Orientation);
    if(Orientation=="LandScape"){
      console.log("redraw");
      document.getElementById('myCanvas').width = 900;
      document.getElementById('myCanvas').height = 600;
    }

}
function draw(){
    ctx.beginPath();
    ctx.moveTo(50,50);
    ctx.lineTo(50+height,);
    ctx.lineTo(50,150);
    ctx.closePath();
}

function showCardInit(){
    GetCard();
    
}