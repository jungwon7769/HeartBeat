package comjungwon7769heartbeat.github.heartbeat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class MessageListActivity extends AppCompatActivity {
	private ArrayList<MsgDTO> msgList;
	private int flag;
	private MsgListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_list);

		//get intent -> Flag check
		Intent intent = getIntent();
		flag = intent.getIntExtra("Flag", 0);
		switch(flag) {
			case Constants.msgFlag_Friend:
				setTitle(getText(R.string.msgList_friend_Label));
				break;
			case Constants.msgFlag_Voice:
				setTitle(getText(R.string.msgList_voice_Label));
				break;
			case Constants.msgFlag_Emotion:
				setTitle(getText(R.string.msgList_emotion_Label));
				break;
			case Constants.msgFlag_Bzz:
				setTitle(getText(R.string.msgList_bzz_Label));
				break;
		}

		//Msg Load Using MsgDAO
		MsgDAO msgDAO = new MsgDAO(getApplicationContext(), MsgDAO.DataBase_name, null, 1);

		//Notcomplete Test  지우기
		Random r = new Random();
		for(int i = 0; i < 1; i++) {
			MsgDTO testDTO = new MsgDTO(r.nextInt(4), "id" + r.nextInt(1000), System.currentTimeMillis(), Constants.Emotion.sad, "");
			msgDAO.addMsg(testDTO);
		}



		msgList = msgDAO.listMsg(flag);

		//리스트어댑터 생성 밑 리스트뷰와 연결
		final ListView msgListView = (ListView) findViewById(R.id.msglistView);

		adapter = new MsgListAdapter(this, R.layout.item_message, msgList);
		msgListView.setAdapter(adapter);

		//View Display and Setting
		//메세지리스트 아이템클릭 리스너 지정
		msgListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				MsgDTO selectMsg = msgList.get(position);
				//Popup
				Intent intent = new Intent(getApplicationContext(), PopupActivity.class);
				TextView txtNick = (TextView) view.findViewById(R.id.msgItem_txtFriend);

				switch(flag) {
					case Constants.msgFlag_Friend:
						intent.putExtra("Popup", Constants.popup_msgFriend);
						break;
					case Constants.msgFlag_Voice:
						intent.putExtra("Popup", Constants.popup_msgVoice);
						intent.putExtra("Nick", txtNick.getText());
						intent.putExtra("Path", selectMsg.getSoundPath());
						break;
					case Constants.msgFlag_Emotion:
						intent.putExtra("Popup", Constants.popup_msgEmotion);
						intent.putExtra("Nick", txtNick.getText());
						intent.putExtra("Emotion", selectMsg.getModeInt());
						break;
					case Constants.msgFlag_Bzz:
						intent.putExtra("Popup", Constants.popup_msgBzz);
						intent.putExtra("Nick", txtNick.getText());
						intent.putExtra("Count", selectMsg.getCount());
						break;
				}
				intent.putExtra("ID", selectMsg.getSender());
				intent.putExtra("Time", selectMsg.getTime());
				startActivityForResult(intent, 1);
			}
		});

	}

	private void delete_msg(String id, long time) {
		MsgDAO msgDAO = new MsgDAO(getApplicationContext(), MsgDAO.DataBase_name, null, 1);
		msgDAO.deleteMsg(id, time);

		msgList = msgDAO.listMsg(flag);
		adapter.setItemList(msgList);
		adapter.notifyDataSetChanged();
	} //delete_msg()

	private void accept_friend(String friend_id) {
		Log.i("Test", "acceptFriend");
		//Notcomplete
		//ServerComu
	} //accept_friend()

	private void no_friend(String friend_id) {
		Log.i("Test", "no friend");
		//Notcomplete
		//ServerComu
	} //no friend()

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 1 && resultCode == RESULT_OK) {
			String id = data.getStringExtra("ID");
			long time = data.getLongExtra("Time", 0);
			boolean select = data.getBooleanExtra("select", true);

			if(data.getIntExtra("Popup", 1) == Constants.popup_msgFriend) {
				if(select) accept_friend(id);
				else no_friend(id);

				delete_msg(id, time);
			} else if(data.getIntExtra("Popup", 1) == Constants.popup_msgVoice) {
				if(!select) delete_msg(id, time);
			} else if(data.getIntExtra("Popup", 1) == Constants.popup_msgEmotion) {
				if(!select) delete_msg(id, time);
			} else if(data.getIntExtra("Popup", 1) == Constants.popup_msgBzz) {
				if(!select) delete_msg(id, time);
			}

		}
	} //onActivityResult

	//MsgListAdapter(ListView 디스플레이용)
	private class MsgListAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private ArrayList<MsgDTO> myMsg;
		private int layout;

		public MsgListAdapter(Context context, int layout, ArrayList<MsgDTO> value) {
			this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.layout = layout;
			myMsg = value;   //인자로받은 FriendDTO 리스트(사용자 친구목록)
		}

		@Override
		public int getCount() {
			return myMsg.size();
		}

		@Override
		public Object getItem(int position) {
			return myMsg.get(position).getSender();
		}

		public void setItemList(ArrayList<MsgDTO> value) {
			myMsg = value;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null) {
				convertView = inflater.inflate(layout, parent, false);
			}
			TextView name = (TextView) convertView.findViewById(R.id.msgItem_txtFriend);      //텍스트뷰와 닉네임 연결
			ImageView mode = (ImageView) convertView.findViewById(R.id.msgItem_imgMode);

			MsgDTO msgItem = myMsg.get(position);  //position에 해당하는 MsgDTO

			//친구 요청 메세지의 경우(친구관계 아님)
			if(flag == Constants.msgFlag_Friend) {
				//친구요청한 사람의 ID 표시
				name.setText(msgItem.getSender());
				//기분 표시
				Constants.Emotion[] e = Constants.Emotion.values();
				mode.setImageResource(getResources().getIdentifier(e[0].toString(), "drawable", getPackageName()));
				mode.setBackgroundColor(Color.parseColor("#" + e[0].getColor()));
			}
			//다른 메세지인 경우
			else {
				FriendDAO friendDAO = new FriendDAO(getApplicationContext(), FriendDAO.DataBase_name, null, 1);
				FriendDTO friendDTO = friendDAO.getFriend(msgItem.getSender());
				//친구관계인 상태
				if(friendDTO != null) {
					//친구 닉네임 표시
					name.setText(friendDTO.getNick());

					//기분 표시
					Constants.Emotion[] e = Constants.Emotion.values();
					mode.setImageResource(getResources().getIdentifier(e[friendDTO.getModeInt()].toString(), "drawable", getPackageName()));
					mode.setBackgroundColor(Color.parseColor("#" + e[friendDTO.getModeInt()].getColor()));
				}
				//친구관계가 끊어진 상태
				else {
					//닉네임 표시
					name.setText(getText(R.string.noNameFriend));
					//기분 표시
					Constants.Emotion[] e = Constants.Emotion.values();
					mode.setImageResource(getResources().getIdentifier(e[0].toString(), "drawable", getPackageName()));
					mode.setBackgroundColor(Color.parseColor("#" + e[0].getColor()));
				}

			}

			//Time 표시

			TextView txtTime = (TextView) convertView.findViewById(R.id.msgItem_txtTime);
			long msgTime = msgItem.getTime();
			long currentTime = System.currentTimeMillis();

			if((msgTime - currentTime) > 86400) {
				DateFormat dateFormat = new SimpleDateFormat("HH:mm");
				txtTime.setText(dateFormat.format(msgTime));
			} else {
				DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
				txtTime.setText(dateFormat.format(msgTime));
			}


			return convertView;
		}

	} //ListAdapter
}
