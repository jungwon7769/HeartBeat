package comjungwon7769heartbeat.github.heartbeat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AlaramListActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alaram_list);

		Button btnFriend = (Button)findViewById(R.id.almList_btnFriend);
		final Button btnVoice = (Button)findViewById(R.id.almList_btnVoice);
		Button btnEmotion = (Button)findViewById(R.id.almList_btnEmotion);
		Button btnBzz = (Button)findViewById(R.id.almList_btnBzz);

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
		Intent intent = new Intent(AlaramListActivity.this, MessageListActivity.class);
		intent.putExtra("Flag", select);
		startActivity(intent);

	}

}
