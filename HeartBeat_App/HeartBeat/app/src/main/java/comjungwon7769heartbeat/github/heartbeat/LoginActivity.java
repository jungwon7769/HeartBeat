package comjungwon7769heartbeat.github.heartbeat;

import android.content.Intent;
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
				Login_Usable_Check(txtId.getText().toString(), txtPwd.getText().toString());
			}
		});
		btnJoin.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Join_Button();
			}
		});

	} //onCreate()

	/*
	Button.OnClickListener btnLogin_click = new View.OnClickListener() {
		public void onClick(View v) {
			//이곳에 버튼 클릭시 일어날 일을 적습니다.
			TextView textV = (TextView)findViewById(R.id.login_txtLogo);
			textV.setText("HOHO");
		}
	};
	*/

	private boolean Login_Usable_Check(String id, String pwd) {
		TextView textV = (TextView) findViewById(R.id.login_txtLogo);
		textV.setText(id + pwd);

		return false;
	} //loginUsableCheck()

	private void Join_Button() {
		Intent intent = new Intent(this, JoinActivity.class);
		startActivity(intent);
	} //join_button()
}
