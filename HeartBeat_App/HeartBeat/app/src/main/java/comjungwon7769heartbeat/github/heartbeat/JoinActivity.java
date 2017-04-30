package comjungwon7769heartbeat.github.heartbeat;

import android.app.Activity;
import android.os.Bundle;

public class JoinActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_join);
	}

	private  boolean ID_Usable_Check(String id){

		return false;
	} //id_usable_check()

	private void Join(String id, String pwd, String Nick){

	} //join()
}
