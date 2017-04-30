package comjungwon7769heartbeat.github.heartbeat;

/**
 * Created by AH on 2017-04-30.
 */
public class FriendDTO {
	private String friendID, friendNick, friendColor;
	private int friendMode;

	public String getID(){return friendID;}
	public void setID(String value){friendID = value;}

	public String getNick(){return friendNick;}
	public void setNick(String value){friendNick = value;}

	public int getMode(){return  friendMode;}
	public void setMode(int value){friendMode = value;}

	public String getColor(){return friendColor;}
	public void setColor(String value){friendColor = value;}
}
