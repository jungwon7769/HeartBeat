package comjungwon7769heartbeat.github.heartbeat;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Random;

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
			saveMsg(message);
			pushAllarm(message);

			try {
				//Thread.sleep(Constants.RequestMsg_Interval);
				Thread.sleep(30000);
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
			while(svComu.wait){
			}
			MsgDTO res = (MsgDTO)svComu.final_data;
			if(res==null){
				Log.d("MSGTEST", "수신할 메시지 없음");
			}else{
				//Log.d("MSGTEST", "메시지 수신완료");
				res.setSoundPath(res.getSender()+"_"+preference.getString("my_id","0")+"_"+res.getTime());
				//Log.d("MSGTEST", res.getSender()+"/"+res.getModeInt()+"/"+res.getFlag()+"/"+res.getTime()+"/"+res.getSoundPath());
				//msg 디비에 저장
				MsgDAO msgDAO = new MsgDAO(mContext.getApplicationContext(), MsgDAO.DataBase_name, null, 1);
				msgDAO.addMsg(res);
				if(res.getFlag()==Constants.msgFlag_Voice){//음성파일인경우 수신하기

				}
			}
			svComu.join();  //thread end Wait...
		} catch(Exception e) {
			e.printStackTrace();
		}


		//test Data
		Random r = new Random();
		msgDTO.setSender("현정이");
		msgDTO.setFlag(r.nextInt(4));
		msgDTO.setCount(3);
		msgDTO.setTime(System.currentTimeMillis());
		msgDTO.setMode(r.nextInt(10));
		msgDTO.setSoundPath("");


		return msgDTO;
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

		if(setPush == Constants.set_push_no) {
			return;
		}

		NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(mContext.NOTIFICATION_SERVICE);

		PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, new Intent(mContext, LoadingActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
		Notification.Builder builder = new Notification.Builder(mContext);

		builder.setSmallIcon(R.mipmap.ico);
		builder.setTicker(mContext.getText(R.string.app_name) + " " + message.getSender());
		builder.setWhen(System.currentTimeMillis());
		if(message.getFlag() == Constants.msgFlag_Bzz) builder.setNumber(message.getCount());
		builder.setContentTitle(message.getSender());
		builder.setContentText("내용");
		builder.setContentIntent(pendingIntent).setAutoCancel(true);

		if(setPush == Constants.set_push_bzz)
			builder.setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS);
		else if(setPush == Constants.set_push_sound)
			builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS);
		else if(setPush == Constants.set_push_both) builder.setDefaults(Notification.DEFAULT_ALL);

		notificationManager.notify(1, builder.build());
	}
}
