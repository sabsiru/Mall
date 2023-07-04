
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<html>
<head>
<title>아이디 찾기</title>
</head>
<body>
	<h1>아이디 찾기</h1>
	<form action="findId.do" method="post">
		<table>
			<tr>
				<td>이름</td>
				<td><input type="text" name="name" id="name"></td>
			</tr>
			<tr>
				<td>휴대폰번호</td>
				<td><input type="text" name="phone" id="phone"></td>
			</tr>
			<tr>
				<td colspan="2" align="center"><input type="button" value="아이디 찾기" id="findIdBtn"></td>
			</tr>
		</table>
	</form>
</body>
<script>
<!--    컨트롤러에서 보낸 msg 받기-->
	$("#findIdBtn").on('click', function() {
		var name = $("#name").val();
		var phone = $("#phone").val();
		$.ajax({
			url : "findId.do",
			type : "post",
			data : {
				name : name,
				phone : phone
			},
			success : function(result) {
				if (result === "") {
					
					return false;
				} else {
					//window 창 닫기
					window.close();
					window.opener.location.href = "login.do";
				}
			},
		});
	});
</script>
</html>
