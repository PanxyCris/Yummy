var storage = window.localStorage;
var user_id = storage.getItem("user_id");
var addressId = storage.getItem("address_id");
var mapLocation = "";
var point;
getAddress();

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

function getAddress() {
    if (addressId != null && addressId != "") {
        $.ajax({
            url: "/address/getAddress",
            type: "GET",
            async: false,
            data: {
                addressId: parseInt(addressId)
            },
            success: function (data) {
                $("#contactName").val(data.contactName);
                $("#sex").val(data.sex);
                $("#phone").val(data.phoneNumber);
                $("#address").val(data.location);
                $("#houseNumber").val(data.houseNumber);
                $("#label").val(data.label);
                point = {
                    lng: data.longitude,
                    lat: data.latitude
                };
            },
            error: function (error) {
                console.log(error);
            }
        });
    }
}

function save() {
    if (addressId == null || addressId == "")
        addressId = -1;
    $.ajax({
        url: "http://api.map.baidu.com/geocoder",
        type: "GET",
        async: false,
        data: {
            address: $("#address").val(),
            output: "json",
            key: "7tfThudqLk1Q4ZLhrBSqsEEcj76pXafi"
        },
        success: function (data) {
            console.log(data);
            point = data.location;
        },
        error: function (error) {
            console.log(error);
        }
    });
    if (point == null)
        point = {
            lng: 0,
            lat: 0
        };
    $.ajax({
        url: "/address/save",
        type: "POST",
        async: false,
        data: {
            userId: user_id,
            aid: parseInt(addressId),
            contactName: $("#contactName").val(),
            sex: $("#sex").val(),
            phoneNumber: $("#phone").val(),
            location: $("#address").val(),
            longitude: point.lng,
            latitude: point.lat,
            houseNumber: $("#houseNumber").val(),
            label: $("#label").val()
        },
        success: function (data) {
            var content = "修改成功！";
            TINY.box.show(content, 0, 0, 0, 0, 2);
            storage.removeItem("address_id");
            setTimeout(function () {
                var index = parent.layer.getFrameIndex(window.name);
                console.log(index);
                parent.layer.close(index);
                parent.location.href = parent.location.href;
            }, 2000);
        },
        error: function () {
            content = "请求失败！再试一次 . . .";
            TINY.box.show(content, 0, 0, 0, 0, 3);
        }
    });
}