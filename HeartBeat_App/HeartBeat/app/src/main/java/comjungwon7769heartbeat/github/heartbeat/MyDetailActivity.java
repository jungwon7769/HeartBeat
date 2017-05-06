package comjungwon7769heartbeat.github.heartbeat;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MyDetailActivity extends AppCompatActivity {
	Button btnSetLED, btnSetEmotion, btnTransBzz, btnTransVoice;


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
		for(int i = 0; i < e.length; i++) {
			if(e[i].getMode() == myMode) {
				user_mode.setImageResource(getResources().getIdentifier(e[i].toString(), "drawable", this.getPackageName()));
				user_mode.setBackgroundColor(Color.parseColor("#" + e[i].getColor()));
			}
		}
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
	}

	private void transBzzToMe_Click() {
		//Trans Bzz Using BluetoothComu
	}

	private void transSoundMsgToMe_Click() {
		//Popup(RecordVoice)
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==1 && resultCode==RESULT_OK){
				if(data.getIntExtra("Popup", 1) == Constants.popup_pickColor) {
					String selectedColor = data.getStringExtra("selectedColor");
					setLED(selectedColor);
				}
				else if(false){

				}


		}
	} //onActivityResult

	private void setLED(String color) {
		Log.i("Test", "setLED~~" + color);

	}

	private void setEmotion(Constants.Emotion e) {

	}

	private void playSoundMsg() {

	}
}
