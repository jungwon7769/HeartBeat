<%@page import="java.net.URLEncoder"%>
<%@page import="dao.MemberDAO"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%
	String id = (String)request.getParameter("id");
	String pwd = (String)request.getParameter("pwd");
	MemberDAO dao = new MemberDAO();
	boolean flag = dao.memberExist(id,pwd);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Insert title here</title>
</head>
<body>
<%if(flag){
	response.sendRedirect("hello.jsp?id="+id);
}else {
	response.sendRedirect("loginForm.jsp");
}%>
</body>
</html>