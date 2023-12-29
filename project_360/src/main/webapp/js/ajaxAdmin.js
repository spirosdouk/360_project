google.charts.load('current', {'packages':['corechart']});
google.charts.setOnLoadCallback(function() {
    googleChartsLoaded = true;
});

function callChartFunction(chartFunction) {
    if (googleChartsLoaded) {
        chartFunction();
    } else {
        google.charts.setOnLoadCallback(chartFunction);
    }
}

var numOfPK = 0;
var numOfPO = 0;
var numOfDogs = 0;
var numOfCats = 0;
var profit;
var keep_prof;

function createOwnerTable(data) {
    let html = "<table class=\"table table-striped\"><tr><th>Username</th><th>First Name</th><th>Last Name</th><th></th></tr>";
    var usertype = "PO"

    let extractedData = data.data.map(item => {
        return { owner_id: item.owner_id , username: item.username, firstname: item.firstname, lastname: item.lastname };
    });
    
    numOfPO = 0;
    extractedData.forEach(item => {
        var id = item.owner_id;
        numOfPO++;
        
        html+="<tr><td>"+item.username+"</td><td>"+item.firstname+"</td><td>"+item.lastname+"</td><td>";
        html+="<button class=\"delete-but\" onclick=\"delete"+usertype+"("+id+")\">Delete</button>";
        html+="</td></tr>"
    });
   
    return html;
}

function createKeeperTable(data) {
    let html = "<table class=\"table table-striped\"><tr><th>Username</th><th>First Name</th><th>Last Name</th><th></th></tr>";
    var usertype = "PK";
    
    console.log(data);
    let extractedData = data.data.map(item => {
        return { keeper_id: item.keeper_id , username: item.username, firstname: item.firstname, lastname: item.lastname };
    });
    
    numOfPK = 0;
    extractedData.forEach(item => {
        var id = item.keeper_id;
        numOfPK++;
        
        html+="<tr><td>"+item.username+"</td><td>"+item.firstname+"</td><td>"+item.lastname+"</td><td>";
        html+="<button class=\"delete-but\" onclick=\"delete"+usertype+"("+id+")\">Delete</button>";
        html+="</td></tr>"
    });
   
    return html;
}

function getPO(){
    const xhr = new XMLHttpRequest();
    xhr.onload = function () {
            if (xhr.readyState === 4 && xhr.status === 200) {
                document.getElementById("po-table").innerHTML=createOwnerTable(JSON.parse(xhr.responseText));
                getProfit();
                getPets();
                getPK();
            } else if (xhr.status !== 200) {
                document.getElementById('msg')
                        .innerHTML = 'Request failed. Returned status of ' + xhr.status + "<br>";
            }
        };
    xhr.open("GET", "http://localhost:4560/adminAPI/PO");
    xhr.setRequestHeader("Accept", "application/json");
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send();
}

function getPK(){
    const xhr = new XMLHttpRequest();
    xhr.onload = function () {
            if (xhr.readyState === 4 && xhr.status === 200) {
                console.log(xhr.responseText);
                document.getElementById("pk-table").innerHTML=createKeeperTable(JSON.parse(xhr.responseText));
                console.log(numOfPO);
                callChartFunction(drawUsersChart);
                callChartFunction(drawPetsChart);
                callChartFunction(drawProfitsChart);
            } else if (xhr.status !== 200) {
                document.getElementById('msg')
                        .innerHTML = 'Request failed. Returned status of ' + xhr.status + "<br>";
            }
        };
    xhr.open("GET", "http://localhost:4560/adminAPI/PK");
    xhr.setRequestHeader("Accept", "application/json");
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send();
}

function deletePO(id){
    const xhr = new XMLHttpRequest();
    xhr.onload = function () {
            if (xhr.readyState === 4 && xhr.status === 200) {
                document.getElementById("po-table").innerHTML=createOwnerTable(JSON.parse(xhr.responseText));
                callChartFunction(drawUsersChart);
            } else if (xhr.status !== 200) {
                document.getElementById('msg')
                        .innerHTML = 'Request failed. Returned status of ' + xhr.status + "<br>";
            }
        };
    xhr.open("DELETE", "http://localhost:4560/adminAPI/PO/delete/"+id);
    xhr.setRequestHeader("Accept", "application/json");
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send();
}

function deletePK(id){
    const xhr = new XMLHttpRequest();
    xhr.onload = function () {
            if (xhr.readyState === 4 && xhr.status === 200) {
                document.getElementById("pk-table").innerHTML=createKeeperTable(JSON.parse(xhr.responseText));
                callChartFunction(drawUsersChart);
            } else if (xhr.status !== 200) {
                document.getElementById('msg')
                        .innerHTML = 'Request failed. Returned status of ' + xhr.status + "<br>";
            }
        };
    xhr.open("DELETE", "http://localhost:4560/adminAPI/PK/delete/"+id);
    xhr.setRequestHeader("Accept", "application/json");
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send();
}

function getProfit(){
    const xhr = new XMLHttpRequest();
    xhr.onload = function () {
            if (xhr.readyState === 4 && xhr.status === 200) {
                console.log(xhr.responseText);
                var numbers = JSON.parse(xhr.responseText);
        
                profit = numbers[0];
                keep_prof = numbers[1];
            } else if (xhr.status !== 200) {
                document.getElementById('msg')
                        .innerHTML = 'Request failed. Returned status of ' + xhr.status + "<br>";
            }
        };
    xhr.open("GET", "http://localhost:4560/adminAPI/profit");
    xhr.setRequestHeader("Accept", "application/json");
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send();
}

function getPets(){
    const xhr = new XMLHttpRequest();
    xhr.onload = function () {
            if (xhr.readyState === 4 && xhr.status === 200) {
                console.log(xhr.responseText);
                var numbers = JSON.parse(xhr.responseText);
        
                numOfCats = numbers[0];
                numOfDogs = numbers[1];
            } else if (xhr.status !== 200) {
                document.getElementById('msg')
                        .innerHTML = 'Request failed. Returned status of ' + xhr.status + "<br>";
            }
        };
    xhr.open("GET", "http://localhost:4560/adminAPI/pets");
    xhr.setRequestHeader("Accept", "application/json");
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send();
}

function drawUsersChart() {
    var data = google.visualization.arrayToDataTable([
        ['Chart', 'Numbers'],
        ['Owners', numOfPO],
        ['Keepers', numOfPK]
    ]);

    var options = {
        title: 'Number of Users'
    };

    var chart = new google.visualization.PieChart(document.getElementById('users-chart'));
    chart.draw(data, options);
}

function drawPetsChart() {
    var data = google.visualization.arrayToDataTable([
        ['Chart', 'Numbers'],
        ['Cats', numOfCats],
        ['Dogs', numOfDogs]
    ]);

    var options = {
        title: 'Number of Pets'
    };

    var chart = new google.visualization.PieChart(document.getElementById('pets-chart'));
    chart.draw(data, options);
}

function drawProfitsChart() {
    var data = google.visualization.arrayToDataTable([
        ['Chart', 'Numbers'],
        ['Company', profit],
        ['Keepers', keep_prof]
    ]);

    var options = {
        title: 'Profits'
    };

    var chart = new google.visualization.PieChart(document.getElementById('profits-chart'));
    chart.draw(data, options);
}