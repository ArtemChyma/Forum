let mainPart = document.querySelector('#main-part');
let hiddenPart = document.querySelector(".hidden-part");
let sendLink = document.querySelector("#send-link");

console.log(mainPart == null);
console.log(hiddenPart == null);
console.log(sendLink == null);

sendLink.addEventListener("click", function (event) {
    event.preventDefault();
    mainPart.style.display = "none";
    hiddenPart.style.display = "block";
})