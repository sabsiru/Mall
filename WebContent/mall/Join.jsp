<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="Header.jsp" %>

<!DOCTYPE html>
<html>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<style>
    /*body width*/
    body {
        width: 100%;
    }

    /*container안에 중앙으로 정렬*/
    .container {
        width: 650px;
        margin: 0 auto;
    }

    /*버튼 트렌디한 디자인으로 회색*/
    input[type=submit] {
        background-color: #639aee;
        color: white;
        padding: 12px 20px;
        border: none;
        border-radius: 4px;
        cursor: pointer;
    }

    /*a 버튼처럼 만들기*/
    a {
        background-color: #639aee;
        color: white;
        padding: 12px 20px;
        border: none;
        border-radius: 4px;
        cursor: pointer;
        text-decoration: none;
    }
</style>
<head>

    <meta charset="UTF-8">
    <title>회원가입</title>
</head>
<body>

<h1 align="center">회원가입</h1>


<div class="container">
    <form action="join.do" method="post" id="form">
        <!--table id, pass,name,mail,phone,addr-->
        <table>
            <tr>
                <td>아이디</td>
                <td><input type="text" name="id" id="id" required></td>
                <td colspan="3"><span id="idMessage" style="font-size: x-small">중복 확인</span></td>
            </tr>
            <tr>
                <td>비밀번호</td>
                <td><input type="password" name="pass" id="password" required></td>
                <td><span id="passMessage" style="font-size: x-small"></span></td>
            </tr>
            <tr>
                <td>비밀번호 확인</td>
                <td><input type="password" name="password2" id="password2" required></td>
                <td><span id="passMessage2" style="font-size: x-small">비밀번호 확인</span></td>
            </tr>
            <tr>
                <td>이름</td>
                <td><input type="text" name="name" id="name" required></td>
                <td><span id="nameMessage" style="font-size: x-small"></span></td>
            </tr>
            <tr>
                <td>이메일</td>
                <td><input type="email" name="mail" id="mail" required></td>
                <td><span id="mailMessage" style="font-size: x-small"></span></td>

            </tr>
            <tr>
                <td>전화번호</td>
                <td><input type="tel" name="phone" id="phone" required placeholder="-를 제외하고 입력해주세요."></td>
                <td><span id="phoneMessage" style="font-size: x-small">전화번호 중복확인</span></td>
            </tr>
            <tr>
                <td>우편번호</td>
                <td><input type="text" name="postcode" id="postcode" size="10px" readonly required></td>
            </tr>
            <tr>
                <td rowspan="3">주소</td>
                <td><input type="text" name="addr" id="addr" size="40px" readonly required></td>
            </tr>
            <tr>
                <td><input type="text" name="detailaddr" id="detailaddr" placeholder="상세주소" required></td>
            </tr>

            <tr>
                <td><input type="button" value="주소검색" onclick="daum_address()"></td>
            </tr>
            <div class="checkbox" hidden="hidden">
                <!--                id중복 체크 확인-->
                <input type="text" name="idCheck" id="idCheck" value="false"><label for="idCheck">id 중복체크</label>
                <!--                pass,pass2 일치 체크-->
                <input type="text" name="passCheck" id="passCheck" value="false"><label for="passCheck">비밀번호 일치체크</label>
                <!--pass 유효성-->
                <input type="text" name="passValid" id="passValid" value="false"><label for="passValid">비밀번호 유효성</label>
                <!--                phone 중복체크 확인-->
                <input type="text" name="phoneCheck" id="phoneCheck" value="false"><label for="phoneCheck">phone 중복체크</label>
                <!--                이름 유효성 체크-->
                <input type="text" name="nameValid" id="nameValid" value="false"><label for="nameValid">이름 유효성</label>
                <!--                이메일 유효성 체크-->
                <input type="text" name="mailValid" id="mailValid" value="false"><label for="mailValid">이메일 유효성</label>
                <!--                id 유효성-->
                <input type="text" name="idValid" id="idValid" value="false"><label for="idValid">id 유효성</label>
                <!--                상세주소 입력-->
                <input type="text" name="detailAddrCheck" id="detailAddrCheck" value="false"><label for="detailAddrCheck">상세주소 입력 확인</label>
            </div>
            <tr>
                <td><input type="button" id="join" value="회원가입"></td>
                <td></td>
            </tr>
        </table>
    </form>
</div>
</body>
<script>
    //form check id중복체크,phone중복체크, id 4~12자,pass 4~12자, pass,pass2 일치체크 모든 입력값 공백 체크 true 반환
    $(document).ready(function () {
        let form = document.querySelector('form');
        let formCheck = function () {
            //공백
            if (form.id.value === "" || form.pass.value === "" || form.password2.value === "" || form.name.value === "" || form.mail.value === "" || form.phone.value === "" || form.postcode.value === "" || form.addr.value === "" || form.detailaddr.value === "") {
                $("#idCheck").val("false");
                $("#passCheck").val("false");
                $("#phoneCheck").val("false");
                $("#passValid").val("false");
                $("#nameValid").val("false");
                $("#mailValid").val("false");
                $("#detailAddrCheck").val("false");
                alert("모든 항목을 입력해주세요.");
                return false;
                //id 중복
            } else if (form.idCheck.value === "false") {
                $("#idCheck").val("false");
                alert("중복된 아이디이거나 잘못된 형식입니다. 다른 아이디를 입력해주세요.");
                return false;
            }
            //phone 중복 체크
            else if (form.phoneCheck.value === "false") {
                $("#phoneCheck").val("false");
                alert("중복된 휴대폰 번호 입니다. 다른 번호를 입력해주세요.");
                return false;
                //phone 유효성
            } else if (form.passCheck.value === "false") {
                $("#passCheck").val("false");
                alert("비밀번호가 일치하지 않습니다.")
                return false;
            } else if (form.idValid.value === "false") {
                $("#idValid").val("false");
                alert("아이디는 4~12자리 영어,숫자만 가능합니다.");
                return false;
            } else if (form.passValid.value === "false") {
                $("#passValid").val("false");
                alert("비밀번호는 4~12자리 영어,숫자만 가능합니다.");
                return false;
            } else if (form.nameValid.value === "false") {
                $("#nameValid").val("false");
                alert("이름은 한글만 가능합니다.");
                return false;
            } else if (form.mailValid.value === "false") {
                $("#mailValid").val("false");
                alert("이메일 형식이 잘못되었습니다.");
                return false;
            } else if (form.detailAddrCheck.value === "false") {
                $("#detailAddrCheck").val("false");
                alert("상세주소를 입력해주세요.");
                return false;
            } else {
                return true;
            }
        };
        //아이디 중복확인
        //idCheck컨트롤러로 id값을 보내고 result값을 받아옴 result가 1이면 false, 아니면 true ajax로
        var idMessage = document.querySelector('#idMessage');
        var id = document.querySelector('#id');
        $("#id").on('input', function () {
            $.ajax({
                url: 'idCheck.do',
                type: 'post',
                data: {
                    id: $('input[name=id]').val()
                },
                success: function (result) {
                    if (result == "used") {
                        idMessage.innerHTML = '중복된 아이디입니다.';
                        $("#idMessage").css('color', 'red');
                        $("#idCheck").val("false");
                        //4~12자리 영어,숫자
                    } else if (id.value.length < 4 || id.value.length > 12 || !id.value.match(/^[a-zA-Z0-9]*$/)) {
                        idMessage.innerHTML = '4~12자리 영어,숫자만 가능합니다.';
                        $("#idMessage").css('color', 'red');
                        $("#idValid").val("false");
                    } else {
                        idMessage.innerHTML = '사용가능한 아이디입니다.';
                        $("#idMessage").css('color', 'blue');
                        $("#idValid").val("true")
                        $("#idCheck").val("true");
                    }
                }
                //======//
            });
        });
        //phone 중복체크 길이 11 숫자만
        //phoneCheck컨트롤러로 phone값을 보내고 result값을 받아옴 result가 1이면 false, 아니면 true ajax로
        var phoneMessage = document.querySelector('#phoneMessage');
        var phone = document.querySelector('#phone');
        $("#phone").on('input', function () {
            $.ajax({
                url: 'phoneCheck.do',
                type: 'post',
                data: {
                    phone: $('input[name=phone]').val()
                },
                success: function (result) {
                    if (result == "used") {
                        phoneMessage.innerHTML = '중복된 휴대폰 번호입니다.';
                        $("#phoneMessage").css('color', 'red');
                        $("#phoneCheck").val("false");
                    } else if (phone.value.length != 11 || !/^[0-9]+$/.test(phone.value)) {
                        phoneMessage.innerHTML = '휴대폰 번호는 "-" 없이 숫자만 입력해주세요.';
                        $("#phoneMessage").css('color', 'red');
                        $("#phoneCheck").val("false");
                    } else {
                        phoneMessage.innerHTML = '사용가능한 휴대폰 번호입니다.';
                        $("#phoneMessage").css('color', 'blue');
                        $("#phoneCheck").val("true");
                    }
                }
                //======//
            });
        });
        //비밀번호 확인
        const password = document.querySelector('#password');
        const password2 = document.querySelector('#password2');

        var passMessage = document.querySelector('#passMessage');
        var passMessage2 = document.querySelector('#passMessage2');
        //password가 4~16자리 영문,숫자,특수문자가 아닐때
        password.addEventListener('keyup', function () {
            if (password.value.length < 4 || password.value.length > 12 || !/^[a-zA-Z0-9]+$/.test(password.value)) {
                passMessage.innerHTML = '4~12자리 영문,숫자만 가능합니다.';
                passMessage.style.color = 'red';
                $("#passValid").val("false");

            }else if(password.value!=password2.value){
                passMessage.innerHTML = '일치하지 않습니다.';
                passMessage.style.color = 'red';
                $("#passCheck").val("false");
            }
            else {
                passMessage.innerHTML = '';
                $("#passValid").val("true");
                passMessage.style.color = 'blue';
            }
        });
        password2.addEventListener('keyup', function () {
            if (password.value === password2.value) {
                passMessage2.innerHTML = '비밀번호가 일치합니다.';
                passMessage2.style.color = 'blue';
                passMessage.style.color = 'blue';
                passMessage.innerHTML = '';
                $("#passCheck").val("true");
            } else {
                passMessage2.innerHTML = '비밀번호가 일치하지 않습니다.';
                passMessage2.style.color = 'red';
                $("#passCheck").val("false");
            }
        });
        //이름은 한글만2~5자리
        const name = document.querySelector('#name');
        var nameMessage = document.querySelector('#nameMessage');
        name.addEventListener('keyup', function () {
            if (name.value.length < 2 || name.value.length > 5 || !/^[가-힣]+$/.test(name.value)) {
                nameMessage.innerHTML = '2~5자리 한글만 가능합니다.';
                nameMessage.style.color = 'red';
                $("#nameValid").val("false");
            } else {
                nameMessage.innerHTML = '';
                nameMessage.style.color = 'blue';
                $("#nameValid").val("true");
            }
        });

        const mail = document.querySelector('#mail');
        var mailMessage = document.querySelector('#mailMessage');
        mail.addEventListener('keyup', function () {
            ///^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/;
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
        //detailAddr
        const detailaddr = document.querySelector('#detailaddr');
        var detailAddrMessage = document.querySelector('#detailAddrMessage');
        detailaddr.addEventListener('keyup', function () {
            if (detailaddr.value.length < 2 || detailaddr.value.length > 50) {
                $("#detailAddrCheck").val("false");
            } else {
                $("#detailAddrCheck").val("true");
            }
        });
        //join버튼을 눌렀을때 formCheck가 트루,check 들이 true일경우 회원가입 아닐경우 return false
        $("#join").click(function () {
            var addr = document.querySelector('#addr');
            var detailaddr = document.querySelector('#detailaddr');
            if (formCheck()) {
                if ($("#idCheck").val() == "true" && $("#phoneCheck").val() == "true" && $("#passCheck").val() == "true" && $("#passValid").val() == "true" && $("#nameValid").val() == "true" && $("#mailValid").val() == "true" && $("#detailAddrCheck").val() == "true") {
                    alert("회원가입이 완료되었습니다.");
                    form.submit();
                } else {
                    alert("입력값을 확인해주세요.");
                }
            } else {
                return false;
            }
        });
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
</script>
</html>
