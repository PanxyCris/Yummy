var storage = window.localStorage;
var user_id = storage.getItem("user_id");
var foodId = storage.getItem("add_food_id");
getFood();

function getFood() {
    console.log(foodId);
    if (foodId != null && foodId != "") {
        $.ajax({
            url: "/food/getFood",
            type: "GET",
            async: false,
            data: {
                fid: parseInt(foodId)
            },
            success: function (data) {
                $("#addFoodName").val(data.name);
                $("#currentNumber").val(data.number);
            },
            error: function (error) {
                console.log(error);
            }
        });
        $.ajax({
            url: "/food/getStockList",
            type: "GET",
            async: false,
            data: {
                fid: parseInt(foodId)
            },
            success: function (data) {
                var stockList = "";
                if (data == null || data.length == 0) {
                    stockList = "<p>您还没有库存记录</p>";
                } else {
                    stockList = "<table class=\"table table-hover table-striped\">\n" +
                        "                                    <thead>\n" +
                        "                                        <th>数量</th>\n" +
                        "                                    \t<th>开始时间</th>\n" +
                        "                                    \t<th>结束时间</th>\n" +
                        "                                    </thead>\n" +
                        "                                    <tbody>";
                    for (var i = 0, len = data.length; i < len; i++) {
                        stockList += "<tr>\n" +
                            "                                        \t<td>" + data[i].leftNumber + "/" + data[i].number + "</td>\n" +
                            "                                        \t<td>" + data[i].startTime + "</td>\n" +
                            "                                        \t<td>" + data[i].endTime + "</td>\n" +
                            "                                        </tr>";
                    }
                    stockList += "       </tbody>\n" +
                        "                                </table>";
                }
                document.getElementById("stockList").innerHTML = stockList;
            }, error: function (error) {
                console.log(error);
            }
        });
    }
}

function addStockFood() {
    if (foodId == null || foodId == "")
        foodId = -1;
    console.log($("#startTime").val());
    $.ajax({
        url: "/food/addStock",
        type: "POST",
        async: false,
        data: {
            fid: parseInt(foodId),
            number: $("#addFoodNumber").val(),
            startTime: $("#startTime").val(),
            days: $("#timeSpan").val()
        },
        success: function (data) {
            storage.removeItem("add_food_id");
            content = "修改成功！";
            TINY.box.show(content, 0, 0, 0, 0, 2);
            setTimeout(function () {
                var index = parent.layer.getFrameIndex(window.name)
                console.log(index);
                parent.layer.close(index);
                parent.location.href = "/foodManagement.html"
            }, 2000);
        },
        error: function () {
            content = "请求失败！再试一次 . . .";
            TINY.box.show(content, 0, 0, 0, 0, 3);
        }
    });
}