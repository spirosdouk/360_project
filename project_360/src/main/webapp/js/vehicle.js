function handleSearchSubmit(event) {
    event.preventDefault();

    // Get values from the form
    const vehicleType = document.getElementById('vehicleType').value;

    // Create an AJAX request to your server
    var xhr = new XMLHttpRequest();
    xhr.open('GET', '/project_360/GetVehicles?vehicleType='+encodeURIComponent(vehicleType), true);


    xhr.onreadystatechange = function () {
        if (this.readyState===4&&this.status===200) {
            // Handle the response received from the server
            var vehicles = JSON.parse(this.responseText);
            console.log(vehicles); // For debugging

            // Clear previous results
            const resultsDiv = document.getElementById('searchResults');
            resultsDiv.innerHTML = '';

            // Check if any vehicles are returned
            if (vehicles.length===0) {
                resultsDiv.innerHTML = '<p>No vehicles available for this type.</p>';
                return;
            }

            // Create and append new elements for each vehicle
            vehicles.forEach(vehicle=>{
                const vehicleDiv = document.createElement('div');
                vehicleDiv.classList.add('vehicle-result');
                vehicleDiv.innerHTML = `
                    <h4>${vehicle.brand} ${vehicle.model}</h4>
                    <p>Color: ${vehicle.color}</p>
                    <p>Type: ${vehicle.type}</p>
                    <p>Daily Rental Cost: ${vehicle.daily_rental_cost}</p>
                    <p>Insurance Cost: ${vehicle.daily_insurance_cost}</p>
                    <p>License Plate: ${vehicle.lic_plate}</p>
                `;
                resultsDiv.appendChild(vehicleDiv);
            });
        }
    };

    // Send the request
    xhr.send();
}