// Web App JS File

// Variables
var email;
var password;


// ON START
$(document).ready(function () {
    document.getElementById('userEmail').addEventListener('focus', () => {
        document.getElementById('userEmailLabel').style.marginBottom = '1%';
    })
});

$(document).ready(function () {
    document.getElementById('userEmail').addEventListener('blur', () => {
        if (document.getElementById('userEmail').value.length == 0) {
            document.getElementById('userEmailLabel').style.marginBottom = '-8%';
        }
    })
});

$(document).ready(function () {
    document.getElementById('userPassword').addEventListener('focus', () => {
        document.getElementById('userPasswordLabel').style.marginBottom = '1%';
    })
});

$(document).ready(function () {
    document.getElementById('userPassword').addEventListener('blur', () => {
        if (document.getElementById('userPassword').value.length == 0) {
            document.getElementById('userPasswordLabel').style.marginBottom = '-8%';
        }
    })
});

$(document).ready(function () {
    document.querySelectorAll('input[type=text], input[type=email]').forEach(
        input => {
            input.setAttribute('autocomplete', 'off')
        }
    );
});

// General Functions

// LOGIN
$(document).ready(function () {
    function postSend(url, request) {
        window.alert(JSON.stringify(request));
        $.ajax({
            type: "POST",
            url: url,
            // The key needs to match your method's input parameter (case-sensitive).
            data: JSON.stringify(request),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (data) {
                window.alert(data.reqID);
            },
            failure: function (errMsg) {
                alert(errMsg);
            }
        });
    }

    $("#loginSubmit").click(function () {
        let signIn = [];
        signIn[0] = new Object();
        email = document.getElementById("userEmail").value;
        signIn[0].email = email;
        signIn[0].password = document.getElementById("userPassword").value;
        let SecureObj = new SecurityObj(email, "1000", "Login", signIn);
        postSend("/Login", SecureObj);
    });
});