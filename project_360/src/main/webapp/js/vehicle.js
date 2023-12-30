var vehicles = [];
var selectedVehicleLicPlate = null;
function handleSearchSubmit(event) {
    var user = localStorage.getItem('user');
    var age = localStorage.getItem('age');
    var drivLic = localStorage.getItem('driv_lic');
    console.log("User: ", user, "Age: ", age, "Driving License: ", drivLic);
    event.preventDefault();

    const vehicleType = document.getElementById('vehicleType').value;

    var xhr = new XMLHttpRequest();
    xhr.open('GET', '/project_360/GetVehicles?vehicleType='+encodeURIComponent(vehicleType), true);

    xhr.onreadystatechange = function () {
        if (this.readyState===4&&this.status===200) {
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
        return;
    }
    if (isNaN(fromDate.getTime())||isNaN(toDate.getTime())) {
        alert("Invalid dates. Please enter valid 'From Date' and 'To Date'.");
        return;
    }
    if (toDate<=fromDate) {
        alert("'To Date' must be after 'From Date'.");
        return;
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

            const vehicle = vehicles.find(v=>v.lic_plate===selectedVehicleLicPlate);
            const duration = (toDate-fromDate)/(1000*3600*24);
            const dailyCost = vehicle.daily_rental_cost+(includeInsurance?vehicle.daily_insurance_cost:0);
            const totalCost = dailyCost*duration;

            const newRentalData = {
                username: localStorage.getItem('user'),
                lic_plate: licensePlate,
                driv_lic: localStorage.getItem('driv_lic'),
                duration: (toDate-fromDate)/(1000*3600*24),
                daily_cost: dailyCost,
                total_cost: totalCost,
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
                    var myModalEl = document.getElementById('rentalModal');
                    var modal = bootstrap.Modal.getInstance(myModalEl);
                    modal.hide();
                    alert("Rental transaction recorded successfully");

                    window.location.reload();
                } else {
                    console.error("Error creating rental transaction:", xhrRentalCreation.responseText);
                    revertVehicleStatus(licensePlate);
                }
            };
            xhrRentalCreation.send(JSON.stringify(newRentalData));
        } else {
            console.error("Error updating vehicle status:", xhrVehicleUpdate.responseText);
        }
    };
    xhrVehicleUpdate.send(JSON.stringify(updateVehicleData));

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
function fetchUserRentals() {
    var user = localStorage.getItem('user');
    var xhr = new XMLHttpRequest();

    xhr.open('GET', '/project_360/RentalServlet?username='+encodeURIComponent(user), true);
    xhr.onload = function () {
        if (xhr.status===200) {
            var rentals = JSON.parse(xhr.responseText);
            displayUserRentals(rentals);
        } else {
            console.error("Error fetching rentals:", xhr.responseText);
        }
    };
    xhr.send();
}

let Rentals = [];

function displayUserRentals(rentals) {
    const tableBody = document.getElementById('currentRentalsTable').getElementsByTagName('tbody')[0];
    tableBody.innerHTML = ''; // Clear existing rows
    Rentals = rentals;

    rentals.forEach(rental=>{
        const row = tableBody.insertRow();
        row.insertCell(0).innerHTML = rental.username;
        row.insertCell(1).innerHTML = rental.lic_plate;
        row.insertCell(2).innerHTML = rental.driv_lic;
        row.insertCell(3).innerHTML = rental.duration;
        row.insertCell(4).innerHTML = rental.daily_cost;
        row.insertCell(5).innerHTML = rental.total_cost;
        row.insertCell(6).innerHTML = rental.rental_date;
        if (rental.is_returned==="false") {
            let returnCell = row.insertCell(7);
            let returnButton = document.createElement('button');
            returnButton.innerHTML = 'Return';
            returnButton.classList.add('btn', 'btn-primary');
            returnButton.onclick = function () {
                returnRental(rental.lic_plate, rental.total_cost, rental.rental_date, rental.duration);
            };
            returnCell.appendChild(returnButton);
        } else {
            row.insertCell(7).innerHTML = 'Returned';
        }
        row.insertCell(8).innerHTML = rental.has_insurance;
        row.insertCell(9).innerHTML = rental.car_change;
    });
}

function returnRental(licensePlate, totalCost, rentdate, dur) {
    console.log("Returning rental with license plate:", licensePlate, "and total cost:", totalCost);
    var xhr = new XMLHttpRequest();
    xhr.open('POST', '/project_360/ReturnRentalServlet', true);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.onload = function () {
        if (xhr.status===200) {
            console.log("Rental returned successfully");
            // Refresh the rentals display
            fetchUserRentals();
        } else {
            console.error("Error returning rental:", xhr.responseText);
        }
    };

    var data = {
        lic_plate: licensePlate,
        total_cost: totalCost,
        is_returned: true,
        duration: dur,
        rental_date: rentdate
    };

    xhr.send(JSON.stringify(data));
}

document.getElementById('issueForm').addEventListener('submit', function (event) {
    event.preventDefault();

    const licensePlate = document.getElementById('licensePlate').value;
    const issueType = document.getElementById('issueType').value;
    const rentalRecord = Rentals.find(rental=>rental.lic_plate===licensePlate);

    if (rentalRecord) {
        const hasInsurance = rentalRecord.has_insurance;
        const totalCost = rentalRecord.total_cost;
        const username = rentalRecord.username;
        const daily_cost = rentalRecord.daily_cost;
        const rental_date = rentalRecord.rental_date;
        const duration = rentalRecord.duration;

        fetch('/project_360/ReturnRentalServlet', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({licensePlate, issueType, hasInsurance, totalCost, username, daily_cost, rental_date, duration}),
        })
                .then(response=>response.json())
                .then(data=>{
                    if (data.status==="success") {
                        let message = "Report submitted successfully.";
                        // Check for additional charges
                        if (data.chargeAmount) {
                            message += "\nAdditional charge due to accident: $"+data.chargeAmount;
                        }
                        // Check for assigned vehicle
                        if (data.newVehicleLicensePlate) {
                            message += "\nYour new vehicle license plate: "+data.newVehicleLicensePlate;
                        }
                        alert(message);
                        window.location.reload();
                    } else {
                        alert("Failed to process your request.");
                    }
                })
                .catch(error=>{
                    console.error('Error:', error);
                    alert("An error occurred while processing your request.");
                });
    } else {
        alert("License plate not found in your rentals.");
    }
});


