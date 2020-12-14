console.log('Im in');

var loginRestult, userName;

document.getElementById('loginSubmit').addEventListener('click', function(){
    submitFunction();
});

document.getElementById('userID').addEventListener('keypress', function(event){
    enterSubmit(event);
});

document.getElementById('password').addEventListener('keypress', function(event){
    enterSubmit(event);
});

function enterSubmit(event){
    if (event.keyCode === 13) {
        submitFunction()
    }
}

function submitFunction() {

    userName = document.getElementById('userID').value;
    checkAndExecuteLogin();
}

function checkAndExecuteLogin() {
    if (userName === "" || document.getElementById('password').value === "" ) {
        alert('Userid / password needs to be filled');
    } else {
        getLoginStatus();
        processLoginResponse()
    }
}


function getLoginStatus() {

    var authRequest = {
        userId: userName,
        password: document.getElementById('password').value
    };

    console.log(authRequest);

    var request = new XMLHttpRequest();
    request.open('POST','/userLoginRequest', false);
    request.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    request.onload = function () {
        loginRestult = JSON.parse(this.responseText);
        if (!(request.status >= 200 && request.status < 400)) {
            loginRestult.loginMessage = "Server error. Please try after sometime";
        }
    }

    request.send(JSON.stringify(authRequest))
}

function processLoginResponse() {

    if (typeof loginRestult != 'undefined' && !loginRestult.loginStatus) {
        document.getElementById('errorMessage').textContent = loginRestult.loginMessage;
    } else if (typeof loginRestult != 'undefined' && loginRestult.loginStatus) {
        sessionStorage.setItem("loginStatus", "AUTHENTICATED");
        sessionStorage.setItem("userName", userName);
        window.location.href = 'FindAFileSearch.html';
    }

}