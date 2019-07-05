var storage = window.localStorage;
var user_id = storage.getItem("user_id");

getCanteenBills("NoLimit", "");

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
        case "member":
            $.ajax({
                url: "/user/getMemberListByCanteen",
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
    getCanteenBills($("#condition").val(), $("#value").val());
}

function getCanteenBills(key, value) {
    $.ajax({
        url: "/bill/getCanteenBills",
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
                    "                            <div class=\"content table-responsive table-full-width\">\n" +
                    "                                <div class=\"content table-responsive table-full-width\"><table class=\"table table-hover table-striped\">\n" +
                    "                                    <thead>\n" +
                    "<tr>\n" +
                    "                                            <th>会员</th>\n" +
                    "                                            <th>下单时间</th>\n" +
                    "                                            <th>状态</th>\n" +
                    "                                            <th>商品</th>\n" +
                    "                                            <th>成交价</th>\n" +
                    "                                        </tr>" +
                    "                                    </thead>\n" +
                    "                                    <tbody>";
                for (var i = 0, len = data.length; i < len; i++) {
                    canteenList += "<tr onclick='viewBillDetail(this.id)' id='bill-" + data[i].bid + " '>\n" +
                        "                                        \t<td>" + data[i].memberName + "</td>\n" +
                        "                                        \t<td>" + data[i].timeStamps + "</td>\n" +
                        "                                        \t<td>" + data[i].billState + "</td>\n" +
                        "                                        \t<td>" + data[i].cartList[0].food.name + "等</td>\n" +
                        "                                        \t<td>¥" + data[i].totalPrice + "</td>\n" +
                        "                                        </tr>";
                }
                canteenList += "       </tbody>\n" +
                    "                                </table></div>\n" +
                    "                        </div>";
            }
            document.getElementById("canteenBills").innerHTML = canteenList;
        }, error: function (error) {
            console.log(error);
        }
    });
}

function viewBillDetail(bid) {
    bid = (bid + "").substring((bid + "").indexOf("-") + 1);
    storage.setItem("bid", bid);
    window.location.href = "viewCanteenBillDetail.html";
}