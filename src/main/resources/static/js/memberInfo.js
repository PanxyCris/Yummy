var storage = window.localStorage;
var user_id = storage.getItem("user_id");
$("#id").val(user_id);
getMemberInfo();
getAddressList();
getAccountList();

function getMemberInfo() {
    $.ajax({
        url: "/user/getMember",
        type: "GET",
        async: false,
        data: {
            userId: user_id
        },
        success: function (data) {
            $("#name").val(data.name);
            $("#level").val(data.grade);
        }, error: function (error) {
            console.log(error);
        }
    });
}

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
                addressList = "<p>您还没有送餐地址，快点补充吧！</p>";
            } else {
                addressList = "<table class=\"table table-hover table-striped\">\n" +
                    "                                    <thead>\n" +
                    "                                        <th>地址</th>\n" +
                    "                                    \t<th>门牌号</th>\n" +
                    "                                    \t<th>联系人</th>\n" +
                    "                                    \t<th>性别</th>\n" +
                    "                                    \t<th>电话</th>\n" +
                    "                                        <th>标签</th>\n" +
                    "                                        <th>编辑</th>\n" +
                    "                                    </thead>\n" +
                    "                                    <tbody>";
                for (var i = 0, len = data.length; i < len; i++) {
                    var sex = data[i].sex;
                    if (sex == "Male")
                        sex = "男";
                    else
                        sex = "女";
                    addressList += "<tr>\n" +
                        "                                        \t<td>" + data[i].location + "</td>\n" +
                        "                                        \t<td>" + data[i].houseNumber + "</td>\n" +
                        "                                        \t<td>" + data[i].contactName + "</td>\n" +
                        "                                        \t<td>" + sex + "</td>\n" +
                        "                                        \t<td>" + data[i].phoneNumber + "</td>\n" +
                        "                                        \t<td>" + data[i].label + "</td>\n" +
                        "                                        \t<td><input onclick='edit(this.id)'id=\"edit-" + data[i].aid + "\"type=\"button\" class=\"form-control\" value='编辑'></td>\n" +
                        "                                        </tr>";
                }
                addressList += "       </tbody>\n" +
                    "                                </table>";
            }
            document.getElementById("addressList").innerHTML = addressList;
        }, error: function (error) {
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
            var addressList = "";
            if (data == null || data.length == 0) {
                addressList = "<p>您还没有银行卡，快点添加吧！</p>";
            } else {
                addressList = "<table class=\"table table-hover table-striped\">\n" +
                    "                                    <thead>\n" +
                    "                                        <th>银行名称</th>\n" +
                    "                                    \t<th>卡号</th>\n" +
                    "                                    \t<th>充值</th>\n" +
                    "                                    </thead>\n" +
                    "                                    <tbody>";
                for (var i = 0, len = data.length; i < len; i++) {
                    addressList += "<tr>\n" +
                        "                                        \t<td>" + data[i].bankName + "</td>\n" +
                        "                                        \t<td>" + data[i].accountId + "</td>\n" +
                        "                                        \t<td><input onclick='charge(this.id)'id=\"bank-" + data[i].accountId + "\"type=\"button\" class=\"form-control\" value='充值'></td>\n" +
                        "                                        </tr>";
                }
                addressList += "       </tbody>\n" +
                    "                                </table>";
            }
            document.getElementById("accountList").innerHTML = addressList;
        }, error: function (error) {
            console.log(error);
        }
    });
}

function charge(aid) {
    if (aid == null)
        aid = "";
    else
        aid = (aid + "").substring((aid + "").indexOf("-") + 1);
    storage.setItem("account_id", aid);
    layer.open({
        type: 2,
        area: ['700px', '600px'],
        // fixed: false, //不固定
        // maxmin: true,
        content: 'accountBind.html'
    });
}

function checkRule() {
    layer.open({
        type: 2,
        area: ['700px', '800px'],
        // fixed: true, //不固定
        // maxmin: true,
        content: 'gradeRegulation.html'
    });
}

function edit(aid) {
    if (aid == null)
        aid = "";
    else
        aid = (aid + "").substring((aid + "").indexOf("-") + 1);
    storage.setItem("address_id", aid);
    layer.open({
        type: 2,
        area: ['700px', '800px'],
        // fixed: true, //不固定
        // maxmin: true,
        content: 'newAddress.html'
    });
}

function save() {
    var content = "";
    $.ajax({
        url: "/user/memberInfo",
        type: "POST",
        async: false,
        data: {
            id: user_id,
            name: $("#name").val()
        },
        success: function (data) {
            content = "修改成功！";
            TINY.box.show(content, 0, 0, 0, 0, 2);
            setTimeout(function () {
                //跳转到登录后的主页（待定）
                window.location.href = "/market.html";
            }, 2000);
        },
        error: function () {
            content = "请求失败！再试一次 . . .";
            TINY.box.show(content, 0, 0, 0, 0, 3);
        }
    });
}

function deleteUser() {
    layer.confirm('您确认要注销您的账户吗？', {
        btn: ['确认', '取消'] //按钮
    }, function () {
        layer.close(layer.index);
        layer.open({
            type: 1,
            area: ['500px', '180px'],
            content: '<div class="card"><div class="content">\n' +
                '                                <div class="row">\n' +
                '                                    <div class="col-md-12">\n' +
                '                                        <div class="form-group">' +
                '<p>请输入您的登陆密码，以让我们再一次验证</p>' +
                '<input class="form-control" id="password" type="password">' +
                '<button onclick="confirmDelete()" class="btn btn-danger btn-fill pull-left">确认</button>'+
                '<button onclick="cancelDelete()" class="btn btn-primary btn-fill pull-right">取消</button>' +
                '</div></div></div></div></div>'
        });
    }, function () {
        layer.close(layer.index);
    });

}

function confirmDelete() {
    $.ajax({
        url: "/user/deleteUser",
        type: "POST",
        async: false,
        data: {
            id: user_id,
            password: $("#password").val()
        },
        success: function (data) {
            layer.close(layer.index);
            if(data == "Success"){
                var content = "您已注销！";
                TINY.box.show(content, 0, 0, 0, 0, 2);
                setTimeout(function () {
                    window.location.href = "/login.html";
                }, 2000);
            } else{
                var content = "注销失败！";
                TINY.box.show(content, 0, 0, 0, 0, 2);
            }

        },
        error: function () {
            layer.close(layer.index);
        }
    });
}

function cancelDelete() {
    layer.close(layer.index);
}