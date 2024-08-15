/*const textarea = document.querySelector("section[aria-labelledby=\"translation-target-heading\"] > div > div > d-textarea > div");
let output = "";
for(element of textarea.children) output = output + element.querySelector("p > span").innerText + "\n";
return output


const textarea = document.querySelector("section[aria-labelledby=\"translation-target-heading\"] > div > div > d-textarea");

const output = Array.from(textarea.querySelectorAll("p > span"))
  .map(element => element.textContent + "\n")
  .join("");

return output;*/


/*
const spans = document.querySelectorAll("section[aria-labelledby=\"translation-target-heading\"] > div > d-textarea > p > span");
const output = Array.from(spans).map(element => element.textContent).join("\n");
return output;*/

return Array.from(document.querySelectorAll("d-textarea")[1].childNodes[0].childNodes).map(p => p.children[0].textContent).join("\n")