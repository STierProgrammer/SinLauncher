// profile.js

document.addEventListener("DOMContentLoaded", function () {
    const username = window.javaApp.getCurrentUser(); 
    
    if (username) {
        document.getElementById("username").textContent = username;
    } else {
        document.getElementById("username").textContent = "Unknown User";
    }
});
