package comjungwon7769heartbeat.github.heartbeat;

/**
 * Created by AH on 2017-05-02.
 */
public class Constants {
	public static final int maxString = 32;
	public static final int minString = 4;

	public static final int friendLoad_Interval = 300000;    //millsec -> 5min


	/*
	PopupActivity 의 Type 정의
	 */
	//PopupActivity Type에 따른, Intent를 이용해 (팝업으로 보낼 Data), (팝업에서 받아올 Data)
	public static final int popup_ok = 0;   //Popup = 0, To PopupActivity(Message = String), From PopupActivity()
	public static final int popup_re = 1;   //Popup = 1, To(Message = String), From(select = boolean)

	public static final int popup_pickEmotion = 10;  //Popup = 10, To (), From (selectedEmotion = int)
	public static final int popup_pickColor = 11;    //Popup = 11, To (), From (selectedColor = String)
	public static final int popup_recordVoice = 12;     //Popup = 12, To(), From(voicePath = String)

	public static final int popup_msgFriend = 20;   //Popup = 20, To(ID = String, Time = int), From(select = boolean)
	public static final int popup_msgVoice = 21;    //Popup = 21, To(ID = String, Nick = String, Time = int..??), From()     야ㅒ도 오디오관련 좀더 공부하겠음..
	public static final int popup_msgEmotion = 22;  //Popup = 22, To(ID = String, Nick = String, Emotion = int, Time = int), From()
	public static final int popup_msgBzz = 23;      //Popup = 23, To(ID = String, Nick = String, Count = int, Time = int), From()




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
