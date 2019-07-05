var storage = window.localStorage;
var user_id = storage.getItem("user_id");
var canteenId = storage.getItem("canteen_id");

getCurrentBills("NoLimit", "");
getUnpaidBills("NoLimit", "");
getHistoryBills("NoLimit", "");

$("#condition").on('change', function () {
    var options = "";
    switch ($("#condition").val()) {
        case "NoLimit":
            options += " <select tabindex=\"2\" class=\"form-control\"><option value='1'></option></select>";
            break;
        case "time":
            options += " <select id='value' tabindex=\"2\" class=\"form-control\"><option value='1'>1天内</option>" +
                "<option value='7'>1周内</option>" +
                "<option value='30'>1月内</option></select>";
            break;
        case "money":
            options += "<input id='value' type='number' class='form-control' placeholder='以下'>";
            break;
        case "canteen":
            $.ajax({
                url: "/user/getCanteenListByMember",
                type: "GET",
                async: false,
                data: {
                    userId: user_id
                },
                success: function (data) {
                    options += "<select id='value' tabindex=\"2\" class=\"form-control\">";
                    for (var i in data)
                        options += "<option value='"+data[i].id+"'>" + data[i].name + "</option>";
                    options += "</select>";
                },
                error: function (error) {
                    console.log(error);
                }
            });
            break;
        default:
            options += " <select tabindex=\"2\" class=\"form-control\"><option value='1'></option></select>";
            break;
    }
    document.getElementById("options").innerHTML = options;
});

function sift() {
    getCurrentBills($("#condition").val(), $("#value").val());
    getUnpaidBills($("#condition").val(), $("#value").val());
    getHistoryBills($("#condition").val(), $("#value").val());
}

function getUnpaidBills(key, value) {
    $.ajax({
        url: "/bill/getStateBills",
        type: "GET",
        async: false,
        data: {
            userId: user_id,
            billState: "NotPaid",
            key: key,
            value: value
        },
        success: function (data) {
            var canteenList = "";
            if (data.length != 0) {
                canteenList = "<div class=\"card\">\n" +
                    "                            <div class=\"header\">\n" +
                    "                                <h4 class=\"title\">待支付订单</h4>\n" +
                    "                                <!--<p class=\"category\">新的申请</p>-->\n" +
                    "                            </div>\n" +
                    "                            <div class=\"content table-responsive table-full-width\">\n" +
                    "                                <div class=\"content table-responsive table-full-width\"><table class=\"table table-hover table-striped\">\n" +
                    "                                    <thead>\n" +
                    "<tr>\n" +
                    "                                            <th>商家</th>\n" +
                    "                                            <th>下单时间</th>\n" +
                    "                                            <th>商品</th>\n" +
                    "                                            <th>成交价</th>\n" +
                    // "                                            <th>支付</th>\n" +
                    // "                                            <th>取消</th>\n" +
                    "                                        </tr>" +
                    "                                    </thead>\n" +
                    "                                    <tbody>";
                for (var i = 0, len = data.length; i < len; i++) {
                    canteenList += "<tr onclick='viewBillDetail(this.id)' id='bill-" + data[i].bid + " '>\n" +
                        "                                        \t<td>" + data[i].canteenName + "</td>\n" +
                        "                                        \t<td>" + data[i].timeStamps + "</td>\n" +
                        "                                        \t<td>" + data[i].cartList[0].food.name + "等</td>\n" +
                        "                                        \t<td>¥" + data[i].totalPrice + "</td>\n" +
                        // "                                        \t<td><button onclick='pay(this.id)' id=\"pay-" + data[i].bid + "\" class=\"btn btn-fill btn-primary\">支付</button></td>\n" +
                        // "                                        \t<td><button onclick='cancel(this.id)' id=\"cancel-" + data[i].bid + "\"class=\"btn btn-fill btn-warning\">取消</button></td>\n" +
                        "                                        </tr>";
                }
                canteenList += "       </tbody>\n" +
                    "                                </table></div>\n" +
                    "                        </div>";
            }
            document.getElementById("unpaidBill").innerHTML = canteenList;
        }, error: function (error) {
            console.log(error);
        }
    });
}

function getCurrentBills(key, value) {
    $.ajax({
        url: "/bill/getStateBills",
        type: "GET",
        async: false,
        data: {
            userId: user_id,
            billState: "Sending",
            key: key,
            value: value
        },
        success: function (data) {
            var canteenList = "";
            if (data.length != 0) {
                canteenList = "<div class=\"card\">\n" +
                    "                            <div class=\"header\">\n" +
                    "                                <h4 class=\"title\">当前订单</h4>\n" +
                    "                            </div>\n" +
                    "                            <div class=\"content table-responsive table-full-width\">\n" +
                    "                                <div class=\"content table-responsive table-full-width\"><table class=\"table table-hover table-striped\">\n" +
                    "                                    <thead>\n" +
                    "<tr>\n" +
                    "                                            <th>商家</th>\n" +
                    "                                            <th>下单时间</th>\n" +
                    "                                            <th>商品</th>\n" +
                    "                                            <th>成交价</th>\n" +
                    // "                                            <th>收货</th>\n" +
                    // "                                            <th>退订</th>\n" +
                    "                                        </tr>" +
                    "                                    </thead>\n" +
                    "                                    <tbody>";
                for (var i = 0, len = data.length; i < len; i++) {
                    canteenList += "<tr onclick='viewBillDetail(this.id)' id='bill-" + data[i].bid + " '>\n" +
                        "                                        \t<td>" + data[i].canteenName + "</td>\n" +
                        "                                        \t<td>" + data[i].timeStamps + "</td>\n" +
                        "                                        \t<td>" + data[i].cartList[0].food.name + "等</td>\n" +
                        "                                        \t<td>¥" + data[i].totalPrice + "</td>\n" +
                        // "                                        \t<td><button onclick='receive(this.id)' id=\"receive-" + data[i].bid + "\" class=\"btn btn-fill btn-primary\">收货</button></td>\n" +
                        // "                                        \t<td><button onclick='reject(this.id)' id=\"reject-" + data[i].bid + "\"class=\"btn btn-fill btn-warning\">退订</button></td>\n" +
                        "                                        </tr>";
                }
                canteenList += "       </tbody>\n" +
                    "                                </table></div>\n" +
                    "                        </div>";
            }
            document.getElementById("currentBill").innerHTML = canteenList;
        }, error: function (error) {
            console.log(error);
        }
    });
}

function getHistoryBills(key, value) {
    $.ajax({
        url: "/bill/getHistoryBills",
        type: "GET",
        async: false,
        data: {
            userId: user_id,
            key: key,
            value: value
        },
        success: function (data) {
            var canteenList = "";
            if (data.length != 0) {
                canteenList = "<div class=\"card\">\n" +
                    "                            <div class=\"header\">\n" +
                    "                                <h4 class=\"title\">历史订单</h4>\n" +
                    "                            </div>\n" +
                    "                            <div class=\"content table-responsive table-full-width\">" +
                    "<table class=\"table table-hover table-striped\">\n" +
                    "                                    <thead>\n" +
                    "<tr>\n" +
                    "                                            <th>商家</th>\n" +
                    "                                            <th>下单时间</th>\n" +
                    "                                            <th>状态</th>\n" +
                    "                                            <th>商品</th>\n" +
                    "                                            <th>成交价</th>\n" +
                    // "                                            <th>再来一单</th>\n" +
                    "                                        </tr>" +
                    "                                    </thead>\n" +
                    "                                    <tbody>";
                for (var i = 0, len = data.length; i < len; i++) {
                    canteenList += "<tr onclick='viewBillDetail(this.id)' id='bill-" + data[i].bid + " '>\n" +
                        "                                        \t<td>" + data[i].canteenName + "</td>\n" +
                        "                                        \t<td>" + data[i].timeStamps + "</td>\n" +
                        "                                        \t<td>" + data[i].billState + "</td>\n" +
                        "                                        \t<td>" + data[i].cartList[0].food.name + "等</td>\n" +
                        "                                        \t<td>¥" + data[i].totalPrice + "</td>\n" +
                        // "                                        \t<td><button onclick='again(this.id)'id=\"again-" + data[i].bid + "\" class=\"btn btn-fill btn-primary\">再来一单</button></td>\n" +
                        "                                        </tr>";
                }
                canteenList += "       </tbody>\n" +
                    "                                </table></div>\n" +
                    "                        </div>";
            }
            document.getElementById("historyBill").innerHTML = canteenList;
        }, error: function (error) {
            console.log(error);
        }
    });
}

function pay(bid) {
    bid = (bid + "").substring((bid + "").indexOf("-") + 1);
    storage.setItem("bid", bid);
    var content = "准备进入支付状态 . . .";
    TINY.box.show(content, 0, 0, 0, 0, 2);
    setTimeout(function () {
        window.location.href = "/pay.html";
    }, 2000);
}

function cancel(bid) {
    bid = (bid + "").substring((bid + "").indexOf("-") + 1);
    layer.confirm('您确认要取消该订单吗？', {
        btn: ['确认', '取消'] //按钮
    }, function () {
        $.ajax({
            url: "/bill/cancelBill",
            type: "GET",
            async: false,
            data: {
                bid: parseInt(bid)
            },
            success: function (data) {
                storage.setItem("bid", bid);
                var content = "已取消";
                TINY.box.show(content, 0, 0, 0, 0, 2);
                setTimeout(function () {
                    window.location.href = "/viewBillDetail.html";
                }, 2000);
            }, error: function (error) {
                console.log(error);
            }
        });
    }, function () {
        layer.close(layer.index);
    });
}

function receive(bid) {
    bid = (bid + "").substring((bid + "").indexOf("-") + 1);
    $.ajax({
        url: "/bill/confirmBill",
        type: "GET",
        async: false,
        data: {
            bid: parseInt(bid)
        },
        success: function (data) {
            storage.setItem("bid", bid);
            var content = "已收到";
            TINY.box.show(content, 0, 0, 0, 0, 3);
            setTimeout(function () {
                window.location.href = "/viewBillDetail.html";
            }, 2000);
        }, error: function (error) {
            console.log(error);
        }
    });
}

function reject(bid) {
    bid = (bid + "").substring((bid + "").indexOf("-") + 1);
    storage.setItem("bid", bid);
    layer.open({
        type: 2,
        area: ['700px', '450px'],
        content: 'rejectRegulation.html'
    });
}

function again(bid) {
    bid = (bid + "").substring((bid + "").indexOf("-") + 1);
    $.ajax({
        url: "/bill/againBill",
        type: "GET",
        async: false,
        data: {
            bid: parseInt(bid)
        },
        success: function (data) {
            storage.setItem("canteen_id", data.canteenId);
            window.location.href = "/cartCheck.html";
        }, error: function (error) {
            console.log(error);
        }
    });
}

function viewBillDetail(bid) {
    bid = (bid + "").substring((bid + "").indexOf("-") + 1);
    storage.setItem("bid", bid);
    window.location.href = "viewBillDetail.html";
}