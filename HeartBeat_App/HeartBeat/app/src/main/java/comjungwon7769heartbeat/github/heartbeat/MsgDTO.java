package comjungwon7769heartbeat.github.heartbeat;

/**
 * Created by AH on 2017-05-13.
 */
public class MsgDTO {

	private String Sender;
	private int Flag, Time, Count;
	private Constants.Emotion Mode;
	private String File_Path;

	public MsgDTO(){}

	public MsgDTO(int flag, String sender, int time, Constants.Emotion e, String filePath){
		this.Flag = flag;
		this.Sender = sender;
		this.Time = time;
		this.Count = 1;
		this.Mode = e;
		this.File_Path = filePath;
	}

	public String getSender(){return this.Sender;}
	public int getFlag(){return this.Flag;}
	public int getTime(){return this.Time;}
	public int getCount(){return this.Count;}
	public Constants.Emotion getMode(){return this.Mode;}
	public int getModeInt(){return this.Mode.getMode();}
	public String getSoundPath(){return this.File_Path;}

	public void setSender(String sender){this.Sender = sender;}
	public void setFlag(int flag){this.Flag = flag;}
	public void setTime(int time){this.Time = time;}
	public void setCount(int count){this.Count = count;}
	public void setMode(Constants.Emotion e){this.Mode = e;}
	public void setMode(int e){this.Mode = Constants.Emotion.values()[e];}
	public void setSoundPath(String path){this.File_Path = path;}
}
