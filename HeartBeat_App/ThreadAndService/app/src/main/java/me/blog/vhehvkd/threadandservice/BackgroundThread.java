package me.blog.vhehvkd.threadandservice;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * Created by SJCE on 2017-05-26.
 */

public class BackgroundThread implements Runnable {
    public Context context;

    @Override
    public void run() {
        pushpush();
        return;
    }

    public void pushpush(){
        for(int i=0; i<10; i++) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class),
                    PendingIntent.FLAG_UPDATE_CURRENT);
            Notification.Builder builder = new Notification.Builder(context);

            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setTicker("푸시왔어");
            builder.setWhen(System.currentTimeMillis());
            builder.setNumber(i);
            builder.setContentTitle("제목");
            builder.setContentText("내용");
            builder.setDefaults(Notification.DEFAULT_ALL);
            builder.setContentIntent(pendingIntent).setAutoCancel(true);

            notificationManager.notify(1, builder.build());

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
