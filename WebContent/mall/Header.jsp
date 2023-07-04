<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<script>
    /*submit 버튼 눌렀을떄 로그인 실패시 알람 띄우기*/
    /*컨트롤러에서 보낸 session.msg 받기*/
    var msg = "${sessionScope.msg}";
    if (msg !== "" && msg !== null && msg !== "null" && msg !== undefined) {
        alert(msg);
    }
    <% session.removeAttribute("msg"); %>
</script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<head>
    <title>루트몰</title>
</head>
<body>
<!--페이지 우측 상단에 로그인, 회원가입 버튼-->
<div class="header">
    <div class="header-right">
        <!--로그인했고 powerno가 0일때-->
        <c:if test="${sessionScope.id != null && sessionScope.powerno == 0}">
            <div class="box">
                <c:out value="${sessionScope.name}"/>님 환영합니다.
            </div>
            <a href="myPage.do">마이페이지</a>
            <a href="listCart.do">장바구니</a>
            <a href="logout.do">로그아웃</a>
        </c:if>
<!--        powerno가 5일때 관리자-->
        <c:if test="${sessionScope.id != null && sessionScope.powerno == 5}">
            <div class="box">
                관리자로 접속중.
            </div>
            <a href="logout.do">로그아웃</a>
            <a href="admin.do">관리자페이지</a>
        </c:if>
        <!--로그인했을경우 회원가입 버튼 안보이기-->
        <c:if test="${sessionScope.id == null}">
            <a href="login.do">로그인</a>
            <a href="join.do">회원가입</a>
        </c:if>

        <a href="main.do">메인페이지</a>
    </div>
</div>
</body>
<style>
    .header {
        overflow: hidden;
        background-color: #639aee;
        padding: 20px 10px;
    }
    .header a {
        float: right;
        color: white;
        text-align: center;
        padding: 12px;
        text-decoration: none;
        font-size: 18px;
        line-height: 25px;
        border-radius: 4px;
    }
</style>
</html>
