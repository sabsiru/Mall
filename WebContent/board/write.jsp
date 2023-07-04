<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>글쓰기</title>
</head>
<body>
	<form action="insert.do" method="post" id="form" enctype="multipart/form-data">
		<table>
			<tr>
				<td align="left">제목</td>
				<td align="left"><input type="text" name="title" placeholder="제목을 입력하세요." autofocus="autofocus"></td>
			</tr>
			<tr>
				<td align="left">이름</td>
				<td align="left"><input type="text" name="name"></td>
			</tr>
			<tr>
				<td align="left">비밀번호</td>
				<td align="left"><input type="password" name="pass"></td>
			</tr>
			<tr>
				<td align="left">내용</td>
				<td align="left"><textarea cols="40" rows="10" name="content" style="IME-MODE: active"></textarea></td>
			</tr>
			<tr>
				<td align="left">첨부파일</td>
				<td><input type="file" name="orgfile"></td>
			</tr>
			<tr>
				<td colspan="2" align="center"><input type="button" value="새글 등록" id="write"></td>
				<td colspan="2" align="center"><input type="button" value="취소" onclick="location.href='list.do'"></td>
			</tr>
		</table>
	</form>
</body>
<script type="text/javascript">
	$(document).ready(function() {
		let formCheck = function() {
			let form = document.getElementById("form");
			if (form.title.value.trim() == "") {
				alert("제목을 입력하세요");
				form.title.focus();
				return false;
			}
			if (form.title.value.length > 50) {
				alert("제목은 50자 이하로 작성");
				form.title.focus();
				return false;
			}
			if (form.name.value.trim() == "") {
				alert("이름을 입력하세요");
				form.name.focus();
				return false;
			}
			if (form.name.value.length > 10) {
				alert("이름은 10자 이하로 작성");
				form.name.focus();
				return false;
			}
			if (form.pass.value.trim() == "") {
				alert("비밀번호를 입력하세요");
				form.pass.focus();
				return false;
			}
			if (form.pass.value.length > 10) {
				alert("비밀번호는 10자 이하로 작성");
				form.pass.focus();
				return false;
			}
			if (form.content.value.trim() == "") {
				alert("내용을 입력하세요");
				form.content.focus();
				return false;
			}
			return true;
		};
		$("#write").on("click", function() {
			let form = $("#form");
			if (formCheck()) {
				alert("게시글이 등록되었습니다.")
				form.submit();
			}
			;
		});
	});
</script>
</html>