var searchResult, searchContent;
init();



function init() {
    searchContent = sessionStorage.getItem('searchString');

    searchDocument();
    console.log(searchResult);

    if (searchResult.length > 0) {
        document.getElementById('search-label').textContent = searchResult.length + ' documents found for search \"' + searchContent + '\" :'

    } else {
        document.getElementById('search-label').textContent = 'Your search - \"' + searchContent + '\" did not match any documents.'

    }

    table = document.getElementById("resultTable");
    /*
    searchResult.forEach(row => {
        var newRow = table.insertRow(table.length);
        newRow.insertCell(0).innerHTML = row.fileName;
        newRow.insertCell(1).innerHTML = row.uploadedBy;
        newRow.insertCell(2).innerHTML = row.description;
        newRow.insertCell(3).appendChild(createButton(row.fileName));
    });*/

    var i;
    for(i = 0 ; i < searchResult.length ; i++){
        var row = searchResult[i];
        var newRow = table.insertRow(table.length);
        newRow.insertCell(0).innerHTML = row.fileName;
        newRow.insertCell(1).innerHTML = row.uploadedBy;
        newRow.insertCell(2).innerHTML = row.description;
        newRow.insertCell(3).appendChild(createButton(row.fileName));
    }

}

function createButton(fileName){
    console.log( 'creating button' +fileName);
    var downloadButton = document.createElement("BUTTON");
    downloadButton.id = 'dButton';
    downloadButton.form = 'downloadForm';
    downloadButton.name = 'downloadBtn';
    downloadButton.value = fileName; 
    downloadButton.type = 'submit';
    downloadButton.innerHTML = "Click here to download";
    return downloadButton;
}

function searchDocument() {

    console.log(searchContent);

    var request = new XMLHttpRequest();
    request.open('POST', '/search', false);
    request.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    request.onload = function () {
        searchResult = JSON.parse(this.responseText);
        if (!(request.status >= 200 && request.status < 400)) {
            alert("Server error. Please try after sometime");
        }
    }

    request.send(JSON.stringify(searchContent))
}