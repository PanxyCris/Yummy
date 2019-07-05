var storage = window.localStorage;
var user_id = storage.getItem("user_id");
var canteenId = storage.getItem("canteen_id");
var isCategory = false;
getCategoryList();
getCartList();
var firstCategoryId = "";

function getCategoryList() {
    var canteenName = "";
    $.ajax({
        url: "/user/getCanteenInfo",
        type: "GET",
        async: false,
        data: {
            userId: canteenId
        },
        success: function (data) {
            canteenName = data.name;
        }, error: function (error) {
            console.log(error);
        }
    });
    $.ajax({
        url: "/food/getCategoryList",
        type: "GET",
        async: false,
        data: {
            userId: canteenId
        },
        success: function (data) {
            var categoryList = "<nav class=\"navbar navbar-default navbar-fixed\">\n" +
                "            <div class=\"container-fluid\">\n" +
                "                <div class=\"navbar-header\">\n" +
                "                    <a class=\"navbar-brand\" href=\"#\">" + canteenName + "</a>\n" +
                "                </div>\n" +
                "                <div class=\"collapse navbar-collapse\">\n" +
                "<ul class=\"nav navbar-nav\">";
            if (data == null || data.length == 0) {
                categoryList = "<li>\n" +
                    "                            <a\">\n" +
                    "                                <p>该餐厅还没有分类</p>\n" +
                    "                            </a>\n" +
                    "                        </li>";
            } else {
                isCategory = true;
                for (var c in data) {
                    if (c == 0)
                        firstCategoryId = "category-" + data[c].cid;
                    categoryList += "<li>\n" +
                        "                            <a id=\"category-" + data[c].cid + "\" onclick=\"getFoodList(this.id)\">\n" +
                        "                                <p>" + data[c].name + "</p>\n" +
                        "                            </a>\n" +
                        "                        </li>";
                }
            }
            categoryList += " </ul>" +
                "</div>\n" +
                "            </div>\n" +
                "        </nav>\n";
            document.getElementById("categoryList").innerHTML = categoryList;
            if (firstCategoryId != "")
                getFoodList(firstCategoryId);
        }, error: function (error) {
            console.log(error);
        }
    });
}

function getFoodList(cid) {
    cid = (cid + "").substring((cid + "").indexOf("-") + 1);
    $.ajax({
        url: "/food/getFoodList",
        type: "GET",
        async: false,
        data: {
            userId: canteenId,
            cid: parseInt(cid),
        },
        success: function (data) {
            var foodList = "";
            if (data == null || data.length == 0) {
                foodList = "<p>该分类还没有菜品</p>";
            } else {
                for (var f in data) {
                    if (f % 4 == 0)
                        foodList += "<div class=\"row\">\n";
                    foodList +=
                        "                    <div class=\"col-md-3\">\n" +
                        "                        <div class=\"card \">\n" +
                        "                            <div class=\"header\">\n" +
                        "                                <h4 class=\"title\"><strong>" + data[f].name + "</strong></h4>\n" +
                        "                                <p style='color:red' class=\"category\">¥ " + data[f].price + "</p>\n" +
                        "                            </div>\n" +
                        "                            <div class=\"content\">\n" +
                        "                                <img>\n" +
                        "                                <button onclick='addCart(this.id)' id=\"foodCart-" + data[f].fid + "\"class=\"btn btn-primary btn-fill\">加入购物车</button>\n" +
                        "                            </div>\n" +
                        "                        </div>\n" +
                        "                    </div>\n";
                    if (f % 4 == 3 || f == data.length - 1)
                        foodList += "                </div>";
                }
            }
            document.getElementById("foodList").innerHTML = foodList;
        }, error: function (error) {
            console.log(error);
        }
    });
}

function getCartList() {
    $.ajax({
        url: "/bill/getCartList",
        type: "GET",
        async: false,
        data: {
            userId: user_id,
            canteenId: canteenId,
        },
        success: function (data) {
            if (data != null) {
                var totalPrice = 0;
                for (var i in data) {
                    var food = data[i].food;
                    totalPrice += food.price * data[i].number;
                    $("#cart").append("<li id='cartLi" + food.fid + "'>\n" +
                        "                <div class=\"title\">" + food.name + "</div>\n" +
                        "                <div id=\"foodPrice-" + food.fid + "\"class=\"cd-price\">¥" + food.price + "</div>" +
                        "                       <button onclick='addNumber(this.id)' id=\"addNumber-" + food.fid + "\"class=\"btn-sm\">+</button>" +
                        "                <span id='foodNumber-" + food.fid + "' class=\"cd-qty\">" + data[i].number + "</span>\n" +
                        "                       <button onclick='minusNumber(this.id)' id=\"minusNumber-" + food.fid + "\"class=\"btn-sm\">-</button>" +
                        "                <a onclick='removeFood(this.id)' id='remove-" + food.fid + "' class=\"cd-item-remove cd-img-replace\">移除</a>\n" +
                        "            </li>"
                    );
                }
                console.log(totalPrice);
                document.getElementById("totalPrice").innerHTML = "¥" + totalPrice;
            }
        }, error: function (error) {
            console.log(error);
        }
    });
}

function addCart(fid) {
    fid = (fid + "").substring((fid + "").indexOf("-") + 1);

    if (document.getElementById("foodNumber-" + fid) != null) {
        changeNumber(fid, "add");
    }
    else {
        $.ajax({
            url: "/food/getFood",
            type: "GET",
            async: false,
            data: {
                fid: parseInt(fid)
            },
            success: function (data) {
                console.log(data);
                $("#cart").append("<li id='cartLi" + fid + "'>\n" +
                    "                <div class=\"title\">" + data.name + "</div>\n" +
                    "                <div id=\"foodPrice-" + fid + "\"class=\"cd-price\">¥" + data.price + "</div>" +
                    "                       <button onclick='addNumber(this.id)' id=\"addNumber-" + fid + "\"class=\"btn-sm\">+</button>" +
                    "                <span id='foodNumber-" + fid + "' class=\"cd-qty\">1</span>\n" +
                    "                       <button onclick='minusNumber(this.id)' id=\"minusNumber-" + fid + "\"class=\"btn-sm\">-</button>" +
                    "                <a onclick='removeFood(this.id)' id='remove-" + fid + "' class=\"cd-item-remove cd-img-replace\">移除</a>\n" +
                    "            </li>"
                );
                var totalPrice = document.getElementById("totalPrice").innerHTML;
                console.log(totalPrice);
                var originVal = parseFloat(totalPrice.substr(1));
                changeCart(fid, true);
                document.getElementById("totalPrice").innerHTML = "¥" + (originVal + data.price);
                layer.msg('添加成功');
            },
            error: function (error) {
                console.log(error);
            }
        });
    }
}

function changeCart(fid, isAdd) {
    $.ajax({
        url: "/cart/changeCart",
        type: "POST",
        async: false,
        data: {
            userId: user_id,
            canteenId: canteenId,
            fid: parseInt(fid),
            isAdd: isAdd
        },
        success: function (data) {
        },
        error: function (error) {
            console.log(error);
        }
    });
}

function addNumber(fid) {
    fid = (fid + "").substring((fid + "").indexOf("-") + 1);
    changeNumber(fid, "add");
}

function minusNumber(fid) {
    fid = (fid + "").substring((fid + "").indexOf("-") + 1);
    changeNumber(fid, "minus");
}

function removeFood(fid) {
    fid = (fid + "").substring((fid + "").indexOf("-") + 1);
    var originVal = parseFloat(document.getElementById("totalPrice").innerHTML.substr(1));
    var originNumber = parseInt(document.getElementById("foodNumber-" + fid).innerHTML);
    var foodPrice = parseFloat(document.getElementById("foodPrice-" + fid).innerHTML.substr(1));
    document.getElementById("totalPrice").innerHTML = "¥" + (originVal - foodPrice * originNumber);
    $("#cartLi" + fid).remove();
    $.ajax({
        url: "/cart/removeFood",
        type: "POST",
        async: false,
        data: {
            userId: user_id,
            canteenId: canteenId,
            fid: parseInt(fid)
        },
        success: function (data) {
        },
        error: function (error) {
            console.log(error);
        }
    });
}

function changeNumber(fid, type) {
    var originNumber = parseInt(document.getElementById("foodNumber-" + fid).innerHTML);
    var originVal = parseFloat(document.getElementById("totalPrice").innerHTML.substr(1));
    var finalNumber = 0;
    var finalPrice = 0;
    var foodPrice = parseFloat(document.getElementById("foodPrice-" + fid).innerHTML.substr(1));
    if (type == "add") {
        finalNumber = originNumber + 1;
        finalPrice = originVal + foodPrice;
        changeCart(fid, true);
    } else if (type == "minus") {
        finalNumber = originNumber - 1;
        finalPrice = originVal - foodPrice;
        changeCart(fid, false);
    }
    document.getElementById("foodNumber-" + fid).innerHTML = finalNumber;
    document.getElementById("totalPrice").innerHTML = "¥" + finalPrice;
}


