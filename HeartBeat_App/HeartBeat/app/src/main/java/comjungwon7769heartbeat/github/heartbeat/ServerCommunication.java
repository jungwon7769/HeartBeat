package comjungwon7769heartbeat.github.heartbeat;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by AH on 2017-05-16.
 */
public class ServerCommunication {
	private final String sv_URL = "210.107.230.193";//호빈수정 : 임의로 지금 내 컴퓨터 IP주소 넣어둘게!!(집가면 바뀔수있음)
	private final int sv_PORT = 1200;

	//호빈추가
	private InputStream in = null;
	private OutputStream out = null;
	//여기까지

	private Socket sv_sock=null;
	//private SocketAddress sv_addr;//필요없을듯?? (호빈)
	public  byte[] buf;//호빈수정 : 네트워크통신하려면 byte[]로 바꿔야함
	public String msg;//호빈추가 : 서버로 보낼 메시지 정의

	public boolean init(){
		//호빈추가 : 여기부터
		try {
			sv_sock = new Socket(sv_URL, sv_PORT);
			if(sv_sock!=null)return true;
		} catch(IOException e) {
			e.printStackTrace();
		}
		return false;
		//여기까지
	} //init()

	//sockConnect()//(호빈수정)_ 이거 안쓸거같아서 주석처리해놓을게
	/*public boolean sockConnect(){
		return false;
	}*/

	//호빈_ 메소드 수정(매개변수에 ME추가함)
	public boolean sandMsg(String ME, String ID, String PWD, String NICK, int Flag, File Sound, String Color, Constants.Emotion Mode){
		//호빈추가 : 여기부터
		msg=Flag+"/";
		switch(Flag) {
			case 0: case 2: case 3:case 6: case 7:case 8://음성전송, 진동전송, 친구추가, 친구요청 수락, 거절, 친구관계삭제
				msg+=ME+"/"+ID+"//";
				break;
			case 1://기분전송
				msg+=ME+"/"+ID+"/"+Mode.getMode()+"//";
				break;
			case 4://닉네임설정
				msg+=ME+"/"+NICK+"//";
				break;
			case 5://기분설정
				msg+=ME+"/"+Mode.getMode()+"//";
				break;
			case 9://친구색지정
				msg+=ME+"/"+ID+"/"+Color+"//";
				break;
			case 10://회원정보 유효검사
				msg+=ME+"/"+PWD+"//";
				break;
			case 11:case 13:case 14://ID유무검사, 사용자친구목록전달, 신호수신
				msg+=ME+"//";
				break;
			case 12://회원가입
				msg+=ME+"/"+PWD+"/"+NICK+"//";
				break;
		}//buf에 보낼 메세지 Flag값에 맞게 저장!!
		try {
			out = sv_sock.getOutputStream();
			out.write(msg.getBytes());
			in = sv_sock.getInputStream();
			in.read(buf);
			Log.i("test", new String(buf));//test용
			//parsingMsg(new String(buf));
		} catch(IOException e){
			e.printStackTrace();
		}
		//여기까지
		return false;
	} //sandMsg

	public ArrayList<FriendDTO> loadFriendList(){
		ArrayList<FriendDTO> fr_list = new ArrayList<>();
		return fr_list;
	} //loadFriendList()

	/*public Object recvMsg(int flag){
		return 0;
	}*/ //recvMsg()안쓸듯?_호빈

	//호빈_ 구현한 메소드
	public Object parsingMsg(String message){
		String [] value=message.split("/");
		int flag = Integer.parseInt(value[0]);
		switch(flag){
			case 0:
				break;
			case 1:
				break;
			case 2:
				break;
			case 3:
				break;
			case 4:
				break;
			case 5:
				break;
			case 6:
				break;
			case 7:
				break;
			case 8:
				break;
			case 9:
				break;
			case 10:
				break;
			case 11:
				break;
			case 12:
				break;
			case 13:
				break;
			case 14:
				break;
		}
	return 0;
	} //parsingMsg

	public boolean sockClose(){
		return false;
	} //sockClose()

}
