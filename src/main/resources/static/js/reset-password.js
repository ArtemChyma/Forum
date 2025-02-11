let mainPart = document.querySelector('#main-part');
let hiddenPart = document.querySelector(".hidden-part");
let sendButton = document.querySelector("#send-button");
let sendingForm = document.querySelector("form");

console.log(mainPart == null);
console.log(hiddenPart == null);
console.log(sendButton == null);
console.log(sendingForm== null)
sendingForm.addEventListener("submit", function (event) {
    event.preventDefault(); // предотвращаем перезагрузку страницы
    mainPart.style.display = "none";
    hiddenPart.style.display = "block";
    const formData = new FormData(sendingForm);

    fetch(sendingForm.action, {
        method: sendingForm.method,
        body: formData,
    })
        .then(response => response.text()) // или response.json() в зависимости от сервера
        .then(data => {
            console.log("Успешно отправлено:", data);

        })
        .catch(error => console.error("Ошибка при отправке формы:", error));
});

// sendButton.addEventListener("click", function (event) {
//     event.preventDefault();
//     mainPart.style.display = "none";
//     hiddenPart.style.display = "block";
// })