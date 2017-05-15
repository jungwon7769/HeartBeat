package comjungwon7769heartbeat.github.heartbeat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class PopupActivity extends AppCompatActivity {

	final String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/HeartBeat/tmp/myVoice";

	private Constants.Emotion mode;
	private MediaPlayer player;
	private MediaRecorder recorder;
	private String recordFilePath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Popup 종류 설정
		Intent intent = getIntent();
		int popupMode = intent.getIntExtra("Popup", 0);

		//Popup 종류별로 레이아웃 및 초기화
		switch(popupMode) {
			case Constants.popup_ok:
				popup_ok(intent);
				break;
			case Constants.popup_re:
				popup_re(intent);
				break;

			case Constants.popup_pickColor:
				popup_pickColor(intent);
				break;
			case Constants.popup_pickEmotion:
				popup_pickEmotion(intent);
				break;
			case Constants.popup_recordVoice:
				popup_recordVoice(intent);
				break;

			case Constants.popup_msgFriend:
				break;
			case Constants.popup_msgEmotion:
				break;
			case Constants.popup_msgVoice:
				break;
			case Constants.popup_msgBzz:
				break;
			/*
			public static final int popup_re = 1;   //Popup = 1, To(Message = String), From(select = boolean)

	public static final int popup_pickEmotion = 10;  //Popup = 10, To (), From (selectedEmotion = int)
	public static final int popup_pickColor = 11;    //Popup = 11, To (), From (selectedColor = String)
	public static final int popup_recordVoice = 12;     //Popup = 12, To(), From(??) 녹음부분 좀더 공부한 뒤에 다시 적겠음

	public static final int popup_msgFriend = 20;   //Popup = 20, To(ID = String, Time = int), From(select = boolean)
	public static final int popup_msgVoice = 21;    //Popup = 21, To(ID = String, Nick = String, Time = int..??), From()     야ㅒ도 오디오관련 좀더 공부하겠음..
	public static final int popup_msgEmotion = 22;  //Popup = 22, To(ID = String, Nick = String, Emotion = int, Time = int), From()
	public static final int popup_msgBzz = 23;
			 */

		}


	} // onCreate

	private void popup_recordVoice(Intent intent) {
		setContentView(R.layout.activity_popup_recordvoice);
		setTitle(R.string.popup_recordVoice_label);

		player = null;
		recorder = null;
		recordFilePath = null;

		//녹음버튼 리스너 지정
		Button btnRecord = (Button) findViewById(R.id.popup_rv_btnRecord);
		btnRecord.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				recordVoice();
			}
		});

		//재생버튼 리스너 지정
		ImageButton btnPlay = (ImageButton) findViewById(R.id.popup_rv_btnPlay);
		btnPlay.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(recordFilePath == null) {
					Toast.makeText(getApplicationContext(), R.string.plzRecordFirst, Toast.LENGTH_SHORT).show();
				} else playVoice(recordFilePath);
			}
		});

		//중지버튼 리스너 지정
		ImageButton btnStop = (ImageButton) findViewById(R.id.popup_rv_btnStop);
		btnStop.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				stopVoice();
			}
		});

		//팝업의 전송 버튼의 리스너 지정
		Button btnTrans = (Button) findViewById(R.id.popup_rv_btnTrans);
		btnTrans.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				//전송버튼 클릭시 액티비티 종료
				Intent intent = new Intent();
				intent.putExtra("Popup", Constants.popup_recordVoice);
				intent.putExtra("voicePath", recordFilePath);
				setResult(RESULT_OK, intent);

				finish();
			}
		});

		//취소버튼 클릭시 팝업액티비티 종료
		Button btnCancel = (Button) findViewById(R.id.popup_rv_btnCancel);
		btnCancel.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void recordVoice() {
		//녹음중이 아닌 경우
		if(recorder == null) {
			((Button) findViewById(R.id.popup_rv_btnTrans)).setEnabled(false);   //전송버튼 사용불가
			//Set FilePath AND Create
			recordFilePath = dirPath + "/" + System.currentTimeMillis() + ".mp3";
			File file = new File(dirPath);
			file.mkdirs();

			file = new File(recordFilePath);
			if(!file.exists()) {
				try {
					file.createNewFile();
					Toast.makeText(getApplicationContext(), "file Create", Toast.LENGTH_SHORT).show();
				} catch(Exception e) {

				}
			}

			//Change Button Img
			((Button) findViewById(R.id.popup_rv_btnRecord)).setBackgroundResource(R.drawable.record_stop);

			//Recorder Setting
			recorder = new MediaRecorder();
			recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
			recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
			recorder.setOutputFile(recordFilePath);

			//Record Start
			try {
				recorder.prepare();
				recorder.start();
			} catch(Exception e) {
				Log.e("Test", "recordVoice Exception : ", e);
			}
		}
		//녹음중에 버튼을 누른 경우
		else if(recorder != null) {
			//Record STOP
			recorder.stop();
			recorder.release();
			recorder = null;
			//Change Button IMG
			((Button) findViewById(R.id.popup_rv_btnRecord)).setBackgroundResource(R.drawable.record_start);
			((Button) findViewById(R.id.popup_rv_btnTrans)).setEnabled(true);    //전송버튼 사용가능
		}
	}

	private void playVoice(String path) {
		File file = new File(path);
		//파일이 없는 경우
		if(!file.exists()) {
			Toast.makeText(getApplicationContext(), "File Path ERROR", Toast.LENGTH_SHORT).show();
			return;
		}
		try {
			if(player != null) {
				player.stop();
				player.release();
				player = null;
			}
			player = new MediaPlayer();
			player.setDataSource(path);
			player.prepare();
			player.start();
		} catch(Exception e) {

		}
	}

	private void stopVoice() {
		if(player != null) {
			player.stop();
			player.release();
			player = null;
		}
	}

	private void popup_ok(Intent intent) {
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

	private void popup_re(Intent intent) {
		setContentView(R.layout.activity_popup_re);
		setTitle(R.string.popup_re_label);

		//팝업을 호출한 액티비티로부터 데이터를 불러와 셋팅
		TextView message = (TextView) findViewById(R.id.popup_re_txt);
		message.setText(intent.getStringExtra("Message"));

		//팝업의 OK 버튼의 리스너 지정
		Button btnOK = (Button) findViewById(R.id.popup_re_ok);
		btnOK.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				//확인버튼 클릭시 액티비티 종료
				Intent intent = new Intent();
				intent.putExtra("Popup", Constants.popup_re);
				intent.putExtra("select", true);
				setResult(RESULT_OK, intent);

				finish();
			}
		});

		//취소버튼 클릭시 팝업액티비티 종료
		Button btnCancel = (Button) findViewById(R.id.popup_re_cancel);
		btnCancel.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void popup_pickColor(Intent intent) {
		setContentView(R.layout.activity_popup_pickcolor);
		setTitle(R.string.popup_pickColor_label);

		//색상선택기
		final ColorPickerView colorPickerView = (ColorPickerView) findViewById(R.id.popup_pickColor_colorPickerView);

		//확인버튼 리스너지정
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
		//취소버튼 클릭시 팝업액티비티 종료
		Button btnCancel = (Button) findViewById(R.id.popup_pickColor_cancel);
		btnCancel.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void popup_pickEmotion(Intent intent) {
		setContentView(R.layout.activity_popup_pickemotion);
		setTitle(R.string.popup_pickEmotion_label);

		player = null;
		mode = null;    //선택한 모드 초기화

		//선택가능한 기분을 보여줄 GridView 레이아웃과 연결
		GridView gridView = (GridView) findViewById(R.id.popup_pickEmotion_grid);
		gridView.setAdapter(new emotionAdapter(this));  //어댑터지정

		//그리드뷰의 기분(아이템)클릭시 동작할 리스너 지정
		gridView.setOnItemClickListener(new GridView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//Exist Select Emotion
				if(mode != null) {
					ImageView prev = (ImageView) parent.getChildAt(mode.getMode());
					prev.setBackgroundColor(Color.parseColor("#" + mode.getColor()));   //background init
				}
				//Change background of Select Emotion
				mode = Constants.Emotion.values()[position];
				view.setBackgroundResource(R.drawable.border);
				try {
					if(player != null) {
						player.stop();
						player.release();;
						player = null;
					}
					Log.i("Test", "try");
					player = MediaPlayer.create(getApplicationContext(), Constants.Emotion_sound[position]);
					//player.prepare();
					player.start();
				} catch(Exception e) {

				}
			}
		});

		//확인버튼 클릭시 동작할 리스너 지정
		Button btnOK = (Button) findViewById(R.id.popup_pickEmotion_ok);
		btnOK.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mode != null) {
					//선택한 기분이 있는 경우에만 해당 기분을 전달후 액티비티 종료
					Intent intent = new Intent();
					intent.putExtra("Popup", Constants.popup_pickEmotion);
					intent.putExtra("selectedEmotion", mode.getMode());
					setResult(RESULT_OK, intent);

					finish();
				} else {
					Toast.makeText(getApplicationContext(), R.string.requestEmotionSelect, Toast.LENGTH_SHORT).show();
				}
			}
		});
		//취소버튼 클릭시 액티비티 종료
		Button btnCancel = (Button) findViewById(R.id.popup_pickEmotion_cancel);
		btnCancel.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	//popup_pickEmotion 에서 기분을 GridView로 보여주기위한 커스텀어댑터 클래스
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
			GridView grid = (GridView) parent;
			imgEmotion.setLayoutParams(new GridView.LayoutParams(grid.getColumnWidth(), grid.getColumnWidth()));    //Size
			Constants.Emotion[] e = Constants.Emotion.values();
			imgEmotion.setImageResource(getResources().getIdentifier(e[position].toString(), "drawable", getPackageName()));    //Emotion Image

			imgEmotion.setBackgroundColor(Color.parseColor("#" + e[position].getColor()));  //Emotion Background Color
			if(mode != null) {
				if(position == mode.getMode())
					imgEmotion.setBackgroundResource(R.drawable.border); //Emotion Border (Selected Emotion)
			}
			return imgEmotion;
		}
	}


	@Override
	public boolean onTouchEvent(MotionEvent event) {
		/*
		if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
			return false;
		}
		*/
		if(recorder != null) recorder.stop();
		recorder = null;
		return true;
	}


}
