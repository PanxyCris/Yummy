var storage = window.localStorage;
var user_id = storage.getItem("user_id");
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
            if (data.systemReduction != null && data.systemRedution != 0)
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
        }, error: function (error) {
            console.log(error);
        }
    });
}

function goBack() {
    storage.removeItem("bid");
    window.location.href = "/canteenBillManagement.html";
}