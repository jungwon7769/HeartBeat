package comjungwon7769heartbeat.github.heartbeat;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

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
		/*
		try {
			svComu = new ServerCommunication();
			SharedPreferences preference = mContext.getSharedPreferences("user_info", Activity.MODE_PRIVATE);
			//svComu.makeMsg(preference.getString("my_id", "0"),,,,,);
			svComu.start();     //thread Start
			svComu.join();  //thread end Wait...
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		*/

		//test Data
		msgDTO.setSender("현정이");
		msgDTO.setFlag(Constants.msgFlag_Bzz);
		msgDTO.setCount(3);
		msgDTO.setTime(System.currentTimeMillis());
		msgDTO.setMode(3);
		msgDTO.setSoundPath("");


		return msgDTO;
	}

	//Message 저장
	public boolean saveMsg(MsgDTO message) {
		MsgDAO msgDAO = new MsgDAO(mContext, MsgDAO.DataBase_name, null, 1);
		msgDAO.addMsg(message);

		return true;
	}

	//푸시알림 띄움
	public void pushAllarm(MsgDTO message) {
		NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(mContext.NOTIFICATION_SERVICE);

		PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, new Intent(mContext, LoadingActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
		Notification.Builder builder = new Notification.Builder(mContext);

		builder.setSmallIcon(R.drawable.heartbeat_icon);
		builder.setTicker(mContext.getText(R.string.app_name) + " " + message.getSender());
		builder.setWhen(System.currentTimeMillis());
		if(message.getFlag() == Constants.msgFlag_Bzz) builder.setNumber(message.getCount());
		builder.setContentTitle(message.getSender());
		builder.setContentText("내용");
		builder.setDefaults(Notification.DEFAULT_ALL);
		builder.setContentIntent(pendingIntent).setAutoCancel(true);

		notificationManager.notify(1, builder.build());
	}
}
