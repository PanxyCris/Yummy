var storage = window.localStorage;
var user_id = storage.getItem("user_id");
var categoryId = -1;
getCategory();

function getCategory() {

}

function saveCategory() {
    if (categoryId == null)
        categoryId = -1;
    $.ajax({
        url: "/food/saveCategory",
        type: "POST",
        async: false,
        data: {
            cid: parseInt(categoryId),
            name: $("#categoryName").val(),
            userId: user_id
        },
        success: function (data) {
            storage.removeItem("category_id");
            content = "修改成功！";
            TINY.box.show(content, 0, 0, 0, 0, 2);
            setTimeout(function () {
                var index = parent.layer.getFrameIndex(window.name);
                console.log(index);
                parent.layer.close(index);
                parent.location.href = "/foodManagement.html";
            }, 2000);
        },
        error: function () {
            content = "请求失败！再试一次 . . .";
            TINY.box.show(content, 0, 0, 0, 0, 3);
        }
    });
}