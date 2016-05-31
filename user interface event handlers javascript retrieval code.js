var startID = 1000;
var regex = new RegExp("([A-Z])([A-Z][a-z])|([a-z0-9])([A-Z])", "g");
var regexReplace = "$1$3_$2$4";
var regexQuote = new RegExp("\"", "g");

function Event(){};
Event.prototype = {
  priority: 0,
  event:"",
  fired:"",
  notes:"",
  scope:"",
  toString: function(id){
    return "EVENT_"+this.event.replace(regex, regexReplace).toUpperCase() + "("+ id + ", \"" + this.event + "\", PropertyType.EVENT, strArr(\"" + this.fired + "\", \"" + this.notes + "\", " + this.priority +", \""+this.scope+"\"))";
  }
};

var retrieve = ["priority", "event", "fired", "notes", "scope"];
var events = new Array(document.getElementsByTagName("tr").length);
for(var i = 0; i < events.length; i++){
  events[i] = new Event();
}


var rName;
for(var r = 0; r < retrieve.length; r++){
  rName = retrieve[r];
  var eles = document.getElementsByClassName(rName);
  for(var i = 0; i < eles.length; i++){
    var value = eles[i].innerText.replace(regexQuote, "'").trim();
    if(rName == "priority"){
      if(value == "&nbsp;?"){
        value = -1;
      }
      value = "priority("+value+")"
    }
    events[i][rName] = value;
  }
}
var str = "";
for(var i = 0; i < events.length; i++){
  if(events[i].event == ""){
    break;
  }
  str = str + events[i].toString(startID+i) +",\n"
}
console.log(str);
