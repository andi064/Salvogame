let grid = new Vue({
    el: "#gameView",
    data: {
        letters: [" ", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J"],
        numbers: [" ", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"],
        fetchInfo: [],
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
        }
    },
    created() {
        this.getID();

    }
});