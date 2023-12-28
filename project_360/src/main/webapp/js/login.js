function handleLoginSubmit(event) {
    event.preventDefault(); // Prevents the default form submission action

    const username = document.getElementById("loginUsername").value;
    const password = document.getElementById("loginPassword").value;

    // Normally, here you would handle authentication.
    // For now, we will just log the data and redirect.
    console.log("Login Data:", {username, password}); // Caution with logging sensitive data

    // Redirect to login.html
    window.location.href = "login.html";
}
