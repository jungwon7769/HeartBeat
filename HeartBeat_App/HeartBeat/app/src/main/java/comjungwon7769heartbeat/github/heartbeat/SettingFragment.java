package comjungwon7769heartbeat.github.heartbeat;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingFragment extends Fragment {
	EditText txtNick;
	Button btnNickOK;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_setting, container, false);
	}

	@Override
	public void onStart() {
		super.onStart();

		//Layout Link
		txtNick = (EditText) getView().findViewById(R.id.setting_txtNick);
		btnNickOK = (Button) getView().findViewById(R.id.setting_btnNickOK);

		btnNickOK.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				OK_Button(txtNick.getText().toString());
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
				if(s.length() >= Constants.minString && s.length() <= Constants.maxString)
					btnNickOK.setEnabled(true);
				else btnNickOK.setEnabled(false);
			}
		});

		SharedPreferences preference = getActivity().getSharedPreferences("user_info", Activity.MODE_PRIVATE);
		txtNick.setText(preference.getString("my_nick", "0"));
	}

	private void OK_Button(String nick) {
		//Preference Save
		SharedPreferences preference = getActivity().getSharedPreferences("user_info", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = preference.edit();

		//서버통신
		//ServerCommunication
		String my_id = preference.getString("my_id", "0");
		ServerCommunication sc = new ServerCommunication();
		sc.makeMsg(my_id, null, null, nick, 4, null, null, 0);
		//Toast.makeText(getApplicationContext(),sc.msg,Toast.LENGTH_SHORT).show();//test
		sc.start();
		Toast.makeText(getActivity().getApplicationContext(), getText(R.string.sv_waiting), Toast.LENGTH_SHORT).show();
		while(sc.wait) {
			///스레드처리완료 기다리기
		}
		if(sc.chkError) {
			Toast.makeText(getActivity().getApplicationContext(), getText(R.string.sv_notConnect), Toast.LENGTH_SHORT).show();
		} else {
			if((boolean) sc.final_data) {//닉네임설정 성공
				editor.putString("my_nick", nick);
				editor.commit();
				//popup
				Intent intent = new Intent(getActivity().getApplicationContext(), PopupActivity.class);
				intent.putExtra("Popup", Constants.popup_ok);
				intent.putExtra("Message", getText(R.string.NickSetSuccess));
				startActivity(intent);
				txtNick.setText("");
				btnNickOK.setEnabled(false);
				((MainActivity) MainActivity.mainContext).frListRefresh();
			} else {
				Toast.makeText(getActivity().getApplicationContext(), getText(R.string.sv_notConnect), Toast.LENGTH_SHORT).show();
			}
		}

	}
}
