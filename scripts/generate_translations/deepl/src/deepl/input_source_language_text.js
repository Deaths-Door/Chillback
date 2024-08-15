const input_fields = document.querySelector("section[aria-labelledby=\"translation-source-heading\"] > div > div > d-textarea");
/*Replace with text*/
input_fields.value = "hello";
var evt = document.createEvent("Events");
evt.initEvent("change", true, true);
input_fields.dispatchEvent(evt);
