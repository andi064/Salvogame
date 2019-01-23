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
               this.dateFormat(data);
               //this.players(data);
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
               
            });
      },
      gameStats(data) {
         //the fetch for the stats second table
      },
      dateFormat(date){

         date = new Date(date);
         let monthNames = [
            "Jan", "Feb", "Mar",
            "Apr", "May", "Jun", "Jul",
            "Aug", "Sept", "Oct",
            "Nov", "Dec"
          ];
        
          let day = date.getDate();
          let monthIndex = date.getMonth();
          let year = date.getFullYear();
        
          return day + ' ' + monthNames[monthIndex] + ' ' + year;
      }

   },
   created() {
      this.getGames();
      this.getStats();
   }
});

// getGPlayers: function (data) {
//    var id = this.gp;
//    // console.log(id);
//    for (i = 0; i < data.GamePlayers.length; i++) {
//      if(data.GamePlayers[i].id == id){
//        this.gamePlayer1 = data.GamePlayers[i].player.userName;
//        }else{
//            this.gamePlayer2 = data.GamePlayers[i].player.userName;

//        }
//        if(data.GamePlayers.length == 1){
//            this.gamePlayer2 = "Waiting for opponent";
//        }
//    }
// },