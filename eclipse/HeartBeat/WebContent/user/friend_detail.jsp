<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%
	String userid = (String)request.getParameter("userid");
	String friendid = (String)request.getParameter("friendid");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Insert title here</title>
</head>
<body>
	<ul>
		<li><a href="controlFlag.jsp?msg=9/<%=userid%>/<%=friendid%>/555555//">진동색 지정(그냥 "555555"으로 지정하도록한다)</a></li>
		<li><a href="controlFlag.jsp?msg=1/<%=userid%>/<%=friendid%>/3//">기분전송(그냥 기분값 3으로 지정)</a></li>
		<li><a href="controlFlag.jsp?msg=2/<%=userid%>/<%=friendid%>//">진동전송</a></li>
		<li><a href="controlFlag.jsp?msg=0/<%=userid%>/<%=friendid%>//">음성메시지전송</a></li>
		<li><a href="">버튼진동대상 설정</a></li>
		<li><a href="controlFlag.jsp?msg=8/<%=userid%>/<%=friendid%>//">친구삭제</a></li>
	</ul>
</body>
</html>