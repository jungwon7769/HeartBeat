package comjungwon7769heartbeat.github.heartbeat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LoadingActivity extends AppCompatActivity {

	private boolean Data_Check; //사용자데이터 저장 유무
	private boolean Login_Check;    //사용자데이터 로그인유효

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);
	}

	private  boolean App_Data_Check(){

		return false;
	}

	private boolean Login_Usable_Check(){

		return false;
	}
}
