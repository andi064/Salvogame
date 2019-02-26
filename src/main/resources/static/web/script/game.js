let grid = new Vue({
    el: "#gameView",
    data: {
        letters: [" ", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J"],
        numbers: [" ", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"],
        fetchInfo: {},
        gp: "",
        id: "",
        shipsDiv: "",
        testShip: " ", //array to store the current valid position of the boats
        ship: [{
                type: "Carrier",
                myLocation: [], //has to store the location of the ship after placement 
                size: 5
            },
            {
                type: "Battleship",
                myLocation: [],
                size: 4
            },
            {
                type: "Submarine",
                myLocation: [],
                size: 3
            },
            {
                type: "Destroyer",
                myLocation: [],
                size: 3
            },
            {
                type: "PatrolBoat",
                myLocation: [],
                size: 2
            }
        ],
        salvoLoc: [],//only a list of locations
        shipcount: 5,
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
                        document.getElementById(fetchInfo.Ships[i].Ship_Location[j]).classList.add("shipLoc");
                    }
                }
            }

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
            fetch('/api/games/players/' + grid.gp + '/ships', {
                credentials: 'include',
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(grid.ship)
            }).then(function (response) {
                return response.json();
            }).then(function (json) {
                console.log('parsed json', json);
                location.reload();
            }).catch(function (ex) {
                console.log('parsing failed', ex)
            });
            this.displayNone();
        },
        displayNone(){
            document.getElementById("createShipsButton").style.display="none";
            document.getElementById("shipsDiv").style.display = "none";
        },
        goBack() {
            location.replace("/web/games.html");
        },
        allowDrop(ev) {
            ev.preventDefault();
        },
        allowDropSalvo(ev) {
            ev.preventDefault();
        },
        dragStart(ev) {
            console.log(ev);
            this.id = ev.target.id;
            console.log("It Works //", "ship_id : ", this.id);
        },
        dragStartSalvo(ev) {
            console.log(ev);
            this.id = ev.target.id;
            console.log("salvos dragg", this.id);
        },
        drop(ev) {
            document.querySelectorAll(".shipGrid td").forEach(cell => cell.classList.remove("ships"));
            let letters = ev.target.id.substr(0, 1);
            let numbers = ev.target.id.substr(1, 2);
            console.log(letters)
            ev.target.append(document.getElementById(this.id));
            console.log(this.id)
            for (let u = 0; u < this.ship[this.id].size; u++) {
                document.getElementById(letters + (Number(numbers) + u)).classList.add("ships");
                if (this.ship[this.id].size == 5) {
                    this.ship[0].myLocation.push(letters + (Number(numbers) + u));
                }
                if (this.ship[this.id].size == 4) {
                    this.ship[1].myLocation.push(letters + (Number(numbers) + u));
                }
                if (this.ship[this.id].size == 3) {
                    this.ship[2].myLocation.push(letters + (Number(numbers) + u));
                }
                if (this.ship[this.id].size == 3) {
                    this.ship[3].myLocation.push(letters + (Number(numbers) + u));
                }
                if (this.ship[this.id].size == 2) {
                    this.ship[4].myLocation.push(letters + (Number(numbers) + u));
                }
                console.log(letters + (Number(numbers) + u));
            }
            this.countShips();
            this.id = null;
        },
        salvoDrop(ev) {
            document.querySelectorAll(".salvoLoc td").forEach(cell => cell.classList.remove("salvos"));
            let letters = ev.target.id.substr(0, 1);
            let numbers = ev.target.id.substr(1, 2);
            ev.target.append(document.getElementById(this.id));
            document.getElementById(letters + (Number(numbers) + u)).classList.add("salvos");
            this.salvoLocation.push(letters + (Number(numbers) + u));
            console.log(letters + (Number(numbers) + u));
            this.id = null;
        },
        countShips() {
            this.shipsDiv = $("#shipsDiv > div").length;
            console.log(this.shipsDiv);
            let x = this.shipcount - 1;
            this.shipcount = x;
            console.log(x);
            if (x == 0) {
                let div = document.getElementById("shipsDiv");
                div.style.display = "none";
            }
        }
    },
    created() {
        this.getID();
        this.cheat();
    }
});

$(document).ready(function () {
    var movementStrength = 10;
    var height = movementStrength / $(window).height();
    var width = movementStrength / $(window).width();
    $("#top-image").mousemove(function (e) {
        var pageX = e.pageX - ($(window).width() / 2);
        var pageY = e.pageY - ($(window).height() / 2);
        var newvalueX = width * pageX * -1 - 25;
        var newvalueY = height * pageY * -1 - 50;
        $('#top-image').css("background-position", newvalueX + "px " + newvalueY + "px");
    });
});