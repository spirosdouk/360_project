let cars = [];
let bikes = [];
let bicycles = [];
let scooters = [];

let yr_rent = [];
let rev = [];
let maint = [];
let popular = [];

function createTableForCarPage(data) {
    var html = "<table><tr><th>Lic Plate</th><th>Type</th><th>Brand</th><th>Model</th><th>Rented</th><th>Damaged</th><th>Maint Status</th><th></th></tr><tr>";
    
    data.forEach(item => {
        var rent = item[4];
        var dam = item[5];
        var maint = item[6];

        
        html+="<tr><td>"+item[0]+"</td><td>"+item[1]+"</td><td>"+item[2]+"</td><td>"+item[3]+"</td><td>"+item[4]+"</td><td>"+item[5]+"</td><td>"+item[6]+"</td><td>";
        if(maint !== "ongoing"){
            if(rent !== "true")
                html+="<button class=\"rel-but\" onclick=\"btn_clicked(\'"+item[0]+"\', \'release\')\">Release</button>";
            if(rent !== "true" && dam !== "true")
                html+="<button class=\"maint-but\" onclick=\"btn_clicked(\'"+item[0]+"\', \'maint\')\">Maint</button>";
            if(rent !== "true" && dam === "true")
                html+="<button class=\"repair-but\" onclick=\"btn_clicked(\'"+item[0]+"\', \'repair\')\">Repair</button>";
        }
        html+="</td></tr>"
    });
    html += "</table>";
    return html;
}

function removeActive(){
    document.getElementById("li1").classList.remove('active');
    document.getElementById("li2").classList.remove('active');
    document.getElementById("li3").classList.remove('active');
}

function VehiclePage(){
    removeActive();
    document.getElementById("li1").classList.add('active');
    document.getElementById("input-div").innerHTML = "<h1 id=\"label\">Vehicles</h1> <div id=\"vih-t\"></div>";
    getVehicles();
    document.getElementById("input-div").innerHTML += "<h1 id=\"label\">Add Vehicle</h1>"
    +"<form>"
    +"<input type=\"text\" class= \"form-input\" id=\"LP-input\" placeholder=\"Licence Plate\" onblur=\"checkLP()\" >"
    +"<input type=\"text\" class= \"form-input\" id=\"type-input\" placeholder=\"Type\" onblur=\"checkType()\" required>"
    +"<input type=\"text\" class= \"form-input\" id=\"sub-input\" placeholder=\"Car Subtype\" disabled>"
    +"<input type=\"text\" class= \"form-input\" id=\"brand-input\" placeholder=\"Brand\" required>"
    +"<input type=\"text\" class= \"form-input\" id=\"model-input\" placeholder=\"Model\" required>"
    +"<input type=\"text\" class= \"form-input\" id=\"color-input\" placeholder=\"Color\" required>"
    +"<input type=\"text\" class= \"form-input\" id=\"km-input\" placeholder=\"KM Range\" required>"
    +"<input type=\"text\" class= \"form-input\" id=\"drc-input\" placeholder=\"Daily Rental Cost\" required>"
    +"<input type=\"text\" class= \"form-input\" id=\"dic-input\" placeholder=\"Daily Insurance Cost\" required>"
    +"<button type=\"submit\" id=\"submit-button\" value=\"Register\" onclick=\"addNewVehicle()\">Add Vehicle</button>"
    +"</form>";
}

function StatisticPage(){
    removeActive();
    document.getElementById("li2").classList.add('active');
    document.getElementById("input-div").innerHTML = "<h1 id=\"label\">Statistics</h1>";
    getStatistics();
}

function checkType(){
    if (document.getElementById("type-input").value === "car")
        document.getElementById("sub-input").disabled = false;
}

function checkLP(){
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                console.log("New Vehicle");
            } else {
                document.getElementById("LP-input").value="";
            }
        }
    };

    // Construct the query string manually
    var data = "lic_plate="+document.getElementById("LP-input").value;
    var url = 'AddVehicleAdmin?' + data;

    xhr.open('GET', url);
    xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
    xhr.send();
}

function addNewVehicle(){
    event.preventDefault();
    
    var sub_v = document.getElementById("sub-input").value;
    if(sub_v === ""){
        sub_v = "NULL"
        console.log("Not a car")
    }

    var formData = {
        lic_plate: document.getElementById("LP-input").value,
        brand: document.getElementById("brand-input").value,
        model: document.getElementById("model-input").value,
        color: document.getElementById("color-input").value,
        type: document.getElementById("type-input").value,
        sub: sub_v,
        km_range: document.getElementById("km-input").value,
        drc: document.getElementById('drc-input').value,
        dic: document.getElementById("dic-input").value
    };
    
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                getVehicles();
            } else {
                callback(1);
            }
        }
    };

    // Construct the query string manually
    var data = new URLSearchParams(formData).toString();
    console.log(data);
    var url = 'AddVehicleAdmin?' + data;

    xhr.open('POST', url);
    xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
    xhr.send();
}

function getVehicles(){
    cars.length = 0;
    bikes.length = 0;
    bicycles.length = 0;
    scooters.length = 0;
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                console.log(xhr.responseText);
                var data = xhr.responseText; 
                parseVehicles(data);
                document.getElementById("vih-t").innerHTML=createTableForCarPage(cars);
                document.getElementById("vih-t").innerHTML+=createTableForCarPage(bikes);
                document.getElementById("vih-t").innerHTML+=createTableForCarPage(bicycles);
                document.getElementById("vih-t").innerHTML+=createTableForCarPage(scooters);
            } else {
                console.log("Something occurd")
            }
        }
    };

    var url = 'VehicleAdmin?';

    xhr.open('GET', url);
    xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
    xhr.send();
}

function parseVehicles(data){
    data = data.split("|");
    
    data.forEach(entry => {
        if (entry) {
            let attributes = entry.split(',');
            let type = attributes[1];

            if (type === 'car') {
                cars.push(attributes);
            } else if (type === 'bike') {
                bikes.push(attributes);
            } else if (type === 'bicycle') {
                bicycles.push(attributes);
            } else if (type === 'scooter') {
                scooters.push(attributes);
            }
        }
    });
}

function getStatistics(){
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                var data = xhr.responseText; 
                console.log(data);
                parseStats(data);
                document.getElementById("input-div").innerHTML+= makeYR_TABLE();
                document.getElementById("input-div").innerHTML+= makeRev_TABLE();
                document.getElementById("input-div").innerHTML+= makeMaint_TABLE();
                document.getElementById("input-div").innerHTML+= makePop_TABLE();
            } else {
                console.log("Something occurd")
            }
        }
    };

    var url = 'AdminStatistics?';

    xhr.open('GET', url);
    xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
    xhr.send();
}

function parseStats(data){
    data = data.split("?");
    
    data.forEach(entry => {
        if(entry){       
            let attributes = entry.split('|');
            let type = attributes[0];

            if (type === 'Yearly Rentals') {
                yr_rent.push(attributes);
            } else if (type === 'Revenue by Time Period and Category') {
                rev.push(attributes);
            } else if (type === 'Total Maintenance and Repair Costs') {
                maint.push(attributes);
            } else if (type === 'Most Popular Vehicle') {
                popular.push(attributes);
            }
                
        }
    });
}

function makeYR_TABLE(){
    var title = yr_rent[0][0];
    var html = "<div id=\"yr_table\"><h1 id=\"label\">"+title+"</h1><div id=\"yr-data\">";
    var table1 = "<table><tr><th>Year</th><th>Type</th><th>MIN</th><th>MAX</th><th>Average</th></tr>";
    var table2 = "<table><tr><th>Year</th><th>Type</th><th>MIN</th><th>MAX</th><th>Average</th></tr>";
    
    yr_rent[0].forEach(item => {
        if(item !== "Revenue by Time Period and Category" && item){
            var atr = item.split(",");
            if(atr[0] === "2023")
                table1 +="<tr><td>"+atr[0]+"</td><td>"+atr[1]+"</td><td>"+atr[2]+"</td><td>"+atr[3]+"</td><td>"+atr[4]+"</td></tr>";
            if(atr[0] === "2024")
                table2 +="<tr><td>"+atr[0]+"</td><td>"+atr[1]+"</td><td>"+atr[2]+"</td><td>"+atr[3]+"</td><td>"+atr[4]+"</td></tr>";
        }
    });
    table1 += "</table>";
    table2 += "</table>";
    html += table1 + table2;
    html += "</div></div>";
    return html;
}

function makeRev_TABLE(){
    var title = rev[0][0];
    var html = "<div id=\"rev_table\"><h1 id=\"label\">"+title+"</h1><div id=\"rev-data\">";
    var table1 = "<table><tr><th>Year</th><th>Type</th><th>Revenue</th></tr>";
    var table2 = "<table><tr><th>Year</th><th>Type</th><th>Revenue</th></tr>";
    
    rev[0].forEach(item => {
        if(item !== "Revenue by Time Period and Category" && item){
            var atr = item.split(",");
            if(atr[0] === "2023")
                table1 +="<tr><td>"+atr[0]+"</td><td>"+atr[1]+"</td><td>"+atr[2]+"</td></tr>";
            if(atr[0] === "2024")
                table2 +="<tr><td>"+atr[0]+"</td><td>"+atr[1]+"</td><td>"+atr[2]+"</td></tr>";
        }
    });
    table1 += "</table>";
    table2 += "</table>";
    html += table1 + table2;
    html += "</div></div>";
    return html;
}

function makeMaint_TABLE(){
    var title = maint[0][0];
    var html = "<div id=\"maint_table\"><h1 id=\"label\">"+title+"</h1><div id=\"maint-data\">";
    var table1 = "<table><tr><th>Year</th><th>Type</th><th>Cost</th></tr>";
    var table2 = "<table><tr><th>Year</th><th>Type</th><th>Cost</th></tr>";
    
    maint[0].forEach(item => {
        if(item !== "Total Maintenance and Repair Costs" && item){
            var atr = item.split(",");
            if(atr[0] === "2023")
                table1 +="<tr><td>"+atr[0]+"</td><td>"+atr[1]+"</td><td>"+atr[2]+"</td></tr>";
            if(atr[0] === "2024")
                table2 +="<tr><td>"+atr[0]+"</td><td>"+atr[1]+"</td><td>"+atr[2]+"</td></tr>";
        }
    });
    table1 += "</table>";
    table2 += "</table>";
    html += table1 + table2;
    html += "</div></div>";
    return html;
}

function makePop_TABLE(){
    var title = popular[0][0];
    var html = "<div id=\"pop_table\"><h1 id=\"label\">"+title+"</h1>";
    html += "<table><tr><th>Lic_palte</th><th>Type</th><th>Brand</th><th>Model</th></tr>";
    
    popular[0].forEach(item => {
        if(item !== "Most Popular Vehicle" && item && item.length > 4){
            var atr = item.split(",");
            html+="<tr><td>"+atr[0]+"</td><td>"+atr[1]+"</td><td>"+atr[2]+"</td><td>"+atr[3]+"</td></tr>";
        }
    });
    html += "</table></div>";
    return html;
}

function btn_clicked(vehicle, status){
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                getVehicles();
            } else {
                console.log("Something occurd")
            }
        }
    };

    var url = 'VehicleAdmin?lic_plate='+vehicle+'&status='+status;

    xhr.open('POST', url);
    xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
    xhr.send();
}

function logoutUser() {
    window.location.href = 'index.html';
}

function HistoryPage(){
    removeActive();
    document.getElementById("li3").classList.add('active');
    document.getElementById("input-div").innerHTML = "<h1 id=\"label\">Rental History</h1>";
    getHistory();
}

function getHistory(){
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                var data = xhr.responseText; 
                document.getElementById("input-div").innerHTML += parseHistory(data);
            } else {
                console.log("Something occurd")
            }
        }
    };

    var url = 'RentalHistory?';

    xhr.open('GET', url);
    xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
    xhr.send();
}

function parseHistory(data){
    data = data.split("|");
                    console.log(data);

    var html = "<div id=\"hist_table\"><div id=\"hist-data\">";
    var table1 = "<table><tr><th>Year 2023</th></tr>";
    var table2 = "<table><tr><th>Year 2024</th></tr>";
    
    data.forEach(entry => {
        if (entry) {
            let attributes = entry.split(',');
            let lic_plate = attributes[0];
            let date = attributes[1];
            let tc = attributes[2];
            let returned = attributes[3];
            let car = attributes[4];
            
            if(date.includes("2023")){
                table1+= "<tr><td>"+lic_plate+" was rented in: "+date+" | For a total of: "+ tc +"</td></tr>";
                
                if(car !== "none" && car !== "null")
                    table1+= "<tr><td>"+lic_plate+" was replaced wirh: "+car+"</td></tr>";
                
                if(returned === "true")
                    table1+= "<tr><td>"+lic_plate+" was returned!</td></tr>";
            }

            if(date.includes("2024")){
                table2+= "<tr><td>"+lic_plate+" was rented in: "+date+" | For a total of: "+ tc +"</td></tr>";
                
                if(car !== "none" && car !== "null")
                    table2+= "<tr><td>"+lic_plate+" was replaced wirh: "+car+"</td></tr>";
                
                if(returned === "true")
                    table2+= "<tr><td>"+lic_plate+" was returned!</td></tr>";
            }
            
        }
    });
    
    table1 += "</table>";
    table2 += "</table>";
    html += table1 + table2;
    html += "</div></div>";
    return html;
}