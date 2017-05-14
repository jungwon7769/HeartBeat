package comjungwon7769heartbeat.github.heartbeat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
				if(Friend_ID == "") return;
				//존재하는 ID 인 경우
				if(Check = ID_Usable_Check(Friend_ID)) {
					btnAdd.setEnabled(true);
				}
				//존재하지 않는 아이디인 경우
				else {
					btnAdd.setEnabled(false);
					//Popup 으로 알림
					Intent intent = new Intent(getApplicationContext(), PopupActivity.class);
					intent.putExtra("Popup", Constants.popup_ok);
					intent.putExtra("Message", getText(R.string.addfr_notExistID));
					startActivity(intent);
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
		//Server Comu - 아이디 존재여부 검사 요청


		//존재하는 경우 true
		return true;
	}

	//친구요청버튼 클릭시 Handler
	private void OK_Button(String friend_id) {
		//Server Comu - 친구요청 메세지 전송
		Toast.makeText(getApplicationContext(),"친구요청한다",Toast.LENGTH_SHORT).show();
		txtID.setText("");
	}
}
