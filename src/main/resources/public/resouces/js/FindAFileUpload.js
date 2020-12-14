document.querySelector('.goToSearchPage').addEventListener('click', function(){
    window.location.href='FindAFileSearch.html';
})

document.getElementById('file-upload').addEventListener('change', function(){
    var fileLocation = document.getElementById('file-upload').value;
    var fileName = fileLocation.match(/[^\/\\]+$/);
    console.log(fileName);
    document.querySelector('.upload-file-name').value = fileName;
})


document.getElementById('uploadButton').addEventListener('click',function() {
    console.log("going to submit form");
    if (document.getElementById('descriptionArea').value != ''){
        uploadFile();
    } else {
        alert('Please add document description');
    }
});


function uploadFile() {

    
    document.getElementById('uploadUserName').value = sessionStorage.getItem('userName');
    var form = document.getElementById( "uploadForm" );
    var FD = new FormData( form );

    var request = new XMLHttpRequest();

    request.open('POST', '/findAFileUpload', false);
    //request.setRequestHeader("Content-Type", "multipart/form-data");
    request.onload = function () {
        
        if (!(request.status >= 200 && request.status < 400)) {
            alert("Server error. Please try after sometime");
        } else{ 
            searchResult = this.responseText;
            window.location.href='FindAFileUpload.html';
            alert(searchResult)

        }
    }

    request.send(FD);

}