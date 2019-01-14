let grid = new Vue({
    el: "#gameView",
    data: {
        letters : [" ", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J"],
        numbers : [" ", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"]
    },
    methods: {},
    created() {
        fetch("http://localhost:8080/api/game_view/2", {}).then(function (result) {
            return result.json()
        }).then(function (datta) {
            data = datta;
            console.log(data);
        })
    }
});
