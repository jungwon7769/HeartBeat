package comjungwon7769heartbeat.github.heartbeat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class FriendListActivity extends AppCompatActivity {
	public static Context listContext;
	ListView frList;
	ArrayList<FriendDTO> friend_list;
	FriendListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_list);
		listContext = this;

		//User Info Load and Display ***
		displayUserInfo();

		frList = (ListView) findViewById(R.id.frList_list);
		friend_list = new ArrayList<>();

		//Friend List Load ***

		//StoreFriendTime 검사
		SharedPreferences preference = getSharedPreferences("user_info", Activity.MODE_PRIVATE);
		long saveTime = preference.getLong("friend_time", 0);
		//if 저장한지 오래된경우 FriendList_Load 호출(서버에서 친구목록 가져옴)
		if((System.currentTimeMillis() - saveTime) > Constants.friendLoad_Interval) {

			FriendList_Load();
		}

		//App Database 에서 친구목록 가져오기
		FriendDAO friendDAO = new FriendDAO(getApplicationContext(), FriendDAO.DataBase_name, null, 1);
		friend_list = friendDAO.listFriend();

		//리스트어댑터 생성 밑 리스트뷰와 연결
		adapter = new FriendListAdapter(this, R.layout.item_friend, friend_list);
		frList.setAdapter(adapter);

		//친구리스트 클릭이벤트리스너 지정
		frList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				FriendDTO selectFriend = friend_list.get(position);
				Intent intent = new Intent(FriendListActivity.this, FriendDetailActivity.class);
				intent.putExtra("ID", selectFriend.getID());
				intent.putExtra("Color", selectFriend.getColor());
				intent.putExtra("Mode", selectFriend.getModeInt());
				intent.putExtra("Nick", selectFriend.getNick());
				startActivity(intent);
			}
		});
		//*** Friend List Load END

		//Button Handler Setting ***
		//MyDetail Activity Button
		findViewById(R.id.frList_myDetailLayout).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//myDetail 페이지로 이동
				Intent intent = new Intent(FriendListActivity.this, MyDetailActivity.class);
				startActivity(intent);
			}
		});

		//AlarmMessage Button
		findViewById(R.id.frList_btnAlarm).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(FriendListActivity.this, AlaramListActivity.class);
				startActivity(intent);
			}
		});

		//Add Friend Button
		findViewById(R.id.frList_btnAddFriend).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), FriendRequestActivity.class);
				startActivity(intent);
			}
		});

		//Setting Page Button
		findViewById(R.id.frList_btnSetting).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(FriendListActivity.this, NickSetActivity.class);
				startActivity(intent);
			}
		});

		//***Button Handelr Setting END

	} //onCreate()

	private void displayUserInfo() {
		//User Info Load and Display ***
		ImageView imgMyMode = (ImageView) findViewById(R.id.frList_myMode);
		TextView txtMyNick = (TextView) findViewById(R.id.frList_myNick);

		SharedPreferences preference = getSharedPreferences("user_info", Activity.MODE_PRIVATE);
		txtMyNick.setText(preference.getString("my_nick", "DataLoadError"));
		int myMode = preference.getInt("my_mode", 2);

		Constants.Emotion[] e = Constants.Emotion.values();
		imgMyMode.setImageResource(getResources().getIdentifier(e[myMode].toString(), "drawable", this.getPackageName()));
		imgMyMode.setBackgroundColor(Color.parseColor("#" + e[myMode].getColor()));
	}

	public void dataRefresh() {
		displayUserInfo();

		//FrinedList_Load 호출(Server)
		//Notcomplete

		FriendDAO friendDAO = new FriendDAO(getApplicationContext(), FriendDAO.DataBase_name, null, 1);
		friend_list = friendDAO.listFriend();

		adapter.setItemList(friend_list);
		adapter.notifyDataSetChanged();
	}

	//친구목록 서버로부터 불러오기
	private ArrayList<FriendDTO> FriendList_Load() {
		//Notcomplete
		Log.i("HBTest", "FriendList_Load");
		//서버통신
		SharedPreferences preference = getSharedPreferences("user_info", Activity.MODE_PRIVATE);
		ServerCommunication sc = new ServerCommunication();
		sc.makeMsg(preference.getString("my_id","0"),null, null, null, 13, null, null, 0);
		sc.start();
		while(sc.wait){
			//스레드 기다리기
		}
		HashMap<String, FriendDTO> f_list = (HashMap<String, FriendDTO>)sc.final_data;
		if(f_list==null){//친구없음
			Toast.makeText(getApplicationContext(), "불러올 친구목록이 없습니다.", Toast.LENGTH_SHORT).show();
		}else{
			//친구 1명이상
			FriendDAO friendDAO = new FriendDAO(getApplicationContext(), FriendDAO.DataBase_name, null, 1);
			friendDAO.deleteAll();
			Iterator it = f_list.keySet().iterator();
			while (it.hasNext()) {
				String fid = (String) it.next();
				FriendDTO dto = f_list.get(fid);
				Log.d("HBTEST", dto.getID() + "/" + dto.getNick() + "/" + dto.getColor() + "/" + dto.getModeInt());///test
				//friendDAO.addFriend(new FriendDTO(dto.getID(), dto.getNick(), dto.getColor(), Constants.Emotion.values()[dto.getModeInt()]));
				friendDAO.addFriend(new FriendDTO(dto.getID(), dto.getNick(), dto.getColor(), Constants.Emotion.values()[dto.getModeInt()]));
				//friendDAO.addFriend(new FriendDTO("id", "친구지롱" , "33F2DD", Constants.Emotion.values()[0]));
				//((FriendListActivity)FriendListActivity.listContext).dataRefresh();
				//Log.d("HBTEST", String.valueOf(addchk));///test
			}
		}

		//"Update Time" 갱신
		SharedPreferences.Editor editor = preference.edit();
		editor.putLong("friend_time", System.currentTimeMillis());
		editor.commit();


		return new ArrayList<FriendDTO>();
	} //FriendList_Load()

	//친구목록 리스트뷰의 어댑터 클래스(내부클래스)
	private class FriendListAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private ArrayList<FriendDTO> myFriend;
		private int layout;

		public FriendListAdapter(Context context, int layout, ArrayList<FriendDTO> value) {
			this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.layout = layout;
			myFriend = value;   //인자로받은 FriendDTO 리스트(사용자 친구목록)
		}

		@Override
		public int getCount() {
			return myFriend.size();
		}

		@Override
		public Object getItem(int position) {
			return myFriend.get(position).getNick();
		}

		public void setItemList(ArrayList<FriendDTO> value) {
			myFriend = value;
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
			FriendDTO frItem = myFriend.get(position);  //position에 해당하는 FriendDTO
			Constants.Emotion frMode = frItem.getMode();


			//친구 기분 표시
			ImageView mode = (ImageView) convertView.findViewById(R.id.frItem_Mode);    //뷰와 이미지 연결

			Constants.Emotion[] e = Constants.Emotion.values();
			mode.setImageResource(getResources().getIdentifier(e[frMode.getMode()].toString(), "drawable", getPackageName()));
			mode.setBackgroundColor(Color.parseColor("#" + e[frMode.getMode()].getColor()));

			//친구 색 표시
			mode.setBackgroundColor(Color.parseColor("#" + frItem.getColor()));

			//친구 닉네임 표시
			TextView name = (TextView) convertView.findViewById(R.id.frItem_Nick);      //텍스트뷰와 닉네임 연결
			name.setText(frItem.getNick());

			SharedPreferences preference = getSharedPreferences("user_info", Activity.MODE_PRIVATE);
			String bzzFriend = preference.getString("bzz_id", "");
			ImageView imgBzz = (ImageView) convertView.findViewById(R.id.frItem_imgBzz);
			imgBzz.setImageResource(R.drawable.bzzfriend_no);
			if(bzzFriend.equals(frItem.getID())) {
				imgBzz.setImageResource(R.drawable.bzzfriend_yes);
			}

			return convertView;
		}

	} //ListAdapter
}
