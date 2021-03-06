package comjungwon7769heartbeat.github.heartbeat;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FriendRequestActivity extends AppCompatActivity {
	private EditText txtID;
	private Button btnAdd;

	private String Friend_ID;
	private boolean Check;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_request);

		txtID = (EditText) findViewById(R.id.addfr_txtNick);
		btnAdd = ((Button) findViewById(R.id.addfr_btnAdd));

		Friend_ID = "";
		Check = false;

		((Button)findViewById(R.id.addfr_btnCancel)).setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		//ID 입력 란의 값이 바뀌는 경우
		txtID.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				Check = false;              //존재여부 검사 결과 false로 초기화
				btnAdd.setEnabled(false);   //요청버튼 비활성화
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		((Button) findViewById(R.id.addfr_btnSearch)).setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				Friend_ID = txtID.getText().toString();
				if(Friend_ID.equals("")) {//아이디를 입력하지 않은경우
					Toast.makeText(getApplicationContext(), getText(R.string.notInputID), Toast.LENGTH_SHORT).show();
				}
				else {
					SharedPreferences pref = getSharedPreferences("user_info", Activity.MODE_PRIVATE);
					//내 아이디를 입력한 경우
					if(Friend_ID.equals(pref.getString("my_id","0"))){
						Toast.makeText(getApplicationContext(), getText(R.string.addfr_inputSelfID), Toast.LENGTH_SHORT).show();
					}
					//존재하는 ID 인 경우
					else if (Check = ID_Usable_Check(Friend_ID)) {
						btnAdd.setEnabled(true);
					}
					//존재하지 않는 아이디인 경우
					else {
						btnAdd.setEnabled(false);
					}
				}
			}
		});

		((Button) findViewById(R.id.addfr_btnAdd)).setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(Check) OK_Button(Friend_ID);
			}
		});
	}

	//ID 존재 여부 검사
	private boolean ID_Usable_Check(String friend_id) {
		if(friend_id.length() < Constants.minString) return false;
		ServerCommunication sc = new ServerCommunication();
		sc.makeMsg(friend_id, null, null, null, 11, null, null, 0);
		sc.start();
		try {
			sc.join(Constants.ServerWaitTime);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		if(sc.chkError){
			Toast.makeText(getApplicationContext(), getText(R.string.sv_notConnect), Toast.LENGTH_SHORT).show();
			return false;
		}else if(sc.final_data == null){
			Toast.makeText(getApplicationContext(), getText(R.string.sv_notConnect), Toast.LENGTH_SHORT).show();//test
			return false;
		}else if(!(boolean)sc.final_data){//해당 id를 갖는 회원이 존재하지 않는경우
			Toast.makeText(getApplicationContext(), getText(R.string.no_member), Toast.LENGTH_SHORT).show();//test
			return false;
		}
		Toast.makeText(getApplicationContext(), getText(R.string.yes_friend), Toast.LENGTH_SHORT).show();//test
		return true; //존재하는 경우
	}

	//친구요청버튼 클릭시 Handler
	private void OK_Button(String friend_id) {
		//Server Comu - 친구요청 메세지 전송
		SharedPreferences pref = getSharedPreferences("user_info", Activity.MODE_PRIVATE);
		ServerCommunication sc = new ServerCommunication();
		sc.makeMsg(pref.getString("my_id","0"), friend_id, null, null, 3, null, null, 0);
		sc.start();
		try {
			sc.join(Constants.ServerWaitTime);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		if(sc.chkError){
			Toast.makeText(getApplicationContext(), getText(R.string.sv_notConnect), Toast.LENGTH_SHORT).show();
		}else {
			if(sc.final_data == null){
				Toast.makeText(getApplicationContext(), getText(R.string.sv_notConnect), Toast.LENGTH_SHORT).show();//test
			}else if(!(boolean) sc.final_data) {//친구요청추가 실패
				Toast.makeText(getApplicationContext(), "이미 친구관계입니다", Toast.LENGTH_SHORT).show();
			}
		}

		txtID.setText("");
	}


}
