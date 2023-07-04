<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="Header.jsp" %>
<html>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<style>
    body {
        font-family: Arial, sans-serif;
        background-color: #f1f1f1;
    }

    .container {
        margin: 20px auto;
        background-color: #fff;
        border-radius: 8px;
        padding: 20px;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    }

    h1 {
        color: #007bff;
        font-size: 24px;
        margin-bottom: 20px;
        text-align: center;
    }

    table {
        width: 100%;
        border-collapse: collapse;
    }

    th, td {
        padding: 10px;
        text-align: center;
        border-bottom: 1px solid #ddd;
    }

    th {
        color: #007bff;

    }

    tr:nth-child(even) {
        background-color: #f2f2f2;
    }

    .orderList input[type="button"] {
        background-color: #007bff;
        color: #fff;
        border: none;
        padding: 8px 12px;
        border-radius: 4px;
        cursor: pointer;
    }

    .orderList input[type="button"]:hover {
        background-color: #0056b3;
    }

    .orderList img {
        max-width: 50px;
        max-height: 50px;
    }

    .search {
        margin-bottom: 20px;
    }

    .search form {
        display: flex;
        align-items: center;
    }

    .search select, .search input[type="text"], .search input[type="submit"] {
        margin: 0 5px;
        padding: 8px;
        border: 1px solid #ddd;
        border-radius: 4px;
        font-size: 14px;
    }

    .search input[type="submit"] {
        background-color: #007bff;
        color: #fff;
        border: none;
        cursor: pointer;
    }

    .search input[type="submit"]:hover {
        background-color: #0056b3;
    }

    .noProducts {
        text-align: center;
        font-family: Gulim;
        font-size: 12px;
        color: #999;
    }

    .paging {
        margin-top: 20px;
        text-align: center;
    }

    .paging a {
        display: inline-block;
        padding: 6px 10px;
        margin: 0 3px;
        border: 1px solid #ddd;
        border-radius: 4px;
        color: #007bff;
        text-decoration: none;
    }

    .paging a.active {
        background-color: #007bff;
        color: #fff;
    }
</style>
<script>
    /*submit 버튼 눌렀을떄 로그인 실패시 알람 띄우기*/
    /*컨트롤러에서 보낸 session.msg 받기*/
    var msg = "${sessionScope.msg}";
    if (msg !== "" && msg !== null && msg !== "null" && msg !== undefined) {
        alert(msg);
    }
    <% session.removeAttribute("msg"); %>
</script>
<head>
    <title>주문내역</title>
</head>
<body>
<table>
    <tr>
        <td align="right" style="font-family: Gulim; font-size: 12px;">총&nbsp;${count}건 ${currentPage}페이지</td>
    </tr>
</table>
<div class="search">
    <form action="orderList.do" method="post">
        <select name="keyField">
            <option value="all">전체</option>
            <option value="orderidseq">주문번호</option>
            <option value="pname">상품명</option>
        </select>
        <input type="text" name="keyWord">
        <input type="submit" value="검색">
    </form>
</div>
<div class="container">
    <h1>주문내역</h1>
    <form action="orderList.do" method="post">
        <table class="forRowspan">
            <tr>
                <th>주문 날짜</th>
                <th>주문번호</th>
                <th>상품 번호</th>
                <th colspan="4">상품정보</th>
                <th>상태</th>
                <th colspan="2">확인/신청</th>
            </tr>
            <div class="orderList">
                <c:forEach var="order" items="${orderList}">
                <tr>
                    <td>${order.orderdate}</td>
                    <td><input type="button" value="${order.orderidseq}" name="orderidseq"
                               readonly></td>
                    <td><input type="button" value="${order.pno}"
                               onclick="location.href='productDetail.do?pno=${order.pno}'"></td>
                    <td><img src="/spring/upload/${order.pimage}" alt="" width="50px" height="50px"></td>
                    <td>${order.pname}</td>
                    <td>${order.optionname}</td>
                    <td>수량 :${order.stockcount}</td>

                        <%--                    상태 /확인/신청--%>
                    <c:if test="${order.status eq 'ready' &&order.refund eq 'a' &&order.receive eq 'yet'}">
                        <td>배송 준비 중</td>
                        <td colspan="2"><input type="button" value="주문취소" name="orderCancel" onclick="refundBtn(${order.orderidseq}, '${order.pname}','${order.optionname}')"></td>
                    </c:if>
                    <c:if test="${order.status eq 'ready' &&order.refund eq 'request' &&order.receive eq 'yet'}">
                        <td>환불 대기 중</td>
                        <td colspan="2"><input type="button" value="반품 취소" name="cancelRequest" onclick="cancel(${order.orderidseq}, '${order.pname}','${order.optionname}')"></td>
                    </c:if>
                    <c:if test="${order.status eq 'ready' &&order.refund eq 'accept' &&order.receive eq 'yet'}">
                        <td colspan="3">환불 완료</td>
                    </c:if>
                    <c:if test="${order.status eq 'shipping' &&order.refund eq 'a' && order.receive eq 'yet'}">
                        <td>배송 중</td>
                        <td colspan="2"><input type="button" value="반품 신청" name="refundRequest" onclick="refundBtn(${order.orderidseq}, '${order.pname}','${order.optionname}')"></td>
                    </c:if>
                    <c:if test="${order.status eq 'shipping' &&order.refund eq 'request' && order.receive eq 'yet'}">
                        <td>반품 대기 중</td>
                        <td colspan="2"><input type="button" value="반품 취소" name="cancelRequest" onclick="cancel(${order.orderidseq}, '${order.pname}','${order.optionname}')"></td>
                    </c:if>
                    <c:if test="${order.status eq 'shipping' &&order.refund eq 'accept' && order.receive eq 'yet'}">
                        <td colspan="3">반품 완료</td>
                    </c:if>
                    <c:if test="${order.status eq 'arrive' &&order.refund eq 'a' && order.receive eq 'yet'}">
                        <td>배송 완료</td>
                        <td><input type="button" value="반품 신청" name="refundRequest" onclick="refundBtn(${order.orderidseq}, '${order.pname}','${order.optionname}')"></td>
                        <td><input type="button" value="구매 확정" name="refundComplete" onclick="orderConfirm(${order.orderidseq}, '${order.pname}','${order.optionname}')"></td>
                    </c:if>
                    <c:if test="${order.status eq 'arrive' &&order.refund eq 'request' && order.receive eq 'yet'}">
                        <td>반품 대기 중</td>
                        <td colspan="2"><input type="button" value="반품 취소" name="cancelRequest" onclick="cancel(${order.orderidseq}, '${order.pname}','${order.optionname}')"></td>
                    </c:if>
                    <c:if test="${order.status eq 'arrive' &&order.refund eq 'accept' && order.receive eq 'yet'}">
                        <td colspan="3">반품 완료</td>
                    </c:if>
                    <c:if test="${order.status eq 'arrive' &&order.refund eq 'a' && order.receive eq 'done'}">
                        <td colspan="3">구매 확정</td>
                    </c:if>

                </tr>
                </c:forEach>
                <c:if test="${count==0}">
                <tr>
                    <td colspan="9" align="center" style="font-family: Gulim; font-size: 12px;">주문 내역이 없습니다.
                    </td>
                </tr>
                </c:if>
        </table>
    </form>
</div>
<table align="center">
    <c:if test="${count>0}">
        <tr>
            <td align="center" class="paging">${pagingHtml}</td>
        </tr>
    </c:if>
</table>
</body>
</html>
<script>
    function orderConfirm(orderidseq,pname,optionname) {
        var data = {
            orderidseq: orderidseq,
            pname:pname,
            optionname:optionname
        };
        $.ajax({
            url: "orderConfirm.do",
            type: "post",
            data: data,
            success: function (data) {
                alert("주문확정 완료");
                location.reload();
            }
        });
    }

    function refundBtn(orderidseq,pname,optionname) {
        var data = {
            orderidseq: orderidseq,
            pname:pname,
            optionname:optionname
        };
        $.ajax({
            url: "refundRequest.do",
            type: "post",
            data: data,
            success: function (data) {
                alert("반품요청 완료");
                location.reload();
            }
        });
    }

    function cancel(orderidseq,pname,optionname) {
        var data = {
            orderidseq: orderidseq,
            pname:pname,
            optionname:optionname
        };
        $.ajax({
            url: "cancelRequest.do",
            type: "post",
            data: data,
            success: function (data) {
                alert("반품 취소 완료");
                location.reload();
            }
        });
    }


</script>