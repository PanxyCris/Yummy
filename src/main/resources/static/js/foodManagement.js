var storage = window.localStorage;
var user_id = storage.getItem("user_id");
var isCategory = false;
getCategoryList();
var firstCategoryId = "";

function getCategoryList() {
    $.ajax({
        url: "/food/getCategoryList",
        type: "GET",
        async: false,
        data: {
            userId: user_id
        },
        success: function (data) {
            var categoryList = "<nav class=\"navbar navbar-default navbar-fixed\">\n" +
                "            <div class=\"container-fluid\">\n" +
                "                <div class=\"navbar-header\">\n" +
                "                    <a class=\"navbar-brand\" href=\"#\">分类列表</a>\n" +
                "                </div>\n" +
                "                <div class=\"collapse navbar-collapse\">\n" +
                "<ul class=\"nav navbar-nav\">";
            if (data == null || data.length == 0) {
                categoryList = "<li>\n" +
                    "                            <a\">\n" +
                    "                                <p>您还没有分类，请尽快增加</p>\n" +
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
            cid: parseInt(cid),
        },
        success: function (data) {
            var foodList = "";
            if (data == null || data.length == 0) {
                foodList = "<p>您还没有菜品，快点补充吧！</p>";
            } else {
                for (var f in data) {
                    if (f % 2 == 0)
                        foodList += "<div class=\"row\">\n";
                    foodList +=
                        "                    <div class=\"col-md-5\">\n" +
                        "                        <div class=\"card \">\n" +
                        "                            <div class=\"header\">\n" +
                        "                                <h4 class=\"title\">" + data[f].name + "</h4>\n" +
                        "                                <p class=\"category\">¥ " + data[f].price + "</p>\n" +
                        "                            </div>\n" +
                        "                            <div class=\"content\">\n" +
                        "                                <img>\n" +
                        "                                <p class=\"legend\">当前所剩" + data[f].number + "</p>\n" +
                        "                                <button onclick='addStock(this.id)' id=\"add-" + data[f].fid + "\"class=\"btn btn-success btn-fill\">添置菜品</button>\n" +
                        "                                <!--<button class=\"btn btn-warning btn-fill\">查看库存</button>-->\n" +
                        "                                <button onclick='newFood(this.id)' id=\"edit-" + data[f].fid + "\"class=\"btn btn-primary btn-fill\">编辑信息</button>\n" +
                        "                            </div>\n" +
                        "                        </div>\n" +
                        "                    </div>\n";
                    if (f % 2 == 1)
                        foodList += "                </div>";
                }
            }
            document.getElementById("foodList").innerHTML = foodList;
        }, error: function (error) {
            console.log(error);
        }
    });
}

function addStock(fid) {
    if (fid == null)
        fid = "";
    else
        fid = (fid + "").substring((fid + "").indexOf("-") + 1);
    storage.setItem("add_food_id", fid);
    layer.open({
        type: 2,
        area: ['700px', '600px'],
        // fixed: false, //不固定
        // maxmin: true,
        content: 'addStock.html'
    });
}

function newFood(fid) {
    if (fid == null)
        fid = "";
    else
        fid = (fid + "").substring((fid + "").indexOf("-") + 1);
    storage.setItem("food_id", fid);
    layer.open({
        type: 2,
        area: ['700px', '600px'],
        // fixed: false, //不固定
        // maxmin: true,
        content: 'newFood.html'
    });
}

function newCategory(cid) {
    if (cid == null)
        cid = "";
    else
        cid = (cid + "").substring((cid + "").indexOf("-") + 1);
    storage.setItem("category_id", cid);
    layer.open({
        type: 2,
        area: ['700px', '450px'],
        // fixed: true, //不固定
        // maxmin: true,
        content: 'newCategory.html'
    });
}


