var storage = window.localStorage;
var user_id = storage.getItem("user_id");
var bid = parseInt(storage.getItem("bid"));
getAccountList();
setTime();

function setTime() {
    $.ajax({
        url: "/bill/getBill",
        type: "GET",
        async: false,
        data: {
            bid: parseInt(bid)
        },
        success: function (bill) {
            var now = new Date().getTime();
            var passTime = now - bill.timeStamps;
            var second = 2 * 60 - parseInt(passTime / 1000);
            document.getElementById("time").innerHTML = "00:00:" + second;
        },
        error: function (error) {
            console.log(error);
        }
    });
}

function getAccountList() {
    $.ajax({
        url: "/account/getAccountList",
        type: "GET",
        async: false,
        data: {
            userId: user_id
        },
        success: function (data) {
            console.log(data);
            var canteenList = "";
            for (var i in data) {
                if (i % 4 == 0) {
                    canteenList += "<div class=\"row\">";
                }
                canteenList += "<div id=\"account-" + data[i].accountId + "\"onclick=\"pay(this.id)\" class=\"col-md-3\">\n" +
                    "                        <div class=\"card \">\n" +
                    "                            <div class=\"header\">\n" +
                    "                                <!--<img class=\"\" style=\"width: 100px;height: 100px\" src=\"images/kfc.jpg\">-->\n" +
                    "                                <h4 class=\"title\">" + data[i].bankName + "</h4>\n" +
                    "                            </div>\n" +
                    "                            <div class=\"content\">\n" +
                    "                                <p class=\"category top-right\">" + data[i].accountId + "</p>\n" +
                    "                            </div>\n" +
                    "                        </div>\n" +
                    "                    </div>";
                if (i % 4 == 3 || i == data.length - 1)
                    canteenList += "</div>";
            }
            document.getElementById("accountList").innerHTML = canteenList;
        },
        error: function (error) {
            console.log(error);
        }
    });

}

function cancel() {
    $.ajax({
        url: "/bill/cancelBill",
        type: "POST",
        async: false,
        data: {
            bid: bid
        },
        success: function (data) {
            layer.close(layer.index);
            var content = "订单已失效";
            layer.msg(content);
            //跳往订单详情界面
            setTimeout(function () {
                window.location.href = "/billManagement.html";
            }, 2000);
        },
        error: function (error) {
            console.log(error);
        }
    });
}

function pay(aid) {
    aid = (aid + "").substring((aid + "").indexOf("-") + 1);
    storage.setItem("account_id", aid);
    layer.open({
        type: 2,
        area: ['700px', '600px'],
        // fixed: false, //不固定
        // maxmin: true,
        content: 'offset.html'
    });
}



