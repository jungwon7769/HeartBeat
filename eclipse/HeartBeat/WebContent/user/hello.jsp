<%@page import="java.util.Iterator"%>
<%@page import="dto.MFDTO"%>
<%@page import="java.util.HashMap"%>
<%@page import="dao.MFDAO"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<%
	String id = (String) request.getParameter("id");
	MFDAO mfDAO = new MFDAO();
	HashMap<String, MFDTO> friend = mfDAO.listFriend(id);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Insert title here</title>
</head>
<body>
	<h3> Welcome!! <%=id%></h3>
	!!본인상세!!
	<ul>
		<li><a href="">메시지수신확인</a></li>
		<li><a href="controlFlag.jsp?msg=5/<%=id%>/6//">기분설정(그냥 6값 으로 수정하기로)</a></li>
	</ul>
	<hr color="orange">
	<ul>
		<li><a href="">메시지수신요청</a></li>
		<li><a href="controlFlag.jsp?msg=4/<%=id%>/빨체반//">닉네임설정(그냥 빨체반으로 함)</a></li>
		<li><a href="controlFlag.jsp?msg=3/<%=id%>/j//">친구요청</a></li>
	</ul>
	<hr color="orange">
	<h2> 친구상세 : <%=friend.size()%>명 </h2>

	<ul>
		<%
			if (friend.size() != 0 && friend != null) {
				Iterator it = friend.keySet().iterator();
				while (it.hasNext()) {
					String fid = (String) it.next();
					MFDTO dto = friend.get(fid);
		%>
		<li><a href="friend_detail.jsp?userid=<%=id%>&friendid=<%=fid%>"><%=dto.getFriendid()%> / <%=dto.getNick()%> / <%=dto.getMode()%> / <%=dto.getColor()%></a></li>

		<% } } %>
	</ul>
	<hr color="orange">
</body>
</html>