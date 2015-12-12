package com.example.kostya.test;

/**
 * Created by Kostya on 26.05.2015.
 */

import java.util.concurrent.TimeUnit;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class NotificationService extends Service {
    NotificationManager nm;

    @Override
    public void onCreate() {
        super.onCreate();
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sendNotif();
        return super.onStartCommand(intent, flags, startId);
    }

    void sendNotif() {
        // 1-я часть
////        Notification notif = new Notification(R.drawable.ic_launcher, "Text in status bar",
////                System.currentTimeMillis());
////
////        // 3-я часть
////        Intent intent = new Intent(this, FullscreenActivity.class);
////        intent.putExtra(FullscreenActivity.FILE_NAME, "somefile");
//        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
//
//        // 2-я часть
//        notif.setLatestEventInfo(this, "Notification's title", "Notification's text", pIntent);
//
//        // ставим флаг, чтобы уведомление пропало после нажатия
//        notif.flags |= Notification.FLAG_AUTO_CANCEL;
//
//        // отправляем
//        nm.notify(1, notif);
    }

    public IBinder onBind(Intent arg0) {
        return null;
    }
}
