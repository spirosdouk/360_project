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
            .then(response=>response.json())
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
                            'Content-Type': 'application/json',
                        },
                        body: JSON.stringify(registrationData)
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
    event.preventDefault();

    const username = document.getElementById("loginUsername").value;
    const password = document.getElementById("loginPassword").value;

    console.log("Attempting login for:", username); // Debugging line

    if (username==="admin"&&password==="admin") {
        window.location.href = "html/AdminLoggedin.html";
    } else {
        var xhr = new XMLHttpRequest();
        xhr.onload = function () {
            if (xhr.readyState===4) {
                if (xhr.status===200) {
                    var response = JSON.parse(xhr.responseText);
                    if (Object.keys(response).length!==0) {
                        localStorage.setItem('user', username); // Store username in localStorage
                        localStorage.setItem('age', calculateAge(response.birthdate)); // Store age
                        localStorage.setItem('driv_lic', response.driv_lic); // Store driving license number
                        window.location.href = "loggedin.html";
                    } else {
                        document.getElementById("error").innerHTML = "Wrong Credentials";
                    }
                } else {
                    document.getElementById("error").innerHTML = "Error: "+xhr.status;
                }
            }
        };

        var data = JSON.stringify({"username": username, "password": password});
        xhr.open('POST', 'CheckUsernameServlet');
        xhr.setRequestHeader('Content-type', 'application/json');
        xhr.send(data);
    }
}



function calculateAge(birthdate) {
    var dob = new Date(birthdate);
    var diff_ms = Date.now()-dob.getTime();
    var age_dt = new Date(diff_ms);
    return Math.abs(age_dt.getUTCFullYear()-1970);
}
