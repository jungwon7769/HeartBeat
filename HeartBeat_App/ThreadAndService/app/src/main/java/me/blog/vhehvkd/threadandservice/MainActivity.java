package me.blog.vhehvkd.threadandservice;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

	EditText txtBtName, txtBtData;
	TextView txtStatus;
	Button btnConnect, btnTrans, btnBack;
	BluetoothThread bluetoothThread;
	MyHandler myHandler = new MyHandler(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		txtBtName = (EditText)findViewById(R.id.txtBtName);
		txtBtData = (EditText)findViewById(R.id.txtValue);
		txtStatus = (TextView)findViewById(R.id.txtStatus);
		btnConnect = (Button) findViewById(R.id.btn_connect);
		btnTrans = (Button) findViewById(R.id.btn_trans);
		btnBack = (Button) findViewById(R.id.btn_runBack);

		btnConnect.setOnClickListener(btnClick);
		btnTrans.setOnClickListener(btnClick);
		btnBack.setOnClickListener(btnClick);


		bluetoothThread = new BluetoothThread();
		bluetoothThread.myHandler = this.myHandler;


	} //onc

	Button.OnClickListener btnClick = new Button.OnClickListener(){
		@Override
		public void onClick(View v) {
			Button sender = (Button)v;
			if(sender == btnConnect){
				try {
					bluetoothThread.btName = txtBtName.getText().toString();
					Thread thread = new Thread(bluetoothThread);
					thread.start();
				}catch(Exception e){}
			}else if(sender == btnTrans){
				try {
					bluetoothThread.btData = txtBtData.getText().toString();
					Thread thread = new Thread(bluetoothThread);
					thread.start();
				}catch(Exception e){}
				txtBtData.setText("");
			}else if(sender == btnBack){
				if(!btnBack.isSelected()) {
					Intent intent = new Intent(MainActivity.this, MyService.class);
					startService(intent);
					btnBack.setSelected(true);
					btnBack.setText("Stop Back");
				}else{
					Intent intent = new Intent(MainActivity.this, MyService.class);
					stopService(intent);
					btnBack.setSelected(false);
					btnBack.setText("Start Back");
				}
			}
		}
	};
}
