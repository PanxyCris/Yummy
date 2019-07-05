getCanteenList();

function getCanteenList() {
    $.ajax({
        url: "/user/check",
        type: "GET",
        async: false,
        success: function (data) {
            var canteenList = "<table class=\"table table-hover table-striped\">\n" +
                "                                    <thead>\n" +
                "                                        <th>编号</th>\n" +
                "                                    \t<th>餐厅名称</th>\n" +
                "                                    \t<th>联系人</th>\n" +
                "                                    \t<th>性别</th>\n" +
                "                                    \t<th>地址</th>\n" +
                "                                        <th>餐厅类型</th>\n" +
                "                                        <th>是否通过</th>\n" +
                "                                    </thead>\n" +
                "                                    <tbody>";
            for (var i = 0, len = data.length; i < len; i++) {
                var sex = data[i].sex;
                if (sex == "Male")
                    sex = "男";
                else
                    sex = "女";
                canteenList += "<tr>\n" +
                    "                                        \t<td>" + data[i].id + "</td>\n" +
                    "                                        \t<td>" + data[i].name + "</td>\n" +
                    "                                        \t<td>" + data[i].contactName + "</td>\n" +
                    "                                        \t<td>" + sex + "</td>\n" +
                    "                                        \t<td>" + data[i].address + "</td>\n" +
                    "                                        \t<td>" + data[i].canteenTypes + "</td>\n" +
                    "                                        \t<td><input name='agree' id='agree-" + data[i].id + "' type=\"checkbox\" class=\"form-control\"></td>\n" +
                    "                                        </tr>";
            }
            canteenList += "       </tbody>\n" +
                "                                </table>";
            document.getElementById("canteenList").innerHTML = canteenList;
        }, error: function (error) {
            console.log(error);
        }
    });
}

function agree() {
    var inputs = document.getElementsByName("agree");
    var agreements = [];
    for (var i in inputs) {
        if (inputs[i].checked) {
            var agreeId = inputs[i].id.split("-")[1];
            agreements.push(agreeId);
        }
    }
    var content = "";
    $.ajax({
        url: "/user/pass",
        type: "POST",
        async: false,
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify({
            ids: agreements
        }),
        success: function (data) {
            content = "已审核 . . .";
            TINY.box.show(content, 0, 0, 0, 0, 2);
            setTimeout(function () {
                window.location.href = "/check.html";
            }, 2000);

        },
        error: function () {
            content = "请求失败！再试一次 . . .";
            TINY.box.show(content, 0, 0, 0, 0, 3);
        }
    });

}