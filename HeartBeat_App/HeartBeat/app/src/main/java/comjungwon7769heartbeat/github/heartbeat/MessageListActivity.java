package comjungwon7769heartbeat.github.heartbeat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MessageListActivity extends AppCompatActivity {
	private ArrayList<MsgDTO> msgList;
	private int flag;
	private MsgListAdapter adapter;
	private String frinedID;
	private boolean selectMode = false;
	private ListView msgListView = null;
	private LinearLayout selectMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_list);

		selectMenu = (LinearLayout)findViewById(R.id.msglist_selectMenu);
		selectMenu.setVisibility(View.GONE);
		frinedID = null;

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
			case Constants.msgFlag_any_id:
				frinedID = intent.getStringExtra("FriendID");
				setTitle(frinedID);
				break;
		}

		//Msg Load Using MsgDAO
		MsgDAO msgDAO = new MsgDAO(getApplicationContext(), MsgDAO.DataBase_name, null, 1);
		msgList = msgDAO.listMsg(flag, frinedID);

		//리스트어댑터 생성 밑 리스트뷰와 연결
		msgListView = (ListView) findViewById(R.id.msglistView);

		adapter = new MsgListAdapter(this, R.layout.item_message, msgList);
		msgListView.setAdapter(adapter);

		//View Display and Setting
		//메세지리스트 아이템클릭 리스너 지정
		msgListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(!selectMode) {
					MsgDTO selectMsg = msgList.get(position);
					//Popup
					Intent intent = new Intent(getApplicationContext(), PopupActivity.class);
					TextView txtNick = (TextView) view.findViewById(R.id.msgItem_txtFriend);

					switch(selectMsg.getFlag()) {
						case Constants.msgFlag_Friend:
							intent.putExtra("Popup", Constants.popup_msgFriend);
							break;
						case Constants.msgFlag_Voice:
							intent.putExtra("Popup", Constants.popup_msgVoice);
							intent.putExtra("Nick", txtNick.getText());
							intent.putExtra("Path", "/storage/emulated/0/HeartBeat/tmp/myVoice/"+selectMsg.getSoundPath());
							Log.d("PLAYTEST",selectMsg.getSoundPath());
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
			}
		});

		msgListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				msgListView.clearChoices();
				msgListView.setItemChecked(position, true);
				selectMode = true;
				selectMenu.setVisibility(View.VISIBLE);
				return true;
			}
		});

		Button btnSelectAll = (Button)findViewById(R.id.msglist_selectAll);
		Button btnDelete = (Button)findViewById(R.id.msglist_selectDelete);

		btnSelectAll.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				for (int i=0; i<adapter.getCount(); i++) {
					msgListView.setItemChecked(i, true) ;
				}
				adapter.notifyDataSetChanged();
			}
		});
		btnDelete.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				MsgDTO selectMsg;
				SparseBooleanArray sb = msgListView.getCheckedItemPositions();
				if(sb.size() != 0){
					for(int i=msgListView.getCount()-1; i>=0; i--){
						if(sb.get(i)){
							selectMsg = msgList.get(i);
							msgList.remove(i);
							delete_msg(selectMsg.getSender(), selectMsg.getTime());
						}
					}
					msgListView.clearChoices();
					selectMode = false;
					adapter.notifyDataSetChanged();
					selectMenu.setVisibility(View.GONE);
				} //if
			}
		});
	}

	private void delete_msg(String id, long time) {
		MsgDAO msgDAO = new MsgDAO(getApplicationContext(), MsgDAO.DataBase_name, null, 1);
		msgDAO.deleteMsg(id, time);

		msgList = msgDAO.listMsg(flag, frinedID);
		adapter.setItemList(msgList);
		adapter.notifyDataSetChanged();
	} //delete_msg()

	private void accept_friend(String friend_id) {
		//ServerComu
		SharedPreferences preference = getSharedPreferences("user_info", Activity.MODE_PRIVATE);
		ServerCommunication sc = new ServerCommunication();
		sc.makeMsg(preference.getString("my_id", null), friend_id, null, null, 6, null, null, 0);
		sc.start();
		try {
			sc.join(Constants.ServerWaitTime);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		if(sc.chkError) {
			Toast.makeText(getApplication(), getText(R.string.sv_notConnect), Toast.LENGTH_SHORT).show();
		} else {
			if(sc.final_data == null) {
				Toast.makeText(getApplication(), getText(R.string.sv_notConnect), Toast.LENGTH_SHORT).show();
			}
			else if((boolean) sc.final_data) {//성공
				SharedPreferences.Editor editor = preference.edit();
				editor.putLong("friend_time", 0);
			}else if(!(boolean) sc.final_data) {//실패
				Toast.makeText(getApplication(), getText(R.string.sv_notConnect), Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplication(), getText(R.string.sv_notConnect), Toast.LENGTH_SHORT).show();
			}
		}
		((MainActivity)MainActivity.mainContext).frListRefresh();
	} //accept_friend()

	private void no_friend(String friend_id) {
		//ServerComu
		SharedPreferences preference = getSharedPreferences("user_info", Activity.MODE_PRIVATE);
		ServerCommunication sc = new ServerCommunication();
		sc.makeMsg(preference.getString("my_id", null), friend_id, null, null, 7, null, null, 0);
		sc.start();
		try {
			sc.join(Constants.ServerWaitTime);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		if(sc.chkError) {
			Toast.makeText(getApplication(), getText(R.string.sv_notConnect), Toast.LENGTH_SHORT).show();
		} else {
			if(sc.final_data == null) {
				Toast.makeText(getApplication(), getText(R.string.sv_notConnect), Toast.LENGTH_SHORT).show();
			}
			else if((boolean) sc.final_data) {//성공
				SharedPreferences.Editor editor = preference.edit();
				editor.putLong("friend_time", 0);
			}else if(!(boolean) sc.final_data) {//실패
				Toast.makeText(getApplication(), getText(R.string.sv_notConnect), Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplication(), getText(R.string.sv_notConnect), Toast.LENGTH_SHORT).show();
			}
		}
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
			TextView content = (TextView) convertView.findViewById(R.id.msgItem_txtContent);
			CheckBox checkBox = (CheckBox)convertView.findViewById(R.id.msgItem_check);

			MsgDTO msgItem = myMsg.get(position);  //position에 해당하는 MsgDTO

			//친구 요청 메세지의 경우(친구관계 아님)
			if(msgItem.getFlag() == Constants.msgFlag_Friend) {
				//친구요청한 사람의 ID 표시
				name.setText(msgItem.getSender());
				//기분 표시
				Constants.Emotion[] e = Constants.Emotion.values();
				mode.setImageResource(getResources().getIdentifier(e[0].toString(), "drawable", getPackageName()));
				mode.setBackgroundColor(Color.parseColor("#" + e[0].getColor()));
				//내용
				content.setText(getText(R.string.msg_content_friend));
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
					mode.setBackgroundColor(Color.parseColor("#" + friendDTO.getColor()));
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
				if(msgItem.getFlag() == Constants.msgFlag_Bzz) {
					content.setText(getText(R.string.msg_content_bzz));
				} else if(msgItem.getFlag() == Constants.msgFlag_Emotion) {
					content.setText(getText(Constants.Emotion_content[msgItem.getModeInt()]));
				} else if(msgItem.getFlag() == Constants.msgFlag_Voice) {
					content.setText(getText(R.string.msg_content_voice));
				}

			}

			//Time 표시

			TextView txtTime = (TextView) convertView.findViewById(R.id.msgItem_txtTime);
			long msgTime = msgItem.getTime();
			long currentTime = System.currentTimeMillis();

			if((currentTime - msgTime) < 86400000) {
				DateFormat dateFormat = new SimpleDateFormat("HH:mm");
				txtTime.setText(dateFormat.format(msgTime));
			} else {
				DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
				txtTime.setText(dateFormat.format(msgTime));
			}

			if(selectMode) {
				((View) txtTime).setVisibility(View.GONE);
				((View) checkBox).setVisibility(View.VISIBLE);
			}
			else {
				((View) txtTime).setVisibility(View.VISIBLE);
				((View) checkBox).setVisibility(View.GONE);
			}

			return convertView;
		}

	} //ListAdapter

	@Override
	public void onBackPressed() {
		if(selectMode) {
			selectMode = false;
			msgListView.clearChoices();
			adapter.notifyDataSetChanged();
			selectMenu.setVisibility(View.GONE);

		}else{
			super.onBackPressed();
		}
	}
}
