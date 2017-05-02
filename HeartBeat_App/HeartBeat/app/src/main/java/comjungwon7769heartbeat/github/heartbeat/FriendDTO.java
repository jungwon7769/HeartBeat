package comjungwon7769heartbeat.github.heartbeat;

/**
 * Created by AH on 2017-04-30.
 */
public class FriendDTO {
	private String friendID, friendNick, friendColor;
	private Constants.Emotion friendMode;

	public FriendDTO(){}

	public FriendDTO(String id, String nick, String color, Constants.Emotion mode){
		this.friendID = id;
		this.friendNick = nick;
		this.friendColor = color;
		this.friendMode = mode;
	}

	//ID get set
	public String getID() {
		return friendID;
	}

	public void setID(String value) {
		friendID = value;
	}

	//Nick get set
	public String getNick() {
		return friendNick;
	}

	public void setNick(String value) {
		friendNick = value;
	}

	//Mode(Emotion) get set
	public int getModeInt() {
		return friendMode.getMode();
	}

	public Constants.Emotion getMode() {return  friendMode;}

	public void setMode(int value) {
		friendMode.setMode(value);
	}

	public void setMode(Constants.Emotion mode) { friendMode = mode;}

	//Color get set
	public String getColor() {
		return friendColor;
	}

	public void setColor(String value) {
		friendColor = value;
	}
}
