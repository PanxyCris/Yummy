var storage = window.localStorage;
var bid = parseInt(storage.getItem("bid"));

function confirm() {
    layer.confirm('您确认要退订该订单并愿意承担相应的损失吗？', {
        btn: ['确认', '取消'] //按钮
    }, function () {
        $.ajax({
            url: "/bill/rejectBill",
            type: "GET",
            async: false,
            data: {
                bid: parseInt(bid)
            },
            success: function (data) {
                layer.msg('已退订', {
                    time: 2000
                });
                setTimeout(function () {
                    var index = parent.layer.getFrameIndex(window.name);
                    console.log(index);
                    parent.layer.close(index);
                    parent.location.href = parent.location.href;
                }, 2500);
            }, error: function (error) {
                console.log(error);
            }
        });
    }, function () {
        layer.close(layer.index);
    });
}

function cancel() {
    var index = parent.layer.getFrameIndex(window.name);
    console.log(index);
    parent.layer.close(index);
    parent.location.href = parent.location.href;
}

