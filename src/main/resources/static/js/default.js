

// Toggle between showing and hiding the sidebar, and add overlay effect
function w3_open() {
    // Get the Sidebar
    var mySidebar = document.getElementById("mySidebar");

// Get the DIV with overlay effect
    var overlayBg = document.getElementById("myOverlay");
    if (mySidebar.style.display === 'block') {
        mySidebar.style.display = 'none';
        overlayBg.style.display = "none";
    } else {
        mySidebar.style.display = 'block';
        overlayBg.style.display = "block";
    }
}

// Close the sidebar with the close button
function w3_close() {
    // Get the Sidebar
    var mySidebar = document.getElementById("mySidebar");
    // Get the DIV with overlay effect
    var overlayBg = document.getElementById("myOverlay");
    mySidebar.style.display = "none";
    overlayBg.style.display = "none";
}

function w3_select(element) {
    element.classList.add('w3-blue');

}

send = (url, value, method) => {
    var payload = { method: method,
        mode: 'cors',
        cache: 'default',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'},
        body: JSON.stringify(value)
    };
    fetch(url, payload)
        .then(response => response.json()) // Result from the
        .then(data => {
            console.log(data) // Prints result from `response.json()`
        })
        .catch(error => console.error(error))
}

updateInterval = (id) => {
    var value = document.getElementById(id).value;
    var object = {updateInterval: value};
    send('/setting', object, 'PUT');
    showMessageBox('Обновлено: ' + value)
}

showMessageBox = (message) => {
    var x = document.getElementById("snackbar");
    x.textContent = message;
    x.className = "show";
    setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);

}

showMessageBoxById = (id) => {
    var value = document.getElementById(id).value;
    showMessageBox(value);
}