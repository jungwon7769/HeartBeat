<%@page import="java.util.Iterator"%>
<%@page import="dto.MFDTO"%>
<%@page import="java.util.HashMap"%>
<%@page import="dao.MFDAO"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<% 
	MFDAO dao = new MFDAO();
	HashMap<String,MFDTO> list = new HashMap<>();
	list = dao.listFriend("h");
	HashMap<String,MFDTO> test = dao.test("h");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Insert title here</title>
</head>
<body>
<%=test.size()%>
<ul>
		<%
			if (test.size() != 0 && test != null) {
				Iterator it = test.keySet().iterator();
				while (it.hasNext()) {
					String fid = (String) it.next();
					MFDTO dto = test.get(fid);
		%>
		<li><a href="friend_detail.jsp?userid=h&friendid=<%=fid%>"><%=dto.getFriendid()%> / <%=dto.getNick()%> / <%=dto.getMode()%> / <%=dto.getColor()%></a></li>

		<% } } %>
	</ul>
</body>
</html>