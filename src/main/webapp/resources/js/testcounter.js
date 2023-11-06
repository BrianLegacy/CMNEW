function countText(){
    var textValue = document.getElementById("form:message").value;
    var optOut = document.getElementById("form:optout").value.length;
    
    var typed = (textValue[textValue.length - 1] !== " " ? textValue.length + 1 : textValue.length)+optOut;
    console.log("Total Charaters typed: ",typed);
    var MAX=1024;
    if(typed===MAX){
        console.log("Maximum characters reached");
    }
};
