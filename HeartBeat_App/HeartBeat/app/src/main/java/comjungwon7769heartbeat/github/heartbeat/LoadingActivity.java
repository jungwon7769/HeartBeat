package comjungwon7769heartbeat.github.heartbeat;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class LoadingActivity extends AppCompatActivity {

	private boolean Data_Check; //사용자데이터 저장 유무
	private boolean Login_Check;    //사용자데이터 로그인유효

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);

		Data_Check = false;
		Login_Check = false;

		Data_Check = App_Data_Check();

		if(Data_Check == true) {
			Login_Check = Login_Usable_Check();
		}

		if(Login_Check == true) {
			//친구목록화면으로
		}


		new Timer().schedule(new TimerTask() {
			public void run() {
				test();
			}
		}, 1000);



	}

	protected void test() {
		Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
		startActivity(intent);
		finish();
	}

	private boolean App_Data_Check() {

		return false;
	}

	private boolean Login_Usable_Check() {

		return false;
	}
}
