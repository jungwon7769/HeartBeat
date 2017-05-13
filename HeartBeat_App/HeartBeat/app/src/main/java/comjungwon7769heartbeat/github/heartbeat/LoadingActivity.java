package comjungwon7769heartbeat.github.heartbeat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class LoadingActivity extends AppCompatActivity {
	public final int MY_PERMISSION_REQUEST_RECORD_AUDIO = 0;
	public final int MY_PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
	public final int MY_PERMISSION_REQUEST_EVERY = 10;

	private boolean Data_Check; //사용자데이터 저장 유무
	private boolean Login_Check;    //사용자데이터 로그인유효

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);

		//Android Version Check
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			int chkPermission_Record = ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.RECORD_AUDIO);
			int chkPermission_Storage = ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

			//Audio Record Permission, STORAGE Permission 없음
			if(chkPermission_Record == PackageManager.PERMISSION_DENIED && chkPermission_Storage == PackageManager.PERMISSION_DENIED) {
				ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST_EVERY);
			}
			//STORAGE Permission 없음
			else if(chkPermission_Record == PackageManager.PERMISSION_DENIED) {
				ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.RECORD_AUDIO}, MY_PERMISSION_REQUEST_RECORD_AUDIO);
			}
			//STORAGE Permission 없음
			else if(chkPermission_Storage == PackageManager.PERMISSION_DENIED) {
				ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);
			}
			//Permission 모두 있음
			else moveActivityByData();
		} else moveActivityByData();

	} //OnCreate

	private void moveActivityByData() {
		Data_Check = false;
		Login_Check = false;

		Data_Check = App_Data_Check();

		if(Data_Check == true) {
			Login_Check = Login_Usable_Check();
		}

		if(Login_Check == true) {
			Intent intent = new Intent(getApplicationContext(), FriendListActivity.class);
			startActivity(intent);
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

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if(requestCode == MY_PERMISSION_REQUEST_RECORD_AUDIO) {
			if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				moveActivityByData();
			} else finish();
		} else if(requestCode == MY_PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE) {
			if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				moveActivityByData();
			} else finish();
		} else if(requestCode == MY_PERMISSION_REQUEST_EVERY) {
			if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
				moveActivityByData();
			} else finish();
		} //if
	}
}
