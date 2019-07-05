var storage = window.localStorage;
var user_id = storage.getItem("user_id");
var mapLocation = "";
var point;
getLocation();
getCanteenTypes();
function getLocation() {
    // var geolocation = new BMap.Geolocation();
    // layer.msg('正在获取您的当前位置', {
    //     time: 0,//不自动关闭
    //     icon: 16,
    //     shade: 0.1
    // });
    // // layer.msg("正在获取您的位置");
    // geolocation.getCurrentPosition(function (r) {
    //     if (this.getStatus() == BMAP_STATUS_SUCCESS) {
    //         layer.close(layer.index);
    //         var address = r.address;
    //         var location = address.province + address.city + address.district + address.street;
    //         mapLocation = location;
    //         point = r.point;
    //         console.log(location);
    //         console.log(r.point);
    //         $("#address").val(location);
    //     }
    //     else {
            $.ajax({
                url: "/address/getDefaultAddress",
                type: "GET",
                async: false,
                data: {
                    userId: user_id
                },
                success: function (data) {
                    mapLocation = data.location;
                    point = {
                        lng: data.longitude,
                        lat: data.latitude
                    };
                    // layer.close(layer.index);
                    // layer.msg("获取失败");
                },
                error: function (error) {
                    // layer.close(layer.index);
                    // layer.msg("获取失败");
                    // console.log(error);
                }
            });
        // }

    // }, {enableHighAccuracy: true})
}

function getCanteenTypes() {
    $.ajax({
        url: "/user/getAllCanteenTypes",
        type: "GET",
        async: false,
        success: function (data) {
            var canteenTypes = "<ul class=\"nav navbar-nav\">";
            for (var i in data) {
                canteenTypes += "<li onclick='getCanteen(this.id)' id=\"type-" + data[i] + "\"class=\"li-category btn\" style=\"margin-left: 10px\">" + data[i] + "</li>";
            }
            canteenTypes += "</ul>";
            document.getElementById("canteenTypes").innerHTML = canteenTypes;
        },
        error: function (error) {
            console.log(error);
        }
    });
}

function getCanteen(type) {
    $(".li-category").removeClass("active");
    $("#" + type).addClass("active");
    type = (type + "").substring((type + "").indexOf("-") + 1);
    $.ajax({
        url: "/user/getCanteenListByTypeAndLocation",
        type: "GET",
        async: false,
        data: {
            type: type,
            lng: point.lng,
            lat: point.lat
        },
        success: function (data) {
            console.log(data);
            var canteenList = "";
            for (var i in data) {
                if (i % 4 == 0) {
                    canteenList += "<div class=\"row\">";
                }
                canteenList += "<div id=\"canteen-" + data[i].id + "\"onclick=\"viewDetail(this.id)\" class=\"col-md-3\">\n" +
                    "                        <div class=\"card \">\n" +
                    "                            <div class=\"header\">\n" +
                    "                                <!--<img class=\"\" style=\"width: 100px;height: 100px\" src=\"images/kfc.jpg\">-->\n" +
                    "                                <h4 class=\"title\">" + data[i].name + "</h4>\n" +
                    "                                <p class=\"category\">月售" + data[i].sales + "</p>\n" +
                    "                            </div>\n" +
                    "                            <div class=\"content\">\n" +
                    "                                <p class=\"category top-right\">" + data[i].time + " " + data[i].distance + "km</p>\n";
                var discounts = data[i].discounts;
                for (var j in discounts)
                    canteenList += "                                <button class=\"btn btn-sm btn-danger btn-fill\">满" +
                        discounts[j].fullPrice + "减" + discounts[j].reduction + "</button>\n";
                canteenList += "                            </div>\n" +
                    "                        </div>\n" +
                    "                    </div>";
                if (i % 4 == 3 || i == data.length - 1)
                    canteenList += "</div>";
            }
            document.getElementById("canteenList").innerHTML = canteenList;
        },
        error: function (error) {
            console.log(error);
        }
    });

}

function viewDetail(cid) {
    cid = (cid + "").substring((cid + "").indexOf("-") + 1);
    storage.setItem("canteen_id", cid);
    window.location.href = "/viewCanteenDetail.html?";
    //跳到餐厅的菜品界面
}

