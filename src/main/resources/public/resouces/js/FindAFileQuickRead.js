var starterKitDetails, htmlGenerater;

init();

function gotoNode(tag){
    console.log(tag);
    sessionStorage.setItem('searchString',tag);
    window.location.href = 'FindAFileSearchResult.html';
}


function init(){
    console.log('Inside init');
    getStarterKitDetails();
    populateList();
    document.getElementById('mainBox').innerHTML = htmlGenerater;
}


function populateList(){
    htmlGenerater =  " ";
    var i;
    var j;
    for (i = 0 ; i < starterKitDetails.length ; i ++) {
        htmlGenerater = htmlGenerater + '<div class="dropdown" id= "dropdown"><button class="dropbtn">'+ starterKitDetails[i].topicName +'</button><div class="dropdown-content" id="dropdown-content">';
        console.log(starterKitDetails[i].topicName)
        for(j = 0; j < starterKitDetails[i].starterKitSubTopics.length; j++){
            htmlGenerater = htmlGenerater + '<button class="selectedTag" type="submit" onClick="gotoNode(\''+ starterKitDetails[i].starterKitSubTopics[j].subTopicHashTag +'\')" > '+starterKitDetails[i].starterKitSubTopics[j].subTopicName+' </button>'
        }
        htmlGenerater = htmlGenerater + '</div></div>'
    }
}



function getStarterKitDetails() {

    var request = new XMLHttpRequest();
    request.open('GET','/getAllTopics', false);
    request.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    request.onload = function () {
        starterKitDetails = JSON.parse(this.responseText);
        if (!(request.status >= 200 && request.status < 400)) {
            alert("Server error. Please try after sometime");
        }
    }

    request.send(JSON.stringify(starterKitDetails))
}