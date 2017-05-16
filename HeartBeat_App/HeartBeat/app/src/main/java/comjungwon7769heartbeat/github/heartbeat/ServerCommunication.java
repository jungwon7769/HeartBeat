package comjungwon7769heartbeat.github.heartbeat;

import java.io.File;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by AH on 2017-05-16.
 */
public class ServerCommunication {
	private final String sv_URL = "";
	private final int sv_PORT = 1200;

	private Socket sv_sock;
	private SocketAddress sv_addr;
	public  char[] buf;

	public boolean init(){
		return false;
	} //init()

	public boolean sockConnect(){
		return false;
	} //sockConnect()

	public boolean sandMsg(String ID, String PWD, String NICK, int Flag, File Sound, String Color, Constants.Emotion Mode){
		return false;
	} //sandMsg

	public ArrayList<FriendDTO> loadFriendList(){
		ArrayList<FriendDTO> fr_list = new ArrayList<>();
		return fr_list;
	} //loadFriendList()

	public Object recvMsg(int flag){
		return 0;
	} //recvMsg()

	public Object parsingMsg(String message){
		return 0;
	} //parsingMsg

	public boolean sockClose(){
		return false;
	} //sockClose()

}
