<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="Header.jsp"%>
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
<head>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f7f7f7;
            color: #333;
        }

        .container {
            border: 1px solid #eaeaea;
            padding: 20px;
            font-family: 'Noto Sans', sans-serif;
            width: 80%;
            margin: 0 auto;
        }

        .productinfo {
            width: 100%;
            border-collapse: collapse;
        }

        .product th {
            background-color: #639aee;
            color: #fff;
            padding: 10px;
            text-align: center;
        }

        .product td {
            border: 1px solid #eaeaea;
            padding: 10px;
            text-align: center;
        }

        .product .ship {
            background-color: #f7f7f7;
            padding: 5px;
        }

        .product img {
            max-width: 100px;
            height: auto;
            display: block;
            margin: 0 auto;
        }

        .product input[type="text"] {
            border: 1px solid #eaeaea;
            padding: 5px;
            width: 100%;
            box-sizing: border-box;
            pointer-events: none;
        }

        .product input[type="text"]:focus {
            outline: none;
            border-color: #639aee;
        }

        /* Style for ship and totalPay inputs */
        .payinfo input[name="ship"],
        .payinfo input[name="totalPay"],
        .payinfo input[name="totalcount"] {
            border: none;
            background-color: transparent;
            text-align: center;
            font-size: larger;
            font-weight: bold;
            pointer-events: none;
        }

        .payinfo {
            width: 25%;
            border-collapse: collapse;
        }

        .payinfo th {
            background-color: #639aee;
            color: #fff;
            padding: 10px;
            text-align: center;
        }

        .payinfo td {
            border: 1px solid #eaeaea;
            padding: 10px;
            text-align: center;
        }

        .payinfo .ship {
            background-color: #f7f7f7;
            padding: 5px;
        }

        .addrinfo,
        .paymethod,
        .payinfo,
        .btn {
            margin-bottom: 20px;
        }

        .addrinfo table,
        .paymethod table,
        .payinfo table {
            width: 50%;
        }

        /*postcode 크기 30px*/
        .addrinfo input[name="postcode"] {
            width: 70px;
        }

        .addrinfo h3,
        .paymethod h3 {
            margin-bottom: 10px;
        }

        .addrinfo .oldaddr,
        .addrinfo {
            margin-top: 10px;
        }

        .newaddr td.address {
            white-space: nowrap;
        }

        .addrinfo .oldaddr tr,
        .addrinfo .newaddr tr {
            margin-bottom: 10px;
        }

        .addrinfo .newaddr input[type="text"],
        .addrinfo .newaddr label {
            width: 100%;
        }

        .paymethod input[type="radio"] {
            margin-right: 10px;
        }

        .payinfo th {
            text-align: left;
        }

        .payinfo td {
            padding-top: 10px;
        }
        .payinfo{
            display: flex;
        }

        .btn {
            display: flex;
            justify-content: center;
        }

        /*    oldaddr 안에 있는 인풋 리드온리 테두리 배경화면 없애기*/
        .addrinfo .oldaddr input[type="text"] {
            border: none;
            background-color: transparent;
            pointer-events: none;
        }

    </style>
    <title>주문</title>
</head>
<body>
<div class="hidden">
    <input type="text" name="id" value="${sessionScope.id}" hidden="hidden">
</div>

<div class="container">
    <h2>주문하기</h2>
    <form action="insertOrder.do" method="post" id="form">
        <div class="product">
            <table class="productinfo">
                <tr>
                    <th colspan="3">상품정보</th>
                    <th>수량</th>
                    <th>남은 재고</th>
                    <th>상품금액</th>
                    <th>합계</th>
                </tr>
                <!--                cart에 값이 있으면-->
                <c:if test="${not empty cartList}">
                    <c:forEach var="cart" items="${cartList}" varStatus="status">
                        <tr id="row_${status.index}">
                            <input type="text" name="cartid" value="${cart.cartid}" hidden="hidden">
                            <input type="text" name="pno" value="${cart.pno}" hidden="hidden">
                            <td><input type="text" name="pimage" hidden="hidden"><img
                                    src="/spring/upload/${cart.pimage}" alt="no image" width="50px" height="50px"
                                    name="pimage"></td>
                            <td><input type="text" name="pname" value="${cart.pname}"></td>
                            <td><input type="text" name="optionname" value="${cart.optionname}"></td>
                            <td><input type="text" name="cartcount" value="${cart.cartcount}"></td>
                            <td><input type="text" name="remain" value="${cart.remain}"></td>
                            <td><input type="text" name="price" value="${cart.price}"></td>
                            <td><input type="text" name="totalPrice" value=""></td>
                                <%--                        optionname optionvalue1 optionvalue2로 나눠서 담을 인풋--%>
                            <input type="text" name="optionvalue1" value="" hidden="hidden">
                            <input type="text" name="optionvalue2" value="" hidden="hidden">
                        </tr>
                    </c:forEach>
                </c:if>
            </table>
        </div>
        <div class="addrinfo">
            <h3>배송지 정보</h3>
            <!--           배송지 선택 radio 1개만 선택가능-->
            <td><input type="radio" name="addrselect" value="old" checked="checked">기존 배송지</td>
            <td><input type="radio" name="addr2" value="new">새로운 배송지</td>
            <div class="oldaddr">
                <table>
                    <tr>
                        <td><input type="text" name="name" id="name" value="${sessionScope.name}"></td>
                    </tr>
                    <tr>
                        <td><input type="text" name="phone" id="phone" value="${sessionScope.phone}"></td>
                    </tr>
                    <tr>
                        <td><input type="text" name="postcode" id="postcode" value="${sessionScope.postcode}"></td>
                    <tr>
                        <td><input type="text" name="addr" id="addr" size="50px"
                                   value="${sessionScope.addr} ${sessionScope.detailaddr}"></td>
                    </tr>
                    <tr>
                        <td>요청사항</td>
                    </tr>
                    <tr>
                        <td>
                            <select name="selRequest">
                                <option value="직접입력">직접입력</option>
                                <option value="부재시 경비실에 맡겨주세요">부재시 경비실에 맡겨주세요</option>
                                <option value="부재시 문앞에 놓아주세요">부재시 문앞에 놓아주세요</option>
                                <option value="부재시 전화주세요">부재시 전화주세요</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td><input type="text" name="request" value="" size="25px"></td>
                    </tr>
                </table>
            </div>
            <!--   <div class="newaddr" style="display: none;">
                  <tr>
                      <td>수령인</td>
                      <td colspan="2"><input type="text" name="receiver" value=""></td>
                  </tr>
                  <tr>
                      <td>전화번호</td>
                      <td colspan="2"><input type="text" name="phone" value=""></td>
                  </tr>
                  <tr>
                      <td rowspan="2">주소</td>
                      <td><input type="text" name="postcode" value="" width="25%"></td>
                      <td>
                          <button>우편번호</button>
                      </td>

                  </tr> -->
            <!--             <tr>
                            <td colspan="2"><input type="text" name="address" value=""></td>
                            <td><input type="text" name="detailaddr" value=""></td>
                            <input type="text" name="addr" value="" hidden="hidden">
                        </tr> -->
            <tr>
                <td>요청사항</td>
                <td><input type="text"></td>
            </tr>
        </div>
        <hr>
        <div class="paymethod">
            <!--            결제 수단 선택-->
            <h3>결제 수단</h3>
            <tr>
                <td><input type="radio" name="paymethod" value="card" checked="checked">신용카드</td>
            </tr>
            <tr>
                <td><input type="radio" name="paymethod" value="bank">계좌 이체</td>
            </tr>
            <!--                    카드사 선택 -->
            <div class="paymentDiv" id="cardPayment">
                <table>
                    <tr>
                        <td>
                            <select name="card">
                                <option value="shinhan">신한</option>
                                <option value="kb">국민</option>
                                <option value="lotte">롯데</option>
                                <option value="samsung">삼성</option>
                                <option value="hyundai">현대</option>
                            </select>
                        </td>
                        <td>
                            <!--                            카드번호 4자리씩 4개-->
                            <input type="text" name="cardnum1" value="" maxlength="4" size="4" required >-
                            <input type="text" name="cardnum2" value="" maxlength="4" size="4" required>-
                            <input type="text" name="cardnum3" value="" maxlength="4" size="4" required>-
                            <input type="text" name="cardnum4" value="" maxlength="4" size="4" required>
                        </td>
                    </tr>
                </table>
            </div>
            <div id="bankTransfer" class="paymentDiv" style="display: none">
                <table>
                    <tr>
                        <td><select name="bank">
                            <option value="shinhan">신한</option>
                            <option value="kb">국민</option>
                            <option value="lotte">롯데</option>
                            <option value="samsung">삼성</option>
                            <option value="hyundai">현대</option>
                        </select></td>
                        <!--                        계좌번호 최대 14자리-->
                        <td><input type="text" name="banknum" value="" minlength="10" maxlength="14" size="30"
                                   placeholder="10~14자리 계좌번호 입력"></td>
                    </tr>
                </table>
            </div>
        </div>
        <hr>
        <div class="payinfo">
            <table>
                <tr>
                    <th colspan="3">주문 정보</th>
                </tr>
                <tr>
                    <td>총 주문건 : <input type="text" name="totalcount" value="" width="10%"></td>
                    <td>배송비 : <input type="text" name="ship" value="" width="25%"></td>
                    <td>총 결제 금액 :<input type="text" name="totalPay" value="" width="25%"></td>
                </tr>
            </table>
        </div>
        <div class="btn">
                      <input type="button" value="결제하기" id="orderBtn">

        </div>
    </form>
</div>
</body>
<script>
    <!--    페이지 로딩시 totalprice의 값이 50000원보다 높으면 ship의 value는 무료 아니면 2,500하고 totalprice에 2500원 추가-->
    $(document).ready(function () {
        var totalPay = 0;
        var count=0;
        $('tr[id^="row_"]').each(function () {
            var row = $(this);
            var cartcount = row.find('input[name^="cartcount"]').val();
            var price = row.find('input[name^="price"]').val();
            var totalPriceInput = row.find('input[name^="totalPrice"]');
            var totalprice = parseInt(cartcount) * parseInt(price);

            count++;
            totalPay += totalprice;

            totalPriceInput.val(totalprice.toLocaleString('ko-KR') + "원");
            price = parseInt(price);
            row.find('input[name^="price"]').val(price.toLocaleString('ko-KR') + "원");
        });
        if (totalPay >= 50000) {
            $('input[name^="ship"]').val("무료");
        } else {
            $('input[name^="ship"]').val("2,500원");
            totalPay += 2500;
        }
        var totalinput=$('input[name^="totalcount"]');
        totalinput.val(count+" 건");
        var totalPayInput = $('input[name^="totalPay"]');
        totalPayInput.val(totalPay.toLocaleString('ko-KR') + "원");
    });
    //     배송지 선택

    //    radio button 선택 유무에 따라 결제수단 보이기
    const radioButtons = document.querySelectorAll('input[name="paymethod"]');
    const paymentDivs = document.querySelectorAll('.paymentDiv');

    // Add change event listener to the radio buttons
    radioButtons.forEach(radioButton => {
        radioButton.addEventListener('change', function () {
            // Get the value of the selected radio button
            const selectedValue = this.value;

            // Hide all payment divs
            paymentDivs.forEach(div => {
                div.style.display = 'none';
            });

            // Show the corresponding payment div based on the selected value
            if (selectedValue === 'card') {
                document.getElementById('cardPayment').style.display = 'block';
            } else if (selectedValue === 'bank') {
                document.getElementById('bankTransfer').style.display = 'block';
            }
        });
    });
    //요청사항
    var selRequest = document.getElementsByName('selRequest');
    var request = document.getElementsByName('request');
    //select 인 selRequest선택시 request에 value값 넣기
    //직접입력은 값 없게
    selRequest[0].addEventListener('change', function () {
        if (selRequest[0].value == "직접입력") {
            request[0].value = "";
            request[0].disabled = false;
        } else {
            request[0].value = selRequest[0].value;
            request[0].disabled = true;
        }
    });

    //버튼 누르면 결제 확인창 띄우기
    var orderBtn = document.getElementById('orderBtn');
    var form= document.getElementById('form');
    //card 선택시 cardnum1,2,3,4이 공백이면 경고창 띄우기
    //bank 선택시 banknum이 공백이면 경고창 띄우기
    //결제하기 버튼 누르면 결제 확인창 띄우기
    $("#orderBtn").click(function () {
        var paymethod = document.getElementsByName('paymethod');
        var cardnum1 = document.getElementsByName('cardnum1');
        var cardnum2 = document.getElementsByName('cardnum2');
        var cardnum3 = document.getElementsByName('cardnum3');
        var cardnum4 = document.getElementsByName('cardnum4');
        var banknum = document.getElementsByName('banknum');
        var cardnum = cardnum1[0].value + cardnum2[0].value + cardnum3[0].value + cardnum4[0].value;
        if (paymethod[0].checked) {
            if (cardnum1[0].value == "" || cardnum2[0].value == "" || cardnum3[0].value == "" || cardnum4[0].value == "") {
                alert("카드번호를 입력해주세요");
                return false;
            }
        } else if (paymethod[1].checked) {
            if (banknum[0].value == "") {
                alert("계좌번호를 입력해주세요");
                return false;
            }
        }
        var result = confirm("결제하시겠습니까?");
        
        if (result) {
            form.submit();
        }
    });


</script>
</html>
