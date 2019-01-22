let tableInfo = new Vue({
   el: "#View",
   data: {
      games: {},
      stats: {}
   },
   methods: {
      getGames() {
         fetch('/api/games')
            .then((response) => {
               return response.json();
            })
            .then((data) => {
               tableInfo.games = data;
               console.log(this.games);
               this.gamesCreation(data);
            });
      },
      getStats() {
         fetch('/api/leaderboard')
            .then((response) => {
               return response.json();
            })
            .then((data) => {
               tableInfo.stats = data;
               console.log(this.stats);
               this.playerStats(data);
            });
      },

      gamesCreation(data) {
         let ol = document.getElementById("gameList");
         for (let i = 0; i < data.length; i++) {
            let li = document.createElement("li");
            let date = new Date(data[i].created);
            li.append("Game Id : " + data[i].id + " - " + "Created on " + date.toLocaleDateString() + " ");
            for (let x = 0; x < data[i].gamePlayers.length; x++) {
               let player = data[i].gamePlayers[x].player;
               li.append(" " + player.userName + " ( " + player.email + " )");
            }
            ol.append(li);
         }
      },

      playerStats(data) {
         console.log("it works");
      }

   },
   created() {
      this.getGames();
      this.getStats();
   }
});