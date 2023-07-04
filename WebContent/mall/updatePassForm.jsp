<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<%@ include file="Header.jsp" %>
<html>
<head>
    <title>비밀번호 수정</title>
</head>
<body>
<!--pass 1, pass2 입력창-->
<form action="updatePass.do" method="POST" id="form">
    <table>
        <tr>
            <td>비밀번호</td>
            <td><input type="password" name="pass" id="pass"></td>
            <td><span id="passMessage" style="font-size: x-small"></span></td>
        </tr>
        <tr>
            <td>비밀번호 확인</td>
            <td><input type="password" name="pass2" id="pass2"></td>
            <td><span id="passMessage2" style="font-size: x-small">비밀번호 확인</span></td>
        </tr>
        <tr>
            <td colspan="2"><input type="button" value="비밀번호 변경" id="updatePassBtn"></td>
        </tr>
        <div class="checkbox" hidden="hidden">
            <input type="text" id="passCheck" name="passCheck" value="false">
            <input type="text" id="passValid" name="passValid" value="false">
        </div>
    </table>

</form>
</body>
<script>
    $(document).ready(function () {
        let form = document.querySelector('form');
        let formCheck = function () {
            if (form.pass.value == ""||form.pass2.value == "") {
                //passCheck false
                $("#passCheck").val("false");
                //passValid false
                $("#passValid").val("false");
                alert("비밀번호를 입력해주세요");
                return false;
            }
            return true;
        }
        //비밀번호 확인
        const password = document.querySelector('#pass');
        const password2 = document.querySelector('#pass2');

        var passMessage = document.querySelector('#passMessage');
        var passMessage2 = document.querySelector('#passMessage2');
        //password가 4~16자리 영문,숫자,특수문자가 아닐때
        password.addEventListener('keyup', function () {
            if (password.value.length < 4 || password.value.length > 12 || !/^[a-zA-Z0-9]+$/.test(password.value)) {
                passMessage.innerHTML = '4~12자리 영문,숫자만 가능합니다.';
                passMessage.style.color = 'red';
                $("#passValid").val("false");
            } else {
                passMessage.innerHTML = '';
                $("#passValid").val("true");
                passMessage.style.color = 'blue';
            }
        });
        password2.addEventListener('keyup', function () {
            if (password.value === password2.value) {
                passMessage2.innerHTML = '비밀번호가 일치합니다.';
                passMessage2.style.color = 'blue';
                $("#passCheck").val("true");
            } else {
                passMessage2.innerHTML = '비밀번호가 일치하지 않습니다.';
                passMessage2.style.color = 'red';
                $("#passCheck").val("false");
            }
        });
        //updatePassBtn 클릭시 formcheck가 true일때만 submit
        $("#updatePassBtn").click(function () {
            if(formCheck()){
                if($("#passCheck").val()=="true"&&$("#passValid").val()=="true"){
                    form.submit();
                }else{
                    alert("비밀번호를 확인해주세요");
                }
            }
        });
    });


</script>
</html>
