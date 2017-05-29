package comjungwon7769heartbeat.github.heartbeat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class LoadingActivity extends AppCompatActivity {
    public final int MY_PERMISSION_REQUEST_RECORD_AUDIO = 0;
    public final int MY_PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    public final int MY_PERMISSION_REQUEST_EVERY = 10;

    public final String APP_PERMISSION_LIST[] = {android.Manifest.permission.BLUETOOTH, android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.RECORD_AUDIO};

    private boolean Data_Check; //사용자데이터 저장 유무
    private String ID, PWD;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        //Android Version Check
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> arrPms = new ArrayList<>();

            //Permission List 중 권한이 없는 경우
            for (int i = 0; i < APP_PERMISSION_LIST.length; i++) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), APP_PERMISSION_LIST[i]) == PackageManager.PERMISSION_DENIED) {
                    arrPms.add(APP_PERMISSION_LIST[i]);     //List에 해당 퍼미션 추가
                }
            }

            String[] strPms = arrPms.toArray(new String[arrPms.size()]);

            if (strPms.length == 0) moveActivityByData();
            else ActivityCompat.requestPermissions(this, strPms, 0);    //Permission Request

        }
        //Version 6.0 이하
        else moveActivityByData();
    } //OnCreate

    private void moveActivityByData() {
        Data_Check = false;

        Data_Check = App_Data_Check();

        if (Data_Check) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            Intent intent_back = new Intent(getApplicationContext(), BackgroundService.class);
            startService(intent_back);
            finish();
        } else {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private boolean App_Data_Check() {
        SharedPreferences preference = getSharedPreferences("user_info", Activity.MODE_PRIVATE);
        ID = preference.getString("my_id", "");
        PWD = preference.getString("my_pwd", "");
        String nick = preference.getString("my_nick", "");
        int mode = preference.getInt("my_mode", 100);

        if (mode == 100 || ID.equals("") || PWD.equals("") || nick.equals("")) return false;
        else return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        int chkGrant = 0;

        if (grantResults.length < permissions.length) {
            Intent intent = new Intent(getApplicationContext(), PopupActivity.class);
            intent.putExtra("Popup", Constants.popup_ok);
            intent.putExtra("Message", "HEARTBEAT 이용을 위해 권한을 설정하여주세요");
            startActivity(intent);
        }

        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                chkGrant++;
            }
        }

        if (chkGrant == 0) {
            moveActivityByData();
        } else {
            Intent intent = new Intent(getApplicationContext(), PopupActivity.class);
            intent.putExtra("Popup", Constants.popup_ok);
            intent.putExtra("Message", "HEARTBEAT 이용을 위해 권한을 설정하여주세요");
            startActivity(intent);
        }

    }
}
