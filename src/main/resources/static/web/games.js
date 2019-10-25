function buildGameList(list) {
    list.forEach((item, index) => {
    let listItem = document.createElement("li");
    listItem.innerHTML = "Game id: " + item.id + " created on " + item.created + ". Players: ";
    let gamePlayerList = item.gameplayers;
    gamePlayerList.forEach((item, index) => {
        listItem.innerHTML += item.player.username;
        if (index < gamePlayerList.length - 1) {
            listItem.innerHTML += ", ";
        }
    })
    document.getElementById("game-list").append(listItem);
    });
};


var data;
fetch("http://localhost:8080/api/games")
    .then(response =>
        response.json())
    .then((json) => buildGameList(json))
    .catch((error) => {console.log("There was an error: " + error);});

