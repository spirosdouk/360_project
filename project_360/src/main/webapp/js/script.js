function handleRegistrationSubmit(event) {
    event.preventDefault(); // Prevents the default form submission action

    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;
    const name = document.getElementById("name").value;
    const address = document.getElementById("address").value;
    const birthdate = document.getElementById("birthdate").value;
    const creditCard = document.getElementById("creditCard").value;
    const drivingLicence = document.getElementById("drivingLicence").value;

    fetch('/project_360/CheckUsernameServlet?username='+encodeURIComponent(username))
            .then(response=>response.json()) // Assuming JSON response
            .then(data=>{
                if (data.exists) {
                    alert('Username already exists. Please choose a different username.');
                } else {
                    const registrationData = {
                        username: username,
                        password: password,
                        name: name,
                        address: address,
                        birthdate: birthdate,
                        driv_lic: drivingLicence,
                        credit_card: creditCard
                    };

                    fetch('/project_360/UserRegistrationServlet', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded',
                        },
                        body: new URLSearchParams(registrationData)
                    })
                            .then(response=>{
                                if (response.ok) {
                                    return response.text();
                                }
                                throw new Error('Network response was not ok.');
                            })
                            .then(responseData=>{
                                console.log(responseData); // Handle the response
                                alert('Registration completed successfully.');
                            })
                            .catch(error=>{
                                console.error('Error:', error);
                                alert('Registration failed.');
                            });
                }
            })
            .catch(error=>{
                console.error('Error checking username:', error);
                alert('Error checking username.');
            });
}

function handleLoginSubmit(event) {
    event.preventDefault(); // Prevents the default form submission action

    const username = document.getElementById("loginUsername").value;
    const password = document.getElementById("loginPassword").value;
    if (username==="admin"&&password==="admin") {
        window.location.href = "html/AdminLoggedin.html";
    }
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState===4) {
            if (xhr.status===200) {
                var response = JSON.parse(xhr.responseText);
                if (response.userExists) {
                    window.location.href = "login.html";
                } else {
                    $("#error").html("Wrong Credentials");
                }
            } else {
                $("#error").html("Error: "+xhr.status);
            }
        }
    };

    var data = JSON.stringify({"username": username, "password": password});
    xhr.open('POST', 'CheckUsernameServlet');
    xhr.setRequestHeader('Content-type', 'application/json');
    xhr.send(data);
}
