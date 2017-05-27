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
		Log.i("Test","service~~");
		btThread = new BTSignalThread(getApplicationContext());
		Thread thread_bt = new Thread(btThread);
		thread_bt.start();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}
}
