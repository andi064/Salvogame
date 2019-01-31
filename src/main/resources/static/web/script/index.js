let LoginModalController = {
    tabsElementName: ".logmod__tabs li",
    tabElementName: ".logmod__tab",
    inputElementsName: ".logmod__form .input",
    hidePasswordName: ".hide-password",
    inputElements: null,
    tabsElement: null,
    tabElement: null,
    hidePassword: null,
    activeTab: null,
    tabSelection: 0, // 0 - first, 1 - second

    findElements: function () {
        let base = this;

        base.tabsElement = $(base.tabsElementName);
        base.tabElement = $(base.tabElementName);
        base.inputElements = $(base.inputElementsName);
        base.hidePassword = $(base.hidePasswordName);

        return base;
    },

    setState: function (state) {
        let base = this,
            elem = null;

        if (!state) {
            state = 0;
        }

        if (base.tabsElement) {
            elem = $(base.tabsElement[state]);
            elem.addClass("current");
            $("." + elem.attr("data-tabtar")).addClass("show");
        }

        return base;
    },

    getActiveTab: function () {
        let base = this;

        base.tabsElement.each(function (i, el) {
            if ($(el).hasClass("current")) {
                base.activeTab = $(el);
            }
        });

        return base;
    },

    addClickEvents: function () {
        let base = this;

        base.hidePassword.on("click", function (e) {
            let $this = $(this),
                $pwInput = $this.prev("input");

            if ($pwInput.attr("type") == "password") {
                $pwInput.attr("type", "text");
                $this.text("Hide");
            } else {
                $pwInput.attr("type", "password");
                $this.text("Show");
            }
        });

        base.tabsElement.on("click", function (e) {
            var targetTab = $(this).attr("data-tabtar");

            e.preventDefault();
            base.activeTab.removeClass("current");
            base.activeTab = $(this);
            base.activeTab.addClass("current");

            base.tabElement.each(function (i, el) {
                el = $(el);
                el.removeClass("show");
                if (el.hasClass(targetTab)) {
                    el.addClass("show");
                }
            });
        });

        base.inputElements.find("label").on("click", function (e) {
            let $this = $(this),
                $input = $this.next("input");

            $input.focus();
        });

        return base;
    },

    initialize: function () {
        let base = this;

        base.findElements().setState().getActiveTab().addClickEvents();
    }
};

$(document).ready(function () {
    LoginModalController.initialize();
});


function login() {
    let login = {
        email: document.getElementById("loginEmail").value,
        pwd: document.getElementById("login-pw").value
    }
    fetch("/api/login", {
            credentials: 'include',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            method: 'POST',
            body: getBody(login)
        })
        .then(function (data) {
            console.log('Request success: ', data);
            if (data.status == 200) {
                redirect();
            } else {
                window.onload = function () {
                    setTimeout(failPopup, 4000)
                };

                function failPopup() {
                    alert("naah it didnt work");
                }
            }
        })
        .catch(function (error) {
            console.log('Request failure: ', error);
        });

    function getBody(json) {
        var body = [];
        for (var key in json) {
            var encKey = encodeURIComponent(key);
            var encVal = encodeURIComponent(json[key]);
            body.push(encKey + "=" + encVal);
        }
        return body.join("&");
    }
}

function redirect() {
    location.replace("http://localhost:8080/web/games.html");
}

function signup() {
    let register = {
        email: document.getElementById("email").value,
        pwd: document.getElementById("password").value

    }
    fetch('/api/players' , {
        credentials: 'include',
        method: 'POST',
        headers: {
 
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            userName : document.getElementById("userName").value,
            email : document.getElementById("email").value,
            password : document.getElementById("password").value
        })
    }).then(function(response) {
        return response.json();
    }).then(function(json) {
        console.log('parsed json', json); 
    }).catch(function(ex) {
        console.log('parsing failed', ex)
    });
}


