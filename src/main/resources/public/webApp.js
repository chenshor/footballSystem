// Web App JS File

// Variables
var email;
var password;
var fan = false;
var referee = false;
var representative = false;
var secretKey;

// Classes
class SecurityObj {
    constructor(userID, reqID, functionName, object = []) {
        this.userID = userID;
        this.reqID = reqID;
        this.functionName = functionName;
        this.object = object;
    }
}

class User {
    constructor(name, mail) {
        this.name = name;
        this.mail = mail;
    }
}

class GameEvent {
    constructor(type, description, minute) {
        this.type = type;
        this.description = description;
        this.minute = minute;
    }
}

class Team {
    constructor(name, stadium, league, status) {
        this.name = name;
        this.stadium = stadium;
        this.league = league;
        this.status = status;
    }
}

class League {
    constructor(type, name) {
        this.type = type;
        this.name = name;
    }
}

class Season {
    constructor(start, end) {
        this.start = start;
        this.end = end;
    }
}

class Game {
    constructor(id, home, away, date, start, end) {
        this.id = id;
        this.home = home;
        this.away = away;
        this.date = date;
        this.start = start;
        this.end = end;
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
});


//<editor-fold desc="LOGIN">

// LOGIN function
let stompClient;
let newMessages = new Array();

function connectToChat(userName) {
    console.log("connecting to chat...");
    let socket = new SockJS('/chat');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log("connected to: " + frame);
        stompClient.subscribe("/topic/messages/" + userName, function (response) {
            console.log("updated!");
            let data = JSON.parse(response.body);
            // new Alert(data.description, data.date, data.read);
            window.alert(data.description + ' - ' + data.date);
            let gameID = [];
            gameID[0] = new Object();
            let SecureObj = new SecurityObj(email, secretKey, "getAlerts", gameID);
            postSend("/Fan/getUpdates", SecureObj).then(function (data) {
                // let alerts = [];
                let content = "";
                $('#NewAlertsContent').empty();
                let number = 0;
                for (let i = 0; i < data.length; i++) {
                    if (!data[i].readed) {
                        number += 1;
                    }
                }
                $('#notificationsNumber').html(number);
            }).catch(function (data) {
            });
        });
    });
}

$(document).ready(function () {
    $("#loginSubmit").click(function (event) {
        event.preventDefault();
        let signIn = [];
        signIn[0] = new Object();
        email = document.getElementById("userEmail").value;
        signIn[0].email = email;
        signIn[0].password = document.getElementById("userPassword").value;
        let SecureObj = new SecurityObj(email, secretKey, "Login", signIn);
        postSend("/Login", SecureObj).then(function (data) {
            // data is 'null' when authentication fail
            if (data == null) {
                return;
            }
            secretKey = data.reqID;
            for (let i = 0; i < data.object[0].length; i++) {
                if (data.object[0][i] == 'Fan') fan = true;
                if (data.object[0][i] == 'Representative') representative = true;
                if (data.object[0][i] == 'Referee') referee = true;
            }
            connectToChat(email);
            changeLayout(fan, representative, referee);

            if(fan == true){
                let gameID = [];
                gameID[0] = new Object();
                let SecureObj = new SecurityObj(email, secretKey, "getAlerts", gameID);
                postSend("/Fan/getUpdates", SecureObj).then(function (response) {
                    // let alerts = [];
                    let content = "";
                    $('#NewAlertsContent').empty();
                    let number = 0;
                    for (let i = 0; i < response.length; i++) {
                        if (!response[i].readed) {
                            number += 1;
                        }
                    }
                    $('#notificationsNumber').html(number);
                }).catch(function (data) {
                });
            }


        }).catch(function (err) {
            console.log("failed: " + err);
        });
    });

    $('#guest-btn').click(function (event) {
        event.preventDefault();
        changeLayout(false, false, false);
    })
});



//</editor-fold>

//<editor-fold desc="GENERAL FUNCTIONS">

// POST request with return value
async function postSend(url, request) {
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

// POST request with no return value
function postSendWithoutReturn(url, request) {
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

// CHANGE LAYOUT - based on user privileges
function changeLayout(fan, representative, referee) {
    document.getElementById('login').hidden = true;
    document.getElementById('mainLayout').hidden = false;
    if (fan == true) {
        $('#notifications').removeAttr('hidden');
    }
    if (representative == true) {
        $('#representativeButton').removeAttr('hidden');
        $.get("/Seasons", function (data) {
            cards = $();
            data.forEach(function (item) {
                cards = (seasonSelect(item));
                $('#SeasonypeGame').append(cards);
            });
        });
    }
    if (referee == true) {
        $('#refereeButton').removeAttr('hidden');
    }
    hideAll();
}

function hideAll() {
    $('#leaguesView').removeAttr('hidden');
    $('#leaguesView').hide();
    $('#seasonsView').removeAttr('hidden');
    $('#seasonsView').hide();
    $('#TeamsView').removeAttr('hidden');
    $('#TeamsView').hide();
    $('#GamesView').removeAttr('hidden');
    $('#GamesView').hide();
    $('#RefView').removeAttr('hidden');
    $('#RefereeView').removeAttr('hidden');
    $('#RefereeView').hide();
    $('#RefView').hide();
    $('#RepView').removeAttr('hidden');
    $('#RepView').hide();
    $('#AlertsView').removeAttr('hidden');
    $('#AlertsView').hide();
}

//</editor-fold>

//<editor-fold desc="CONTENT GENERATOR">

var cards = $();

// Create League View Content
$(document).ready(function () {
    $('#leagueButton').click(function () {
        hideAll();
        $('#leaguesView').show();
        $('#leagueCards').empty();
        $.get("/Leagues", function (data) {
            cards = $();
            data.forEach(function (item) {
                cards = (createLeagueCard(item));
                $('#leagueCards').append(cards);
            });
        });
    });
});

// Info Card Builder - league
function createLeagueCard(cardInfo) {
    var leagueCardTemplate = [
        '<div class="card" style="width: 18rem;">',
        '<div class="card-body">',
        '<h5 class="card-title">',
        cardInfo.name || 'undefined',
        '</h5>',
        '<h6 class="card-subtitle mb-2 text-muted">',
        cardInfo.type || 'undefined',
        '</h6>',
        '<button class="moreInfoButton">More Info</button></div></div>'
    ];
    return $(leagueCardTemplate.join(''));
}


// Create Season View Content
$(document).ready(function () {
    $('#seasonButton').click(function () {
        hideAll();
        $('#seasonsView').show();
        $('#seasonCards').empty();
        $.get("/Seasons", function (data) {
            cards = $();
            data.forEach(function (item) {
                cards = (createSeasonCard(item));
                $('#seasonCards').append(cards);
            });
        });
    });
});

// Info Card Builder - league
function createSeasonCard(cardInfo) {
    var seasonCardTemplate = [
        '<div class="card" style="width: 18rem;">',
        '<div class="card-body">',
        '<h5 class="card-title">Season</h5>',
        '<h6 class="card-subtitle mb-2 text-muted">',
        cardInfo.start.substr(0, 4) || 'undefined',
        ' - ',
        cardInfo.end.substr(0, 4) || 'undefined',
        '</h6>',
        '<button class="moreInfoButton">More Info</button></div></div></div></div>'
    ];
    return $(seasonCardTemplate.join(''));
}

$(document).ready(function () {
    $('#teamsButton').click(function () {
        hideAll();
        $('#TeamsView').show();
        $('#TeamCards').empty();
        $.get("/Teams", function (data) {
            cards = $();
            data.forEach(function (item) {
                cards = (createTeamsCard(item));
                $('#TeamCards').append(cards);
            });
        });
    });
});

function createTeamsCard(cardInfo) {
    var teamsCardTemplate = [
        '<div class="card" style="width: 18rem;">',
        '<div class="card-body">',
        '<h5 class="card-title">',
        cardInfo.name || 'undefined',
        '</h5>',
        '<h6 class="card-subtitle mb-2 text-muted">Stadium: ',
        cardInfo.stadium || 'undefined',
        '</h6>',
        '<h6 class="card-subtitle mb-2 text-muted">League: ',
        cardInfo.league.name || 'undefined',
        '</h6>',
        '<button class="moreInfoButton">More Info</button></div></div></div></div>'
    ];
    return $(teamsCardTemplate.join(''));
}

$(document).ready(function () {
    $('#gamesButton').click(function () {
        hideAll();
        $('#GamesView').show();
        $('#GamesCards').empty();
        $.get("/Games", function (data) {
            cards = $();
            data.forEach(function (item) {
                cards = (createGamesCard(item));
                $('#GamesCards').append(cards);
            });
        });
    });
});

function createGamesCard(cardInfo) {
    var gamesCardTemplate = [
        '<div class="card" style="width: 18rem;">',
        '<div class="card-body">',
        '<h5 class="card-title">',
        cardInfo.home.name,
        '<br>VS<br>',
        cardInfo.away.name,
        '</h5>',
        '<hr>',
        '<h6 class="card-subtitle mb-3 text-muted">Stadium: ',
        cardInfo.home.stadium || 'undefined', //
        '</h6>',
        '<h6 class="card-subtitle mb-3 text-muted">Date: ',
        cardInfo.date || 'undefined', //
        '</h6>',
        '<button class="moreInfoButton" id="button-',
        cardInfo.id || "undefined",
        '" onclick="follow(\'',
        cardInfo.id || "undefined",
        '\')">Follow</button></div></div></div></div>'
    ];
    return $(gamesCardTemplate.join(''));
}

function follow(game_id) {
    let gameID = [] ;
    gameID[0] = new Object() ;
    gameID[0].game_id = game_id ;
    if ($('#button-' + game_id).html() == 'Follow') {
        gameID[0].Subscribe = true;
        $('#button-' + game_id).html('UnFollow');
    } else {
        gameID[0].Subscribe = false;
        $('#button-' + game_id).html('Follow');
    }
    let SecureObj = new SecurityObj(email, secretKey, "SubscibeToGame", gameID);
  //  window.alert(JSON.stringify(SecureObj));
    postSend("/Fan/Subscribe", SecureObj).then(function (data) {
        console.log(data);
    }).catch(function (data) {
        console.log(data);
    });
}

//</editor-fold>

function createGameOption(gameOptionInfo) {
    let gameOptionTemplate = [
        '<div class="option">',
        '<input type="radio" class="radio" id="gameOption',
        gameOptionInfo.id || 'undefined',
        '" name="GameOption">',
        '<label for="gameOption',
        gameOptionInfo.id || 'undefined',
        '">',
        gameOptionInfo.home.name || 'undefined',
        ' VS ',
        gameOptionInfo.away.name || 'undefined',
        '</label></div>'
    ];
    var gameOptionGenerated = $(gameOptionTemplate.join(''));
    gameOptionGenerated.click(function () {
        document.querySelector(".selected").innerHTML = gameOptionInfo.home.name + ' VS ' + gameOptionInfo.away.name;
        GameOptionID = gameOptionInfo.id;
        document.querySelector(".option-container").classList.remove("active");
    });
    return gameOptionGenerated;
}

let activeGame = false;
let interval;
let minute;
let strip = 10;

$('#StartEnd').click(function () {
    activeGame = !activeGame;
    if (activeGame) {
        document.querySelector('#StartEnd').innerHTML = 'END GAME';
        $('#addEvent').removeAttr("disabled");
        $('#sideNavBar').css('pointer-events', 'none');
        $('#HomeEvents').empty();
        $('#AwayEvents').empty();
        minute = 0;
        strip = 10;
        document.querySelector('#minute').innerHTML = minute + "'";
        $('#minutePB').css('width', strip + '%').attr('aria-valuenow', strip);
        interval = setInterval(updateMinute, 1000);
    } else {
        document.querySelector('#StartEnd').innerHTML = 'START GAME';
        $('#sideNavBar').css('pointer-events', 'auto');
        $('#eventTypeSelectAddEvent').empty();
        $('#teamSelectAddEvent').empty();
        clearInterval(interval);
        finalReport();
    }
});

function finalReport() {

}

function updateMinute() {
    if (minute >= 89) clearInterval(interval);
    minute += 1;
    document.querySelector('#minute').innerHTML = minute.toString() + "'";
    strip += 1;
    $('#minutePB').css('width', strip + '%').attr('aria-valuenow', strip);
}

var GameOptions = $();
var GameOptionID;
let game_id;

function setID(id){
    GameOptionID =id;
}

$('#refereeButton').click(function () {
    hideAll();
    $("#leaguesView").css("display", "none");
    $("#seasonsView").css("display", "none");
    $("#TeamsView").css("display", "none");
    $("#GamesView").css("display", "none");
    $("#RefereeView").css("display", "none");
    $("#RefView").css("display", "block");
    let refereeOptions = [];
    refereeOptions[0] = new Object();
    refereeOptions[0].referee_id = email;
    let SecureObj = new SecurityObj(email, secretKey, "GameChoose", refereeOptions);
    postSend("/Referee/getGamesByReferee", SecureObj).then(function (data) {
        let Games = [];
        $('#gameOptions').empty();
        GameOptions = $();
        data.forEach(game => {
            GameOptions = (createGameOption(game));
            $('#gameOptions').append(GameOptions);
        });
    }).catch(function (data) {
        console.log("request failed");
    });
    minute = 0;
    strip = 10;
    document.querySelector('#minute').innerHTML = minute + "'";
    $('#minutePB').css('width', strip + '%').attr('aria-valuenow', strip);
    document.querySelector(".selected").addEventListener("click", () => {
        document.querySelector(".option-container").classList.toggle("active");
    });
});

let game_id_chosen;

$("#selectGame").click(function () {
    let gameID = [];
    gameID[0] = new Object();
    gameID[0].game_id = GameOptionID;
    game_id_chosen = GameOptionID;
    let SecureObj = new SecurityObj(email, secretKey, "startGame", gameID);
    postSend("/Referee/getEventProperties", SecureObj).then(function (data) {
        $('#eventTypeSelectAddEvent').empty();
        $('#teamSelectAddEvent').empty();
        var eventType = document.getElementById("eventTypeSelectAddEvent");
        for (let i = 0; i < data[0].length; i++) {
            var option = document.createElement("option");
            option.text = data[0][i];
            eventType.add(option);
        }
        var teamSelector = document.getElementById("teamSelectAddEvent");
        var option = document.createElement("option");
        option.text = data[1].home;
        document.querySelector('#homeTeam').innerHTML = data[1].home;
        teamSelector.add(option);
        var option = document.createElement("option");
        option.text = data[1].away;
        document.querySelector('#awayTeam').innerHTML = data[1].away;
        teamSelector.add(option);
        $('#RefView').hide();
        $('#RefereeView').removeAttr('hidden');
        $('#RefereeView').css('display', 'block');
        $('#HomeEvents').empty();
        $('#AwayEvents').empty();
    }).catch(function (data) {
        console.log(data);
    });

});

function createHomeEvent(data) {
    let homeEvent = [
        '<div>',
        data.minute || 'undefined',
        ' - ',
        data.eventType || 'undefined',
        ' - ',
        data.description || 'undefined',
        '</div>'
    ];
    return $(homeEvent.join(''));
}

function createAwayEvent(data) {
    let awayEvent = [
        '<div>',
        data.eventType || 'undefined',
        ' - ',
        data.description || 'undefined',
        ' - ',
        data.minute || 'undefined',
        '</div>'
    ];
    return $(awayEvent.join(''));
}

let homeGoals = 0;
let awayGoals = 0;

$("#addEvent").click(function (event) {
    event.preventDefault();
    let team = document.getElementById("teamSelectAddEvent").value;
    let eventType = document.getElementById("eventTypeSelectAddEvent").value;
    let description = document.getElementById("description").value;
    let eventProperties = [];
    eventProperties[0] = new Object();
    eventProperties[0].game_id = game_id_chosen;
    eventProperties[0].minute = minute;
    eventProperties[0].team = team;
    eventProperties[0].eventType = eventType;
    eventProperties[0].description = description;

    let SecureObj = new SecurityObj(email, secretKey, "addGameEvent", eventProperties);
   // window.alert(JSON.stringify(SecureObj));
    //postSendWithoutReturn("/app/Referee/addEventToGame", SecureObj);
    stompClient.send("/app/chat", {}, JSON.stringify(SecureObj));
    //console.log("done!!!");
    let emptyEvent = $('<div><br></div>');
    if (team == document.querySelector('#homeTeam').innerHTML) {
        if (eventType == 'goal') {
            homeGoals += 1;
            $('#score').html(homeGoals + ' : ' + awayGoals);
        }
        let event = createHomeEvent(eventProperties[0]);
        $('#HomeEvents').append(event);
        $('#AwayEvents').append(emptyEvent);
    } else {
        if (eventType == 'goal') {
            awayGoals += 1;
            $('#score').html(homeGoals + ' : ' + awayGoals);
        }
        let event = createAwayEvent(eventProperties[0]);
        $('#AwayEvents').append(event);
        $('#HomeEvents').append(emptyEvent);
    }
});

function createAlert(alert) {
    let alertTemplate = [
        '<div class="alertUpdate"><h5>',
        alert.date,
        ' - ',
        alert.description,
        '</h5></div>'
    ];
    return $(alertTemplate.join(''));
}

$("#alertsButton").click(function () {
    hideAll();
    $('#AlertsView').show();
    $('#notificationsNumber').html(0);
    let gameID = [];
    gameID[0] = new Object();
    let SecureObj = new SecurityObj(email, secretKey, "getAlerts", gameID);
    postSend("/Fan/getUpdates", SecureObj).then(function (data) {
        // let alerts = [];
        let content = "";
        $('#NewAlertsContent').empty();
        for (let i = 0; i < data.length; i++) {
            // alerts[i] = new Alert(data[0].description, data[0].date, data[0].readed);
            $('#NewAlertsContent').append(createAlert(data[i]));
        }
        console.log(content);
        setAlertsRead();
    }).catch(function (data) {
        console.log(data);
    });
});

function setAlertsRead() {
    let gameID = [];
    gameID[0] = new Object();
    let SecureObj = new SecurityObj(email, secretKey, "setAllAlertsReaded", gameID);
    postSend("/Fan/setUpdatesReaded", SecureObj);
}

$(document).ready(function () {
    $("#submitTeam").on('click', function (e) {
        // e.preventDefault();
        // e.stopPropagation();
        let newTeam = [];
        newTeam[0] = new Object()
        let leagueType = document.getElementById("leaguesType").value
        leagueType = leagueType.substring(leagueType.indexOf("type:") + 5);
        let league = new League(leagueType, null);
        newTeam[0].Team = new Team(document.getElementById("inputTeamName").value,
            document.getElementById("inputStadium").value,
            league, null);

        let SecureObj = new SecurityObj(email, secretKey, "addTeam", newTeam);
        postSend("/Representative/addTeam", SecureObj);
        window.alert("Team added successfully!");
    });

    function showAddingTeams() {
        var x = document.getElementById("addNewTeam");
        if (x.style.display === "none") {
            x.style.display = "block";
        } else {
            x.style.display = "none";
        }
    }

    function showRanking() {
        // var x = document.getElementById("addNewTeam");
        // if (x.style.display === "none") {
        //     x.style.display = "block";
        // } else {
        //     x.style.display = "none";
        // }
    }


    var numberOfNeededDates = 0;
    var numberOfTeamsInLeague = 0;
    var numberOfGamePerTeam = 0;
    var leagueType;
    var cards = $();

    $("#submitGame").on('click', function (e) {
        leagueType = document.getElementById("leagueTypeGame").value;
        leagueType = leagueType.substring(leagueType.indexOf("type:") + 5);
        numberOfGamePerTeam = document.getElementById("inputNumGames").value;
        let url = "/Seasons/League/" + leagueType + "/numberGamesPerTeam/" + numberOfGamePerTeam;
        // window.alert(url);
        $.get(url, function (data, status) {
            numberOfNeededDates = data.number_of_dates_needed;
            numberOfTeamsInLeague = data.number_of_Teams;

            cards = createDates(numberOfNeededDates);
            $('#GameDates').empty();
            $('#GameDates').append(cards);
            var stage1 = document.getElementById("GamePolicy2");
            stage1.style.display = "block";

        });
    });

    function switchDivSchedual() {
        var stage1 = document.getElementById("GamePolicy1");
        stage1.style.display = "none";
        var stage2 = document.getElementById("GamePolicy2");
        stage2.style.display = "block";
    }

    function createDates(dateInfo) {
        var gamesCardTemplate = [
            '<div class="formDates">',
            '<form>',
            '<label id="dateInput">',
            'Please enter ',
            dateInfo, ' games dates and start and finish hours:', '</label>', '<br>'];
        var index = 8;
        for (var i = 0; i < dateInfo; i++) {
            let id2 = "GameDates" + i;
            gamesCardTemplate[index] = '<input id=' + id2 + ' type="date">';
            index++;
            id2 = "startHour" + i;
            gamesCardTemplate[index] = '<input id=' + id2 + ' type="time">';
            index++;
            id2 = "endHour" + i;
            gamesCardTemplate[index] = '<input id=' + id2 + ' type="time">';
            index++;
            gamesCardTemplate[index] = '<br>';
            index++;
        }
        gamesCardTemplate[index] = '</form></div>';
        // gamesCardTemplate[index] = '<button id="submitGame1" class="btn btn-primary"  ' +
        //     'type="button">Submit Games</button></form></div>';
        return gamesCardTemplate;
    }

    $("#submitGame1").click(function () {
        let newRequest = [];
        newRequest[0] = new Object();
        newRequest[0].league = leagueType;
        let seasonSelection = document.getElementById("SeasonypeGame").value;
        let start = seasonSelection.substring(seasonSelection.indexOf("start:") + 6, seasonSelection.indexOf(" ,end:"));
        let end = seasonSelection.substring(seasonSelection.indexOf(",end:") + 5);
        newRequest[0].start = start;
        newRequest[0].end = end;
        newRequest[0].numberOfGamesPerTeam = numberOfGamePerTeam;
        newRequest[0].date = [];
        for (let i = 0; i < numberOfNeededDates; i++) {
            let id1 = "GameDates" + i;
            let datesFromForm = document.getElementById(id1).value;
            id1 = "startHour" + i;
            let start = document.getElementById(id1).value;
            id1 = "endHour" + i;
            let end = document.getElementById(id1).value;
            newRequest[0].date[i] = new Object();
            newRequest[0].date[i].date = datesFromForm;
            newRequest[0].date[i].start = start;
            newRequest[0].date[i].end = end;
        }

        let SecureObj = new SecurityObj(email, secretKey, "submitGame1", newRequest);
        console.log(SecureObj);
      //  window.alert(JSON.stringify(SecureObj));
        postSend("/Representative/scheduleGame", SecureObj).then(function (v) {
            console.log("good:" + v);
            window.alert("game scheduling done!")
            printGames(v);
        }).catch(function (v) {
            console.log("failed:" + v);
        });
    });

    function seasonSelect(cardInfo) {
        var leagueCardTemplate = [
            '<option value=' +
            cardInfo.start.substr(0, 5) + '-' +
            cardInfo.end.substr(0, 4) + ">" +
            cardInfo.start.substr(0, 5) + '-' +
            cardInfo.end.substr(0, 4) +
            "</option>",
        ];
        return $(leagueCardTemplate.join(''));
    }

    function printGames(data) {
        let Games = [];
        console.log("succsses");
        for (let i = 0; i < data.length; i++) {
            Games[i] = new Game(data[i].id, data[i].home.name, data[i].away.name, data[i].date, data[i].startTime, data[i].endTime);
            console.log("game id:" + Games[i].id + ", home: " + Games[i].home + ", away:" + Games[i].away + ", date:" + Games[i].date + ", start:" + Games[i].start + ", end:" + Games[i].end);
        }
    }

});

$('#representativeButton').click(function () {
    hideAll();
    $('#RepView').show();
    $.get("/Leagues", function (data, status) {

        let select = document.getElementById("leaguesType");
        let length = select.options.length;
        for (let i = length - 1; i >= 0; i--) {
            select.options[i] = null;
        }
        select = document.getElementById("leagueTypeGame");
        length = select.options.length;
        for (let i = length - 1; i >= 0; i--) {
            select.options[i] = null;
        }
        select = document.getElementById("leagueTypeGameRankPolicy");
        length = select.options.length;
        for (let i = length - 1; i >= 0; i--) {
            select.options[i] = null;
        }
        for (let i = 0; i < data.length; i++) {
            let x = document.getElementById("leaguesType");
            let option = document.createElement("option");
            option.text = data[i].name + " , type:" + data[i].type;
            x.add(option);
            let y = document.getElementById("leagueTypeGame");
            let option2 = document.createElement("option");
            option2.text = data[i].name + " , type:" + data[i].type;
            y.add(option2);
            let z = document.getElementById("leagueTypeGameRankPolicy");
            let option3 = document.createElement("option");
            option3.text = data[i].name + " , type:" + data[i].type;
            z.add(option3);
        }
    });
});

$(document).ready(function () {
    let myLink = document.getElementById('leagueTypeGame');
    myLink.onclick = function () {
        let leagueType = document.getElementById("leagueTypeGame").value;
        leagueType = leagueType.substring(leagueType.indexOf("type:") + 5);
        let url = "/Seasons/ByLeague/" + leagueType;
        $.get(url, function (data, status) {
            let select = document.getElementById("SeasonypeGame");
            let length = select.options.length;
            for (let i = length - 1; i >= 0; i--) {
                select.options[i] = null;
            }

            for (let i = 0; i < data.length; i++) {
                var x = document.getElementById("SeasonypeGame");
                var option = document.createElement("option");
                option.text = "start:" + data[i].start + " ,end:" + data[i].end;
                x.add(option);
            }
        });
    }

    let myLink2 = document.getElementById('leagueTypeGameRankPolicy');
    myLink2.onclick = function () {
        let leagueType = document.getElementById("leagueTypeGameRankPolicy").value;
        leagueType = leagueType.substring(leagueType.indexOf("type:") + 5);
        let url = "/Seasons/ByLeague/" + leagueType;
        $.get(url, function (data, status) {
            let select = document.getElementById("SeasonypeGameRankPolicy");
            let length = select.options.length;
            for (let i = length - 1; i >= 0; i--) {
                select.options[i] = null;
            }

            for (let i = 0; i < data.length; i++) {
                var x = document.getElementById("SeasonypeGameRankPolicy");
                var option = document.createElement("option");
                option.text = "start:" + data[i].start + " ,end:" + data[i].end;
                x.add(option);
            }
        });
    }

    $('#submitScore').click(function () {
        let newRequest = [];
        newRequest[0] = new Object();
        let leagueType = document.getElementById("leagueTypeGameRankPolicy").value;
        leagueType = leagueType.substring(leagueType.indexOf("type:") + 5);
        newRequest[0].league = leagueType;
        let seasonSelection = document.getElementById("SeasonypeGameRankPolicy").value;
        let start = seasonSelection.substring(seasonSelection.indexOf("start:") + 6, seasonSelection.indexOf(" ,end:"));
        let end = seasonSelection.substring(seasonSelection.indexOf(",end:") + 5);
        newRequest[0].start = start;
        newRequest[0].end = end;
        newRequest[0].win = document.getElementById("inputWin").value;
        newRequest[0].draw = document.getElementById("inputTie").value;
        newRequest[0].lose = document.getElementById("inputLoose").value;
        let SecureObj = new SecurityObj(email, secretKey, "submitScore", newRequest);
        console.log(SecureObj);
       // window.alert(JSON.stringify(SecureObj));
        postSend("/Representative/rankPolicy", SecureObj).then(function (v) {
            window.alert("rank policy setted successfully!")
            console.log("good:" + v);
        }).catch(function (v) {
            console.log("failed:" + v);
        });
    });
});