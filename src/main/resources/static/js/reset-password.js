let mainPart = document.querySelector('#main-part');
let hiddenPart = document.querySelector(".hidden-part");
let sendButton = document.querySelector("#send-button");
let sendingForm = document.querySelector("form");

console.log(mainPart == null);
console.log(hiddenPart == null);
console.log(sendButton == null);
console.log(sendingForm== null)
sendingForm.addEventListener("submit", function (event) {
    event.preventDefault();
    sendButton.style.display = "none";
    const formData = new FormData(sendingForm);

    fetch(sendingForm.action, {
        method: sendingForm.method,
        body: formData,
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(errorMessage => {
                    alert(errorMessage);
                    sendButton.style.display = "block";
                    throw new Error();
                });
            }
            mainPart.style.display = "none";
            hiddenPart.style.display = "block";
            return response.text();
        })
        .then(data => {
            alert("Mail is sent");
        })
});

// sendButton.addEventListener("click", function (event) {
//     event.preventDefault();
//     mainPart.style.display = "none";
//     hiddenPart.style.display = "block";
// })