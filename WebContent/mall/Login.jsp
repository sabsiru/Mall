<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ include file="Header.jsp" %>
<html>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script>
    /*submit 버튼 눌렀을떄 로그인 실패시 알람 띄우기*/
    /*컨트롤러에서 보낸 session.msg 받기*/
    var msg = "${sessionScope.msg}";
    if (msg !== "" && msg !== null && msg !== "null" && msg !== undefined) {
        alert(msg);
    }
    <%session.removeAttribute("msg");%>
    <%session.removeAttribute("result");%>
</script>
<style>
    /*container 중앙 정렬*/
    .container {
        width: 500px;
        margin: 0 auto;
    }

    table {
        border: 1px solid #ccc;
        border-collapse: collapse;
    }

    td {
        border: 1px solid #ccc;
        padding: 10px;
    }

    input[type=text], input[type=password] {
        border: 1px solid #ccc;
        padding: 10px;
    }

    /*a링크랑 사이즈 똑같게*/
    input[type=submit] {
        border: 1px solid #ccc;
        padding: 10px;
        background-color: #639aee;
        color: #fff;
    }

    /*a 링크 버튼처럼 꾸미기*/
    a {
        text-decoration: none;
        color: #000;
        border: 1px solid #639aee;
        padding: 10px;
        background-color: #639aee;
        color: #fff;
    }

    /*.button 중앙 정렬하고 이쁘게 꾸미기*/
    .button {
        text-align: center;
    }

    .button-blue {
        border: 1px solid #ccc;
        padding: 10px;
        background-color: #639aee;
        color: #fff;
    }
</style>
<head>
    <title>로그인</title>
</head>
<body>
<!--로그인 폼-->
<div class="container">
    <h1>로그인</h1>
    <form action="login.do" method="post" id="login">
        <table>
            <tr>
                <td>아이디</td>
                <td colspan="2"><input type="text" name="id" id="id"></td>
                <td rowspan="3"><input type="submit" value="로그인" class="button-blue"></td>
            </tr>
            <tr>
                <td>비밀번호</td>
                <td colspan="3"><input type="password" name="pass" id="pass"></td>
            </tr>
        </table>
        <br>
        <div class="button">
            <tr>
                <td><input value="아이디 찾기" name="findIdBtn" type="button" class="button-blue"></td>
                <td><input value="비밀번호 찾기" name="findPassBtn" type="button" class="button-blue"></td>
            </tr>
        </div>
    </form>
<div id="findIdDiv" hidden="hidden">
        <h1>아이디 찾기</h1>
        <form  method="post">
            <table>
                <tr>
                    <td>이름</td>
                    <td><input type="text" name="name" id="name" required></td>
                </tr>
                <tr>
                    <td>휴대폰번호</td>
                    <td><input type="text" name="phone" id="phone" required></td>
                </tr>
                <div class="button">
                    <tr>
                        <td colspan="2" align="center"><input type="button" value="아이디 찾기" id="findId" class="button-blue"></td>
                    </tr>
                </div>
            </table>
        </form>
</div>
    <div id="findPassDiv" hidden="hidden">
        <div class="container">
            <h1>비밀번호 찾기</h1>
            <form  method="post">
                <table>
                    <tr>
                        <td>아이디</td>
                        <td><input type="text" name="id" id="passId" required></td>
                    </tr>
                    <tr>
                        <td>휴대폰번호</td>
                        <td><input type="text" name="phone" id="passPhone" required></td>
                    </tr>
                    <div class="button">
                        <tr>
                            <td colspan="2" align="center"><input type="button" value="비밀번호 찾기" id="findPass" class="button-blue"></td>
                        </tr>
                    </div>
                </table>
            </form>
        </div>
    </div>
    <div class="showBox" hidden="hidden">
        <table>
        <tr>
            <td><div id="showText"></div></td>
        </tr>
        </table>
    </div>
</div>


</body>
<script>
    //아이디 찾기
    document.getElementById("findId").onclick = function () {
        var name = document.getElementById("name").value;
        var phone = document.getElementById("phone").value;
        //입력값이 없으면 경고창 띄우기
        if (name === "" || phone === "") {
            document.getElementById("showText").innerHTML = "이름이나 휴대폰번호를 입력해주세요.";
            document.getElementsByClassName("showBox")[0].removeAttribute("hidden");
            return;
        }
        $.ajax({
            url: "findId.do",
            type: "post",
            data: {
                name: name,
                phone: phone
            },
            success: function (result) {
                //result값이 null이 아니면 아이디 보여주기
                if (result !== "no") {
                    document.getElementById("showText").innerHTML = "회원님의 아이디는 "+ result+" 입니다.";
                    document.getElementsByClassName("showBox")[0].removeAttribute("hidden");
                } else {
                    document.getElementById("showText").innerHTML = "일치하는 정보가 없습니다.";
                    document.getElementsByClassName("showBox")[0].removeAttribute("hidden");
                }
            }
        })
    }
    //비밀번호 찾기
    document.getElementById("findPass").onclick = function () {
        var id = document.getElementById("passId").value;
        var phone = document.getElementById("passPhone").value;
        //입력값이 없으면 경고창 띄우기
        if (id === "" || passPhone === "") {
            document.getElementById("showText").innerHTML = "이름이나 휴대폰번호를 입력해주세요.";
            document.getElementsByClassName("showBox")[0].removeAttribute("hidden");
            return;
        }
        $.ajax({
            url: "findPass.do",
            type: "post",
            data: {
                id: id,
                phone: phone
            },
            success: function (result) {
                //result값이 null이 아니면 아이디 보여주기
                if (result !== "no") {
                    document.getElementById("showText").innerHTML = "회원님의 비밀번호는 "+ result+" 입니다.";
                    document.getElementsByClassName("showBox")[0].removeAttribute("hidden");
                } else {
                    document.getElementById("showText").innerHTML = "일치하는 정보가 없습니다.";
                    document.getElementsByClassName("showBox")[0].removeAttribute("hidden");
                }
            }
        })
    }
    //아이디 찾기 버튼
    document.getElementsByName("findIdBtn")[0].onclick = function () {
        document.getElementById("findIdDiv").removeAttribute("hidden");
        document.getElementById("findPassDiv").setAttribute("hidden", "hidden");
    }
    //비밀번호 찾기 버튼
    document.getElementsByName("findPassBtn")[0].onclick = function () {
        document.getElementById("findPassDiv").removeAttribute("hidden");
        document.getElementById("findIdDiv").setAttribute("hidden", "hidden");
    }

</script>
</html>
