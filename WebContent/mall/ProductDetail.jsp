<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="Header.jsp" %>
<!DOCTYPE html>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script>
    /*submit 버튼 눌렀을떄 로그인 실패시 알람 띄우기*/
    /*컨트롤러에서 보낸 session.msg 받기*/
    var msg = "${sessionScope.msg}";
    if (msg !== "" && msg !== null && msg !== "null" && msg !== undefined) {
        alert(msg);
    }
    <% session.removeAttribute("msg"); %>
</script>
<html>
<head>
    <meta charset="UTF-8">
    <title>Product Detail</title>
    <style>
        body {
            background-color: #f1f1f1;
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
        }

        .container {
            max-width: 1000px;
            margin: 0 auto;
            background-color: #fff;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }


        h1 {
            color: #3498db;
        }

        .product-image {
            float: left;
            margin-right: 20px;
        }

        .product-details {
            overflow: hidden;
        }

        .product-details h2 {
            margin: 0;
            color: #3498db;
        }

        .product-details p {
            margin: 5px 0;
        }

        .product-options {
            margin-top: 20px;
        }


        .buttons {
            text-align: right;
        }

        .product-options label {
            display: block;
            margin-bottom: 5px;
            color: #555;
        }


        .product-description {
            clear: both;
            margin-top: 20px;
            /*    컨텐츠 중앙*/
            text-align: center;
        }

        .product-description p {
            line-height: 1.5;
        }

        .purchase-button {
            margin-top: 20px;
        }

        .purchase-button button {
            padding: 10px 20px;
            background-color: #3498db;
            color: #fff;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
        }

        .shopping-cart-button button {
            padding: 10px 20px;
            background-color: #3498db;
            color: #fff;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
            margin-left: 10px;
        }


        /*div.selected-option {*/
        /*    display: flex;*/
        /*    flex-direction: column;*/
        /*    align-items: center;*/
        /*}*/

        .plus-minus,
        .price-text {
            display: flex;
            flex-direction: row;
            justify-content: left;
            align-items: center;
            margin-bottom: 10px;
        }

        .plus-minus button,
        .price-text p {
            margin: 0 5px;
        }

    </style>
</head>
<body>
<div class="input" hidden="hidden">
    <form action="insertCart.do" method="post" id="form">
        <input type="text" id="pno" name="pno" value="${prVO.pno}">
        <input type="text" id="price" name="price" value="${prVO.price}">
        <input type="text" id="pname" name="pname" value="${prVO.pname}">
        <input type="text" id="pimage" name="pimage" value="${prVO.pimage}">
        <input type="text" id="delinfo" name="delinfo" value="${prVO.delinfo}">
        <input type="text" id="optionname" name="optionname" value="">
        <input type="text" id="cartcount" name="cartcount" value="">
        <input type="text" name="remain" id="remain" value="">
    </form>
</div>
<div class="container">
    <h1>Product Name: ${prVO.pname}</h1>
    <div class="product-details">
        <div class="product-image">
            <img src="/spring/upload/${prVO.pimage}" alt="Product Image" width="300">
        </div>
        <p>Product Number: ${prVO.pno}</p>
        <p>Price: ${prVO.price}</p>
        <!--            옵션 select로-->
        <!--                            옵션이 있을경우 보여줌-->
        <c:if test="${optionList != null}">
        <div class="product-options">
            <tr>
                <td>
                    <label for="potions">옵션
                        <select name="potions" id="potions">
                            <option value="0">옵션을 선택하세요</option>
                            <c:forEach var="option" items="${optionList}">
                                <%--                                remain값이 0이면 안보이게 그값은 선택 불가하게--%>
                                <c:if test="${option.remain == 0&&option.option2!=null}">
                                    <option value="${option.optionvalue1}-${option.optionvalue2}-${option.remain}" disabled>${option.optionvalue1}-${option.optionvalue2},
                                        재고 : ${option.remain}</option>
                                </c:if>
                                <%--                                remain값이 0이고 option2가 널일경우--%>
                                <c:if test="${option.remain == 0 && option.option2 == null}">
                                    <option value="${option.optionvalue1}-${option.remain}" disabled>${option.optionvalue1} - 재고 : ${option.remain}</option>
                                </c:if>
                                <c:if test="${option.remain != 0&& option.option2==null}">
                                    <option value="${option.optionvalue1}-${option.remain}">${option.optionvalue1} - 재고 : ${option.remain}</option>
                                </c:if>
                                <%--                                remain이 0이 아니고 option2가 널이 아닐경우--%>
                                <c:if test="${option.remain != 0 && option.option2 != null}">
                                    <option value="${option.optionvalue1}-${option.optionvalue2}-${option.remain}">${option.optionvalue1}-${option.optionvalue2},
                                        재고 : ${option.remain}</option>
                                </c:if>
                            </c:forEach>
                        </select>
                    </label>
                </td>
            </tr>
            </c:if>
            <c:if test="${optionList == null}">
                <div class="product-options">
                    <tr>
                        <td>
                            <label for="potions">옵션
                                <select name="potions" id="potions2">
                                    <option value="0">옵션 없음</option>
                                </select>
                            </label>
                        </td>
                    </tr>
                </div>
            </c:if>
        </div>

        <div class="selected-option" hidden="hidden">
            <div class="plus-minus">
                <p>옵션 : <input type="text" readonly id="selectedOption" value="" size="10px"></input></p>
                <p>재고 : <input type="text" readonly id="selectedOptionRemain" value="" size="10px"></input></p>
            </div>
            <div class="price-text">
                수량 : <input type="text" id="count" readonly value="1" size="10px"></input>
                <button type="button" id="minus">-</button>
                <button type="button" id="plus">+</button>
                <button type="button" id="delete">x</button>
                <p>가격 : <span id="selectedOptionPrice"></span></p>
            </div>
            <div class="totalPrice">
                <!--                총가격-->
                <p>총 가격 : <span id="totalPrice"></span></p>
            </div>
        </div>


        <div class="buttons">
            <div class="purchase-button" style="display: inline-block;">
                <button type="button" id="buyBtn">Buy</button>
            </div>
            <div class="shopping-cart-button" style="display: inline-block;">
                <button type="button" id="CartBtn">Cart</button>
            </div>
        </div>

        <div class="product-description">
            <h2>제품 상세 설명</h2>
            <br>
            <p>${prVO.pcontent}</p>
            <img src="/spring/upload/${prVO.detailimage}" alt="Product Image" width="300">
        </div>
    </div>
</div>
</body>
<script>
    <!--    옵션을 선택하면 옵션명, 가격, 수량 , 총 가격을 보여줄 div를 만듬-->
    $(document).ready(function () {
        $("#potions").change(function () {
            var option = $("#potions option:selected").val();
            var optionArr = (option || '').split("-");
            var option1 = optionArr[0];
            if (optionArr.length < 3) {
                var option2 = "";
                var remainoption = optionArr[1];
            } else {
                var option2 = optionArr[1];
                var remainoption = optionArr[2];
            }
            var price = ${prVO.price};
            var count = $("#count").val();
            var total = price * count;
            //컨트롤러로 보낼값
            var optionname = $("#optionname");
            var cartcount = $("#cartcount");
            var remain = $("#remain");

            //-------------------------//
            var selectedOption = $("#selectedOption");
            var selectedOptionRemain = $("#selectedOptionRemain");

            optionname.val(option1 + "-" + option2);
            remain.val(remainoption);
            //ordercount값 넣기
            cartcount.val(count);
            if (optionArr.length < 3) {
                selectedOption.val(option1);
            } else {
                selectedOption.val(option1 + "-" + option2);
            }
            selectedOptionRemain.val(remainoption);
            //재고가 0이면 옵션선택창에서 선택불가하게 변경
            if (remainoption == 0) {
                $("#potions option:selected").attr("disabled", "disabled");
            }
            $("#selectedOptionPrice").text(price);
            $("#ordercount").text(count);
            $("#totalPrice").text(total);
            $(".selected-option").removeAttr("hidden");
            //remain값이 0이면 옵션선택창에서 삭제


            $("#plus").click(function () {
                count++;
                var remain = $("#remain").val();
                // count가 remain보다 클수없음
                if (count > remain) {
                    alert("재고가 부족합니다.");
                    count = remain;
                }
                $("#count").val(count);
                $("#ordercount").val(count);
                total = price * count;
                $("#totalPrice").text(total);
            });
            $("#minus").click(function () {
                count--;
                if (count < 1) {
                    count = 1;
                }
                $("#count").val(count);
                $("#ordercount").val(count);
                var total = price * count;
                $("#totalPrice").text(total);
            });
            if ($("#potions").val() == 0) {
                $(".selected-option").attr("hidden", "hidden");
            }
            //삭제버튼 누르면 옵션 text들초기화 및 옵션 선택창 초기화
            $("#delete").click(function () {
                $("#selectedOption").text("");
                $("#selectedOptionName").text("");
                $("#selectedOptionPrice").text("");
                $("#ordercount").text("");
                $("#totalPrice").text("");
                $("#count").val(1);
                $("#potions").val(0);
                $(".selected-option").attr("hidden", "hidden");
            });
        });
    });


    //장바구니 버튼 누르면 form태그로 값 전송
    $("#CartBtn").click(function () {
        function checkOption() {
            var optionname = $("#optionname").val();
            var remain = $("#remain").val();
            var result = false;
            $.ajax({
                url: "/spring/mall/CheckOption.do",
                type: "post",
                data: {
                    optionname: optionname,
                    remain: remain
                },
                success: function (data) {
                    if (data != "no") {
                        alert("장바구니에 이미 존재하는 상품입니다. 수량을 확인해주세요.");
                        result = true;
                    } else {
                        result = false;
                    }
                }
            });
            return result;
        }

        if (checkOption(false)) {
            alert("장바구니에 이미 존재하는 상품입니다. 수량을 확인해주세요.");
            return false;
        }
        //옵션값이 없거나 세션 아이디가 없으면 false
        else if ($("#potions").val() == 0 || $("#selectedOption").val() == "") {
            alert("옵션을 선택하세요");
            return false;
        } else if ("${sessionScope.id}" == "") {
            alert("로그인 후 이용해주세요");
            location.href = "/spring/mall/login.do";
            return false;
        } else {
            var optionname = $("#optionname");
            var cartcount = $("#cartcount");
            var count = $("#count").val();
            optionname.val($("#selectedOption").val());
            cartcount.val(count);
            alert("장바구니에 담겼습니다.");
            $("#form").submit();

        }
    });
    //구매하기 버튼 누르면 directorder.do 상품정보를 가지고 이동
    $("#buyBtn").click(function () {
        //옵션이 선택되지 않으면 false
        if ($("#potions").val() == 0) {
            alert("옵션을 선택하세요");
            return false;
        }
        var pno = $("#pno").val();
        var price = $("#price").val();
        var pname = $("#pname").val();
        var pimage = $("#pimage").val();
        var optionname = $("#selectedOption").val();
        var remain = $("#selectedOptionRemain").val();
        var count = $("#count").val();

        // Create a hidden form dynamically
        var form = $('<form action="directOrder.do" method="post"></form>');
        form.append('<input type="hidden" name="pno" value="' + pno + '">');
        form.append('<input type="hidden" name="price" value="' + price + '">');
        form.append('<input type="hidden" name="pname" value="' + pname + '">');
        form.append('<input type="hidden" name="pimage" value="' + pimage + '">');
        form.append('<input type="hidden" name="optionname" value="' + optionname + '">');
        form.append('<input type="hidden" name="remain" value="' + remain + '">');
        form.append('<input type="hidden" name="count" value="' + count + '">');
        $('body').append(form);

        // Submit the form
        form.submit();
    });

</script>
</html>
