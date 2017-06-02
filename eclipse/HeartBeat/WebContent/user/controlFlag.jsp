<%@page import="dto.MemberDTO"%>
<%@page import="dto.FriendDTO"%>
<%@page import="dao.MemberDAO"%>
<%@page import="dao.MsgDAO"%>
<%@page import="dto.MsgDTO"%>
<%@page import="dao.FriendDAO"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
<%
	String msg = (String) request.getParameter("msg");
	String[] value = msg.split("/");
	int flag = Integer.parseInt(value[0]);
	FriendDAO friendDAO = new FriendDAO();
	MsgDAO msgDAO = new MsgDAO();
	MemberDAO memberDAO = new MemberDAO();
	boolean res = false;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Insert title here</title>
</head>
<body>
	<%
		switch (flag) {
		case 0:case 1:case 2://0_음성전송(음성데이터는 따로 전송), 1_기분전송(data에 기분값들어감), 2_진동전송
			if (friendDAO.checkFriend(value[1], value[2])) {
				MsgDTO msgDTO = new MsgDTO();
				msgDTO.setFlag(flag);
				msgDTO.setSender(value[1]);
				msgDTO.setReceiver(value[2]);
				if(flag==1)msgDTO.setData(Integer.parseInt(value[3]));
				res = msgDAO.addMsg(msgDTO);
			} 
			break;
		case 3://3_친구추가
			if(!friendDAO.checkFriend(value[1], value[2])){
				FriendDTO dto = new FriendDTO();
				dto.setFlag(false);
				dto.setUserId(value[1]);
				dto.setFriendId(value[2]);
				if(friendDAO.addFriend(dto)==false) break;
				MsgDTO msgDTO = new MsgDTO();
				msgDTO.setFlag(flag);
				msgDTO.setSender(value[1]);
				msgDTO.setReceiver(value[2]);
				if(flag==1)msgDTO.setData(Integer.parseInt(value[3]));
				res = msgDAO.addMsg(msgDTO);
			}
			break;
		case 4://닉네임설정
			res = memberDAO.updateNick(value[2], value[1]);
			break;
		case 5://기분설정
			int mode = Integer.parseInt(value[2]);
			res = memberDAO.updateMode(mode, value[1]);
			break;
		case 6://친구요청 수락
			break;
		case 7://친구요청 거절
			break;
		case 8://친구삭제
			res=friendDAO.deleteFriend(value[1], value[2]);
			break;
		case 9://친구색지정
			if (friendDAO.checkFriend(value[1], value[2])) {
				res = friendDAO.friendColor(value[1], value[2], value[3]);
			}
			break;
		case 10://회원정보 유효검사
			res = memberDAO.memberExist(value[1], value[2]);
			break;
		case 11://아이디 중복확인
			res = memberDAO.idExist(value[1]);
			break;
		case 12://회원가입
			MemberDTO dto = new MemberDTO();
			dto.setId(value[1]);
			dto.setPwd(value[2]);
			dto.setNick(value[3]);
			res = memberDAO.join(dto);
			break;
		case 13://사용자 친구목록전달
			break;
		case 14://메세지 수신요청
			
			break;
		}
	%>
	flag :
	<%=value[0]%>
	/ 성공여부 :
	<%=res%>
</body>
</html>