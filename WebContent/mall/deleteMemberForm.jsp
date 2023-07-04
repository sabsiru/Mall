<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
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
<head>
    <title>회원탈퇴</title>
</head>
<body>
<!--form tag를 이용한 회원탈퇴-->
<form action="deleteMember.do" method="post">
    <table>
        <tr>
            <td>비밀번호</td>
            <td><input type="password" name="pass" id="pass"></td>
          <td><span id="passMessage" style="font-size: x-small"></span></td>
        </tr>
        <tr>
            <td colspan="2" align="center">
                <input type="submit" value="회원탈퇴">
            </td>
        </tr>
    </table>
</form>
</body>
<script !src="">
  //submit 버튼을 눌렀을 때 비밀번호 값이 비어있으면 경고창을 띄우고 submit을 막는다.
    $("form").submit(function () {
        if ($("#pass").val() == "") {
        alert("비밀번호를 입력해주세요.");
        return false;
        }
    });
</script>

</html>
