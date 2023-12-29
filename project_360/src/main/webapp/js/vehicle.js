var vehicles = [];
var selectedVehicleLicPlate = null;
function handleSearchSubmit(event) {
    var user = localStorage.getItem('user'); // Retrieve username from localStorage
    var age = localStorage.getItem('age');
    var drivLic = localStorage.getItem('driv_lic');
    console.log("User: ", user, "Age: ", age, "Driving License: ", drivLic);
    event.preventDefault();

    const vehicleType = document.getElementById('vehicleType').value;

    var xhr = new XMLHttpRequest();
    xhr.open('GET', '/project_360/GetVehicles?vehicleType='+encodeURIComponent(vehicleType), true);

    xhr.onreadystatechange = function () {
        if (this.readyState===4&&this.status===200) {
            // Parse the response and store it in the global vehicles array
            vehicles = JSON.parse(this.responseText);

            const resultsDiv = document.getElementById('searchResults');
            resultsDiv.innerHTML = '';

            if (vehicles.length===0) {
                resultsDiv.innerHTML = '<p>No vehicles available for this type.</p>';
                return;
            }

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
                    <button onclick="showRentalModal('${vehicle.lic_plate}')" class="btn btn-primary">Rent</button>
                `;
                resultsDiv.appendChild(vehicleDiv);
            });
        }
    };

    xhr.send();
}

function showRentalModal(licensePlate) {
    const vehicle = vehicles.find(v=>v.lic_plate===licensePlate);
    if (!vehicle)
        return;
    // Retrieve user's age and driving license number from localStorage
    const userAge = parseInt(localStorage.getItem('age'), 10);
    const userDrivingLic = localStorage.getItem('driv_lic');

    // Check for age and driving license requirements
    if (vehicle.type==='car'||vehicle.type==='bike') {
        if (userAge<18) {
            alert('You must be over 18 to rent a car or motorcycle.');
            return;
        }
        if (userDrivingLic==='0'||!userDrivingLic) {
            alert('A valid driving license is required to rent a car or motorcycle.');
            return;
        }
    } else if (userAge<16) {
        alert('You must be over 16 to rent a bicycle or scooter.');
        return;
    }

    console.log("Selected Vehicle License Plate:", licensePlate);
    selectedVehicleLicPlate = licensePlate;
    console.log(userAge);

    const modalBody = document.getElementById('rentalModalBody');
    modalBody.innerHTML = `
        <p><strong>Brand:</strong> ${vehicle.brand}</p>
        <p><strong>Model:</strong> ${vehicle.model}</p>
        <p><strong>Daily Rental Cost:</strong> ${vehicle.daily_rental_cost}</p>
        <!-- Rental Date Fields -->
        <label for="modalFromDate">From Date:</label>
        <input type="date" id="modalFromDate" class="form-control" required />
        <label for="modalToDate">To Date:</label>
        <input type="date" id="modalToDate" class="form-control" required />
        <div class="form-check">
            <input class="form-check-input" type="checkbox" id="modalInsuranceCheck">
            <label class="form-check-label" for="modalInsuranceCheck">
                Include Insurance
            </label>
        </div>
    `;

    var myModal = new bootstrap.Modal(document.getElementById('rentalModal'), {
        keyboard: false
    });
    myModal.show();

}

function confirmRental() {
    const fromDateInput = document.getElementById('modalFromDate');
    const toDateInput = document.getElementById('modalToDate');
    const fromDate = new Date(fromDateInput.value);
    const toDate = new Date(toDateInput.value);

    if (!fromDateInput.value||!toDateInput.value) {
        alert("Please select both 'From Date' and 'To Date'.");
        return; // Stop the function if validation fails
    }
    if (isNaN(fromDate.getTime())||isNaN(toDate.getTime())) {
        alert("Invalid dates. Please enter valid 'From Date' and 'To Date'.");
        return; // Stop the function if validation fails
    }
    if (toDate<=fromDate) {
        alert("'To Date' must be after 'From Date'.");
        return; // Stop the function if validation fails
    }

    const includeInsurance = document.getElementById('modalInsuranceCheck').checked;
    const licensePlate = selectedVehicleLicPlate;

    const updateVehicleData = {
        lic_plate: licensePlate,
        isRented: "true"
    };


    console.log("updateVehicleData:", updateVehicleData);

    var xhrVehicleUpdate = new XMLHttpRequest();
    xhrVehicleUpdate.open('POST', '/project_360/GetVehicles', true);
    xhrVehicleUpdate.setRequestHeader('Content-Type', 'application/json');

    xhrVehicleUpdate.onload = function () {
        if (xhrVehicleUpdate.status===200) {
            console.log("Vehicle rental status updated successfully");

            const newRentalData = {
                username: localStorage.getItem('user'),
                lic_plate: licensePlate,
                driv_lic: localStorage.getItem('driv_lic'),
                duration: (toDate-fromDate)/(1000*3600*24), // Calculate duration in days
                daily_cost: vehicles.find(v=>v.lic_plate===licensePlate).daily_rental_cost,
                total_cost: ((toDate-fromDate)/(1000*3600*24))*vehicles.find(v=>v.lic_plate===licensePlate).daily_rental_cost,
                rental_date: fromDateInput.value,
                is_returned: "false",
                has_insurance: includeInsurance?"true":"false",
                car_change: 0
            };

            console.log("newRentalData:", newRentalData);

            var xhrRentalCreation = new XMLHttpRequest();
            xhrRentalCreation.open('POST', '/project_360/RentalServlet', true);
            xhrRentalCreation.setRequestHeader('Content-Type', 'application/json');

            xhrRentalCreation.onload = function () {
                if (xhrRentalCreation.status===200) {
                    console.log("Rental transaction recorded successfully");
                    // Hide the modal after successful operation
                    var myModalEl = document.getElementById('rentalModal');
                    var modal = bootstrap.Modal.getInstance(myModalEl);
                    modal.hide();
                } else {
                    console.error("Error creating rental transaction:", xhrRentalCreation.responseText);
                    // Revert vehicle status to not rented
                    revertVehicleStatus(licensePlate);
                }
            };
            xhrRentalCreation.send(JSON.stringify(newRentalData));
        } else {
            console.error("Error updating vehicle status:", xhrVehicleUpdate.responseText);
        }
    };
    xhrVehicleUpdate.send(JSON.stringify(updateVehicleData));


    console.log('Rental confirmed');



    // Hide the modal after validation and confirmation
    var myModalEl = document.getElementById('rentalModal');
    var modal = bootstrap.Modal.getInstance(myModalEl);
    modal.hide();
}
function revertVehicleStatus(licensePlate) {
    const revertVehicleData = {
        lic_plate: licensePlate,
        isRented: "false"
    };
    var xhrVehicleRevert = new XMLHttpRequest();
    xhrVehicleRevert.open('POST', '/project_360/UpdateVehicleServlet', true);
    xhrVehicleRevert.setRequestHeader('Content-Type', 'application/json');
    xhrVehicleRevert.onload = function () {
        if (xhrVehicleRevert.status===200) {
            console.log("Vehicle rental status reverted successfully");
        } else {
            console.error("Error reverting vehicle status:", xhrVehicleRevert.responseText);
        }
    };
    xhrVehicleRevert.send(JSON.stringify(revertVehicleData));
}