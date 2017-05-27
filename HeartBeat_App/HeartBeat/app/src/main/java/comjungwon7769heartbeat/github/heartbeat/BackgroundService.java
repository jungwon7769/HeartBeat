package comjungwon7769heartbeat.github.heartbeat;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class BackgroundService extends Service {
	BTSignalThread btThread;
	RequestMsgThread svThread;

	public BackgroundService() {
	}

	@Override
	public void onCreate(){
		//Start Bluetooth Signal Receiver Thread
		btThread = new BTSignalThread(getApplicationContext());
		Thread thread_bt = new Thread(btThread);
		thread_bt.start();

		//Start Message Receive From Server Thread
		svThread = new RequestMsgThread(getApplicationContext());
		Thread thread_sv = new Thread(svThread);
		thread_sv.start();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}
}
