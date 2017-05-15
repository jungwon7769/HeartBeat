package comjungwon7769heartbeat.github.heartbeat;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

	private EditText txtId, txtPwd;
	private Button btnLogin, btnJoin;
	private boolean check;
	private String user_nick;
	private Constants.Emotion user_mode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		txtId = (EditText) findViewById(R.id.login_txtID);
		txtPwd = (EditText) findViewById(R.id.login_txtPWD);
		btnLogin = (Button) findViewById(R.id.login_btnLogin);
		btnJoin = (Button) findViewById(R.id.login_btnJoin);


		btnLogin.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				boolean chk = Login_Usable_Check(txtId.getText().toString(), txtPwd.getText().toString());

				if(chk) {
					//Save User Data
					SharedPreferences preference = getSharedPreferences("user_info", Activity.MODE_PRIVATE);
					SharedPreferences.Editor editor = preference.edit();
					editor.putString("my_id", txtId.getText().toString());
					editor.putString("my_pwd", txtPwd.getText().toString());
					editor.putString("my_nick", user_nick);
					editor.putInt("my_mode", user_mode.getMode());
					editor.putString("bzz_id", "");
					editor.putLong("friend_time", 0);
					editor.commit();

					//Move To FriendList Act
					Intent intent = new Intent(getApplicationContext(), FriendListActivity.class);
					startActivity(intent);
					finish();
				} else {
					//Popup Act
					Intent intent = new Intent(getApplicationContext(), PopupActivity.class);
					intent.putExtra("Popup", Constants.popup_ok);
					intent.putExtra("Message", getText(R.string.LoginFailed));
					startActivityForResult(intent, 1);
				}
			}
		});
		btnJoin.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Join_Button();
			}
		});

	} //onCreate()

	private boolean Login_Usable_Check(String id, String pwd) {
		String inputID = txtId.getText().toString();
		String inputPWD = txtPwd.getText().toString();

		//ID나 PWD가 최소길이보다 작거나 최대길이보다 긴 경우
		if(inputID.length() < Constants.minString || inputID.length() > Constants.maxString ||
				inputPWD.length() < Constants.minString || inputPWD.length() > Constants.maxString) return false;

		//Login Request To Server
		//Notcomplete

		//if Server 에 회원정보가 없는 경우 return false;

		//Load User Info(nick, mode) From Server
		user_nick = "현정이지롱";
		user_mode = Constants.Emotion.overeat;

		return true;
	} //loginUsableCheck()

	private void Join_Button() {
		Intent intent = new Intent(this, JoinActivity.class);
		startActivity(intent);
	} //join_button()
}
