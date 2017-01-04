package com.example.nguyendinh.service_demo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by nguyendinh on 27/12/2016.
 */

public class MyService extends Service {
    public Handler handler = new Handler();
    Runnable chay;
    int count=0;
    private NotificationManager mNotificationManager;

    public MyService() {
        handler = new Handler();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Dich vu dang chay: " + count++, Toast.LENGTH_SHORT).show();
            }
        };

        Toast.makeText(getApplicationContext(), "Dich vu da chay", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(chay);
        Toast.makeText(this, "Service da dung lai", Toast.LENGTH_SHORT).show();
    }

    Timer timer;
    TimerTask timerTask;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(getApplicationContext(), "Dich vu dang chay", Toast.LENGTH_SHORT).show();

        chay();

        return START_STICKY;
    }
    public void chay()
    {
        chay = new Runnable() {
            @Override
            public void run() {
                displayNotification();
                chay();
            }
        };

        handler.postDelayed(chay, 5000);
    }
    protected void displayNotification() {

/* Sử dụng dịch vụ */

        Notification.Builder mBuilder = new Notification.Builder(this);

        mBuilder.setContentTitle("Tin nhắn mới");

        mBuilder.setContentText("Dich vu dang chay: " + count++);

        mBuilder.setTicker("Thông báo!");

        mBuilder.setSmallIcon(R.mipmap.ic_launcher);

/* tăng số thông báo */

        mBuilder.setNumber(count);

/* Tạo đối tượng chỉ đến activity sẽ mở khi chọn thông báo */

        Intent resultIntent = new Intent(this, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        stackBuilder.addParentStack(MainActivity.class);

// kèm dữ liệu theo activity để xử lý

        resultIntent.putExtra("TinNhan",

                new String[] { "Dich vu dang chay: " + count++});

        resultIntent.putExtra("id",10000);

/* Đăng ký activity được gọi khi chọn thông báo */

        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,

                PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);

        mNotificationManager = (NotificationManager)

                getSystemService(Context.NOTIFICATION_SERVICE);

/* cập nhật thông báo */

        mNotificationManager.notify(10000, mBuilder.build());

    }
}
