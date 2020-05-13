// Web App JS File

// Variables
var email;
var password;
var fan = false;
var referee = false;
var representative = false;

// Classes

class SecurityObj{
    constructor(userID , reqID , functionName ,object = []) {
        this.userID = userID;
        this.reqID = reqID;
        this.functionName = functionName ;
        this.object = object ;
    }
}

class User{ //UserTest not progect real User
    constructor(name , mail) {
        this.name=name ;
        this.mail = mail ;
    }
}

class GameEvent{
    constructor(type , description , minute) {
        this.type=type ;
        this.description = description ;
        this.minute = minute ;
    }
}

class Team{
    constructor(name, stadium ,league,status) {
        this.name = name;
        this.stadium = stadium;
        this.league = league;
        this.status = status;
    }
}
class League {
    constructor(type,name) {
        this.type = type;
        this.name=name ;
    }
}

class Season {
    constructor(start,end) {
        this.start = start;
        this.end=end ;
    }
}

class Game {
    constructor(id,home,away,date,start,end) {
        this.id=id;
        this.home = home;
        this.away=away ;
        this.date = date;
        this.start = start;
        this.end=end ;
    }
}

// ON START
$(document).ready(function () {
    document.getElementById('userEmail').addEventListener('focus', () => {
        document.getElementById('userEmailLabel').style.marginBottom = '1%';
    });
    document.getElementById('userEmail').addEventListener('blur', () => {
        if (document.getElementById('userEmail').value.length == 0) {
            document.getElementById('userEmailLabel').style.marginBottom = '-8%';
        }
    });
    document.getElementById('userPassword').addEventListener('focus', () => {
        document.getElementById('userPasswordLabel').style.marginBottom = '1%';
    });
    document.getElementById('userPassword').addEventListener('blur', () => {
        if (document.getElementById('userPassword').value.length == 0) {
            document.getElementById('userPasswordLabel').style.marginBottom = '-8%';
        }
    });
    document.querySelectorAll('input[type=text], input[type=email]').forEach(
        input => {
            input.setAttribute('autocomplete', 'off')
        }
    );
    // LOGIN
    $("#loginSubmit").click(function (event) {
        event.preventDefault();
        window.alert("DOD");
        let signIn = [];
        signIn[0] = new Object();
        email = document.getElementById("userEmail").value;
        signIn[0].email = email;
        signIn[0].password = document.getElementById("userPassword").value;
        let SecureObj = new SecurityObj(email, "1000", "Login", signIn);
        postSend("/Login", SecureObj).then(function (data) {
            console.log(data);
            // document.getElementById("myRoles").innerHTML = data.object[0];
            for (let i = 0; i < data.object[0].length; i++) {
                if (data.object[0][i] == 'Fan') fan = true;
                if (data.object[0][i] == 'Representative') representative = true;
                if (data.object[0][i] == 'Referee') referee = true;
            }
            window.alert(fan);
        }).catch(function (err) {
            console.log("failed:" + err);
        });
    });

    // General Functions
    async function postSend(url, request) {
        // document.getElementById("code").value = JSON.stringify(request);
        console.log(JSON.stringify(request));
        const p = async () => {
            try {
                var res;
                await $.ajax({
                    type: "POST",
                    url: url,
                    // The key needs to match your method's input parameter (case-sensitive).
                    data: JSON.stringify(request),
                    contentType: "application/json; charset=utf-8",
                    dataType: "json",
                    processData: false,
                    success: function (resources) {
                        res = resources;
                    },
                    error: function (err) {
                        console.log(err);
                    }
                });
                return res;
            } catch (err) {
                console.log(err);
            }
        };
        return await p();

    }

    function postSendWithoutReturn(url, request) {
        // document.getElementById("code").value = JSON.stringify(request);
        console.log(JSON.stringify(request));
        $.ajax({
            type: "POST",
            url: url,
            // The key needs to match your method's input parameter (case-sensitive).
            data: JSON.stringify(request),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            processData: false,
            success: function () {
            },
            error: function () {
            }
        });
    }
});

