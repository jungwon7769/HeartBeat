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

public class FriendDetailActivity extends AppCompatActivity {

	private FriendDTO selectFriendDTO;
	private Button btnBzzColor, btnTransEmotion, btnTransBzz, btnTransVoice, btnBzzFriend, btnDeleteFriend;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_detail);
		btnBzzFriend = (Button) findViewById(R.id.frDt_btnBzzFriend);

		//Friend Data Load
		Intent intent = getIntent();
		selectFriend(intent.getStringExtra("ID"), intent.getStringExtra("Nick"), intent.getStringExtra("Color"), intent.getIntExtra("Mode", 0));

		//Menu Button JAVA, Layout 연결
		btnBzzColor = (Button) findViewById(R.id.frDt_btnBzzColor);
		btnTransEmotion = (Button) findViewById(R.id.frDt_btnTransEmotion);
		btnTransBzz = (Button) findViewById(R.id.frDt_btnTransBzz);
		btnTransVoice = (Button) findViewById(R.id.frDt_btnTransVoice);
		btnBzzFriend = (Button)findViewById(R.id.frDt_btnBzzFriend);
		btnDeleteFriend = (Button)findViewById(R.id.frDt_btnDeleteFriend);

		btnBzzColor.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				friendColor_Click();
			}
		});
		btnTransEmotion.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				transEmotion_Click();
			}
		});
		btnTransBzz.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				transBzz_Click();
			}
		});
		btnTransVoice.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				transSoundMsg_Click();
			}
		});
		btnBzzFriend.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				setBzzFriend_Click();
			}
		});
		btnDeleteFriend.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				deleteFriend_Click();
			}
		});
	} //OnCreate

	//Friend Info Load Method
	public void selectFriend(String id, String nick, String color, int mode) {
		Constants.Emotion[] e = Constants.Emotion.values();

		//Instance dAta Save
		selectFriendDTO = new FriendDTO(id, nick, color, e[mode]);

		//View Setting
		((TextView) findViewById(R.id.frDt_txtID)).setText(selectFriendDTO.getID());
		((TextView) findViewById(R.id.frDt_txtNick)).setText(selectFriendDTO.getNick());

		ImageView imgMode = (ImageView) findViewById(R.id.frDt_imgMode);
		imgMode.setImageResource(getResources().getIdentifier(selectFriendDTO.getMode().toString(), "drawable", this.getPackageName()));
		imgMode.setBackgroundColor(android.graphics.Color.parseColor("#" + selectFriendDTO.getColor()));

		//Bzz_Friend 검사 및 표시
		SharedPreferences preference = getSharedPreferences("user_info", Activity.MODE_PRIVATE);
		if(selectFriendDTO.getID().equals(preference.getString("bzz_id", "DataLoadError"))) {
			btnBzzFriend.setText(getResources().getString(R.string.DetailAct_BzzFriend) + " ON");
		} else {
			btnBzzFriend.setText(getResources().getString(R.string.DetailAct_BzzFriend) + " OFF");
		}
	}

	//-------Button Handler Method***----------------------
	//Friend Color(Bzz Color) Button
	private void friendColor_Click() {
		//Popup(ColorPick)
		Intent intent = new Intent(getApplicationContext(), PopupActivity.class);
		intent.putExtra("Popup", Constants.popup_pickColor);
		startActivityForResult(intent, 1);
	}

	//Trans Emotion Button
	private void transEmotion_Click() {
		//Popup(EmotionPick)
		Intent intent = new Intent(getApplicationContext(), PopupActivity.class);
		intent.putExtra("Popup", Constants.popup_pickEmotion);
		startActivityForResult(intent, 1);
	}

	//Trans Bzz Button
	private void transBzz_Click() {
		//Trans Bzz Using BluetoothComu
	}

	//Trans Voice Button
	private void transSoundMsg_Click() {
		//Popup(RecordVoice)
		Intent intent = new Intent(getApplicationContext(), PopupActivity.class);
		intent.putExtra("Popup", Constants.popup_recordVoice);
		startActivityForResult(intent, 1);
	}

	//Set Bzz Friend Button
	private void setBzzFriend_Click(){
		SharedPreferences preference = getSharedPreferences("user_info", Activity.MODE_PRIVATE);
		if(!selectFriendDTO.getID().equals(preference.getString("bzz_id", ""))) {
			SharedPreferences.Editor editor = preference.edit();
			editor.putString("bzz_id", selectFriendDTO.getID());
			editor.commit();
			btnBzzFriend.setText(getResources().getString(R.string.DetailAct_BzzFriend) + " ON");

			//List Activity ImgChange
			((FriendListActivity)FriendListActivity.listContext).dataRefresh();
		}

	}

	//Delete Friend Button
	private void deleteFriend_Click(){

	}
	//---------***Button Handler Method END------------------

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 1 && resultCode == RESULT_OK) {
			if(data.getIntExtra("Popup", 1) == Constants.popup_pickColor) {
				String selectedColor = data.getStringExtra("selectedColor");
				saveFriendColor(selectedColor);
			} else if(data.getIntExtra("Popup", 1) == Constants.popup_pickEmotion) {
				int selectedEmotion = data.getIntExtra("selectedEmotion", 0);
				transEmotion(Constants.Emotion.values()[selectedEmotion]);
			} else if(data.getIntExtra("Popup", 1) == Constants.popup_recordVoice){
				String voicePath = data.getStringExtra("voicePath");
				Log.i("Test", "MyDtail Voice path" + voicePath);
			}


		}
	} //onActivityResult

	private void saveFriendColor(String color) {
		//Instance Data update
		selectFriendDTO.setColor(color);

		//APP DB에 Save
		FriendDAO friendDAO = new FriendDAO(getApplicationContext(), "Friend_table.db", null, 1);
		friendDAO.changeColor(selectFriendDTO.getID(), color);

		//ImageView 수정
		ImageView imgMode = (ImageView) findViewById(R.id.frDt_imgMode);
		imgMode.setBackgroundColor(android.graphics.Color.parseColor("#" + color));

		//List Activity ImgChange
		((FriendListActivity)FriendListActivity.listContext).dataRefresh();
	}

	private void transEmotion(Constants.Emotion e) {
		Log.i("Test", e.name());
		//ServerComu 이용
		//기분전송

	}

	private void transSoundMsg(){

	}

	private void deleteFriend(){
		//List Activity ImgChange
		((FriendListActivity)FriendListActivity.listContext).dataRefresh();
	}
}
