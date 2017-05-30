package comjungwon7769heartbeat.github.heartbeat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class AlaramListFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_alaram_list, container, false);
	}

	@Override
	public void onStart() {
		super.onStart();

		final Button btnFriend = (Button)getView().findViewById(R.id.almList_btnFriend);
		final Button btnVoice = (Button)getView().findViewById(R.id.almList_btnVoice);
		final Button btnEmotion = (Button)getView().findViewById(R.id.almList_btnEmotion);
		final Button btnBzz = (Button)getView().findViewById(R.id.almList_btnBzz);

		btnFriend.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				btnMsg_Click(Constants.msgFlag_Friend);
			}
		});
		btnVoice.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				btnMsg_Click(Constants.msgFlag_Voice);
			}
		});
		btnEmotion.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				btnMsg_Click(Constants.msgFlag_Emotion);
			}
		});
		btnBzz.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				btnMsg_Click(Constants.msgFlag_Bzz);
			}
		});

	}   //onCreate

	private  void btnMsg_Click(int select){
		Intent intent = new Intent(getActivity(), MessageListActivity.class);
		intent.putExtra("Flag", select);
		startActivity(intent);

	}

}
