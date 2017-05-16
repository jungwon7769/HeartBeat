package dto;

public class FriendDTO {
	private String userId;
	private String friendId;
	private boolean flag;
	private String color="000000";
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getFriendId() {
		return friendId;
	}
	public void setFriendId(String friendId) {
		this.friendId = friendId;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		color = color;
	}
	public boolean getFlag() {
		return this.flag;
	}
	
	
}
