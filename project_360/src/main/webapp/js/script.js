function handleRegistrationSubmit(event) {
    event.preventDefault(); // Prevents the default form submission action

    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;
    const name = document.getElementById("name").value;
    const address = document.getElementById("address").value;
    const birthdate = document.getElementById("birthdate").value;
    const creditCard = document.getElementById("creditCard").value;
    const drivingLicence = document.getElementById("drivingLicence").value;

    // Define the data object
    const data = {
        username: username,
        password: password,
        name: name,
        address: address,
        birthdate: birthdate,
        creditCard: creditCard,
        drivingLicence: drivingLicence
    };

    fetch('/project_360/UserRegistrationServlet', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: new URLSearchParams(data)
    })
            .then(response=>response.text())
            .then(responseData=>{
                console.log(responseData); // Handle the response
            })
            .catch((error)=>{
                console.error('Error:', error);
            });
}
