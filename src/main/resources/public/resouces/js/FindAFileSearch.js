document.getElementById('goToUploadPage').addEventListener('click', function () {
    window.location.href = 'FindAFileUpload.html';
});


document.getElementById('searchButton').addEventListener('click', function () {
    if (document.getElementById('searchBar').value != "") {
        processSearch();
    } else {
        alert('Search field needs to be filled');
    }
});


document.getElementById('searchBar').addEventListener('keypress', function () {
    if (event.keyCode === 13 && document.getElementById('searchBar').value != "") {
        processSearch()
    }
});

function processSearch() {
    sessionStorage.setItem('searchString', document.getElementById('searchBar').value);
    window.location.href = 'FindAFileSearchResult.html';
}

document.querySelector('.goToHomePage').addEventListener('click', function () {
    window.location.href = 'FindAFileSearch.html';
})

document.getElementById('logOut').addEventListener('click', function () {
    window.location.href = 'login.html';
})

document.getElementById('goToStarterKit').addEventListener('click', function () {
    window.location.href = 'QuickRead.html';
})


