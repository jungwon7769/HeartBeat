package comjungwon7769heartbeat.github.heartbeat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
		//popup_ok
		if(popupMode == Constants.popup_ok){
			setContentView(R.layout.activity_popup_ok);
			setTitle(R.string.popup_ok_label);

			//팝업을 호출한 액티비티로부터 데이터를 불러와 셋팅
			TextView message = (TextView) findViewById(R.id.popup_ok_txt);
			message.setText(intent.getStringExtra("Message"));

			//팝업의 OK 버튼의 리스너 지정
			Button btnOK = (Button)findViewById(R.id.popup_ok_btnOK);
			btnOK.setOnClickListener(new Button.OnClickListener(){
				@Override
				public void onClick(View v) {
					//확인버튼 클릭시 액티비티 종료
					Intent intent = new Intent();
					intent.putExtra("Popup", Constants.popup_ok);
					setResult(RESULT_OK, intent);

					finish();
				}
			});
		}
		//popup_pickColor
		else if(popupMode == Constants.popup_pickColor){
			setContentView(R.layout.activity_popup_pickcolor);
			setTitle(R.string.popup_pickColor_label);

			final ColorPickerView colorPickerView = (ColorPickerView)findViewById(R.id.popup_pickColor_colorPickerView);

			Button btnOK = (Button)findViewById(R.id.popup_pickColor_ok);
			btnOK.setOnClickListener(new Button.OnClickListener(){
				@Override
				public void onClick(View v) {
					//확인버튼 클릭시 선택한 색상 전달후 액티비티 종료
					Intent intent = new Intent();
					intent.putExtra("Popup", Constants.popup_pickColor);
					intent.putExtra("selectedColor", colorPickerView.getSelectColor());
					setResult(RESULT_OK, intent);

					finish();
				}
			});
			Button btnCancel = (Button)findViewById(R.id.popup_pickColor_cancel);
			btnCancel.setOnClickListener(new Button.OnClickListener(){
				@Override
				public void onClick(View v) {
					finish();
				}
			});
		}
		//popup_...



	} // onCreate

	/*
	@Override
	public boolean onTouchEvent(MotionEvent event){
		if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
			return false;
		}
		return true;
	}
	*/



}
