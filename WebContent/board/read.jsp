<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.oreilly.servlet.MultipartRequest"%>
<%@ page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy"%>
<%@ page import="java.util.*"%>
<!DOCTYPE html>
<html>
<head>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<meta charset="utf-8">
<title>${vo.title}</title>
</head>
<body>
	<h2>제목 : ${vo.title}</h2>
	<div>
		<form action="read.do" method="post">
			<table>
				<tr>
					<td><div>내용</div></td>
				<tr>
					<td><textarea name="content" readonly="readonly">${vo.content}</textarea></td>
				</tr>
				<tr>
				<c:if test="${empty vo.storefile}">
                    <td>첨부 파일 : 첨부파일이 없습니다.</td>
                </c:if>
					<c:if test="${not empty vo.storefile}">
						<td>첨부 파일 :<input type="text" value="${vo.orgfile}" name="orgfile" readonly="readonly" />
						</td>
						<td><input type="hidden" value="${vo.storefile}" name="storefile"></td>
						<td><input type="button" value="파일 다운로드" onclick="location.href='download.do?orgfile=${vo.orgfile}&storefile=${vo.storefile}'"></td>
					</c:if>
				</tr>
			</table>
		</form>
	</div>
	<form>
		<div>
			<input type="button" id="modifyBtn" value="수정" disabled="disabled" onclick="location.href='modify.do?seq=${vo.seq}'">
			<input type="button" value="삭제" id="deleteBtn" disabled="disabled" onclick="confirmDelete(${vo.seq})">
			<input type="hidden" value="ajax삭제" id="ajaxDeleteBtn">
			<input type="hidden" value="ajax수정" id="ajaxModifyBtn">
			<input type="button" value="답글 " onclick="location.href='reply.do?seq=${vo.seq}'">
			<input type="button" value="목록" onclick="location.href='list.do'">
			<input type="hidden" name="ref" value="${vo.ref}">
			<input type="hidden" name="step" value="${vo.step}">
			<input type="hidden" name="depth" value="${vo.depth}">
		</div>
	</form>
	<div>
		<div id="messageDiv">비밀번호를 입력하세요.</div>
		<input type="hidden" value="편집" id="editBtn">
		<input type="hidden" name="seq" value="${vo.seq}">
		<input type="password" name="pass" id="passInput" placeholder="비밀번호를 입력하세요">
	</div>
</body>
<script>
	//비밀번호 입력하면 버튼이 바로 살아남.
	$("#passInput").on('input', function() {
		$.ajax({
			url : "passCheck.do",
			type : "post",
			data : {
				seq : $("input[name=seq]").val(),
				pass : $("input[name=pass]").val()
			},
			success : function(data) {
				var messageDiv = $("#messageDiv");
				if (data == "true") {
					messageDiv.text("비밀번호가 맞습니다.");
					changeTextColor(messageDiv, "green");
					$("#deleteBtn").removeAttr("disabled");
					$("#modifyBtn").removeAttr("disabled");
				} else if (data == "empty") {
					$("#deleteBtn").attr("disabled", true);
					$("#modifyBtn").attr("disabled", true);
					changeTextColor(messageDiv, "black");
					messageDiv.text("비밀번호를 입력하세요.");
				} else {
					$("#deleteBtn").attr("disabled", true);
					$("#modifyBtn").attr("disabled", true);
					messageDiv.text("비밀번호가 틀렸습니다.");
					changeTextColor(messageDiv, "red");
				}
			}
		});
	});
	function changeTextColor(element, color) {
		element.css("color", color);
	}
	
	//삭제버튼
		   function confirmDelete(seq) {
        var confirmed = confirm("삭제 하시겠습니까?");
        if (confirmed) {
            location.href = "delete.do?seq=" + seq;
            alert("삭제 되었습니다.");
        }
    }
	
	//안씀
	$("#editBtn").click(function() {
		$.ajax({
			url : "passCheck.do",
			type : "post",
			data : {
				seq : $("input[name=seq]").val(),
				pass : $("input[name=pass]").val()
			},
			success : function(data) {
				if (data == "true") {
					alert("비밀번호가 맞습니다.")
					$("#deleteBtn").removeAttr("disabled");
					$("#modifyBtn").removeAttr("disabled");
				} else if (data == "empty") {
					alert("비밀번호를 입력하세요.")

				} else {

					alert("비밀번호가 틀렸습니다.");
				}
			}
		});
	});

	//ajaxdeleteBtn을 누르면 ajax로 seq데이터를 보내고 삭제에 성공하면 alert를 띄우고 list.do로 이동
	$("#ajaxDeleteBtn").click(function() {
		if (confirm("삭제하시겠습니까? 비밀번호가 맞으면 삭제됩니다. ")) {
			$.ajax({
				url : "deletePass.do",
				type : "post",
				data : {
					seq : $("input[name=seq]").val(),
					pass : $("input[name=pass]").val()
				},
				success : function(data) {
					if (data == "true") {
						alert("삭제되었습니다.")
						location.href = "list.do";
					} else if (data == "empty") {
						alert("비밀번호를 입력하세요.")
					} else {
						alert("비밀번호가 틀렸습니다.");
					}
				}
			});
		}
	});

	$("#ajaxModifyBtn").click(function() {
		if (confirm("수정화면으로 이동하시겠습니까? 비밀번호가 맞으면 이동합니다. ")) {
			$.ajax({
				url : "modifyPass.do",
				type : "post",
				data : {
					seq : $("input[name=seq]").val(),
					pass : $("input[name=pass]").val()
				},
				success : function(data) {
					if (data == "true") {
						alert("비밀번호 확인.")
						location.href = "modify.do?seq=${vo.seq}";
					} else if (data == "empty") {
						alert("비밀번호를 입력하세요.")
					} else {
						alert("비밀번호가 틀렸습니다.");
					}
				}
			});
		}
	});
</script>
<style>
a {
	text-decoration: none;
	color: black;
}
</style>
</html>