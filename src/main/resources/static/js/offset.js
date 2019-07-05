$(".buy-confirm").on("click", function () {
    // 打开支付密码对话框并生成订单
    $('.pay-part').css("display", "block");
})
$(".cancel-btn").on("click", function () {
    var index = parent.layer.getFrameIndex(window.name);
    console.log(index);
    parent.layer.close(index);
});

function clearContent() {
    $('.pay-part').css("display", "none");
    $inputs.each(function () {  //input清空
        $(this).val("");
    });
    pwd = "";
    $(".real-ipt").val("");
}

$(".confirm-btn").on("click", function () {
    confirm();
});

function confirm() {
    console.log("password:" + pwd);
    var storage = window.localStorage;
    if (len === 6 && pwd) {     //付款
        $.ajax({
            url: '/bill/pay',
            type: 'POST',
            data: {
                bid: parseInt(storage.getItem("bid")),
                accountId: storage.getItem("account_id"),
                password: pwd
            },
            success: function (data) {
                var content = "";
                switch (data) {
                    case "Fail":
                        content = "密码错误，请重新输入";
                        TINY.box.show(content, 0, 0, 0, 0, 2);
                        clearContent();
                        break;
                    case "NotEnough":
                        content = "账户余额不足,请前往充值";
                        TINY.box.show(content, 0, 0, 0, 0, 2);
                        setTimeout(function () {
                            parent.location.href = "/memberInfo.html";
                        }, 2000);
                        break;
                    case "Pass":
                        content = "有菜品用量不够，请重新点餐";
                        TINY.box.show(content, 0, 0, 0, 0, 2);
                        setTimeout(function () {
                            parent.location.href = "/market.html";
                        }, 2000);
                        break;
                    case "Success":
                        content = "支付成功";
                        TINY.box.show(content, 0, 0, 0, 0, 2);
                        //跳转至订单详情界面
                        setTimeout(function () {
                            parent.location.href = "/billManagement.html";
                        }, 2000);
                        break;
                    default:
                        content = "未知错误";
                        TINY.box.show(content, 0, 0, 0, 0, 2);
                }

            },
            error: function (error) {
                console.log(error);
            }
        });
        // $.toast("密码错误")
        // window.location.href = 'activity_buy_result.html'

    } else {
        $.toast("请输入支付密码")
    }
}


var pwd = "";
var len = 0;
// type=tel input框
var $inputs = $(".surface-ipt input");
$(".real-ipt").on("input", function () {
    if (!$(this).val()) {   //无值
    }
    if (/^[0-9]*$/g.test($(this).val())) {  //有值且只能是数字（正则）
        pwd = $(this).val().trim();
        len = pwd.length;
        for (var i in pwd) {
            $inputs.eq(i).val(pwd[i]);
        }
        $inputs.each(function () {  //将有值的当前input 后面的所有input清空
            var index = $(this).index();
            if (index >= len) {
                $(this).val("");
            }
        })
        if (len === 6) {
            //执行付款操作
            confirm();
        }

    } else {    //清除val中的非数字，返回纯number的value
        var arr = $(this).val().match(/\d/g);
        try {
            $(this).val($(this).val().slice(0, $(this).val().lastIndexOf(arr[arr.length - 1]) + 1));
        } catch (e) {
            // console.log(e.message)
            //清空
            $(this).val("");
        }
    }
    console.log("password:" + pwd);
})
//  获取焦点事件避免输入键盘挡住对话框
$('.real-ipt').on('focus', function () {
    $('.pay-dialog').css('top', '1rem')
})
$('.real-ipt').on('blur', function () {
    $('.pay-dialog').css('top', '3rem')
})

