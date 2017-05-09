package comjungwon7769heartbeat.github.heartbeat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
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
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class FriendListActivity extends AppCompatActivity {
	ListView frList;
	ArrayList<FriendDTO> friend_list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_list);

		//User Info Load and Display ***
		displayUserInfo();

		findViewById(R.id.frList_myDetailLayout).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//myDetail 페이지로 이동
				Intent intent = new Intent(getApplicationContext(), MyDetailActivity.class);
				startActivity(intent);
			}
		});

		findViewById(R.id.frList_btnRefresh).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				dataRefresh();
			}
		});

		//Friend List Load ***
		//StoreFriendTime 검사
		//if 저장한지 오래된경우 FriendList_Load 호출

		frList = (ListView) findViewById(R.id.frList_list);

		friend_list = new ArrayList<>();

		FriendDAO friendDAO = new FriendDAO(getApplicationContext(), "Friend_table.db", null, 1);
		//friendDAO.addFriend(new FriendDTO("id", "친구지롱", "33F2DD", Constants.Emotion.sleep));
		friend_list = friendDAO.listFriend();

		//리스트어댑터 생성 밑 리스트뷰와 연결
		FriendListAdapter adapter = new FriendListAdapter(this, R.layout.item_friend, friend_list);
		frList.setAdapter(adapter);

		//친구리스트 클릭이벤트리스너 지정
		frList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Toast.makeText(getApplicationContext(), friend_list.get(position).getNick(), Toast.LENGTH_SHORT).show();
			}
		});
		//*** Friend List Load END

		findViewById(R.id.frList_btnAlarm).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//알람메세지 페이지로 이동
				Log.i("Test", "Go Message~~");
			}
		});
		findViewById(R.id.frList_btnAddFriend).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//친구추가 페이지로 이동
				Log.i("Test", "Go Add Friend~~");
			}
		});

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

	private void dataRefresh(){
		displayUserInfo();

		//FrinedList_Load 호출(Server)

		FriendDAO friendDAO = new FriendDAO(getApplicationContext(), "Friend_table.db", null, 1);
		friend_list = friendDAO.listFriend();

		frList.refreshDrawableState();
	}

	//친구목록 서버로부터 불러오기
	private ArrayList<FriendDTO> FriendList_Load() {
		//friendDAO.addFriend(fff);
		return new ArrayList<FriendDTO>();
	} //FriendList_Load()

	private void Friend_Detail_Button() {

	}

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

			return convertView;
		}

	} //ListAdapter
}
