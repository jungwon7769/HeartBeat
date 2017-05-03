package comjungwon7769heartbeat.github.heartbeat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PopupActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Popup 종류 설정
		Intent intent = getIntent();
		int popupMode = intent.getIntExtra("Popup", 0);

		//Popup 종류별로 레이아웃 및 초기화
		switch(popupMode){
			case Constants.popup_ok:
				setContentView(R.layout.activity_popup_ok);

				TextView message = (TextView) findViewById(R.id.popup_ok_txt);
				message.setText(intent.getStringExtra("Message"));

				Button btnOK = (Button)findViewById(R.id.popup_ok_btnOK);
				btnOK.setOnClickListener(new Button.OnClickListener(){
					@Override
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.putExtra("result", "팝업 그만~~");
						setResult(RESULT_OK, intent);

						finish();
					}
				});

				break;

			default:
				break;
		}
	} // onCreate

	@Override
	public boolean onTouchEvent(MotionEvent event){
		if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
			return false;
		}
		return true;
	}



}
