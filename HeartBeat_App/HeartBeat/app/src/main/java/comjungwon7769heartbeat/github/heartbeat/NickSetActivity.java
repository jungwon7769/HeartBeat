package comjungwon7769heartbeat.github.heartbeat;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NickSetActivity extends AppCompatActivity {
	EditText txtNick;
	Button btnNickOK;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nick_set);

		//Layout Link
		txtNick = (EditText)findViewById(R.id.setting_txtNick);
		btnNickOK = (Button)findViewById(R.id.setting_btnNickOK);

		btnNickOK.setOnClickListener(new Button.OnClickListener() {
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

		//ID 입력 란의 값이 바뀌는 경우
		txtNick.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if(s.length() >= Constants.minString && s.length() <= Constants.maxString) btnNickOK.setEnabled(true);
				else btnNickOK.setEnabled(false);
			}
		});
	}

	private void OK_Button(String nick){
		//Preference Save
		SharedPreferences preference = getSharedPreferences("user_info", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = preference.edit();

		//서버통신
		//ServerCommunication
		String my_id = preference.getString("my_id","0");
		ServerCommunication sc = new ServerCommunication();
		sc.makeMsg(my_id, null, null, nick, 4, null, null, 0);
		//Toast.makeText(getApplicationContext(),sc.msg,Toast.LENGTH_SHORT).show();//test
		sc.start();
		Toast.makeText(getApplicationContext(), getText(R.string.sv_waiting), Toast.LENGTH_SHORT).show();
		while(sc.wait){
			///스레드처리완료 기다리기
		}
		if(sc.chkError){
			Toast.makeText(getApplicationContext(), getText(R.string.sv_notConnect), Toast.LENGTH_SHORT).show();
		}else {
			if((boolean) sc.final_data) {//닉네임설정 성공
				editor.putString("my_nick", nick);
				editor.commit();
				//popup
				Intent intent = new Intent(getApplicationContext(), PopupActivity.class);
				intent.putExtra("Popup", Constants.popup_ok);
				intent.putExtra("Message", getText(R.string.NickSetSuccess));
				startActivity(intent);
				txtNick.setText("");
				btnNickOK.setEnabled(false);
				((FriendListActivity) FriendListActivity.listContext).dataRefresh();
			} else {
				Toast.makeText(getApplicationContext(), "닉네임변경 실패..server", Toast.LENGTH_SHORT).show();
			}
		}

	}
}
