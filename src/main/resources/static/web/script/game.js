let grid = new Vue({
    el: "#gameView",
    data: {
        letters: [" ", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J"],
        numbers: [" ", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"],
        fetchInfo: {},
        gp: ""
    },
    methods: {
        getID() {
            let url = new URL(window.location.href);
            this.gp = url.searchParams.get("gp");
            this.getData();
            // console.log(this.gp);
        },
        getData() {
            fetch('/api/game_view/' + this.gp)
                .then((response) => {
                    return response.json();
                })
                .then((data) => {
                    grid.fetchInfo = data;
                    console.log(this.fetchInfo);
                    this.shipLocation_player(data);
                    // this.playerDetails(data);
                    this.salvoLocation(data);
                    this.thyEnemySalvo(data);
                });
        },
        shipLocation_player(fetchInfo) {
            let ship = fetchInfo.Ships;
            for (let i = 0; i < ship.length; i++) {
                for (let j = 0; j < fetchInfo.Ships[i].Ship_Location.length; j++) {
                    // console.log(fetchInfo.Ships[i].Ship_Location[j]);
                    document.getElementById(fetchInfo.Ships[i].Ship_Location[j]).className += "shipLoc";
                }
            }
            // },
            // playerDetails(fetchInfo){
            //     let playerInfo = fetchInfo.GamePlayers;
            //     console.log(playerInfo[0].player.userName)
            //     for(let i = 0; i < playerInfo.length; i++){
            //         if(playerInfo[i].id == this.gp){
            //             this.player1 = playerInfo[i].player.userName;
            //             console.log(player1);
            //         }else{
            //             this.players2 = playerInfo[i].player.userName;
            //             console.log(player1);

            //         }
            //     }

        },
        salvoLocation(fetchInfo) {
            let salvo = fetchInfo.Salvos;
            for (let i = 0; i < salvo.length; i++) {
                for (let j = 0; j < fetchInfo.Salvos[i].Salvo_Location.length; j++) {
                    // console.log(fetchInfo.Ships[i].Ship_Location[j]);
                    document.getElementById(fetchInfo.Salvos[i].Salvo_Location[j]).className += "salvoLoc";
                    for (let v = 0; v < fetchInfo.Salvos[i].Turn.length; v++) {
                     //   document.getElementById(fetchInfo.Salvos[i].Salvo_Location[j]).innerHTML +=Turn[v];
                    }
                }

            }
        },
        thyEnemySalvo(fetchInfo) {
            let salvo = fetchInfo.thyEnemySalvoes;
            for (let i = 0; i < salvo.length; i++) {
                for (let j = 0; j < fetchInfo.thyEnemySalvoes[i].Salvo_Location.length; j++) {
                    document.getElementById(fetchInfo.thyEnemySalvoes[i].Salvo_Location[j]).className += "salvoHit";
                    let imgHit = document.createElement("img");
                    imgHit.className +="salvoHit";
                    imgHit.src = 'images/hit.gif';
                    document.getElementById(fetchInfo.thyEnemySalvoes[i].Salvo_Location[j]).append(imgHit);
                    for (let v = 0; v < fetchInfo.Salvos[i].Turn.length; v++) {
                      //  document.getElementById(fetchInfo.Salvos[i].Salvo_Location[j]).innerHTML +=Turn[v];
                    }
                }

            }
        }
    },
    created() {
        this.getID();
    }
});