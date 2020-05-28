// Web App JS File

// Variables
var email;
var password;
var fan = false;
var referee = false;
var representative = false;

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
const url = 'http://localhost:8080';
let stompClient;
let newMessages = new Array();

function connectToChat(userName) {
    console.log("connecting to chat...");
    let socket = new SockJS(url + '/chat');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log("connected to: " + frame);
        stompClient.subscribe("/topic/messages/" + userName, function (response) {
            console.log("updated!");
            let data = JSON.parse(response.body);
            new Alert(data.description, data.date, data.read);
            window.alert(data.description);
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
        let SecureObj = new SecurityObj(email, "1000", "Login", signIn);
        postSend("/Login", SecureObj).then(function (data) {
            // data is 'null' when authentication fail
            if (data == null) {
                changeLayout(false, false, false);
                return;
            }
            for (let i = 0; i < data.object[0].length; i++) {
                if (data.object[0][i] == 'Fan') fan = true;
                if (data.object[0][i] == 'Representative') representative = true;
                if (data.object[0][i] == 'Referee') referee = true;
            }
            connectToChat(email);
            changeLayout(fan, representative, referee);
        }).catch(function (err) {
            console.log("failed: " + err);
        });
    });
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
    }
    if (referee == true) {
        $('#refereeButton').removeAttr('hidden');
    }
}

//</editor-fold>

//<editor-fold desc="CONTENT GENERATOR">

var cards = $();

// Create League View Content
$(document).ready(function () {
    $('#leagueButton').click(function () {
        $('#leaguesView').removeAttr('hidden');
        $('#leaguesView').show();
        $('#seasonsView').hide();
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
        $('#leaguesView').hide();
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
        cardInfo.start.substr(0, 5) || 'undefined',
        cardInfo.end.substr(0, 4) || 'undefined',
        '</h6>',
        '<button class="moreInfoButton">More Info</button></div></div></div></div>'
    ];
    return $(seasonCardTemplate.join(''));
}

function TeamsContent(event) {
    event.preventDefault();
    $("#leaguesView").css("display", "none");
    $("#seasonsView").css("display", "none");
    $("#GamesView").css("display", "none");
    $("#TeamsView").css("display", "block");
    $('#TeamCards').empty();
    $.get("/Teams", function (data) {
        data.forEach(function (item) {
            cards = cards.add(createTeamsCard(item));
        });
    });
    $('#TeamCards').append(cards);
    cards = $();
}

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

function GamesContent(event) {
    event.preventDefault();
    $('#GamesCards').empty();
    $("#leaguesView").css("display", "none");
    $("#seasonsView").css("display", "none");
    $("#TeamsView").css("display", "none");
    $("#GamesView").css("display", "block");
    $.get("/Games", function (data) {
        data.forEach(function (item) {
            cards = cards.add(createGamesCard(item));
        });
    });
    $('#GamesCards').append(cards);
    cards = $();
}

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
        '<h6 class="card-subtitle mb-3 text-muted">Home: ',
        cardInfo.home.name || 'undefined', //
        '</h6>',
        '<h6 class="card-subtitle mb-3 text-muted">Away: ',
        cardInfo.away.name || 'undefined', //
        '</h6>',
        '<h6 class="card-subtitle mb-3 text-muted">Stadium: ',
        cardInfo.home.stadium || 'undefined', //
        '</h6>',
        '<h6 class="card-subtitle mb-3 text-muted">Date: ',
        cardInfo.date || 'undefined', //
        '</h6>',

        '<button class="moreInfoButton">More Info</button></div></div></div></div>'
    ];
    return $(gamesCardTemplate.join(''));
}

//</editor-fold>

function createGameOption(gameOptionInfo) {
    let gameOptionTemplate = [
        '<div class="option">',
        '<input type="radio" class="radio" id="gameOption',
        gameOptionInfo.id || 'undefined',
        '" name="GameOption">',
        '<label for=gameOption',
        gameOptionInfo.id || 'undefined',
        '>',
        gameOptionInfo.home.name || 'undefined',
        ' VS ',
        gameOptionInfo.away.name || 'undefined',
        '</label></div>'
    ];
    return $(gameOptionTemplate.join(''));
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

$('#refereeButton').click(function () {
    $("#leaguesView").css("display", "none");
    $("#seasonsView").css("display", "none");
    $("#TeamsView").css("display", "none");
    $("#GamesView").css("display", "none");
    $("#RefereeView").css("display", "none");
    $("#RefView").css("display", "block");
    let refereeOptions = [];
    refereeOptions[0] = new Object();
    refereeOptions[0].referee_id = email;
    let SecureObj = new SecurityObj(email, "1000", "GameChoose", refereeOptions);
    postSend("/Referee/getGamesByReferee", SecureObj).then(function (data) {
        let Games = [];
        $('#gameOptions').empty();
        GameOptions = $();
        data.forEach(game => {
            GameOptions = (createGameOption(game));
            GameOptions.click(function () {
                document.querySelector(".selected").innerHTML = GameOptions.find("label").html();
                GameOptionID = GameOptions.find("label").attr('for');
                document.querySelector(".option-container").classList.remove("active");
            });
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

$("#selectGame").click(function () {
    let gameID = [];
    gameID[0] = new Object();
    gameID[0].game_id = GameOptionID.substr(10);
    let SecureObj = new SecurityObj(email, "1000", "startGame", gameID);
    postSend("/Referee/getEventProperties", SecureObj).then(function (data) {
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
    }).catch(function (data) {
        console.log(data);
    });
    $('#RefView').hide();
    $('#RefereeView').removeAttr('hidden');
    $('#RefereeView').css('display', 'block');
    $('#HomeEvents').empty();
    $('#AwayEvents').empty();
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
    eventProperties[0].game_id = game_id;
    eventProperties[0].minute = minute;
    eventProperties[0].team = team;
    eventProperties[0].eventType = eventType;
    eventProperties[0].description = description;

    let SecureObj = new SecurityObj(email, "1000", "addGameEvent", eventProperties);
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
    let gameID = [];
    gameID[0] = new Object();
    let SecureObj = new SecurityObj(email, "1000", "getAlerts", gameID);
    postSend("/Fan/getUpdates", SecureObj).then(function (data) {
        let alerts = [];
        let content = "";
        $('#NewAlertsContent').empty();
        for (let i = 0; i < data.length; i++) {
            alerts[i] = new Alert(data[0].description, data[0].date, data[0].readed);
            $('#NewAlertsContent').append(createAlert(alerts[i]));
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
    let SecureObj = new SecurityObj(email, "1000", "setAllAlertsReaded", gameID);
    postSend("/Fan/setUpdatesReaded", SecureObj);
}
