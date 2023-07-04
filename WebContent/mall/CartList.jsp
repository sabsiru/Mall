<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="Header.jsp" %>
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
<style>
    .cart-table {
        width: 100%;
        border-collapse: collapse;
    }

    .cart-table th, .cart-table td {
        padding: 10px;
        text-align: center;
    }

    .cart-table thead th {
        background-color: #f0f0f0;
        font-weight: bold;
    }

    .cart-table .image-column img {
        width: 150px;
        height: 150px;
        object-fit: cover;
    }

    .delete-button, .order-button {
        padding: 8px 16px;
        margin: 5px;
        border: none;
        border-radius: 4px;
        color: #fff;
        font-weight: bold;
        background-color: #4caf50;
        cursor: pointer;
    }

    .delete-button:hover, .order-button:hover {
        background-color: #45a049;
    }

    #totalPrice {
        border: none;
        background-color: transparent;
        text-align: right;
        font-weight: bold;
    }
</style>
<head>
    <title>장바구니</title>
</head>
<body>
<h2>장바구니</h2>
<form action="order.do" method="post" id="form">
    <table class="cart-table">
        <thead>
        <tr>
            <th>이미지</th>
            <th>상품명</th>
            <th>옵션</th>
            <th>가격</th>
            <th>수량</th>
            <th>재고</th>
            <th>총 금액</th>
            <th><input type="checkbox" id="selectAllCheckbox" onclick="toggleSelectAll()"></th>
        </tr>
        </thead>
        <tbody>
        <!--        cartlist값이 없을경우 나타낼 div-->
        <c:if test="${empty cartList}">
            <tr>
                <td colspan="7">장바구니에 담긴 상품이 없습니다.</td>
            </tr>
        </c:if>
        <c:forEach var="cart" items="${cartList}">
            <tr>
                <td hidden="hidden" id="productId">${cart.pno}</td>
                <td class="image-column"><img src="/spring/upload/${cart.pimage}" width="25px" height="25px"></td>
                <td>${cart.pname}</td>
                <td>${cart.optionname}</td>
                <td id="price">${cart.price}</td>
                <c:if test="${cart.remain ne 0}">
                    <td>
                        <div class="quantity-control">
                            <input type="text" class="quantity-input" name="quantity_${cart.cartid}" value="${cart.cartcount}" size="3px" readonly>
                            <input type="button" value="-" onclick="minus(this)" disabled>
                            <input type="button" value="+" onclick="plus(this)" disabled>
                        </div>
                    </td>

                </c:if>

                <c:if test="${cart.remain eq 0}">
                    <td>
                            <%--                        재고가없습니다.--%>
                        <div class="quantity-control">
                            <input type="text" class="quantity-input" name="quantity_${cart.cartid}" value="재고가 없습니다." size="10px" readonly>
                        </div>
                    </td>
                </c:if>

                <td><input type="text" name="remain" readonly value="${cart.remain}" size="5px"></td>
                <td><input type="text" name="amount" id="amount_${cart.cartid}" value="${cart.price * cart.cartcount}" size="8px"></td>


                    <%--                remain이 0이면 선택 불가--%>
                <c:if test="${cart.remain eq 0}">
                    <td><input type="checkbox" name="selectedItems" value="${cart.cartid}" onclick="checkboxChanged()" disabled>
                            <%--                    삭제버튼 만들기--%>
                        <input type="button" value="삭제" onclick="deleteBtn(${cart.cartid})" class="delete-button">
                    </td>
                </c:if>
                <c:if test="${cart.remain ne 0}">
                    <td><input type="checkbox" name="selectedItems" id="cartId" value="${cart.cartid}" onclick="checkboxChanged()"></td>
                </c:if>
            </tr>

        </c:forEach>
        </tbody>
        <!-- total sum of items all price*cartcount-->
        <tfoot>
        <tr>
            <td colspan="5" class="changeCount2"></td>
            <td class="changedCount" hidden="hidden">수량 변경 확인</td>
            <td>총 금액 :</td>
            <td colspan="2"><input type="text" id="totalPrice" value="원" disabled></td>
        </tr>
        <tr>
            <td colspan="6"><input type="button" value="수량변경" onclick="changeCount()" class="delete-button">
                <input type="button" value="선택삭제" onclick="deleteSelected()" class="delete-button">
                <input type="button" value="주문하기" onclick="orderBtn()" class="order-button"></td>
        </tr>
        </tfoot>
    </table>
</form>
</body>
<script>
    //페이지 시작
    window.onload = function () {
        calculateTotalAmount();
    }

    function toggleSelectAll() {
        var selectAllCheckbox = document.getElementById("selectAllCheckbox");
        var checkboxes = document.getElementsByName("selectedItems");
        //disable가 있을경우 제외하고 선택
        for (var i = 0; i < checkboxes.length; i++) {
            if (checkboxes[i].disabled == false) {
                checkboxes[i].checked = selectAllCheckbox.checked;
            }
        }
        calculateTotalAmount();
        // Calculate and update the total amount
        checkboxChanged();
    }

    function setCount() {
        //수량은 재고값보다 클수없음
        var quantityInput = document.querySelectorAll(".quantity-input");
        var remain = document.querySelectorAll("input[name=remain]");
        for (var i = 0; i < quantityInput.length; i++) {
            if (quantityInput[i].value > remain[i].value) {
                alert("재고보다 많은 수량을 선택할 수 없습니다.");
                quantityInput[i].value = remain[i].value;
            }
        }
    }

    function calculateTotalAmount() {
        var checkboxes = document.getElementsByName('selectedItems');
        var totalAmountInput = document.getElementById('totalPrice');
        var totalAmount = 0;

        for (var i = 0; i < checkboxes.length; i++) {
            if (checkboxes[i].checked) {
                var cartId = checkboxes[i].value;
                var amountInput = document.getElementById('amount_' + cartId);
                var amount = parseFloat(amountInput.value);
                totalAmount += amount;
            }
        }

        totalAmountInput.value = totalAmount.toLocaleString('ko-KR') + " 원"; // Display the total amount
    }

    function checkboxChanged() {
        calculateTotalAmount(); // Update the total price whenever a checkbox is changed
        //disable박스 disable해제
        var checkboxes = document.getElementsByName("selectedItems");
        var quantityInput = document.querySelectorAll(".quantity-input");
        var plusBtn = document.querySelectorAll("input[value='+']");
        var minusBtn = document.querySelectorAll("input[value='-']");
        for (var i = 0; i < checkboxes.length; i++) {
            if (checkboxes[i].checked) {
                quantityInput[i].disabled = false;
                plusBtn[i].disabled = false;
                minusBtn[i].disabled = false;
            } else {
                quantityInput[i].disabled = true;
                plusBtn[i].disabled = true;
                minusBtn[i].disabled = true;
            }
        }
    }

    function setAmount() {
        //amount값은 price*cartcount totalprice값은 amount의 합
        var quantityInput = document.querySelectorAll(".quantity-input");
        var amount = document.querySelectorAll("input[name=amount]");
        var price = document.querySelectorAll("input[name=price]");
        for (var i = 0; i < quantityInput.length; i++) {
            amount[i].value = quantityInput[i].value * price[i].value;
        }
        calculateTotalAmount();
    }

    // plus minus 버튼 클릭시 수량 변경
    // function plus(input) {
    //     var quantityInput = input.parentNode.querySelector(".quantity-input");
    //     quantityInput.value = parseInt(quantityInput.value) + 1;
    //     calculateTotalAmount();
    //     //remain값 보다 클수없음
    //     var remain = input.parentNode.parentNode.parentNode.parentNode.querySelector("input[name='remain']").value;
    //     if (quantityInput.value > remain) {
    //         alert("재고가 부족합니다.");
    //         quantityInput.value = remain;
    //     }
    // }
    //plus버튼 클릭시 수량이 1씩 늘어나고 재고보다 클수없고 totalprice값이 변경되고 재고가 부족할경우 alert
    function plus(input) {
        var quantityInput = input.parentNode.querySelector(".quantity-input");
        var remain = input.parentNode.parentNode.parentNode.parentNode.querySelector("input[name='remain']").value;
        var value = parseInt(quantityInput.value) + 1;
        calculateTotalAmount();
        if (value > remain) {
            alert("재고가 부족합니다.");
            quantityInput.value = remain;
        } else {
            quantityInput.value = value;
        }
        //.changedCount hidden 없애기
        $(".changedCount").css("display", "block");
        $(".changeCount2").attr("colspan", "4");
    }

    function minus(input) {
        var quantityInput = input.parentNode.querySelector(".quantity-input");
        var value = parseInt(quantityInput.value) - 1;
        quantityInput.value = value >= 1 ? value : 1;
        calculateTotalAmount();
        $(".changedCount").css("display", "block");
        $(".changeCount2").attr("colspan", "4");
    }

    //클릭하면 checkbox를 선택하고 cartid와 cartcount를 ajax로 전송
    function ajaxChange() {
        var checkboxes = document.getElementsByName("selectedItems");
        var checkedCount = 0;
        for (var i = 0; i < checkboxes.length; i++) {
            if (checkboxes[i].checked) {
                checkedCount++;
                var cartId = checkboxes[i].value;
                var quantityInput = document.getElementsByName("quantity_"
                    + cartId)[0];
                var quantity = quantityInput.value;
                var data = {
                    cartid: cartId,
                    cartcount: quantity
                };
                $.ajax({
                    url: "updateCart.do",
                    type: "post",
                    data: data,
                    success: function (data) {
                        location.reload();
                        setAmount();
                    },
                    error: function (request, status, error) {
                        alert("code:" + request.status + "\n" + "message:"
                            + request.responseText + "\n" + "error:"
                            + error);
                    }
                });
            }
        }
        if (checkedCount === 0) {
            alert("선택된 상품이 없습니다.");
        }
    }

    // 수량변경 클릭시 ajax로 cartid, cartcount 전송
    function changeCount() {
        var checkboxes = document.getElementsByName("selectedItems");
        // 체크박스가 선택되지 않았으면 alert
        var checkedCount = 0;
        for (var i = 0; i < checkboxes.length; i++) {
            if (checkboxes[i].checked) {
                checkedCount++;
                var cartId = checkboxes[i].value;
                var quantityInput = document.getElementsByName("quantity_"
                    + cartId)[0];
                var quantity = quantityInput.value;
                var data = {
                    cartid: cartId,
                    cartcount: quantity
                };
                $.ajax({
                    url: "updateCart.do",
                    type: "post",
                    data: data,
                    success: function (data) {
                        alert("수량이 변경되었습니다.");
                        location.reload();
                    },
                    error: function (request, status, error) {
                        alert("code:" + request.status + "\n" + "message:"
                            + request.responseText + "\n" + "error:"
                            + error);
                    }
                });
            }
        }
        if (checkedCount === 0) {
            alert("선택된 상품이 없습니다.");
        }
    }

    //delete 버튼 클릭시 ajax로 cartid 전송
    function deleteSelected() {
        var checkboxes = document.getElementsByName("selectedItems");
        var checkedCount = 0;
        var deleteAlertShown = false;
        // 체크박스가 선택되지 않았으면 alert
        for (var i = 0; i < checkboxes.length; i++) {
            if (checkboxes[i].checked) {
                checkedCount++;
                var cartId = checkboxes[i].value;
                var data = {
                    cartid: cartId,
                };
                $.ajax({
                    url: "deleteCart.do",
                    type: "post",
                    data: data,
                    success: function (data) {
                        if (!deleteAlertShown) {
                            alert("삭제되었습니다.");
                            deleteAlertShown = true;
                        }
                        location.reload();
                    },
                    error: function (request, status, error) {
                        alert("code:" + request.status + "\n" + "message:"
                            + request.responseText + "\n" + "error:"
                            + error);
                    }
                });
            }
        }
        if (checkedCount === 0) {
            alert("선택된 상품이 없습니다.");
        }
    }

    //delteBtn 클릭시 ajax로 cartid 전송
    function deleteBtn(cartId) {
        var data = {
            cartid: cartId,
        };
        $.ajax({
            url: "deleteCart.do",
            type: "post",
            data: data,
            success: function (data) {
                alert("삭제되었습니다.");
                location.reload();
            },
            error: function (request, status, error) {
                alert("code:" + request.status + "\n" + "message:"
                    + request.responseText + "\n" + "error:" + error);
            }
        });
    }


    // 주문하기 버튼 클릭시 order.do페이지로 이동
    function orderBtn() {
        var checkboxes = document.getElementsByName("selectedItems");
        var checkedCount = 0;

        for (var i = 0; i < checkboxes.length; i++) {
            if (checkboxes[i].checked) {
                checkedCount++;
                var cartId = checkboxes[i].value;

            }
        }

        if (checkedCount === 0) {
            alert("선택된 상품이 없습니다.");
        } else {
            $("#form").submit();
        }
    }
</script>
</html>
