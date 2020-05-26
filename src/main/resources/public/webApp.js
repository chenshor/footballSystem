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
            if (data == null) changeLayout(false, false, false);
            for (let i = 0; i < data.object[0].length; i++) {
                if (data.object[0][i] == 'Fan') fan = true;
                if (data.object[0][i] == 'Representative') representative = true;
                if (data.object[0][i] == 'Referee') referee = true;
            }
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

const selected = document.querySelector(".selected");
const optionContainer = document.querySelector(".option-container");

const optionsList = document.querySelectorAll(".option");

selected.addEventListener("click", () => {
    optionContainer.classList.toggle("active");
});

optionsList.forEach(o => {
    o.addEventListener("click", () => {
        selected.innerHTML = o.querySelector("label").innerHTML;
        GameOptionID = o.querySelector("label").htmlFor;
        optionContainer.classList.remove("active");
    });
});

function createGameOption(gameOptionInfo) {
    let gameOptionTemplate = [
        '<div class="option">',
        '<input type="radio" class="radio" id="gameOption',
            gameOptionInfo.id || 'undefined',
        '" name="GameOption">',
        '<label for=gameOption',
            gameOptionInfo.id || 'undefined',
        '>',
            gameOptionInfo.home || 'undefined',
        ' VS ',
            gameOptionInfo.away || 'undefined',
        '</label></div>'
    ];
    return $(gameOptionTemplate.join(''));
}

var GameOptions = $();
var GameOptionID;
let game_id;

$('#refereeButton').click(function () {
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
            $('#gameOptions').append(GameOptions);
        });
    }).catch(function (data) {
        console.log("request failed");
    });
});

$("#selectGame").click(function () {
    let gameID = [];
    gameID[0] = new Object();
    // gameID[0].game_id = GameOptionID.substr(10);
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
        teamSelector.add(option);
        var option = document.createElement("option");
        option.text = data[1].away;
        teamSelector.add(option);
    }).catch(function (data) {
        console.log(data);
    });
    $('#RefView').hide();
    $('#RefereeView').removeAttr('hidden');
});

$("#addGameEvent").click(function () {
    let minute = document.getElementById("GameMinute").value;
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
});

