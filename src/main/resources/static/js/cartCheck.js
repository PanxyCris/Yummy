var storage = window.localStorage;
var user_id = storage.getItem("user_id");
var canteenId = storage.getItem("canteen_id");
var bid = -1;
var isAddress = false;
getCartList();
getAddressList();


function getAddressList() {
    $.ajax({
        url: "/address/getAddressList",
        type: "GET",
        async: false,
        data: {
            userId: user_id
        },
        success: function (data) {
            var addressList = "";
            if (data == null || data.length == 0) {
                isAddress = false;
                addressList = "<p>您还没有送餐地址，快点补充吧！</p>";
            } else {
                isAddress = true;
                addressList = "<select id=\"address\"\n" +
                    "                                        tabindex=\"2\" class=\"form-control\">\n";
                for (var i = 0, len = data.length; i < len; i++) {
                    addressList += "<option value='" + data[i].aid + "'>" + data[i].location + data[i].houseNumber
                        + " " + data[i].contactName + "(";
                    var sex = data[i].sex;
                    if (sex == "Male")
                        sex = "先生";
                    else
                        sex = "女士";
                    addressList += sex + ") " + data[i].phoneNumber + "</option>";
                }
                addressList +=
                    "                                </select>";
            }
            document.getElementById("addressList").innerHTML = addressList;
        }, error: function (error) {
            console.log(error);
        }
    });
}

function getCartList() {
    $.ajax({
        url: "/bill/generateBill",
        type: "GET",
        async: false,
        data: {
            userId: user_id,
            canteenId: canteenId
        },
        success: function (data) {
            bid = data.bid;
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
                canteenList += "<tr><td>餐厅优惠</td><td></td><td>-¥" + data.reduction + "</td></tr>";
            if (data.systemRedution != null && data.systemRedution != 0)
                canteenList += "<tr><td>会员等级优惠</td><td></td><td>-¥" + data.systemReduction + "</td></tr>";
            canteenList += "<tr><td><strong class='bg'>总价</strong></td><td></td><td><strong class='bg'>¥" + data.totalPrice + "</strong></td></tr>";
            canteenList += "       </tbody>\n" +
                "                                </table>";
            document.getElementById("canteenList").innerHTML = canteenList;
        }, error: function (error) {
            console.log(error);
        }
    });
}

function edit() {
    layer.open({
        type: 2,
        area: ['700px', '800px'],
        // fixed: true, //不固定
        // maxmin: true,
        content: 'newAddress.html'
    });
}

function check() {
    if (isAddress) {
        var timeStamps = (new Date()).getTime();
        storage.setItem("bid", bid);
        $.ajax({
            url: "/bill/payForPreparation",
            type: "POST",
            async: false,
            data: {
                bid: bid,
                timeStamps: timeStamps,
                aid: parseInt($("#address").val())
            },
            success: function (data) {
                var content = "准备进入支付状态 . . .";
                TINY.box.show(content, 0, 0, 0, 0, 2);
                setTimeout(function () {
                    window.location.href = "/pay.html";
                }, 2000);
            },
            error: function (error) {
                console.log(error);
            }
        });
    } else {
        layer.msg("请补充地址！");
    }

}