package me.blog.vhehvkd.threadandservice;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class MyService extends Service {
	BackgroundThread backgroundThread;

	public MyService() {
	}

	@Override
	public void onCreate() {
		backgroundThread = new BackgroundThread();
		backgroundThread.context = getApplicationContext();

		Thread thread = new Thread(backgroundThread);
		thread.start();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}



}
