package comjungwon7769heartbeat.github.heartbeat;

/**
 * Created by AH on 2017-05-02.
 */
public class Constants {
	public static final int maxString = 32;
	public static final int minString = 4;

	public static final int popup_ok = 0;   //popup = 0, Message = String

	public static enum Emotion{
		smile(0, "59BE7F"), laugh(1, "FFFF00"), sad(2, "FFBB00"), annoy(3, "FF9900"), angry(4, "FF0000"),
		wink(5, "8041D9"), love(6, "FF93B2"), wow(7, "7393D5"), overeat(8, "D9CA74"), sleep(9, "FFFFFF");

		private int mode;
		private String color;

		Emotion(int value, String colorValue){
			this.mode = value;
			this.color = colorValue;
		}
		public int getMode(){return mode;}
		//public void setMode(int value){ this.mode = value; this.color = Emotion.values()[value].getColor();}

		public String getColor(){return color;}
		//public void setColor(String colorValue){this.color = colorValue;}
	} //enum Emotion

}
