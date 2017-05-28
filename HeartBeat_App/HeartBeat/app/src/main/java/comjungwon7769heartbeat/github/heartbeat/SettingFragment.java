package comjungwon7769heartbeat.github.heartbeat;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SettingFragment extends Fragment {
	EditText txtNick;
	Button btnNickOK;
	TextView txtBTName, txtPush, txtBzz;
	View btView, pushView, bzzView;
	public static String pushModeText[];

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_setting, container, false);
	}

	@Override
	public void onStart() {
		super.onStart();

		pushModeText = new String[5];
		pushModeText[Constants.set_push_no] = getText(R.string.setting_push_no).toString();
		pushModeText[Constants.set_push_sound] = getText(R.string.setting_push_sound).toString();
		pushModeText[Constants.set_push_bzz] = getText(R.string.setting_push_bzz).toString();
		pushModeText[Constants.set_push_both] = getText(R.string.setting_push_both).toString();

		//Layout Link
		txtNick = (EditText) getView().findViewById(R.id.setting_txtNick);
		btnNickOK = (Button) getView().findViewById(R.id.setting_btnNickOK);

		txtBTName = (TextView) getView().findViewById(R.id.setting_txtBT);
		btView = (View) getView().findViewById(R.id.setting_viewBT);

		txtPush = (TextView) getView().findViewById(R.id.setting_txtPush);
		pushView = (View) getView().findViewById(R.id.setting_viewPush);

		txtBzz = (TextView) getView().findViewById(R.id.setting_txtBzz);
		bzzView = (View) getView().findViewById(R.id.setting_viewBzz);


		SharedPreferences preference = getActivity().getSharedPreferences("user_info", Activity.MODE_PRIVATE);

		txtNick.setText(preference.getString("my_nick", "0"));
		txtBTName.setText(preference.getString("btName", "0"));
		txtPush.setText(pushModeText[preference.getInt("set_push", 3)]);
		if(preference.getBoolean("set_btBzz", true) == Constants.set_btBzz_ok)
			txtBzz.setText(getText(R.string.setting_bzz_yes));
		else txtBzz.setText(getText(R.string.setting_bzz_no));


		btnNickOK.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				nick_OK_Button(txtNick.getText().toString());
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

		btView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				btNameShow();
			}
		});

		pushView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				pushModeShow();
			}
		});

		bzzView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				bzzModeShow();
			}
		});
	}

	private void nick_OK_Button(String nick) {
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
		try {
			sc.join(10000);
		} catch(InterruptedException e) {
			e.printStackTrace();
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
				Toast.makeText(getActivity().getApplicationContext(), getActivity().getText(R.string.sv_notConnect), Toast.LENGTH_SHORT).show();
			}
		}

	}

	private void btNameShow() {
		BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
		//Device not support bluetooth
		if(btAdapter == null) {
			Toast.makeText(getActivity(), getActivity().getText(R.string.bt_notSupport), Toast.LENGTH_SHORT).show();
			return;
		}
		//Bluetooth Not Enabled
		if(!btAdapter.isEnabled()) {
			Toast.makeText(getActivity(), getActivity().getText(R.string.bt_notEnable), Toast.LENGTH_SHORT).show();
			return;
		}
		//Paired Device Search
		final List<String> ListItems = new ArrayList<>();
		Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
		if(pairedDevices.size() > 0) {
			for(BluetoothDevice device : pairedDevices) {
				ListItems.add(device.getName());
			}
		}
		final CharSequence[] items = ListItems.toArray(new String[ListItems.size()]);

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("페어링 기기 목록");

		builder.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int pos) {
				String selectedText = items[pos].toString();
				SharedPreferences preference = getActivity().getSharedPreferences("user_info", Activity.MODE_PRIVATE);
				SharedPreferences.Editor editor = preference.edit();
				editor.putString("btName", selectedText);
				editor.commit();
				txtBTName.setText(selectedText);
			}
		});
		builder.show();
	}

	private void pushModeShow() {
		final List<String> ListItems = new ArrayList<>();

		for(int i = 0; i < 4; i++) {
			ListItems.add(pushModeText[i]);
		}
		final CharSequence[] items = ListItems.toArray(new String[ListItems.size()]);

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("푸시 설정");

		builder.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int pos) {
				String selectedText = items[pos].toString();
				SharedPreferences preference = getActivity().getSharedPreferences("user_info", Activity.MODE_PRIVATE);
				SharedPreferences.Editor editor = preference.edit();
				//editor.putBoolean("set_btBzz", Constants.set_btBzz_ok);
				editor.putInt("set_push", pos);
				editor.commit();
				txtPush.setText(selectedText);
			}
		});
		builder.show();
	}

	private void bzzModeShow() {
		final List<String> ListItems = new ArrayList<>();

		ListItems.add(getText(R.string.setting_bzz_yes).toString());
		ListItems.add(getText(R.string.setting_bzz_no).toString());

		final CharSequence[] items = ListItems.toArray(new String[ListItems.size()]);

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("진동 설정");

		builder.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int pos) {
				String selectedText = items[pos].toString();
				SharedPreferences preference = getActivity().getSharedPreferences("user_info", Activity.MODE_PRIVATE);
				SharedPreferences.Editor editor = preference.edit();
				if(selectedText.contains(getText(R.string.setting_bzz_yes)))
					editor.putBoolean("set_btBzz", Constants.set_btBzz_ok);
				else editor.putBoolean("set_btBzz", Constants.set_btBzz_no);
				editor.commit();
				txtBzz.setText(selectedText);
			}
		});
		builder.show();
	}
}
