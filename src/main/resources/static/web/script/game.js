let grid = new Vue({
    el: "#gameView",
    data: {
        letters: [" ", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J"],
        numbers: [" ", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"],
        fetchInfo: {},
        gp: "",
        ships: [{
                type: "Carrier",
                myLocation: ["A3", "A4", "A5", "A6", "A7"]
            },
            {
                type: "Battleship",
                myLocation: ["C3", "D3", "E3", "F3"]
            },
            {
                type: "Submarine",
                myLocation: ["I5", "I6", "I7"]
            },
            {
                type: "Destroyer",
                myLocation: ["J7", "J8", "J9"]
            },
            {
                type: "Patrol Boat",
                myLocation: ["E9", "F9", "G9"]
            }
        ]
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
            if (fetchInfo.Ships) {
                let ship = fetchInfo.Ships;

                for (let i = 0; i < ship.length; i++) {
                    for (let j = 0; j < fetchInfo.Ships[i].Ship_Location.length; j++) {
                        // console.log(fetchInfo.Ships[i].Ship_Location[j]);
                        document.getElementById(fetchInfo.Ships[i].Ship_Location[j]).className += "shipLoc";
                    }
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
            if (fetchInfo.Salvos) {
                let salvo = fetchInfo.Salvos;
                for (let i = 0; i < salvo.length; i++) {
                    for (let j = 0; j < fetchInfo.Salvos[i].Salvo_Location.length; j++) {
                        console.log(fetchInfo.Salvos[i].Turn);
                        document.getElementById(fetchInfo.Salvos[i].Salvo_Location[j] + 's').innerHTML = this.fetchInfo.Salvos[i].Turn;
                        document.getElementById(fetchInfo.Salvos[i].Salvo_Location[j] + 's').className += "salvoLoc";

                    }

                }
            }
        },
        thyEnemySalvo(fetchInfo) {
            if (fetchInfo.thyEnemySalvoes) {
                let salvo = fetchInfo.thyEnemySalvoes;
                for (let i = 0; i < salvo.length; i++) {
                    for (let j = 0; j < fetchInfo.thyEnemySalvoes[i].Salvo_Location.length; j++) {
                        if (document.getElementById(fetchInfo.thyEnemySalvoes[i].Salvo_Location[j]).classList.contains("shipLoc")) {
                            let imgHit = document.createElement("img");
                            imgHit.className += "salvoHit";
                            imgHit.src = 'images/hit.gif';
                            document.getElementById(fetchInfo.thyEnemySalvoes[i].Salvo_Location[j]).innerHTML = this.fetchInfo.thyEnemySalvoes[i].Turn;
                            document.getElementById(fetchInfo.thyEnemySalvoes[i].Salvo_Location[j]).append(imgHit);
                        } else {
                            let imgHit = document.createElement("img");
                            imgHit.className += "salvoMiss";
                            imgHit.src = 'images/wattaa.gif';
                            document.getElementById(fetchInfo.thyEnemySalvoes[i].Salvo_Location[j]).innerHTML = this.fetchInfo.thyEnemySalvoes[i].Turn;
                            document.getElementById(fetchInfo.thyEnemySalvoes[i].Salvo_Location[j]).append(imgHit);
                        }
                    }
                }
            }
        },
        cheat() {
            if (this.fetchInfo.length <= 0) {
                document.getElementById("cheat").style.display = "none";
            }
        },
        createShips() {
            fetch('/api/games/players/'+grid.gp+'/ships', {
                credentials: 'include',
                method: 'POST',
                headers: {

                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(grid.ships)
            }).then(function (response) {
                return response.json();
            }).then(function (json) {
                console.log('parsed json', json);
                location.reload();
            }).catch(function (ex) {
                console.log('parsing failed', ex)
            });
        }
    },
    created() {
        this.getID();
        this.cheat();
    }
});
