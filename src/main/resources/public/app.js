var SessionKey;
var email;
var password;

class SecurityObj{
    constructor(UserID , reqID , functionName ,object = []) {
        this.UserID = UserID;
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

class Team{
    constructor(name, stadium ,league,status) {
        this.name = name;
        this.stadium = stadium;
        this.league = league;
        this.status = status;
    }
}

var Users = [] ;
var Teams = [] ;



$( document ).ready(function() {
    console.log( "ready! david" );
    //window.alert("alertttt!!!!!!!!!!!!!!!!!!!");

    $("#b1").click(function(){
        $.get("/Users", function(data, status){

            for(let i=0  ; i < data.length ; i++){
                Users[i] = data[i];
                //Users[i] = new User(data[i].name , data[i].mail);
            }
            for(let i = 0; i<Users.length ; i++){
                console.log(Users[i].name+","+Users[i].mail);
            }
            window.alert("Data: " + Users + "\nStatus: " + status);
        });

    });

    $("#b3").click(function(){
        $.get("/Teams", function(data, status){

            for(let i=0  ; i < data.length ; i++){
                Teams[i] = data[i];
            }
            for(let i = 0; i<Teams.length ; i++){
                console.log(Teams[i]);
            }
            window.alert("Data: " + Teams + "\nStatus: " + status);
        });

    });

    $("#loginButton").click(function(){
        let signIn = [] ;
        signIn[0] = new Object() ;
        signIn[0].email = document.getElementById("email").value;
        signIn[0].password = document.getElementById("name").value;
        var SecureObj  = new SecurityObj("1","12234556","Login", signIn) ;
        postSend("/Login" , SecureObj);
    });

    function postSend(url , request) {
        $.ajax({
            type: "POST",
            url: url,
            // The key needs to match your method's input parameter (case-sensitive).
            data: JSON.stringify(request) ,
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function(data){
                window.alert(data.reqID);
            },
            failure: function(errMsg) {
                alert(errMsg);
            }
        });
    }
});

