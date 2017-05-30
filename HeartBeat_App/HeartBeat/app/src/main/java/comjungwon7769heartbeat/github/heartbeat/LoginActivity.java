package comjungwon7769heartbeat.github.heartbeat;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

	private EditText txtId, txtPwd;
	private Button btnLogin, btnJoin;
	private boolean check;
	//private String user_nick;
	//private Constants.Emotion user_mode;
	private MemberDTO dto = new MemberDTO();

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
					//Toast.makeText(getApplicationContext(),"TRUE",Toast.LENGTH_SHORT).show();
					//Save User Data

					SharedPreferences preference = getSharedPreferences("user_info", Activity.MODE_PRIVATE);
					SharedPreferences.Editor editor = preference.edit();
					editor.putString("my_id", dto.getId());
					editor.putString("my_pwd", dto.getPwd());
					editor.putString("my_nick", dto.getNick());
					editor.putInt("my_mode", dto.getMmode());
					editor.putString("bzz_id", "");
					editor.putLong("friend_time", 0);
					editor.putString("btName", Constants.defaultDeviceName);
					editor.putBoolean("set_btBzz", Constants.set_btBzz_ok);
					editor.putInt("set_push", Constants.set_push_both);
					editor.commit();


					//Move To FriendList Act
					Intent intent = new Intent(getApplicationContext(), MainActivity.class);
					startActivity(intent);
					Intent intent_back = new Intent(getApplicationContext(), BackgroundService.class);
					startService(intent_back);
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
		//매개변수값이 불필요하게 또 선언돼 있길래 지움_호빈
		/*String inputID = txtId.getText().toString();
		String inputPWD = txtPwd.getText().toString();*/

		//ID나 PWD가 최소길이보다 작거나 최대길이보다 긴 경우
		if(id.length() < Constants.minString || id.length() > Constants.maxString || pwd.length() < Constants.minString || pwd.length() > Constants.maxString) {
			return false;
		} else { //아이디 PWD 길이가 적당한 경우
			//서버통신_ 회원정보유무검사
			ServerCommunication sc = new ServerCommunication();
			sc.makeMsg(id, null, pwd, null, 10, null, null, 0);
			//Toast.makeText(getApplicationContext(),sc.msg,Toast.LENGTH_SHORT).show();
			sc.start();
			Toast.makeText(getApplicationContext(), getText(R.string.sv_waiting), Toast.LENGTH_SHORT).show();
			try {
				sc.join(10000);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
			if(sc.chkError) {
				Toast.makeText(getApplication(), getText(R.string.sv_notConnect), Toast.LENGTH_SHORT).show();
				return false;
			} else {
				dto = (MemberDTO) sc.final_data;
				if(dto == null) {
					return false;
				}
			}
		}
		return true;
	} //loginUsableCheck()

	private void Join_Button() {
		Intent intent = new Intent(this, JoinActivity.class);
		startActivity(intent);
	} //join_button()


}
