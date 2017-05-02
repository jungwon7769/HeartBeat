package comjungwon7769heartbeat.github.heartbeat;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

	private EditText txtId, txtPwd;
	private Button btnLogin, btnJoin;
	private boolean check;

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

				//test용 true
				if(true) {
					//Move To FriendList Act
					Intent intent = new Intent(getApplicationContext(), FriendListActivity.class);
					startActivity(intent);
					finish();
				} else {
					//Popup Act
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

		return false;
	} //loginUsableCheck()

	private void Join_Button() {
		Intent intent = new Intent(this, JoinActivity.class);
		startActivity(intent);
	} //join_button()
}
