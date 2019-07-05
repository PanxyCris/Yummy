var storage = window.localStorage;
var user_id = storage.getItem("user_id");
getDiscountList();

function getDiscountList() {
    $.ajax({
        url: "/discount/getDiscountList",
        type: "GET",
        async: false,
        data: {
            userId: user_id
        },
        success: function (data) {
            var discountList = "";
            if (data == null || data.length == 0) {
                discountList = "<p>您还没有设计优惠，快点补充吧！</p>";
            } else {
                for (var f in data) {
                    if (f % 2 == 0)
                        discountList += "<div class=\"row\">\n";
                    discountList +=
                        "                    <div class=\"col-md-5\">\n" +
                        "                        <div class=\"card \">\n" +
                        "                            <div class=\"header\">\n" +
                        "                                <h4 class=\"title\">满" + data[f].fullPrice + "减" + data[f].reduction + "</h4>\n" +
                        "                            </div>\n" +
                        "                            <div class=\"content\">\n" +
                        "                                <button onclick='newDiscount(this.id)' id=\"edit-" + data[f].did + "\"class=\"btn btn-primary btn-fill\">编辑信息</button>\n" +
                        "                                <button onclick='deleteDiscount(this.id)' id=\"delete-" + data[f].did + "\"class=\"btn btn-danger btn-fill\">删除优惠</button>\n" +
                        "                            </div>\n" +
                        "                        </div>\n" +
                        "                    </div>\n";
                    if (f % 2 == 1)
                        discountList += "                </div>";
                }
            }
            document.getElementById("discountList").innerHTML = discountList;
        }, error: function (error) {
            console.log(error);
        }
    });
}

function newDiscount(did) {
    if (did == null)
        did = "";
    else
        did = (did + "").substring((did + "").indexOf("-") + 1);
    storage.setItem("discount_id", did);
    layer.open({
        type: 2,
        area: ['700px', '600px'],
        // fixed: false, //不固定
        // maxmin: true,
        content: 'newDiscount.html'
    });
}

function deleteDiscount(did) {
    did = (did + "").substring((did + "").indexOf("-") + 1);
    var content = "";
    layer.confirm('确定删除该优惠吗？', {
        btn: ['是', '否'] //按钮
    }, function () {
        $.ajax({
            url: "/discount/deleteDiscount",
            type: "POST",
            async: false,
            data: {
                did: parseInt(did)
            },
            success: function (data) {
                content = "删除成功";
                layer.msg(content, {
                    time: 2000
                });
                setTimeout(function () {
                    parent.location.href = "/discountManagement.html";
                }, 2000);
            }, error: function (error) {
                console.log(error);
            }
        });
        // layer.msg('的确很重要', {icon: 1});
    }, function () {
        var index = parent.layer.getFrameIndex(window.name)
        console.log(index);
        parent.layer.close(index);
    });
}


