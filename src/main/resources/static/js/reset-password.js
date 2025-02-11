let mainPart = document.querySelector('#main-part');
let hiddenPart = document.querySelector(".hidden-part");
let sendButton = document.querySelector("#send-button");

console.log(mainPart == null);
console.log(hiddenPart == null);
console.log(sendButton == null);

sendButton.addEventListener("click", function (event) {
    event.preventDefault();
    mainPart.style.display = "none";
    hiddenPart.style.display = "block";
})