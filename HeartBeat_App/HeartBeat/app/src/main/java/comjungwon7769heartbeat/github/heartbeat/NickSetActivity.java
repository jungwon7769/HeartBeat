package comjungwon7769heartbeat.github.heartbeat;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NickSetActivity extends AppCompatActivity {
	EditText txtNick;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nick_set);

		txtNick = (EditText)findViewById(R.id.setting_txtNick);

		((Button)findViewById(R.id.setting_btnNickOK)).setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				OK_Button(txtNick.getText().toString());
			}
		});
		((Button)findViewById(R.id.setting_btnNickCancel)).setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void OK_Button(String nick){
		//Chcek Length
		if(nick.length() > Constants.maxString) {
			Toast.makeText(getApplicationContext(), "닉네임의 길이는 " + Constants.maxString + "보다 짧아야합니다", Toast.LENGTH_SHORT).show();
			return;
		}
		else if(nick.length() < Constants.minString) {
			Toast.makeText(getApplicationContext(), "닉네임의 길이는 " + Constants.minString + "보다 길어야합니다", Toast.LENGTH_SHORT).show();
			return;
		}

		//Preference Save
		SharedPreferences preference = getSharedPreferences("user_info", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = preference.edit();
		editor.putString("my_nick", nick);
		editor.commit();

		//Server Comu


		//popup
		Intent intent = new Intent(getApplicationContext(), PopupActivity.class);
		intent.putExtra("Popup", Constants.popup_ok);
		intent.putExtra("Message", getText(R.string.NickSetSuccess));
		startActivity(intent);
		txtNick.setText("");
		((FriendListActivity)FriendListActivity.listContext).dataRefresh();

	}
}
