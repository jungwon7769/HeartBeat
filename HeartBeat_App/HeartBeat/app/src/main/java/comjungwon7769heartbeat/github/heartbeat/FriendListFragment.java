package comjungwon7769heartbeat.github.heartbeat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class FriendListFragment extends Fragment {
	public static Context listContext;
	ListView frList;
	ArrayList<FriendDTO> friend_list;
	FriendListAdapter adapter;
	EditText txtSearch;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_friend_list, container, false);
	}

	@Override
	public void onStart() {
		super.onStart();
		listContext = this.getActivity();

		//User Info Load and Display ***
		displayUserInfo();

		frList = (ListView) getView().findViewById(R.id.frList_list);
		friend_list = new ArrayList<>();

		//Friend List Load ***

		//StoreFriendTime 검사
		SharedPreferences preference = getActivity().getSharedPreferences("user_info", Activity.MODE_PRIVATE);
		long saveTime = preference.getLong("friend_time", 0);
		//if 저장한지 오래된경우 FriendList_Load 호출(서버에서 친구목록 가져옴)
		if((System.currentTimeMillis() - saveTime) > Constants.friendLoad_Interval) {
			FriendList_Load();
		}
		//App Database 에서 친구목록 가져오기
		FriendDAO friendDAO = new FriendDAO(getActivity().getApplicationContext(), FriendDAO.DataBase_name, null, 1);

		friend_list = friendDAO.listFriend();

		//리스트어댑터 생성 밑 리스트뷰와 연결
		adapter = new FriendListAdapter(this.getActivity(), R.layout.item_friend, friend_list);
		frList.setAdapter(adapter);

		//친구리스트 클릭이벤트리스너 지정
		frList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				FriendDTO selectFriend = friend_list.get(position);
				Intent intent = new Intent(getActivity(), FriendDetailActivity.class);
				intent.putExtra("ID", selectFriend.getID());
				intent.putExtra("Color", selectFriend.getColor());
				intent.putExtra("Mode", selectFriend.getModeInt());
				intent.putExtra("Nick", selectFriend.getNick());
				startActivity(intent);
			}
		});
		//*** Friend List Load END

		//Button Handler Setting ***
		txtSearch = (EditText)getView().findViewById(R.id.frList_editSearch);
		//MyDetail Activity Button
		getView().findViewById(R.id.frList_myDetailLayout).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//myDetail 페이지로 이동
				Intent intent = new Intent(getActivity(), MyDetailActivity.class);
				startActivity(intent);
			}
		});

		//Add Friend Button
		getView().findViewById(R.id.frList_btnAddFriend).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity().getApplicationContext(), FriendRequestActivity.class);
				startActivity(intent);
			}
		});

		getView().findViewById(R.id.frList_btnSearch).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FriendDAO friendDAO = new FriendDAO(getActivity().getApplicationContext(), FriendDAO.DataBase_name, null, 1);
				friend_list = friendDAO.listFriend_contain(txtSearch.getText().toString());
				adapter.setItemList(friend_list);
				adapter.notifyDataSetChanged();
			}
		});

		//***Button Handelr Setting END

	} //onCreate()

	private void displayUserInfo() {
		//User Info Load and Display ***
		ImageView imgMyMode = (ImageView) getView().findViewById(R.id.frList_myMode);
		TextView txtMyNick = (TextView) getView().findViewById(R.id.frList_myNick);

		SharedPreferences preference = getActivity().getSharedPreferences("user_info", Activity.MODE_PRIVATE);
		txtMyNick.setText(preference.getString("my_nick", "DataLoadError"));
		int myMode = preference.getInt("my_mode", 2);

		Constants.Emotion[] e = Constants.Emotion.values();
		imgMyMode.setImageResource(getResources().getIdentifier(e[myMode].toString(), "drawable", getActivity().getPackageName()));
		imgMyMode.setBackgroundColor(Color.parseColor("#" + e[myMode].getColor()));
	}

	//친구목록 서버로부터 불러오기
	public ArrayList<FriendDTO> FriendList_Load() {
		//Notcomplete
		Log.i("HBTest", "FriendList_Load");
		//서버통신
		SharedPreferences preference = getActivity().getSharedPreferences("user_info", Activity.MODE_PRIVATE);
		ServerCommunication sc = new ServerCommunication();
		sc.makeMsg(preference.getString("my_id", "0"), null, null, null, 13, null, null, 0);
		sc.start();
		try {
			sc.join(Constants.ServerWaitTime);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		HashMap<String, FriendDTO> f_list = (HashMap<String, FriendDTO>) sc.final_data;
		if(f_list == null) {//친구없음
		} else {
			//친구 1명이상
			FriendDAO friendDAO = new FriendDAO(getActivity().getApplicationContext(), FriendDAO.DataBase_name, null, 1);
			friendDAO.deleteAll();
			Iterator it = f_list.keySet().iterator();
			while(it.hasNext()) {
				String fid = (String) it.next();
				FriendDTO dto = f_list.get(fid);
				friendDAO.addFriend(new FriendDTO(dto.getID(), dto.getNick(), dto.getColor(), Constants.Emotion.values()[dto.getModeInt()]));
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
			mode.setImageResource(getResources().getIdentifier(e[frMode.getMode()].toString(), "drawable", getActivity().getPackageName()));
			mode.setBackgroundColor(Color.parseColor("#" + e[frMode.getMode()].getColor()));

			//친구 색 표시
			mode.setBackgroundColor(Color.parseColor("#" + frItem.getColor()));

			//친구 닉네임 표시
			TextView name = (TextView) convertView.findViewById(R.id.frItem_Nick);      //텍스트뷰와 닉네임 연결
			name.setText(frItem.getNick());

			SharedPreferences preference = getActivity().getSharedPreferences("user_info", Activity.MODE_PRIVATE);
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
