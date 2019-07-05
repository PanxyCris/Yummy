var storage = window.localStorage;
var user_id = storage.getItem("user_id");
var accountId = storage.getItem("account_id");
getAccount();

function getAccount() {
    console.log(accountId);
    if (accountId != null && accountId != "") {
        $.ajax({
            url: "/account/getAccount",
            type: "GET",
            async: false,
            data: {
                accountId: accountId
            },
            success: function (data) {
                $("#bankName").val(data.bankName);
                $("#accountId").val(accountId);
                $("#bankName").attr("disabled", true);
                $("#accountId").attr("disabled", true);
                $("#balance").val(data.balance);
            },
            error: function (error) {
                console.log(error);
            }
        });
    }
}

function saveAccount() {
    var content = "";
    $.ajax({
        url: "/account/saveAccount",
        type: "POST",
        async: false,
        data: {
            userId: user_id,
            accountId: $("#accountId").val(),
            bankName: $("#bankName").val(),
            password: $("#password").val(),
            balance: $("#balance").val()
        },
        success: function (data) {
            storage.removeItem("account_id");
            content = "修改成功！";
            TINY.box.show(content, 0, 0, 0, 0, 2);
            setTimeout(function () {
                var index = parent.layer.getFrameIndex(window.name)
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