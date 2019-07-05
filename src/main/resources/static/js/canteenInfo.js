var storage = window.localStorage;
var user_id = storage.getItem("user_id");
var mapLocation = "";
var point;
$("#id").val(user_id);
getCanteenTypes();
getCanteenInfo();
getAccountList();
function getCanteenTypes() {
    $.ajax({
        url: "/user/getAllCanteenTypes",
        type: "GET",
        async: false,
        success: function (data) {
            console.log(data);
            var canteenTypes = "<select id=\"demo-chosen-select\"\n" +
                "                                                        multiple tabindex=\"2\" class=\"form-control\">";
            for (var i in data) {
                canteenTypes += "<option value=\"" + data[i] + "\">" + data[i] + "</option>";
            }
            canteenTypes += "</select>";
            document.getElementById("canteenType").innerHTML = canteenTypes;
        },
        error: function () {
            content = "请求失败！再试一次 . . .";
            TINY.box.show(content, 0, 0, 0, 0, 3);
        }
    });
}

function getCanteenInfo() {
    $.ajax({
        url: "/user/getCanteenInfo",
        type: "GET",
        async: false,
        data: {
            userId: user_id
        },
        success: function (data) {
            $("#name").val(data.name);
            $("#contactName").val(data.contactName);
            $("#sex").val(data.sex);
            $("#address").val(data.address);
            $("#demo-chosen-select").val(data.canteenTypes);
            mapLocation = data.address;
            point = {
                lng: data.longitude,
                lat: data.latitude
            };
            if(data.pass)
                getSideBar();

        },
        error: function () {
            content = "请求失败！再试一次 . . .";
            TINY.box.show(content, 0, 0, 0, 0, 3);
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

function save() {
    console.log($("#sex").val());
    var types = $("#demo-chosen-select").val();
    for (var t in types)
        console.log(types[t]);
    // $.ajax({
    //     url: "http://api.map.baidu.com/geocoder?address=",//+$("#address").val()+"&output=json&ak=7tfThudqLk1Q4ZLhrBSqsEEcj76pXafi",
    //     type: "GET",
    //     dataType: 'jsonp',
    //     async: false,
    //     data: JSON.stringify({
    //         address: $("#address").val(),
    //         output: "json",
    //         ak: "7tfThudqLk1Q4ZLhrBSqsEEcj76pXafi"
    //     }),
    //     success: function (data) {
    //         console.log(data);
    //         point = data.location;
    //     },
    //     error: function (error) {
    //         console.log(error);
    //     }
    // });
    if (point == null)
        point = {
            lng: 0,
            lat: 0
        };
    var content = "";
    $.ajax({
        url: "/user/canteenInfo",
        type: "POST",
        async: false,
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify({
            id: user_id,
            name: $("#name").val(),
            contactName: $("#contactName").val(),
            sex: $("#sex").val(),
            longitude: point.lng,
            latitude: point.lat,
            address: $("#address").val(),
            canteenTypes: types
        }),
        success: function (data) {
            content = "提交成功！待管理员审核 . . .";
            TINY.box.show(content, 0, 0, 0, 0, 2);
            setTimeout(function () {
                //跳转到登录后的主页（待定）
                window.location.href = "/canteenInfo.html";
            }, 2000);
        },
        error: function () {
            content = "请求失败！再试一次 . . .";
            TINY.box.show(content, 0, 0, 0, 0, 3);
        }
    });
}

function getLocation() {
    var geolocation = new BMap.Geolocation();
    geolocation.getCurrentPosition(function (r) {
        if (this.getStatus() == BMAP_STATUS_SUCCESS) {
            var address = r.address;
            var location = address.province + address.city + address.district + address.street;
            mapLocation = location;
            point = r.point;
            console.log(location);
            // var mk = new BMap.Marker(r.point);
            // mp.addOverlay(mk);
            // mp.panTo(r.point);
            // mp.centerAndZoom(r.point, 12);
            // mp.enableScrollWheelZoom();
            console.log(r.point);
            $("#address").val(location);
            // alert('您的位置：'+r.point.lng+','+r.point.lat);
        }
        else {
            console.log('failed' + this.getStatus());
            var content = "定位失败！请直接输入地址";
            TINY.box.show(content, 0, 0, 0, 0, 3);
        }
    }, {enableHighAccuracy: true})
}

function getSideBar(){
    document.getElementById("sidebar").innerHTML = "<div class=\"sidebar\" data-color=\"black\" data-image=\"assets/img/sidebar-5.jpg\">\n" +
        "\n" +
        "        <div class=\"sidebar-wrapper\">\n" +
        "            <div class=\"logo\">\n" +
        "                <a href=\"http://www.creative-tim.com\" class=\"simple-text\">\n" +
        "                    Yummy!\n" +
        "                </a>\n" +
        "            </div>\n" +
        "\n" +
        "            <ul class=\"nav\">\n" +
        "                <li>\n" +
        "                    <a href=\"foodManagement.html\">\n" +
        "                        <i class=\"pe-7s-graph\"></i>\n" +
        "                        <p>菜品管理</p>\n" +
        "                    </a>\n" +
        "                </li>\n" +
        "                <li class=\"active\">\n" +
        "                    <a href=\"canteenInfo.html\">\n" +
        "                        <i class=\"pe-7s-user\"></i>\n" +
        "                        <p>餐厅信息</p>\n" +
        "                    </a>\n" +
        "                </li>\n" +
        "                <li>\n" +
        "                    <a href=\"canteenBillManagement.html\">\n" +
        "                        <i class=\"pe-7s-note2\"></i>\n" +
        "                        <p>订单管理</p>\n" +
        "                    </a>\n" +
        "                </li>\n" +
        "                <li>\n" +
        "                    <a href=\"discountManagement.html\">\n" +
        "                        <i class=\"pe-7s-news-paper\"></i>\n" +
        "                        <p>优惠管理</p>\n" +
        "                    </a>\n" +
        "                </li>\n" +
        "            </ul>\n" +
        "        </div>\n" +
        "    </div>";
}