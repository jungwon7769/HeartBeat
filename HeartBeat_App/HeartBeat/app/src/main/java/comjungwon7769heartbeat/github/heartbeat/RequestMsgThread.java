package comjungwon7769heartbeat.github.heartbeat;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by AH on 2017-05-27.
 */
public class RequestMsgThread implements Runnable {
	public BlueToothCommunication btComu;
	public ServerCommunication svComu;
	public Context mContext;
	BlueToothHandler btHandler = new BlueToothHandler(null);

	public RequestMsgThread(Context c) {
		this.mContext = c;
	}

	@Override
	public void run() {
		while(true) {
			MsgDTO message = serverMsgReceive();
			if(message != null) {
				saveMsg(message);
				pushAllarm(message);
				if(message.getFlag() == Constants.msgFlag_Bzz){
					playBzz(message.getSender());
				}
			}

			try {
				Thread.sleep(Constants.RequestMsg_Interval);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	//서버로부터 메시지가져옴
	public MsgDTO serverMsgReceive() {
		MsgDTO msgDTO = new MsgDTO();

		//NotComplete

		try {
			svComu = new ServerCommunication();
			SharedPreferences preference = mContext.getSharedPreferences("user_info", Activity.MODE_PRIVATE);
			svComu.makeMsg(preference.getString("my_id","0"),null, null, null, 14, null, null, 0);
			//Log.d("MSGTEST",svComu.msg);
			svComu.start();     //thread Start
			svComu.join();
			msgDTO = (MsgDTO)svComu.final_data;
			if(msgDTO==null){
				Log.d("MSGTEST", "수신할 메시지 없음");
			}else{
				Log.d("MSGTEST", "메시지 수신완료");
				if(msgDTO.getFlag()==Constants.msgFlag_Voice){//음성파일인경우 수신하기
					msgDTO.setSoundPath(msgDTO.getSender() + "_" + preference.getString("my_id", "0") + "_" + msgDTO.getSoundPath());
					Log.d("RECVTEST", msgDTO.getSoundPath());
					ReceiveMP3 rm = new ReceiveMP3(msgDTO.getSoundPath());
					Log.d("RECVTEST",rm.filename);
					rm.start();
				}
				return msgDTO;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	//Message 저장
	public boolean saveMsg(MsgDTO message) {
		MsgDAO msgDAO = new MsgDAO(mContext, MsgDAO.DataBase_name, null, 1);
		if(message.getFlag() == Constants.msgFlag_Bzz){
			int count = msgDAO.existBzz(message.getSender());
			if(count > 0){
				msgDAO.updateMsg(message.getSender(), message.getFlag(), count+1, message.getTime());
				return true;
			}
		}
		msgDAO.addMsg(message);

		return true;
	}

	//푸시알림 띄움
	public void pushAllarm(MsgDTO message) {
		SharedPreferences preference = mContext.getSharedPreferences("user_info", Activity.MODE_PRIVATE);
		int setPush = preference.getInt("set_push", 0);
		String content;

		if(setPush == Constants.set_push_no) {
			return;
		}

		if(message.getFlag() == Constants.msgFlag_Friend){
			content = mContext.getText(R.string.msg_content_friend).toString();
		}else if(message.getFlag() == Constants.msgFlag_Voice){
			content = mContext.getText(R.string.msg_content_voice).toString();
		}else if(message.getFlag() == Constants.msgFlag_Emotion){
			content = mContext.getText(Constants.Emotion_content[message.getModeInt()]).toString();
		}else{
			content = mContext.getText(R.string.msg_content_bzz).toString();
		}

		NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(mContext.NOTIFICATION_SERVICE);

		PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, new Intent(mContext, LoadingActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
		Notification.Builder builder = new Notification.Builder(mContext);

		builder.setSmallIcon(R.mipmap.ico);
		builder.setTicker(message.getSender() + " : " + content);
		builder.setWhen(System.currentTimeMillis());
		if(message.getFlag() == Constants.msgFlag_Bzz) builder.setNumber(message.getCount());
		builder.setContentTitle(message.getSender());
		builder.setContentText(content);
		builder.setContentIntent(pendingIntent).setAutoCancel(true);

		if(setPush == Constants.set_push_bzz)
			builder.setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS);
		else if(setPush == Constants.set_push_sound)
			builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS);
		else if(setPush == Constants.set_push_both) builder.setDefaults(Notification.DEFAULT_ALL);

		notificationManager.notify(1, builder.build());
	}

	//Trans Bzz to 기기
	public void playBzz(String sender){
		SharedPreferences preference = mContext.getSharedPreferences("user_info", Activity.MODE_PRIVATE);
		BlueToothCommunication btComu = new BlueToothCommunication(preference.getString("btName",""), this.btHandler);
		btComu.btHander = this.btHandler;

		//블루투스 전송 데이터 설정
		btComu.setUseMode(btComu.CODE_BZZ);
		FriendDAO friendDAO = new FriendDAO(mContext.getApplicationContext(), FriendDAO.DataBase_name, null, 1);
		FriendDTO friendDTO = friendDAO.getFriend(sender);
		btComu.setData(friendDTO.getColor());

		//전송
		Thread thread = new Thread(btComu);
		thread.start();
	}
}
