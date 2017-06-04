package comjungwon7769heartbeat.github.heartbeat;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
				if(ID_Usable_Check(txtID.getText().toString())) {
					id = txtID.getText().toString();
					chkIdUsable = true;
				}
			}
		});
		//ID 입력 란의 값이 바뀌는 경우
		txtID.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				chkIdUsable = false;              //존재여부 검사 결과 false로 초기화
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {

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
		ServerCommunication sc = new ServerCommunication();
		sc.makeMsg(txtID.getText().toString(), null, null, null, 11, null, null, 0);
		sc.start();
		try {
			sc.join(Constants.ServerWaitTime);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		if(sc.chkError) {
			Toast.makeText(getApplication(), getText(R.string.sv_notConnect), Toast.LENGTH_SHORT).show();
		} else {
			if(sc.final_data == null) {
				Toast.makeText(getApplicationContext(), getText(R.string.sv_notConnect), Toast.LENGTH_SHORT).show();//test
			} else if(!(boolean) sc.final_data) {//아이디 중복안됨!!
				Toast.makeText(getApplicationContext(), "사용가능한 아이디입니다", Toast.LENGTH_SHORT).show();
				return true;
			} else if((boolean) sc.final_data) {
				Toast.makeText(getApplicationContext(), "!! 사용할수 없는 아이디 입니다", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplication(), getText(R.string.sv_notConnect), Toast.LENGTH_SHORT).show();
			}//여기까지가 통신확인
		}

		return false;
	} //id_usable_check()

	private void Join(String id, String pwd, String Nick) {
		ServerCommunication sc = new ServerCommunication();
		sc.makeMsg(id, null, pwd, Nick, 12, null, null, 0);
		sc.start();
		try {
			sc.join(Constants.ServerWaitTime);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		if(sc.chkError) {
			Toast.makeText(getApplication(), getText(R.string.sv_notConnect), Toast.LENGTH_SHORT).show();
		} else {
			if(sc.final_data == null) {
				Toast.makeText(getApplicationContext(), getText(R.string.sv_notConnect), Toast.LENGTH_SHORT).show();
			} else if((boolean) sc.final_data) {//회원가입성공
				Toast.makeText(getApplication(), getText(R.string.joinSucces), Toast.LENGTH_SHORT).show();
				finish();
			} else if(!(boolean) sc.final_data) {//회원가입실패
				Toast.makeText(getApplication(), getText(R.string.joinFaild), Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplication(), getText(R.string.sv_notConnect), Toast.LENGTH_SHORT).show();
			}

		}
	} //join()
}
