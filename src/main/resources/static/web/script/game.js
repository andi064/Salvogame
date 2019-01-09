getData();

function getData() {
   fetch("http://localhost:8080/api/games", {}).then(function (result) {
      return result.json()
   }).then(function (datta) {
      data = datta;
      gameList();
      console.log(data);
   })
}


function gameList() {
   let ol = document.getElementById("gameList");
   for (let i = 0; i < data.length; i++) {
      let li = document.createElement("li");
      let date = new Date(data[i].created);
      li.append("Game Id : " + data[i].id + " - " + "Created on " + date.toLocaleDateString() + " ");
      for (let x = 0; x < data[i].gamePlayers.length; x++) {
         let player = data[i].gamePlayers[x].player;
         li.append( " " +player.userName + " ( " + player.email + " )");
      }
      ol.append(li);
   }
}
