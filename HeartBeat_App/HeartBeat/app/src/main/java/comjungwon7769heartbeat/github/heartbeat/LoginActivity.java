package comjungwon7769heartbeat.github.heartbeat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

	EditText txtId, txtPwd;
	Button btnLogin, btnJoin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		txtId = (EditText) findViewById(R.id.txtID);
		txtPwd = (EditText) findViewById(R.id.txtPWD);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnJoin = (Button) findViewById(R.id.btnJoin);

		btnLogin.setOnClickListener(btnLogin_click);

	}

	Button.OnClickListener btnLogin_click = new View.OnClickListener() {
		public void onClick(View v) {
			//이곳에 버튼 클릭시 일어날 일을 적습니다.
			TextView textV = (TextView)findViewById(R.id.txtLogo);
			textV.setText("HOHO");
		}
	};

}
