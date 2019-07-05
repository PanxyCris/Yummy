var n_msg = "";
var p_msg = "";
var d_msg = "";
var n_valid = 0;
var p_valid = 0;
var isVertifySucc = false;  //var d_valid = 0;
var oUsername = "";
var oPassword = "";

window.onload = function () {
    loginByEmail();
}

function loginByEmail() {
    var aInput = document.getElementsByTagName('input');
    var oUser = aInput[0];
    var oPwd = aInput[1];

    var aP = document.getElementsByTagName('p');
    var all_msg = aP[0];
    n_msg = "";
    p_msg = "";
    n_valid = 0;
    p_valid = 0;


    //用户名检测
    oUser.onfocus = function () {
        oUser.removeAttribute("placeholder");
    }
    oUser.onkeyup = function () {

    }
    oUser.onblur = function () {
        var tel = /^([a-zA-Z0-9_\-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([a-zA-Z0-9\-]+\.)+))([a-zA-Z]{1,5}|[0-9]{1,3})(\]?)$/;
        n_valid = 0;
        if (this.value == "") {
            n_msg = "用户名不可为空";
            all_msg.innerHTML = n_msg + "<br /><br />";
        }
        // else if(!tel.test(this.value)){
        //     n_msg = "邮箱不正确  ";
        //     all_msg.innerHTML=n_msg + "<br /><br />";
        // }
        else {
            n_valid = 1;
            oUsername = this.value;
            n_msg = "";
            all_msg.innerHTML = n_msg + "<br /><br />";
        }
    }


    //密码检测
    oPwd.onfocus = function () {
        if (n_valid == 0) {
            if (oUser.value == "") {
                n_msg = "用户名不可为空";
            }
            all_msg.innerHTML = n_msg + "<br /><br />";
        }
        oPwd.removeAttribute("placeholder");
    }
    oPwd.onblur = function () {
        if (n_valid == 0) {
            all_msg.innerHTML = n_msg + "<br /><br />";
        }
        else if (n_valid == 1 && this.value == "") {
            p_msg = "密码不可为空";
            all_msg.innerHTML = p_msg + "<br /><br />";
        }
        else {
            p_valid = 1;
            oPassword = this.value;
            p_msg = "";
            all_msg.innerHTML = n_msg + p_msg + "<br /><br />";
        }
        //oPwd.setAttribute("placeholder");
        //oPwd.style.placeholder='请输入确认密码';

        /*if(n_valid == 1 && p_valid == 1){
            loginCon_btn.disabled = false;
        }
        else{
            loginCon_btn.disabled = true;
        }*/
    }

}


function login() {
    //1-调用后端的方法验证（待定）
    //2-若验证为真，则跳转到登录后的主页；若为假，则提示失败原因（待定）

    var content = "";
    if (n_valid == 1 && p_valid == 1) {

        if (!window.localStorage) {
            alert("浏览器不支持localStorage");
        } else {
            var storage = window.localStorage;
            storage.setItem("user_id", oUsername);
        }

        $.ajax({
            url: "/user/login",
            type: "POST",
            async: false,
            data: {
                username: oUsername,
                password: oPassword
            },
            success: function (data) {
                content = "登录成功！即将跳转 . . .";
                TINY.box.show(content, 0, 0, 0, 0, 2);
                switch (data) {
                    case "MemberLogin":
                        setTimeout(function () {
                            window.location.href = "/market.html";
                        }, 2000);
                        break;
                    case "CanteenLogin":
                        setTimeout(function () {
                            $.ajax({
                                url: "/user/ifSubmit",
                                type: "GET",
                                async: false,
                                data: {
                                    id: oUsername
                                },
                                success: function (submit) {
                                    if (submit)
                                        window.location.href = "/foodManagement.html";
                                    else
                                        window.location.href = "/canteenInfo.html";
                                }
                            });
                        }, 2000);
                        break;
                    case "AdminLogin":
                        content = "登录成功！即将跳转 . . .";
                        TINY.box.show(content, 0, 0, 0, 0, 2);
                        setTimeout(function () {
                            //跳转到登录后的主页（待定）
                            window.location.href = "/check.html";
                        }, 2000);
                        break;
                    case "NotExisted":
                        content = "用户不存在！请前往注册 . . .";
                        TINY.box.show(content, 0, 0, 0, 0, 2);
                        setTimeout(function () {
                            window.location.href = "register.html";
                        }, 2000);
                        break;
                    case "Fail":
                        content = "密码错误！再试一次 . . .";
                        TINY.box.show(content, 0, 0, 0, 0, 3);
                        break;
                    case "Cancel":
                        content = "您已注销!";
                        TINY.box.show(content, 0, 0, 0, 0, 3);
                        // setTimeout(function () {
                        //     window.location.href = "/register.html";
                        // }, 2000);
                        break;
                    default:
                        content = "登录失败！再试一次 . . .";
                        TINY.box.show(content, 0, 0, 0, 0, 3);
                        break;
                }
            },
            error: function () {
                content = "请求失败！再试一次 . . .";
                TINY.box.show(content, 0, 0, 0, 0, 3);
            }
        });

    }
    else {
        content = "登录失败！再试一次 . . .";
        TINY.box.show(content, 0, 0, 0, 0, 3);
    }
}
