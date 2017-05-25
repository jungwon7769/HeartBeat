package comjungwon7769heartbeat.github.heartbeat;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

/**
 * Created by AH on 2017-05-16.
 */
public class ServerCommunication extends Thread{

	private final String sv_URL = "192.168.0.13";//내 아이피주소!
	private final int sv_PORT = 1200;


	private BufferedReader br = null;
	private BufferedOutputStream bw = null;

	private Socket sv_sock = null;
	public byte[] buf = new byte[1024];// 호빈수정 : 네트워크통신하려면 byte[]로 바꿔야함
	public String msg=null;// 호빈추가 : 서버로 보낼 메시지 정의
	public Object final_data = null; //서버처리해서 반환된 데이터야~~(boolean / HashMap<String, FriendDTO> 의 형태임)
	public boolean wait=true; //스레드 종료 알릴 플래그 용도

	@Override
	public void run(){
		this.init();
		this.processMsg();
		wait=false;
	}

	//호빈_ init메소드 삭제
	public void init(){
		try {
			sv_sock = new Socket(sv_URL, sv_PORT);
			br = new BufferedReader(new InputStreamReader(sv_sock.getInputStream(),"EUC-KR"));
			bw = new BufferedOutputStream(sv_sock.getOutputStream());
			//bw = new BufferedWriter(new OutputStreamWriter(out));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//** Constant mode...이부분만 이클립스로하느라 바꿈!! -> ctrl+F : mode 해서 다시 바꾸기
	// 호빈_ 메소드 추가(매개변수에 ME추가함)
	public void makeMsg(String ME, String ID, String PWD, String NICK, int Flag, File Sound, String Color, int mode) {
		msg = Flag + "/";
		switch (Flag) {
			case 0:
			case 2:
			case 3:
			case 6:
			case 7:
			case 8:// 음성전송, 진동전송, 친구추가, 친구요청 수락, 거절, 친구관계삭제
				msg += ME + "/" + ID + "//";
				break;
			case 1:// 기분전송
				msg += ME + "/" + ID + "/" + mode + "//";
				break;
			case 4:// 닉네임설정
				msg += ME + "/" + NICK + "//";
				break;
			case 5:// 기분설정
				msg += ME + "/" + mode + "//";
				break;
			case 9:// 친구색지정
				msg += ME + "/" + ID + "/" + Color + "//";
				break;
			case 10:// 회원정보 유효검사
				msg += ME + "/" + PWD + "//";
				break;
			case 11:
			case 13:
			case 14:// ID유무검사, 사용자친구목록전달, 신호수신
				msg += ME + "//";
				break;
			case 12:// 회원가입
				msg += ME + "/" + PWD + "/" + NICK + "//";
				break;
		}
	}


	//메소드 추가 _호빈
	public void processMsg() {
		try {
			// 송신
			//out.write(msg.getBytes("utf-8"));
			bw.write(msg.getBytes("EUC-KR"));
			bw.flush();
			// 수신
			//in.read(buf);
			buf = br.readLine().getBytes();
			parsingMsg(new String(buf).trim());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (sv_sock != null)
					sv_sock.close();
				if(bw!=null)
					bw.close();
				if(br!=null)
					br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void parsingMsg(String message) {
		String[] value = message.split("/");
		int Flag = Integer.parseInt(value[0]);

		// Flag 0~9,11,12
		if (Flag >= 0 && Flag <= 12 && Flag!=10) {
			if (value[1].equals("true")) final_data=(boolean)true;
			else if(value[1].equals("false")) final_data=(boolean)false;
		}
		//Flag 10
		if(Flag==10){
			if(value[1].equals("false")) {
				final_data=null;
			}else {
				MemberDTO dto = new MemberDTO();
				dto.setId(value[1]);
				dto.setPwd(value[2]);
				dto.setNick(value[3]);
				dto.setMmode(Integer.parseInt(value[4]));
				final_data=dto;
			}
		}
		//Flag 13
		else if (Flag == 13) {
			HashMap<String, FriendDTO> res = null;
			FriendDTO dto =null;
			if (value.length > 2) {
				res = new HashMap<>();
				for (int i = 1; i < value.length; i++) {
					switch (i % 4) {
						case 1:
							dto = new FriendDTO();
							dto.setID(value[i]);
							break;
						case 2:
							dto.setColor(value[i]);
							break;
						case 3:
							dto.setNick(value[i]);
							break;
						case 0:
							dto.setMode(Integer.parseInt(value[i]));
							res.put(dto.getID(),dto);
							break;
					}
				}
				final_data = res;
			}
		}
		//Flag 14
		else if(Flag==14){
			HashMap<Long, MsgDTO> res = new HashMap<>();
			MsgDTO dto = null;
			if(value.length>2){
				for(int i=1;i<value.length;i++){
					switch(i%4){
						case 1:
							dto = new MsgDTO();
							dto.setFlag(Integer.parseInt(value[i]));
							break;
						case 2:
							dto.setSender(value[i]);
							break;
						case 3:
							dto.setTime(Long.parseLong(value[i]));
							break;
						case 0:
							dto.setMode(Integer.parseInt(value[i]));
							res.put(dto.getTime(), dto);
							break;
					}
				}
				final_data = res;
			}
		}
	} // parsingMsg

}
