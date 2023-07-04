<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<%@ include file="Header.jsp" %>
<html>
<style>
    body {
        text-align: center;
    }

    input {
        border: none;
    }

    .container {
        margin-top: 100px;
    }

    table {
        margin: 0 auto;
    }

    td {
        padding: 10px;
    }

    .btn {
        margin-top: 20px;
    }

    .btn input {
        padding: 10px;
        background-color: #2e6da4;
        color: white;
        border-radius: 5px;
    }

    .btn input:hover {
        background-color: #204d74;
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
    //세션확인 id 없으면 login.do로 이동
    var id = "${sessionScope.id}";
    if (id === "" || id === null || id === "null" || id === undefined) {
        location.href = "login.do";
    }
</script>
<head>
    <title>My page</title>
</head>
<body>
<div class="container">
    <form action="updateMember.do" method="post" id="updateForm">
        <table>

            <tr>
                <td>아이디</td>
                <td><input type="text" name="id" id="id" value="${sessionScope.id}" readonly></td>
            </tr>
            <tr>
                <td>이름</td>
                <td><input type="text" name="name" id="name" value="${sessionScope.name}" readonly></td>
            </tr>
            <tr>
                <td>전화번호</td>
                <td><input type="text" name="phone" id="phone" value="${sessionScope.phone}" readonly></td>
                <td><span id="phoneMessage" style="font-size: x-small"></span></td>
            </tr>
            <tr>
                <td>이메일</td>
                <td><input type="text" name="mail" id="mail" value="${sessionScope.mail}" readonly></td>
                <td><span id="mailMessage" style="font-size: x-small"></span></td>
            </tr>
            <tr>
                <td>우편번호</td>
                <td><input type="text" name="postcode" id="postcode" size="10px" readonly
                           value="${sessionScope.postcode}"></td>
            </tr>
            <tr>
                <td rowspan="3">주소</td>
                <td><input type="text" name="addr" id="addr" size="50px" readonly value="${sessionScope.addr}">
                </td>
            <tr>
                <td><input type="text" name="detailaddr" id="detailaddr" readonly value="${sessionScope.detailaddr}"></td>
            </tr>
            <tr>
                <td><input type="button" value="주소검색" id="searchAddr" onclick="daum_address()" hidden="hidden"></td>
            </tr>
            <tr>
                <td>가입일</td>
                <td><input type="text" value="${sessionScope.regdate}" readonly></td>
            </tr>
            <div class="check">

                <input type="hidden" name="phoneCheck" id="phoneCheck" value="true">

                <input type="hidden" name="phoneValid" id="phoneValid" value="true">

                <input type="hidden" name="mailValid" id="mailValid" value="true">

                <input type="hidden" name="passCheck" id="passCheck" value="true">
                <input type="hidden" name="detailAddrCheck" id="detailAddrCheck" value="true">


            </div>
            <!--            비밀번호 입력칸 hidden-->
            <tr id="pw" hidden="hidden">
                <td>비밀번호</td>
                <td><input type="password" name="pass" id="pass" value=""></td>
                <td><span id="passMessage" style="font-size: x-small"></span></td>
            </tr>
            <tr>
                <div class="btn">
                    <!--                    정보수정 버튼 -->
                    <input type="button" value="정보 수정" id="modifyBtn">
                    <input type="button" value="주문 내역" id="orderListBtn">
                    <input type="button" value="정보 수정" id="updateFormBtn" hidden="hidden">
                    <!--                    비밀번호 수정 페이지-->
                    <input type="button" value="비밀번호 수정" onclick="location.href='updatePassForm.do'" id="updatePass" hidden="hidden" disabled style="background-color: grey">
                    <input type="button" value="회원탈퇴" id="deleteMember" disabled style="background-color: grey" hidden="hidden" id="deleteMemberBtn">
                </div>
            </tr>
        </table>
    </form>
</div>
<script>
    <!--modify btn을 누르면  숨겨진 pw가 나옴-->
    $("#modifyBtn").click(function () {
        $("#pw").show();
        $("#modifyBtn").hide();
        //     phone,mail,address readonly 제거 detailaddr,주소검색 보이게 테두리 보이게
        $("input[name='phone']").removeAttr("readonly").css("border", "1px solid black");
        $("input[name='mail']").removeAttr("readonly").css("border", "1px solid black");
        $("input[name='addr']").css("border", "1px solid black");
        $("input[name='detailaddr']").removeAttr("readonly").css("border", "1px solid black");
        $("input[name='postcode']").removeAttr("readonly").css("border", "1px solid black");
        $("input[id='searchAddr']").removeAttr("hidden");
        $("input[id='updateFormBtn']").removeAttr("hidden");
        //탈퇴버튼 보이게
        $("input[type='button']").removeAttr("hidden");
        $("input[name='pass']").css("border", "1px solid black");
        //비밀번호 수정페이지 보이게
        //updatePass
        $("input[id='updatePass']").removeAttr("hidden");

        //비밀번호 메시지 '수정하시러면 비밀번호를 입력해주세요'
        $("#passMessage").text("수정하시려면 비밀번호를 입력해주세요");
    });
    // formCheck
    $(document).ready(function () {
        let form = document.querySelector("form");
        let formCheck = function () {
            //공백
            if ($("input[name='phone']").val().trim() == "" || $("input[name='mail']").val().trim() == "" || $("input[name='addr']").val().trim() == "" || $("input[name='postcode']").val().trim() == "" || $("input[name='pass']").val().trim() == ""|| $("input[name='detailaddr']").val().trim() == "") {
                alert("모든 칸을 입력하세요");
                //유효성 박스 false
                $("input[name='phoneCheck']").val("false");
                $("input[name='phone']").focus();
                return false;
            }
            //전화번호 유효성
            else if (form.phoneValid.value == "false") {
                alert("전화번호가 중복이거나 형식이 잘못되었습니다");
                $("input[name='phoneValid']").val("false");
                $("input[name='phone']").focus();
                return false;
            }
            //전화번호 중복
            else if (form.phoneCheck.value == "false") {
                alert("전화번호가 중복이거나 형식이 잘못되었습니다");
                $("input[name='phone']").focus();
                return false;
            }
            //이메일 유효성
            else if (form.mailValid.value == "false") {
                alert("이메일을 형식을 확인하세요");
                $("input[name='mail']").focus();
                return false;
            }
            //비밀번호 확인 유무
            else if (form.passCheck.value == "false") {
                alert("비밀번호를 확인하세요");
                $("input[name='pass']").focus();
                return false;
                //상세주소
            } else if (form.detailAddrCheck.value == "false") {
                alert("상세주소를 입력하세요");
                $("input[name='detailaddr']").focus();
                return false;
            } else {
                return true;
            }
        }
        //phone 중복체크 길이 11 숫자만
        //phoneCheck컨트롤러로 phone값을 보내고 result값을 받아옴 result가 1이면 false, 아니면 true ajax로
        var phoneMessage = document.querySelector('#phoneMessage');
        var phone = document.querySelector('#phone');
        $("#phone").on('input', function () {
            $.ajax({
                url: 'getPhone.do',
                type: 'post',
                data: {
                    phone: $('input[name=phone]').val()
                },
                success: function (result) {
                    if (result == "same") {
                        phoneMessage.innerHTML = '사용가능한 번호입니다(본인)';
                        $("#phoneMessage").css('color', 'green');
                        $("#phoneCheck").val("true");
                        $("#phoneValid").val("true");
                    } else if (phone.value == "") {
                        phoneMessage.innerHTML = '휴대폰 번호를 입력해주세요.';
                        $("#phoneMessage").css('color', 'red');
                        $("#phoneCheck").val("false");
                    } else if (phone.value.length != 11 || !/^[0-9]+$/.test(phone.value)) {
                        phoneMessage.innerHTML = '휴대폰 번호는 "-" 없이 숫자만 입력해주세요.';
                        $("#phoneMessage").css('color', 'red');
                        $("#phoneValid").val("false");
                        //"no"인 경우 중복
                    }else if(result == "no"){
                        phoneMessage.innerHTML = '이미 사용중인 휴대폰 번호입니다.';
                        $("#phoneMessage").css('color', 'red');
                        $("#phoneCheck").val("false");
                    } else {
                        phoneMessage.innerHTML = '사용가능한 휴대폰 번호입니다.';
                        $("#phoneMessage").css('color', 'blue');
                        $("#phoneCheck").val("true");
                        $("#phoneValid").val("true");
                    }
                }
                //======//
            });
        });
        const mail = document.querySelector('#mail');
        var mailMessage = document.querySelector('#mailMessage');
        mail.addEventListener('keyup', function () {
            //email check
            if (!/^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/.test(mail.value)) {
                mailMessage.innerHTML = '이메일 형식이 올바르지 않습니다.';
                mailMessage.style.color = 'red';
                $("#mailValid").val("false");
            } else {
                mailMessage.innerHTML = '';
                mailMessage.style.color = 'blue';
                $("#mailValid").val("true");
            }
        });
        //ajax로 비밀번호를 보내고 result가 ok이면 비밀번호 일치
        var passMessage = document.querySelector('#passMessage');
        var pass = document.querySelector('#pass');
        $("#pass").on('input', function () {
            $.ajax({
                url: 'getPass.do',
                type: 'post',
                data: {
                    pass: $('input[name=pass]').val()
                },
                success: function (result) {
                    if (result == "ok") {
                        passMessage.innerHTML = '비밀번호가 일치합니다.';
                        //updatePass disabled false 배경색 파란색으로
                        $("#updatePass").css('background-color', '#2e6da4');
                        $("#updatePass").attr("disabled", false);
                        //탙뢰버튼 disabled false 배경색 파란색으로
                        $("#deleteMember").css('background-color', '#2e6da4');
                        //탈퇴버튼 활성화
                        $("#deleteMember").attr("disabled", false);
                        $("#passMessage").css('color', 'blue');
                        $("#passCheck").val("true");
                    } else {
                        passMessage.innerHTML = '비밀번호가 일치하지 않습니다.';
                        //updatePass disabled true 배경색 회색으로
                        $("#updatePass").css('background-color', 'gray');
                        $("#updatePass").attr("disabled", true);
                        //탙뢰버튼 disabled true 배경색 회색으로
                        $("#deleteMember").css('background-color', 'gray');
                        //탈퇴버튼 비활성화
                        $("#deleteMember").attr("disabled", true);
                        $("#passMessage").css('color', 'red');
                        $("#passCheck").val("false");
                    }
                }
            });
        });
        //상세주소
        var detailAddrMessage = document.querySelector('#detailAddrMessage');
        var detailaddr = document.querySelector('#detailaddr');
        $("#detailaddr").on('input', function () {
            if (detailaddr.value == "") {

                $("#detailAddrCheck").val("false");
            } else {
                $("#detailAddrCheck").val("true");
            }
        });
        $("#updateFormBtn").on("click", function () {
            var addr = document.querySelector('#addr');
            var detailaddr = document.querySelector('#detailaddr');
            let form = document.querySelector('#updateForm');
            if (formCheck()) {
                if ($("#phoneCheck").val() == "true" && $("#mailValid").val() == "true" && $("#passCheck").val() == "true" && $("#phoneValid").val() == "true"&& $("#detailAddrCheck").val() == "true") {
                    form.submit();
                } else {
                    alert("입력정보를 확인하세요");
                }
            } else {
                return false;
            }
        });
    })
    //deleteMemberBtn클릭시 confirm
    $("#deleteMember").on("click", function () {
        if (confirm("정말 탈퇴하시겠습니까?")) {
            location.href = "deleteMemberForm.do";
        } else {
            return false;
        }
    });
    function daum_address() {
        new daum.Postcode({
            oncomplete: function (data) {
                var addr = ''; // 주소 변수
                var extraAddr = ''; // 참고항목 변수
                if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                    addr = data.roadAddress;
                } else { // 사용자가 지번 주소를 선택했을 경우(J)
                    addr = data.jibunAddress;
                }
                if (data.userSelectedType === 'R') {
                    if (data.bname !== '') {
                        extraAddr += data.bname;
                    }
                    if (data.buildingName !== '') {
                        extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                    }
                    addr += (extraAddr !== '' ? ' (' + extraAddr + ')' : '');
                }
                document.getElementById("addr").value = addr;
                document.getElementById("postcode").value = data.zonecode;
            }
        }).open();
    }
    //주문내역 버튼 클릭시 주문내역으로 이동
    $("#orderListBtn").on("click", function () {
        location.href = "orderList.do";
    });
</script>
</body>
</html>
