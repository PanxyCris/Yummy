var storage = window.localStorage;
var user_id = storage.getItem("user_id");
var discountId = storage.getItem("discount_id");
getDiscount;


function getDiscount() {
    if (discountId != null && discountId != "") {
        $.ajax({
            url: "/discount/getDiscount",
            type: "GET",
            async: false,
            data: {
                did: parseInt(discountId)
            },
            success: function (data) {
                $("#fullPrice").val(data.fullPrice);
                $("#reduction").val(data.reduction);
            },
            error: function (error) {
                console.log(error);
            }
        });
    }
}

function saveDiscount() {
    if (discountId == null || discountId == "")
        discountId = -1;
    $.ajax({
        url: "/discount/saveDiscount",
        type: "POST",
        async: false,
        data: {
            did: parseInt(discountId),
            userId: user_id,
            fullPrice: $("#fullPrice").val(),
            reduction: $("#reduction").val()
        },
        success: function (data) {
            var content = "";
            storage.removeItem("discount_id");
            if (data == "Success")
                content = "修改成功！";
            else
                content = "该优惠已存在";
            TINY.box.show(content, 0, 0, 0, 0, 2);
            setTimeout(function () {
                var index = parent.layer.getFrameIndex(window.name)
                console.log(index);
                parent.layer.close(index);
                parent.location.href = "/discountManagement.html";
                // window.location.href = "/foodManagement.html";
            }, 2000);
        },
        error: function () {
            content = "请求失败！再试一次 . . .";
            TINY.box.show(content, 0, 0, 0, 0, 3);
        }
    });
}