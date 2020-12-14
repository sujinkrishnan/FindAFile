console.log('Inside common');
init();


function init() {

    //sessionStorage.setItem('findAFileDomain', 'http://'+ window.location.host)

    if ((sessionStorage.getItem("loginStatus") !== 'AUTHENTICATED')) {
        window.location.href = 'login.html'
    }
}

document.querySelector('.goToHomePage').addEventListener('click', function () {
    window.location.href = 'FindAFileSearch.html';
})

document.getElementById('logOut').addEventListener('click', function () {
    sessionStorage.removeItem('loginStatus');
    sessionStorage.clear;
    window.location.href = 'login.html'
})