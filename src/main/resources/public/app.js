var SessionKey;
var email;
var password;
var acsses = [] ; // referee / represintative / user / guest

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


class Alert {
    constructor(description,date,readed) {
        this.description=description;
        this.date = date;
        this.readed=readed ;
    }
}
var Users = [] ;
var Teams = [] ;
var Leagues = [];
var Seasons = [];
var Games = [];
var GameEventsHome = [] ;
var GameEventsAway = [] ;

//------------------------------------------------------------------------ updates

const url = 'http://localhost:8080';
let stompClient;
let newMessages = new Array() ;

function connectToChat(userName) {
    console.log("connecting to chat...")
    let socket = new SockJS(url + '/chat');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log("connected to: " + frame);
        stompClient.subscribe("/topic/messages/" + userName, function (response) {
            console.log("updated!");
            let data = JSON.parse(response.body);
            new Alert(data.description , data.date , data.read);
            window.alert(data.description) ;
        });
    });
}



//-------------------------------------------------------------------------
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
    var numberOfGamePerTeam=0;
    var leagueType;
    $("#gameSchedulingContinue").click(function(){
        leagueType = document.getElementById("SelectLeague").value;
        leagueType = leagueType.substring(leagueType.indexOf("type:")+5);
        numberOfGamePerTeam = document.getElementById("numberOfGamePerTeam").value;
        let url = "/Seasons/League/"+leagueType+"/numberGamesPerTeam/"+numberOfGamePerTeam ;
        window.alert(url);
        $.get(url, function(data, status){
            numberOfNeededDates = data.number_of_dates_needed ;
            numberOfTeamsInLeague = data.number_of_Teams ;

            window.alert("teams:"+numberOfTeamsInLeague+" , number of dates:"+numberOfNeededDates);
        });

    });



    $("#ScheduleGames").click(function(){
        let newRequest = [] ;
        newRequest[0] = new Object() ;
        newRequest[0].league = leagueType;
        let seasonSelection = document.getElementById("SelectSeason").value;
        let start = seasonSelection.substring(seasonSelection.indexOf("start:")+6 , seasonSelection.indexOf(" ,end:"));
        let end = seasonSelection.substring(seasonSelection.indexOf(",end:")+5);
        newRequest[0].start = start;
        newRequest[0].end = end ;
        newRequest[0].numberOfGamesPerTeam = numberOfGamePerTeam ;
        let datesFromForm = document.getElementById("allPassableDates").value;
        let dates = datesFromForm.split(" ");
        newRequest[0].date = [];
        let j = 0;
        for(let i=0 ; i < dates.length ; i=i+3){
            newRequest[0].date[j] = new Object();
            newRequest[0].date[j].date = dates[i] ;
            newRequest[0].date[j].start = dates[i+1] ;
            newRequest[0].date[j].end = dates[i+2] ;

            j=j+1;
        }

        let SecureObj  = new SecurityObj(email,"1000","ScheduleGames", newRequest) ;

        //    window.alert(JSON.stringify(SecureObj));
        postSend("/Representative/scheduleGame" , SecureObj ).then(function (v) {
            console.log("good:"+v);
            printGames(v);
        }).catch(function (v) {
            console.log("failed:"+v);
        });
        // for(let i=0 ; i < games.length ; i++){
        //     Games[i] = new Game(games[i].home.name , games[i].away.name , games[i].date , games[i].start , games[i].end);
        //     console.log(Games[i].name + Game[i]);
        // }
        // window.alert(games) ;
    });

    function printGames(data){
        let Games = [] ;
        console.log("succsses");
        for(let i=0 ; i < data.length ; i++){
            Games[i] = new Game(data[i].id ,data[i].home.name , data[i].away.name , data[i].date , data[i].startTime , data[i].endTime);
            console.log("game id:"+Games[i].id +", home: "+Games[i].home +", away:"+ Games[i].away+", date:"+Games[i].date+", start:"+Games[i].start +", end:"+Games[i].end);
        }
    }
    $("#showGames").click(function(){
        $.get("/Games", function(data, status){
            for(let i=0 ; i < data.length ; i++){
                Games[i] = new Game(data[i].id ,data[i].home.name , data[i].away.name , data[i].date , data[i].startTime , data[i].endTime);
                console.log("game id:"+Games[i].id +", home: "+Games[i].home +", away:"+ Games[i].away+", date:"+Games[i].date+", start:"+Games[i].start +", end:"+Games[i].end);
            }
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

        let SecureObj  = new SecurityObj(email,"1000","addTeam", newTeam) ;

        postSend("/Representative/addTeam" , SecureObj);
    });

    $("#loginButton").click(function(){
        let signIn = [] ;
        signIn[0] = new Object() ;
        email = document.getElementById("email").value;
        signIn[0].email = email;
        signIn[0].password = document.getElementById("name").value;
        let SecureObj  = new SecurityObj(email,"1000","Login", signIn) ;
        postSend("/Login" , SecureObj).then(function (data) {
            console.log(data);
            document.getElementById("myRoles").innerHTML = data.object[0];
            connectToChat(email); // --------------------------------------------------------------- must add for push massages
        }).catch(function (err) {
            console.log("failed:"+err);
        });
    });


    $("#GameChoose").click(function(){
        let refereeOptions = [] ;
        refereeOptions[0] = new Object() ;
        refereeOptions[0].referee_id = email  ;
        let SecureObj  = new SecurityObj(email,"1000","GameChoose", refereeOptions) ;
        postSend("/Referee/getGamesByReferee" , SecureObj).then(function (data) {
            let Games = [] ;
            console.log("succsses");
            for(let i=0 ; i < data.length ; i++) {
                Games[i] = new Game(data[i].id, data[i].home.name, data[i].away.name, data[i].date, data[i].startTime, data[i].endTime);
                console.log("game id:" + Games[i].id + ", home: " + Games[i].home + ", away:" + Games[i].away + ", date:" + Games[i].date + ", start:" + Games[i].start + ", end:" + Games[i].end);
            }
            let select  = document.getElementById("SelectGame");
            for (let i = select.options.length-1; i >= 0; i--) {
                select.options[i] = null;
            }

            for (let i = 0; i < data.length; i++) {
                Seasons[i] = data[i];
                var x = document.getElementById("SelectGame");
                var option = document.createElement("option");
                option.text = "game id:"+Games[i].id+ ", home: "+Games[i].home +", away:"+ Games[i].away+", date:"+Games[i].date ;
                x.add(option);
            }

        }).catch(function (data) {
            console.log("request failed");
        });
    });

    let game_id;
    $("#startGameRefereeOption").click(function(){
        let gameSelection = document.getElementById("SelectGame").value;
        game_id = gameSelection.substring(gameSelection.indexOf("game id:")+8 , gameSelection.indexOf(", home:"));
        let gameID = [] ;
        gameID[0] = new Object() ;
        gameID[0].game_id = game_id  ;
        let SecureObj  = new SecurityObj(email,"1000","startGame", gameID) ;
        postSend("/Referee/getEventProperties",SecureObj).then(function (data) {
            console.log(data);
         //   let select  = document.getElementById("eventTypeSelectAddEvent");
            // for (let i = select.options.length-1; i >= 0; i--) {
            //     select.options[i] = null;
            // }

            for (let i = 0; i < data[0].length; i++) {
                var x = document.getElementById("eventTypeSelectAddEvent");
                var option = document.createElement("option");
                option.text = data[0][i] ;
                x.add(option);
            }
            // for (let i = select.options.length-1; i >= 0; i--) {
            //     select.options[i] = null;
            // }

            var x = document.getElementById("teamSelectAddEvent");
            var option = document.createElement("option");
            option.text = data[1].home ;
            x.add(option);
            var option = document.createElement("option");
            option.text = data[1].away ;
            x.add(option);

        }).catch(function (data) {
            console.log(data);
        });
    });

    $("#addGameEvent").click(function(){

        let minute = document.getElementById("GameMinute").value;
        let team = document.getElementById("teamSelectAddEvent").value;
        let eventType = document.getElementById("eventTypeSelectAddEvent").value;
        let description = document.getElementById("descripitionAddEvent").value;
        let eventProperties = [] ;
        eventProperties[0] = new Object() ;
        eventProperties[0].game_id = game_id  ;
        eventProperties[0].minute = minute  ;
        eventProperties[0].team = team  ;
        eventProperties[0].eventType = eventType  ;
        eventProperties[0].description = description  ;

        let SecureObj  = new SecurityObj(email,"1000","addGameEvent", eventProperties) ;
        //postSendWithoutReturn("/app/Referee/addEventToGame", SecureObj);
        stompClient.send("/app/chat",{},JSON.stringify(SecureObj));
        //console.log("done!!!");
    });

    $("#showGameEvents").click(function(){
        let Url  = "/Games/gameUpdates/"+document.getElementById("gameIdForGameEvents").value;
        $.get(Url, function(data, status){
            let content ="home: \n" ;
            for(let i=0; i<data[0].length ;i++){
                GameEventsHome[i] = new GameEvent(data[0][i].type , data[0][i].description ,data[0][i].minute) ;
                content=content+"type: "+GameEventsHome[i].type+", description:"+GameEventsHome[i].description+", minute:"+GameEventsHome[i].minute+"\n";
            }
            content=content+"\n away: \n"
            for(let i=0; i<data[1].length ;i++){
                GameEventsAway[i] = new GameEvent(data[1][i].type , data[1][i].description ,data[1][i].minute) ;
                content=content+"type: "+GameEventsAway[i].type+", description:"+GameEventsAway[i].description+", minute:"+GameEventsAway[i].minute+"\n";
            }
            document.getElementById("GameEvents").innerText = content;
        });

    });

    $("#SubscibeToGame").click(function () {

        let gameID = [] ;
        gameID[0] = new Object() ;
        gameID[0].game_id =  document.getElementById("gameIdSubscribe").value;
        if(document.getElementById("followStatus").value=="true"){
            gameID[0].Subscribe = true ;
        }else{
            gameID[0].Subscribe = false;
        }
        let SecureObj  = new SecurityObj(email,"1000","SubscibeToGame", gameID) ;
        postSend("/Fan/Subscribe",SecureObj).then(function (data) {
            console.log(data);
        }).catch(function (data) {
            console.log(data);
        });
    });
    $("#getAlerts").click(function () {

        let gameID = [] ;
        gameID[0] = new Object() ;
        let SecureObj  = new SecurityObj(email,"1000","getAlerts", gameID) ;
        postSend("/Fan/getUpdates",SecureObj).then(function (data) {
            let alerts=[];
            let content = "";
            for(let i=0; i<data.length ;i++){
                alerts[i] = new Alert(data[0].description , data[0].date , data[0].readed);
                content=content+"description: "+ alerts[i].description+", date:"+ alerts[i].date+", readed:"+ alerts[i].readed+"\n";
            }
            console.log(content);
        }).catch(function (data) {
            console.log(data);
        });
    });

    $("#setAllAlertsReaded").click(function () {

        let gameID = [] ;
        gameID[0] = new Object() ;
        let SecureObj  = new SecurityObj(email,"1000","SubscibeToGame", gameID) ;
        postSend("/Fan/setUpdatesReaded",SecureObj) ;
    });

    async function postSend(url, request) {
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
        }
        return await p();
    }
    function postSendWithoutReturn(url, request) {
        document.getElementById("code").value = JSON.stringify(request);
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

