package comjungwon7769heartbeat.github.heartbeat;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class JoinActivity extends Activity {

	private EditText txtID, txtPWD, txtNick;
	private Button btnJoin, btnUsable;
	private String id;
	private boolean chkIdUsable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_join);

		txtID = (EditText) findViewById(R.id.join_txtID);
		txtPWD = (EditText) findViewById(R.id.join_txtPWD);
		txtNick = (EditText) findViewById(R.id.join_txtNick);
		btnUsable = (Button) findViewById(R.id.join_btnUsable);
		btnJoin = (Button) findViewById(R.id.join_btnJoin);
		chkIdUsable = false;
		id = "";

		//중복검사 버튼 리스너 지정(ID_Usable_Check)
		btnUsable.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				ID_Usable_Check(txtID.getText().toString());

				//if Not Exist
				id = txtID.getText().toString();
				chkIdUsable = true;
			}
		});
		//회원가입 버튼 리스너 지정(Join)
		btnJoin.setOnClickListener(new View.OnClickListener() {
			                           public void onClick(View v) {
				                           //중복확인 결과 사용가능한 아이디, 비밀번호와 닉네임이 입력된상태
				                           if(id.compareTo(txtID.getText().toString()) != 0 || !chkIdUsable)
					                           Toast.makeText(getApplicationContext(), "ID 중복확인을 해주세요", Toast.LENGTH_SHORT).show();
				                           else if(txtPWD.getText().toString().length() > Constants.maxString)
					                           Toast.makeText(getApplicationContext(), "PASSWORD의 길이는 " + Constants.maxString + "보다 짧아야합니다", Toast.LENGTH_SHORT).show();
				                           else if(txtPWD.getText().toString().length() < Constants.minString)
					                           Toast.makeText(getApplicationContext(), "PASSWORD의 길이는 " + Constants.minString + "보다 길어야합니다", Toast.LENGTH_SHORT).show();
				                           else if(txtNick.getText().toString().length() > Constants.maxString)
					                           Toast.makeText(getApplicationContext(), "닉네임의 길이는 " + Constants.maxString + "보다 짧아야합니다", Toast.LENGTH_SHORT).show();
				                           else if(txtNick.getText().toString().length() < Constants.minString)
					                           Toast.makeText(getApplicationContext(), "닉네임의 길이는 " + Constants.minString + "보다 길어야합니다", Toast.LENGTH_SHORT).show();
				                           else {
					                           Join(txtID.getText().toString(), txtPWD.getText().toString(), txtNick.getText().toString());
				                           }

			                           } //onClick()
		                           }

		);

	}

	private boolean ID_Usable_Check(String id) {
		String inputID = id;

		//입력한 ID가 최소길이보다 작거나 최대길이를 넘음
		if(inputID.length() > Constants.maxString) {
			Toast.makeText(getApplicationContext(), "ID의 길이는 " + Constants.maxString + "보다 짧아야합니다", Toast.LENGTH_SHORT).show();
			return false;
		} else if(inputID.length() < Constants.minString) {
			Toast.makeText(getApplicationContext(), "ID의 길이는 " + Constants.minString + "보다 길어야합니다", Toast.LENGTH_SHORT).show();
			return false;
		}
		//ServerComu class Create
		//Request ID Exist


		return false;
	} //id_usable_check()

	private void Join(String id, String pwd, String Nick) {
		//ServerComu Class Create
		//Request Join

		finish();
	} //join()
}
