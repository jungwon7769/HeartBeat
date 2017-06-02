<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Insert title here</title>
</head>
<body>
로그인해주세요-> 로그인 다음화면 가는 용
<form method="post" action="loginPro.jsp">
	아이디 : <input type="text" name="id"><br>
	비밀번호 : <input type="text" name="pwd"><br>
	<input type="submit" value="로그인">
</form>
<hr>
아이디 중복확인
<form method="post" action="controlFlag.jsp">
	아이디 : h(있는아이디)
	<input type="hidden" name="msg" value="11/h//">
	<input type="submit" value="중복확인">
</form>
<form method="post" action="controlFlag.jsp">
	아이디 : hasdf(없는아이디)
	<input type="hidden" name="msg" value="11/hasdf//">
	<input type="submit" value="중복확인">
</form>
<hr>
회원가입
<form method="post" action="controlFlag.jsp">
	[테스트용] 아이디 : hi / 비번 : hi / 닉네임 : hi<br>
	<input type="hidden" name="msg" value="12/hi/hi/hi//">
	<input type="submit" value="회원가입">
</form>
<hr>
로그인처리 flag확인용_ test
<form method="post" action="controlFlag.jsp">
	아이디 : hi / 비번 : hi<br>
	<input type="hidden" name="msg" value="10/hi/hi//">
	<input type="submit" value="회원정보 유무확인">
</form>
</body>
</html>