package comjungwon7769heartbeat.github.heartbeat;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

		//StoreFriendTime 검사
		//if 저장한지 오래된경우 FriendList_Load 호출

		frList = (ListView) findViewById(R.id.frList_list);

		friend_list = new ArrayList<>();

		//이부분에 사실 DAO써서 불러오는게 들어가야한다-----------------------

		FriendDTO me = new FriendDTO("hjjj", "안현정", "66CCFF", Constants.Emotion.smile);
		friend_list.add(me);

		FriendDTO me1 = new FriendDTO("hjjj", "안현정1", "66CCFF", Constants.Emotion.annoy);
		friend_list.add(me1);

		FriendDTO me2 = new FriendDTO("hjjj", "안현정2", "66CCFF", Constants.Emotion.sleep);
		friend_list.add(me2);

		Random r = new Random();
		for(int i=0; i<30; i++){
			FriendDTO fff = new FriendDTO("dd", "친구"+i, "88AAE4", Constants.Emotion.values()[r.nextInt(10)]);
			friend_list.add(fff);
		}

		//DAO로 불러오는거다-------------------------------------

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
	}

	//친구목록 서버로부터 불러오기
	private ArrayList<FriendDTO> FriendList_Load() {

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

			switch(frMode){
				case smile: mode.setImageResource(R.drawable.smile);
					break;
				case laugh: mode.setImageResource(R.drawable.laugh);
					break;
				case sad: mode.setImageResource(R.drawable.sad);
					break;
				case annoy:mode.setImageResource(R.drawable.annoy);
					break;
				case angry:mode.setImageResource(R.drawable.angry);
					break;
				case wink:mode.setImageResource(R.drawable.wink);
					break;
				case love:mode.setImageResource(R.drawable.love);
					break;
				case wow:mode.setImageResource(R.drawable.wow);
					break;
				case overeat:mode.setImageResource(R.drawable.overeat);
					break;
				case sleep:mode.setImageResource(R.drawable.sleep);
					break;
			} //switch

			//친구 색 표시
			mode.setBackgroundColor(Color.parseColor("#"+ frItem.getColor()));

			//친구 닉네임 표시
			TextView name = (TextView) convertView.findViewById(R.id.frItem_Nick);      //텍스트뷰와 닉네임 연결
			name.setText(frItem.getNick());

			return convertView;
		}

	} //ListAdapter
}
