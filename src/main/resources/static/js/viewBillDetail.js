var storage = window.localStorage;
var user_id = storage.getItem("user_id");
var canteenId = storage.getItem("canteen_id");
var bid = storage.getItem("bid");
getBill();

function getBill() {
    $.ajax({
        url: "/bill/getBill",
        type: "GET",
        async: false,
        data: {
            bid: parseInt(bid)
        },
        success: function (data) {
            var carts = data.cartList;
            var canteenList = "<table class=\"table table-hover table-striped\">\n" +
                "                                    <thead>\n" +
                "                                        <th>名称</th>\n" +
                "                                    \t<th>数量</th>\n" +
                "                                    \t<th>价格</th>\n" +
                "                                    </thead>\n" +
                "                                    <tbody>";
            for (var i = 0, len = carts.length; i < len; i++) {
                canteenList += "<tr>\n" +
                    "                                        \t<td>" + carts[i].food.name + "</td>\n" +
                    "                                        \t<td>" + carts[i].number + "</td>\n" +
                    "                                        \t<td>¥" + carts[i].food.price * carts[i].number + "</td>\n" +
                    "                                        </tr>";
            }
            canteenList += "<tr><td>餐盒费</td><td></td><td>¥" + data.boxPrice + "</td></tr>";
            if (data.reduction != 0)
                canteenList += "<tr><td>优惠</td><td></td><td>-¥" + data.reduction + "</td></tr>";
            if (data.systemRedution != 0)
                canteenList += "<tr><td>会员等级优惠</td><td></td><td>-¥" + data.systemReduction + "</td></tr>";
            canteenList += "<tr><td><strong class='bg'>总价</strong></td><td></td><td><strong class='bg'>¥" + data.totalPrice + "</strong></td></tr>";
            canteenList += "       </tbody>\n" +
                "                                </table>";
            document.getElementById("canteenList").innerHTML = canteenList;
            var addressList = "<table class=\"table table-hover table-striped\">\n" +
                "                                    <tbody>";
            var sex = data.address.sex;
            if (sex == "Male")
                sex = "男";
            else
                sex = "女";
            addressList += "<tr>\n" +
                "                                        \t<td>" + data.address.location + "</td>\n" +
                "                                        \t<td>" + data.address.houseNumber + "</td>\n" +
                "                                        \t<td>" + data.address.contactName + "</td>\n" +
                "                                        \t<td>" + sex + "</td>\n" +
                "                                        \t<td>" + data.address.phoneNumber + "</td>\n" +
                "                                        \t<td>" + data.address.label + "</td>\n" +
                "                                        </tr>";

            addressList += "       </tbody>\n" +
                "                                </table>";
            document.getElementById("addressList").innerHTML = addressList;
            var buttons = "";
            var state = data.billState;
            if (state == "NotPaid")
                buttons += " <button onclick=\"pay()\" class=\"btn btn-primary btn-fill\">支付</button>\n" +
                    "                        <button onclick=\"cancel()\" class=\"btn btn-warning btn-fill\">取消</button>";
            else if (state == "Sending")
                buttons += " <button onclick=\"receive()\" class=\"btn btn-info btn-fill\">收货</button>\n" +
                    "                        <button onclick=\"reject()\" class=\"btn btn-warning btn-fill\">退订</button>";
            else if (state == "Cancel" || state == "Finish")
                buttons += " <button onclick=\"again()\" class=\"btn btn-info btn-fill\">再来一单</button>\n" +
                    "                        <button onclick=\"goBack()\" class=\"btn btn-warning btn-fill\">返回</button>";
            document.getElementById("buttons").innerHTML = buttons;
        }, error: function (error) {
            console.log(error);
        }
    });
}

function goBack() {
    storage.removeItem("bid");
    window.location.href = "/billManagement.html";
}


function again() {
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

function pay() {
    storage.setItem("bid", bid);
    var content = "准备进入支付状态 . . .";
    TINY.box.show(content, 0, 0, 0, 0, 2);
    setTimeout(function () {
        window.location.href = "/pay.html";
    }, 2000);
}

function cancel() {
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

function receive() {
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
            TINY.box.show(content, 0, 0, 0, 0, 2);
            setTimeout(function () {
                window.location.href = "/viewBillDetail.html";
            }, 2000);
        }, error: function (error) {
            console.log(error);
        }
    });
}

function reject() {
    storage.setItem("bid", bid);
    layer.open({
        type: 2,
        area: ['700px', '450px'],
        content: 'rejectRegulation.html'
    });
}