// profile.js

document.addEventListener("DOMContentLoaded", function() {
    const usernameElement = document.getElementById("username");
   
    if (window.currentUser) {
        usernameElement.textContent = window.currentUser;
    } else {
        usernameElement.textContent = "Unknown User";
    }
});