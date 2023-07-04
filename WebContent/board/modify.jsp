<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>수정 폼</title>
</head>
<body>
	<h2>${vo.title}게시물수정</h2>
	<form action="update.do" method="post" id="form" enctype="multipart/form-data">
		<input type="hidden" name="seq" value="${vo.seq}">
		<table>
			<tr>
				<td>제목 <input type="text" name="title" value="${vo.title}"></td>
			</tr>
			<tr>
				<td>내용</td>
			</tr>
			<tr>
				<td><textarea name="content">${vo.content}</textarea></td>
			</tr>
			<tr>
				<c:if test="${not empty vo.storefile}">
					<td>첨부 파일 :<input type="text" value="${vo.orgfile}" name="filename" readonly="readonly" />
					</td>
					<td>
					<td><input type="button" value="첨부 파일초기화" onclick="fileDelete()"></td>
				</c:if>
				</td>
			</tr>
			<tr>
				<td>파일 첨부하기 :<input type="file" value="${vo.orgfile}" name="orgfile" onclick="addFile()" readonly="readonly" />
				</td>
			</tr>
			<%-- <c:if test="${empty vo.orgfile }">
			<tr>
				<td><input type="file" name="orgfile"></td>
			</tr>
			</c:if> --%>
		</table>
		<tr>
			<td><input type="hidden" name="confirmdelete" value=""></td>
			<td><input type="submit" value="submit"></td>
			<td><input type="button" id="modifyBtn" value="수정하기 "></td>
			<td><input type="button" value="삭제" onclick="confirmDelete(${vo.seq})"></td>
			<td><input type="button" value="뒤로가기" onclick="location.href='read.do?seq=${vo.seq}'"></td>
		</tr>
	</form>
	<tr>
		<td><input type="button" onclick="location.href='list.do'" value="목록으로"></td>
	</tr>
	<script type="text/javascript">
	$(document).ready(function() {
		let formCheck = function() {
			let form = document.getElementById("form");
			if (form.title.value.trim() == "") {
				alert("제목을 입력하세요");
				form.title.focus();
				return false;
			}
			if(form.title.value.length>50){
				alert("제목은 50자 이하로 작성");
				form.title.focus();
				return false;
			}
			if (form.content.value.trim() == "") {
				alert("내용을 입력하세요");
				form.content.focus();
				return false;
			}
			return true;
		};
		$("#modifyBtn").on("click", function() {
			let form = $("#form");
			if (formCheck()) {
				alert("게시글이 수정되었습니다.")
				form.submit();
			}
			;
		});
	});
		
		//삭제버튼
		   function confirmDelete(seq) {
        var confirmed = confirm("삭제 하시겠습니까?");
        if (confirmed) {
            location.href = "delete.do?seq=" + seq;
            alert("삭제 되었습니다.");
        }
    }
		   function fileDelete() {
			   var confirmed = confirm("첨부파일을 삭제하시겠습니까?");
		        if (confirmed) {
		            $("input[name=storefile]").val("");
		            $("input[name=filename]").val("");
		            $("input[name=confirmdelete]").val("delete");
		        }
		    }
		   function addFile() {
		        var confirmed = confirm("첨부파일을 추가하시겠습니까?");
		        if (confirmed) {
		            $("input[name=confirmdelete]").val("");

		        }
		    }
	</script>
</body>
<style>
a {
	text-decoration: none;
	color: black;
}
</style>
</html>