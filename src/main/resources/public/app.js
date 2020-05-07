var SessionKey;
var email;
var password;

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
var Users = [] ;
var Teams = [] ;
var Leagues = [];
var Seasons = [];
var numberOfDatesToSupply;


$( document ).ready(function() {
    console.log( "ready! david" );
    // init all options ----------------------------------------------------------
  //  $("#leguesGET").click(function() {
        $.get("/Leagues", function (data, status) {
            for (let i = 0; i < data.length; i++) {
                Leagues[i] = data[i];
                let x = document.getElementById("SelectLeague");
                let option = document.createElement("option");
                option.text = Leagues[i].name + " , type:" + Leagues[i].type;
                x.add(option);
            }
        });
    var myLink = document.getElementById('SelectLeague');
    myLink.onchange = function(){
        let leagueType = document.getElementById("SelectLeague").value;
        leagueType = leagueType.substring(leagueType.indexOf("type:")+5);
        let url = "/Seasons/ByLeague/"+leagueType;
        $.get(url, function (data, status) {
            let select  = document.getElementById("SelectSeason");
            let length = select.options.length;
            for (let i = length-1; i >= 0; i--) {
                select.options[i] = null;
            }

            for (let i = 0; i < data.length; i++) {
                Seasons[i] = data[i];
                var x = document.getElementById("SelectSeason");
                var option = document.createElement("option");
                option.text = "start:"+Seasons[i].start + " ,end:" + Seasons[i].end;
                x.add(option);
            }
        });
    }

   // });


    //----------------------------------------------------------------------------

    var numberOfNeededDates=0;
    var numberOfTeamsInLeague=0;
    $("#gameSchedulingContinue").click(function(){
        let leagueType = document.getElementById("SelectLeague").value;
        leagueType = leagueType.substring(leagueType.indexOf("type:")+5);
        let numberOfGamePerTeam = document.getElementById("numberOfGamePerTeam").value;
        let url = "/Seasons/League/"+leagueType+"/numberGamesPerTeam/"+numberOfGamePerTeam ;
        window.alert(url);
        $.get(url, function(data, status){
            numberOfNeededDates = data.number_of_dates_needed ;
            numberOfTeamsInLeague = data.number_of_Teams ;

            window.alert("teams:"+numberOfTeamsInLeague+" , number of dates:"+numberOfNeededDates);
        });

    });

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
                let league = new League(data[i].league.type ,data[i].league.type) ;
                Teams[i] = new Team(data[i].name , data[i].stadium ,league , data[i].status);
            }
            for(let i = 0; i<Teams.length ; i++){
                console.log(Teams[i].name+","+Teams[i].stadium+","+Teams[i].league.type+","+Teams[i].league.name+","+Teams[i].status);
            }
          //  window.alert("Data: " + Teams + "\nStatus: " + status);
        });

    });

    let myTeamSearch;
    $("#getTeam").click(function(){
        let Url  = "/Teams/"+document.getElementById("Tname").value;
        $.get(Url, function(data, status){

                let league = new League(data.league.type ,data.league.type) ;
                myTeamSearch = new Team(data.name , data.stadium ,league , data.status);

                console.log(myTeamSearch.name+","+myTeamSearch.stadium+","+myTeamSearch.league.type+","+myTeamSearch.league.name+","+myTeamSearch.status);
            //  window.alert("Data: " + Teams + "\nStatus: " + status);
        });

    });

    $("#addTeam").click(function(){
        let newTeam = [];
        newTeam[0] = new Object()
        let league= new League(  document.getElementById("LeagueType").value, document.getElementById("leagueName").value );
        newTeam[0].Team = new Team(document.getElementById("TeamName").value,
            document.getElementById("TeamStadium").value,
            league,
            document.getElementById("TeamStatus").value);

        let SecureObj  = new SecurityObj(email,"1000","Login", newTeam) ;
    //    window.alert(JSON.stringify(SecureObj));
        postSend("/Representative/addTeam" , SecureObj);
    });

    $("#loginButton").click(function(){
        let signIn = [] ;
        signIn[0] = new Object() ;
        email = document.getElementById("email").value;
        signIn[0].email = email;
        signIn[0].password = document.getElementById("name").value;
        let SecureObj  = new SecurityObj(email,"1000","Login", signIn) ;
        postSend("/Login" , SecureObj);
    });

    function postSend(url , request) {
        window.alert(JSON.stringify(request));
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

