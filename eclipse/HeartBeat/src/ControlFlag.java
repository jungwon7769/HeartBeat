import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;

import dao.FriendDAO;
import dao.MFDAO;
import dao.MemberDAO;
import dao.MsgDAO;
import dto.FriendDTO;
import dto.MFDTO;
import dto.MemberDTO;
import dto.MsgDTO;

public class ControlFlag {
	// Socket client;
	InputStream in;
	FriendDAO friendDAO = new FriendDAO();
	MsgDAO msgDAO = new MsgDAO();
	MemberDAO memberDAO = new MemberDAO();
	MFDAO mfDAO = new MFDAO();
	MemberDTO memberDTO = null;
	String[] value;
	int flag;

	public ControlFlag(String msg, InputStream in) {
		// System.out.println(msg);
		value = msg.split("/");
		flag = Integer.parseInt(value[0]);
		this.in = in;
	}

	public String doMsg() {
		String res = "false";
		switch (flag) {
		case 0:
		case 1:
		case 2:// 0_음성전송(음성데이터는 따로 전송, data에 파일이름 들어감), 1_기분전송(data에 기분값들어감),
				// 2_진동전송
				// System.out.println("TEST!!!");
			if (friendDAO.checkFriend(value[1], value[2])) {
				// System.out.println("TEST!!!");
				MsgDTO msgDTO = new MsgDTO();
				msgDTO.setFlag(flag);
				msgDTO.setSender(value[1]);
				msgDTO.setReceiver(value[2]);
				// System.out.println("TEST!!!");
				if (flag == 0 || flag == 1) {
					msgDTO.setData(value[3]);
				} else {
					msgDTO.setData("0");
				}
				if (msgDAO.addMsg(msgDTO)) {
					res = "true";
				}
			}
			break;
		case 3:// 3_친구추가
			if (!friendDAO.checkFriend(value[1], value[2])) {
				FriendDTO friendDTO = new FriendDTO();
				friendDTO.setFlag(false);
				friendDTO.setUserId(value[1]);
				friendDTO.setFriendId(value[2]);
				if (friendDAO.addFriend(friendDTO) == false)
					break;
				MsgDTO msgDTO = new MsgDTO();
				msgDTO.setFlag(flag);
				msgDTO.setSender(value[1]);
				msgDTO.setReceiver(value[2]);
				msgDTO.setData("0");
				if (msgDAO.addMsg(msgDTO))
					res = "true";
			}
			break;
		case 4:// 닉네임설정
			if (memberDAO.updateNick(value[2], value[1]))
				res = "true";
			break;
		case 5:// 기분설정
			int mode = Integer.parseInt(value[2]);
			if (memberDAO.updateMode(mode, value[1]))
				res = "true";
			break;
		case 6:// 친구관계성공 (6/RECEIVER/SENDER//)
			if (friendDAO.deleteFriend(value[1], value[2])) {
				//System.out.println("test1!!!!");
				FriendDTO friendDTO = new FriendDTO();
				friendDTO.setFlag(true);
				friendDTO.setFriendId(value[1]);
				friendDTO.setUserId(value[2]);
				if (friendDAO.addFriend(friendDTO)) {
					//System.out.println("test2!!!");
					friendDTO.setFriendId(value[2]);
					friendDTO.setUserId(value[1]);
					if (friendDAO.addFriend(friendDTO))
						res = "true";
					//System.out.println("test3!!! : " + res);
				}
			}
			break;
		case 7:// 친구요청 거절
			if (!friendDAO.checkFriend(value[1], value[2])) {
				if (friendDAO.deleteFriend(value[1], value[2]))
					res = "true";
			}
			break;
		case 8:// 친구삭제
			if (friendDAO.deleteFriend(value[1], value[2]))
				res = "true";
			break;
		case 9:// 친구색 정보 저장
			if (friendDAO.friendColor(value[1], value[2], value[3]))
				res = "true";
			break;
		case 10:// 회원정보 유효검사
			// System.out.println("회원정보유효검사 시작 : "+value[1]+"/"+value[2]);
			memberDTO = memberDAO.memberExist(value[1], value[2]);
			// System.out.println("회원정보유효검사 종료");
			if (memberDTO != null) {
				res = memberDTO.getId() + "/" + memberDTO.getPwd() + "/" + memberDTO.getNick() + "/"
						+ memberDTO.getMmode() + "//";
			} // System.out.println("test~~"+res);
			break;
		case 11:// ID 유무검사
			if (memberDAO.idExist(value[1]))
				res = "true";
			break;
		case 12:// 회원정보 저장
			memberDTO = new MemberDTO();
			memberDTO.setId(value[1]);
			memberDTO.setPwd(value[2]);
			memberDTO.setNick(value[3]);
			if (memberDAO.join(memberDTO))
				res = "true";
			memberDTO = null;
			break;
		case 13:// 사용자 친구목록전달
			HashMap<String, MFDTO> friends = mfDAO.listFriend(value[1]);
			if (friends.size() != 0 && friends != null) {
				res = "";
				Iterator it = friends.keySet().iterator();
				while (it.hasNext()) {
					String fid = (String) it.next();
					MFDTO dto = friends.get(fid);
					res += dto.getFriendid() + "/" + dto.getColor() + "/" + dto.getNick() + "/" + dto.getMode() + "/";
				}
			}
			break;
		case 14:// 신호확인_ 메세지 수신요청
			MsgDTO msgDTO = msgDAO.getMsg(value[1]);
			if (msgDTO == null)
				res = "false";
			else if (msgDTO.getFlag() == 3 || friendDAO.checkFriend(msgDTO.getReceiver(), msgDTO.getSender())) {
				res = msgDTO.getFlag() + "/" + msgDTO.getSender() + "/" + msgDTO.getTime() + "/" + msgDTO.getData()
						+ "/";
				msgDAO.deleteMsg(msgDTO.getSender(), msgDTO.getTime());
			}
			break;
		}
		// System.out.println("test = "+res);
		return flag + "/" + res;
	}
}
