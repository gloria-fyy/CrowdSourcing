/**
 * Created by Administrator on 2015/9/20.
 */
    // $("[data-toggle='tooltip']").tooltip();
$("[data-toggle='popover']").popover();
//alert($("[data-toggle='popover']").state);
var emailReg = /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/;
function changeImg() {
    var imgSrc = $("#checkCode");
    var src = imgSrc.attr("src");
    //alert(src)
    imgSrc.attr("src", chgUrl(src));
}
//ʱ���
//Ϊ��ʹÿ������ͼƬ��һ�£�����������������棬������Ҫ����ʱ���
function chgUrl(url) {
    //alert(url.indexOf("?"));
    var timestamp = (new Date()).valueOf();
    if ((url.indexOf("?") >= 0)) {

        url = url.substr(0, url.indexOf("?")) + "?timestamp=" + timestamp;
    } else {
        url = url + "?timestamp=" + timestamp;
    }
    return url;
}
function check(str) {
    //alert("sub");
    var input = $("#" + str);
    var flag = false;
    if (str == "email") {
        var emailReg = /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/;
        if (!emailReg.test($("#inputEmail").val())) {
            // alert("��������ȷ�������ʽ");
            $("#inputPassword").popover("hide");
            $("#inputPassword-Confirm").popover("hide");
            $("#inputCheckCode").popover("hide");
            $("#inputEmail").popover("show");
        } else {
            $("#inputEmail").popover("hide");
            flag = true;
        }
    } else if (str == "password") {
        if ($("#inputPassword").val().length < 6) {
            //alert(22);
            $("#inputPassword-Confirm").popover("hide");
            $("#inputCheckCode").popover("hide");
            $("#inputEmail").popover("hide");
            $("#inputPassword").popover("show");
        } else {
            $("#inputPassword").popover("hide");
            flag = true;
        }

    } else if (str == "confirm") {
        if ($("#inputPassword-Confirm").val() == $("#inputPassword").val()) {
            //alert(22);
            $("#inputPassword-Confirm").popover("hide");
            flag = true;
        } else {
            $("#inputCheckCode").popover("hide");
            $("#inputEmail").popover("hide");
            $("#inputPassword").popover("hide");
            $("#inputPassword-Confirm").popover("show");
        }

    } else if (str == "checkCode") {
        //����ajax���󣬺������ֵ�����ж�
        //alert("ajax");
        $.post("verifyCheckCode.do", {checkCode: $("#inputCheckCode").val()}, function (data, status) {
            if (data == "error") {
                $("#inputEmail").popover("hide");
                $("#inputPassword").popover("hide");
                $("#inputPassword-Confirm").popover("hide");

                $("#inputCheckCode").popover("show");
            } else if (data == "right") {
                $("#inputCheckCode").popover("hide");
                flag = true;
            }
            //alert("Data: " + data + "\nStatus: " + status);
        });
    }
    return flag;
}
function validate() {
    var arr = ["email", "password", "confirm", "checkCode"];
    var i = 0;
    submitOK = true;
    while (i <= 3) {
        if (!check(arr[i])) {
            //alert(arr[i]+" wrong!");
            submitOK = false;
            break;
        }
        i++;
    }
    if (submitOK) {
        //alert("�ύ�ɹ���");
        return true;
    }
    else {
        //alert("�ύʧ��");
        return false;
    }
}
$(function () {

})