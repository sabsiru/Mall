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
    <% session.removeAttribute("msg"); %>
</script>
<style>
    .container {
        margin: 20px auto;
        width: 1000px;
        background-color: #ffffff;
        padding: 20px;
        box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
    }

    .search {
        text-align: right;
        margin-bottom: 20px;
    }
        .search form {
        display: flex;
        align-items: center;
    }

    .search select,
    .search input[type="text"],
    .search input[type="submit"] {
        font-family: Gulim;
        font-size: 12px;
    }

    .box {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
        gap: 20px;
    }

    .box a {
        display: block;
        text-decoration: none;
    }

    .box img {
        width: 150px;
        height: 150px;
        object-fit: cover;
    }

    .box .product-name {
        margin-top: 10px;
        font-weight: bold;
    }

    .box .price {
        margin-top: 5px;
    }

    table {
        margin: 20px auto;
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
<head>
    <title>메인페이지</title>
</head>
<body>
<table>
    <tr>
        <td align="right" style="font-family: Gulim; font-size: 12px;">총&nbsp;${count}건 ${currentPage}페이지</td>
    </tr>
</table>
<div class="search">
<form action="main.do" name="search" method="post" onsubmit="return searchCheck()">
        <table width="500" align="right">
            <tr>
                <td align="center" width="90%"><select name="keyField">
                    <option value="pname">상품명</option>
                </select> <input type="text" size="16" name="keyWord"> <input type="submit" value="검색"
                                                                              style="font-family: Gulim; font-size: 12px;">
                </td>
            </tr>
        </table>
</form>
</div>
<div class="container">
    <table>
        <div class="box">
            <c:forEach var="product" items="${productList}">
                <div>
                    <a href="productDetail.do?pno=${product.pno}">
                        <img src="${pageContext.request.contextPath}/upload/${product.pimage}" width="150" height="150">
                    </a>
                    <div>${product.pname}</div>
                    <div class="price">${product.price}₩</div>
                </div>
            </c:forEach>
        </div>
        <c:if test="${count==0}">
            <tr>
                <td colspan="5" align="center" style="font-family: Gulim; font-size: 12px;">No products available.</td>
            </tr>
        </c:if>
    </table>
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
