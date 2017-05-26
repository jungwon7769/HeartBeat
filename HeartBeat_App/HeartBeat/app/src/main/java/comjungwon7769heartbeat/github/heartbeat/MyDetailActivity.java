package comjungwon7769heartbeat.github.heartbeat;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class MyDetailActivity extends AppCompatActivity {
	Button btnSetLED, btnSetEmotion, btnTransBzz, btnTransVoice;
	BlueToothHandler btHandler = new BlueToothHandler(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_detail);

		TextView user_nick = (TextView) findViewById(R.id.myDt_txtNick);
		ImageView user_mode = (ImageView) findViewById(R.id.myDt_imgMode);

		//User Nick, Emotion Load From PreferenceData ***
		SharedPreferences preference = getSharedPreferences("user_info", Activity.MODE_PRIVATE);
		user_nick.setText(preference.getString("my_nick", "DataLoadError"));
		int myMode = preference.getInt("my_mode", 1);

		Constants.Emotion[] e = Constants.Emotion.values();
		user_mode.setImageResource(getResources().getIdentifier(e[myMode].toString(), "drawable", this.getPackageName()));
		user_mode.setBackgroundColor(Color.parseColor("#" + e[myMode].getColor()));

		//*** User Nick, Emotion Load Finish

		//Menu Button JAVA, Layout 연결
		btnSetLED = (Button) findViewById(R.id.myDt_btnLEDColor);
		btnSetEmotion = (Button) findViewById(R.id.myDt_btnSetMode);
		btnTransBzz = (Button) findViewById(R.id.myDt_btnTransBzz);
		btnTransVoice = (Button) findViewById(R.id.myDt_btnTransVoice);

		btnSetLED.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				setLED_Click();
			}
		});
		btnSetEmotion.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				setEmotion_Click();
			}
		});
		btnTransBzz.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				transBzzToMe_Click();
			}
		});
		btnTransVoice.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				transSoundMsgToMe_Click();
			}
		});
		//*** Menu Button 연결 및 Listener

	}

	private void setLED_Click() {
		//Popup(ColorPick)
		Intent intent = new Intent(getApplicationContext(), PopupActivity.class);
		intent.putExtra("Popup", Constants.popup_pickColor);
		startActivityForResult(intent, 1);
	}

	private void setEmotion_Click() {
		//Popup(EmotionPick)
		Intent intent = new Intent(getApplicationContext(), PopupActivity.class);
		intent.putExtra("Popup", Constants.popup_pickEmotion);
		startActivityForResult(intent, 1);
	}

	private void transBzzToMe_Click() {
		//Trans Bzz Using BluetoothComu
		BlueToothCommunication btComu = new BlueToothCommunication(this.btHandler);
		btComu.btHander = this.btHandler;
		btComu.setSendMode(btComu.CODE_MY_BZZ);
		Thread thread = new Thread(btComu);
		thread.start();
	}

	private void transSoundMsgToMe_Click() {
		//Popup(RecordVoice)
		Intent intent = new Intent(getApplicationContext(), PopupActivity.class);
		intent.putExtra("Popup", Constants.popup_recordVoice);
		startActivityForResult(intent, 1);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 1 && resultCode == RESULT_OK) {
			if(data.getIntExtra("Popup", 1) == Constants.popup_pickColor) {
				String selectedColor = data.getStringExtra("selectedColor");
				setLED(selectedColor);
			} else if(data.getIntExtra("Popup", 1) == Constants.popup_pickEmotion) {
				int selectedEmotion = data.getIntExtra("selectedEmotion", 0);
				setEmotion(Constants.Emotion.values()[selectedEmotion]);
			} else if(data.getIntExtra("Popup", 1) == Constants.popup_recordVoice){
				String voicePath = data.getStringExtra("voicePath");
				playSoundMsg(voicePath);
			}


		}
	} //onActivityResult

	private void setLED(String color) {
		//Bluetooth Comu - color Trans
		BlueToothCommunication btComu = new BlueToothCommunication(this.btHandler);
		btComu.setSendMode(btComu.CODE_LED);
		btComu.setData(color);
		Thread thread = new Thread(btComu);
		thread.start();
	}

	private void setEmotion(Constants.Emotion e) {
		//SAVE select Mode (Preference Data)
		SharedPreferences preference = getSharedPreferences("user_info", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = preference.edit();

		//서버통신_ 내기분변경 정보 전송
		ServerCommunication sc = new ServerCommunication();
		sc.makeMsg(preference.getString("my_id","0"),null,null,null,5,null,null,e.getMode());
		sc.start();
		while(sc.wait){
			///스레드처리완료 기다리기
		}
		if((boolean)sc.final_data) {//기분설정 성공
			editor.putInt("my_mode", e.getMode());
			editor.commit();
		}


		//Change Emiton Image
		ImageView user_mode = (ImageView) findViewById(R.id.myDt_imgMode);
		user_mode.setImageResource(getResources().getIdentifier(e.toString(), "drawable", this.getPackageName()));
		user_mode.setBackgroundColor(Color.parseColor("#" + e.getColor()));

		//List Activity ImgChange
		((FriendListActivity)FriendListActivity.listContext).dataRefresh();

		//Bluetooth Play
		BlueToothCommunication btComu = new BlueToothCommunication(this.btHandler);
		btComu.setSendMode(btComu.CODE_EMOTION);
		btComu.setData(e);
		Thread thread = new Thread(btComu);
		thread.start();
	}

	private void playSoundMsg(String path) {
		MediaPlayer player;
		File file = new File(path);
		//파일이 없는 경우
		if(!file.exists()) {
			Toast.makeText(getApplicationContext(), "File Path ERROR", Toast.LENGTH_SHORT).show();
			return;
		}
		try {
			player = new MediaPlayer();
			player.setDataSource(path);
			player.prepare();
			player.start();
		} catch(Exception e) {

		}
	}
}
