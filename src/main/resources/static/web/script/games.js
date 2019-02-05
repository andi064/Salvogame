let tableInfo = new Vue({
   el: "#View",
   data: {
      games: {},
      stats: {},
      user:{},
      newGame:{},
      gpID:""
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
      createGame() {
         fetch('/api/games',{
            method: 'POST',
         }).then(function (response){
            console.log(response)
            return response.json();
         }).then(function (data){
            this.gpID = data.gpID;
            window.location = "game.html?gp=" + this.gpID;
         });
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
      addPlayer(gameID){
         fetch('/api/game/'+gameID+'/players',{
            method: 'POST',
         }).then(function (response){
            console.log(response)
            return response.json();
         }).then(function (data){
            window.location = "game.html?gp=" + data.gamePlayerID;
         });
      },
      createGames(){
         return this.gpID;
      },
      logout() {
         fetch('/api/logout', {
            method: 'POST',
         }).then(function (response) {
            location.replace("http://localhost:8080/web/index.html");
            return response.json();
         });
      }
      // ,
      // createShips(){
      //    fetch('/api/games/players/{gamePlayerId}/ships' , {
      //       credentials: 'include',
      //       method: 'POST',
      //       headers: {
     
      //           'Content-Type': 'application/json'
      //       },
      //       body: JSON.stringify({
      //          //  userName : document.getElementById("userName").value,
      //          //  email : document.getElementById("email").value,
      //          //  password : document.getElementById("password").value
      //       })
      //   }).then(function(response) {
      //       return response.json();
      //   }).then(function(json) {
      //       console.log('parsed json', json); 
      //   }).catch(function(ex) {
      //       console.log('parsing failed', ex)
      //   });
      // }

   },
   created() {
      this.getGames();
      this.getStats();
   }
});

   