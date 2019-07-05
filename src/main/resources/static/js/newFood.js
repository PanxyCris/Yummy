var storage = window.localStorage;
var user_id = storage.getItem("user_id");
var foodId = storage.getItem("food_id");
getCategorySelectList();
getFood();

function getCategorySelectList() {
    $.ajax({
        url: "/food/getCategoryList",
        type: "GET",
        async: false,
        data: {
            userId: user_id
        },
        success: function (data) {
            var categoryList = "<select id=\"categorySelectList\"\n" +
                "                                            tabindex=\"2\" class=\"form-control\">\n";
            for (var c in data)
                categoryList += "<option value=\"" + data[c].cid + "\">" + data[c].name + "</option>";
            categoryList += "                                            </select>";
            document.getElementById("categorySelect").innerHTML = categoryList;
        }, error: function (error) {
            console.log(error);
        }
    });
}

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
                $("#foodName").val(data.name);
                $("#categorySelectList").val(data.category.cid);
                $("#price").val(data.price);
                $("#box").val(data.boxPrice);
            },
            error: function (error) {
                console.log(error);
            }
        });
    }
}

function saveFood() {
    if (foodId == null || foodId == "")
        foodId = -1;
    $.ajax({
        url: "/food/saveFood",
        type: "POST",
        async: false,
        data: {
            fid: parseInt(foodId),
            name: $("#foodName").val(),
            cid: parseInt($("#categorySelectList").val()),
            price: $("#price").val(),
            boxPrice: $("#box").val()
        },
        success: function (data) {
            storage.removeItem("food_id");
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