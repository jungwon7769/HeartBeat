package comjungwon7769heartbeat.github.heartbeat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class PopupActivity extends AppCompatActivity {

	private Constants.Emotion mode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Popup 종류 설정
		Intent intent = getIntent();
		int popupMode = intent.getIntExtra("Popup", 0);

		//Popup 종류별로 레이아웃 및 초기화
		//popup_ok
		if(popupMode == Constants.popup_ok) {
			setContentView(R.layout.activity_popup_ok);
			setTitle(R.string.popup_ok_label);

			//팝업을 호출한 액티비티로부터 데이터를 불러와 셋팅
			TextView message = (TextView) findViewById(R.id.popup_ok_txt);
			message.setText(intent.getStringExtra("Message"));

			//팝업의 OK 버튼의 리스너 지정
			Button btnOK = (Button) findViewById(R.id.popup_ok_btnOK);
			btnOK.setOnClickListener(new Button.OnClickListener() {
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
		else if(popupMode == Constants.popup_pickColor) {
			setContentView(R.layout.activity_popup_pickcolor);
			setTitle(R.string.popup_pickColor_label);

			final ColorPickerView colorPickerView = (ColorPickerView) findViewById(R.id.popup_pickColor_colorPickerView);

			Button btnOK = (Button) findViewById(R.id.popup_pickColor_ok);
			btnOK.setOnClickListener(new Button.OnClickListener() {
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
			Button btnCancel = (Button) findViewById(R.id.popup_pickColor_cancel);
			btnCancel.setOnClickListener(new Button.OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});
		}
		//popup_pickEmotion
		else if(popupMode == Constants.popup_pickEmotion) {
			setContentView(R.layout.activity_popup_pickemotion);
			setTitle(R.string.popup_pickEmotion_label);
			mode = null;

			GridView gridView = (GridView) findViewById(R.id.popup_pickEmotion_grid);
			gridView.setAdapter(new emotionAdapter(this));

			gridView.setOnItemClickListener(new GridView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					//Exist Select Emotion
					if(mode != null) {
						Log.i("Test", mode.getMode() + " Prev " + mode.getColor());
						ImageView prev = (ImageView) parent.getChildAt(mode.getMode());
						prev.setBackgroundColor(Color.parseColor("#" + mode.getColor()));   //background init
						Log.i("Test", mode.getMode() + "Color Change~~");
					}
					//Change background of Select Emotion
					mode = Constants.Emotion.values()[position];
					Log.i("Test", mode.getMode() + " Next " + mode.getColor());
					view.setBackgroundResource(R.drawable.border);
				}
			});

			Button btnOK = (Button) findViewById(R.id.popup_pickEmotion_ok);
			btnOK.setOnClickListener(new Button.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(mode != null) {
						Intent intent = new Intent();
						intent.putExtra("Popup", Constants.popup_pickEmotion);
						intent.putExtra("selectedEmotion", mode.getMode());
						setResult(RESULT_OK, intent);

						finish();
					}
				}
			});
			Button btnCancel = (Button) findViewById(R.id.popup_pickEmotion_cancel);
			btnCancel.setOnClickListener(new Button.OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});

		}
		//popup_...


	} // onCreate

	public class emotionAdapter extends BaseAdapter {
		private Context context;

		public emotionAdapter(Context c) {
			context = c;
		}

		@Override
		public int getCount() {
			return Constants.Emotion.values().length;
		}

		@Override
		public Object getItem(int position) {
			return Constants.Emotion.values()[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imgEmotion;
			if(convertView == null) {
				imgEmotion = new ImageView(context);
			} else {
				imgEmotion = (ImageView) convertView;
			}
			GridView grid = (GridView)parent;
			imgEmotion.setLayoutParams(new GridView.LayoutParams(grid.getColumnWidth(), grid.getColumnWidth()));    //Size
			Constants.Emotion[] e = Constants.Emotion.values();
			imgEmotion.setImageResource(getResources().getIdentifier(e[position].toString(), "drawable", getPackageName()));    //Emotion Image

			imgEmotion.setBackgroundColor(Color.parseColor("#" + e[position].getColor()));  //Emotion Background Color
			if(mode != null) {
				if(position == mode.getMode()) imgEmotion.setBackgroundResource(R.drawable.border); //Emotion Border (Selected Emotion)
			}
			return imgEmotion;
		}
	}

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
