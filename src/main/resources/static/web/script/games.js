let tableInfo = new Vue({
   el: "#View",
   data: {
      games: {},
      stats: {},
      user:{}
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
               this.user=data.player.userName;
               console.log(this.user);
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
      dateFormat(date) {

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
      },
      changeGP(gamePlayers){
         if(gamePlayers[0].player.userName == this.user){
            return gamePlayers[0].id
         }else{
            return gamePlayers[1].id
         }
      },
      addPlayer(gamePlayers){
         //*return gamePlayers[0].id;
      }

   },
   created() {
      this.getGames();
      this.getStats();
   }
});

function logout() {
   fetch('/api/logout', {
      method: 'POST',
   }).then(function (response) {
      location.replace("http://localhost:8080/web/index.html");
      return response.json();
   });
}