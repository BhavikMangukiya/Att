package com.players.bmef;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AppService extends Service {
    int MY_PERMISSION_REQUEST_DATA;
    int count = 0;
    int[] id;
    Intent[] intents = new Intent[500];
    PendingIntent[] pintents = new PendingIntent[500];

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        System.out.println("In on create");
        if (VERSION.SDK_INT >= 23) {
            int read_access = ContextCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE");
            int write_access = ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE");
            if (read_access == 0 || write_access == 0) {
                ((AlarmManager) getSystemService(ALARM_SERVICE)).setRepeating(0, System.currentTimeMillis(), 86400000, PendingIntent.getService(this, 0, new Intent(this, AppService.class), 0));
                System.out.println("Service Started!");
                return;
            }
            System.out.println("AppService : Permissions Issue");
            return;
        }
        ((AlarmManager) getSystemService(ALARM_SERVICE)).setRepeating(0, System.currentTimeMillis(), 86400000, PendingIntent.getService(this, 0, new Intent(this, AppService.class), 0));
        System.out.println("Service Started!");
    }

    private void fetchTodaysData() {
        int i;
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (this.count > 0) {
            for (i = 0; i < this.count; i++) {
                alarmManager.cancel(this.pintents[i]);
            }
        }
        DataHandler handler = new DataHandler(this);
        int pos = 0;
        for (i = 0; i < this.id.length; i++) {
            String[] s = handler.getScheduleDetailsv2(this.id[i]);
            if (s.length > 0) {
                for (String parse : s) {
                    try {
                        Date d = new SimpleDateFormat("HH:mm").parse(parse);
                        Calendar c = Calendar.getInstance();
                        c.set(Calendar.HOUR_OF_DAY, d.getHours());
                        c.set(Calendar.SECOND, d.getMinutes());
                        c.add(Calendar.SECOND, -10);
                        Calendar c2 = Calendar.getInstance();
                        c2.setTimeInMillis(System.currentTimeMillis());
                        if (c.getTimeInMillis() > c2.getTimeInMillis()) {
                            this.intents[pos] = new Intent(this, AlarmReceiver.class);
                            this.intents[pos].putExtra("id", this.id[i]);
                            this.pintents[pos] = PendingIntent.getBroadcast(this, pos, this.intents[pos], PendingIntent.FLAG_UPDATE_CURRENT);
                            alarmManager.set(0, c.getTimeInMillis(), this.pintents[pos]);
                            pos++;
                        }
                    } catch (Exception e) {
                        System.out.println("caught");
                    }
                }
            }
        }
        this.count = pos;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("In start command");
        if (VERSION.SDK_INT >= 23) {
            int read_access = ContextCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE");
            int write_access = ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE");
            if (read_access == 0 || write_access == 0) {
                this.id = new DataHandler(this).getAllIds();
                fetchTodaysData();
                System.out.println("Events Refreshed!");
            } else {
                System.out.println("AppService : Permissions Issue");
            }
        } else {
            this.id = new DataHandler(this).getAllIds();
            fetchTodaysData();
            System.out.println("Events Refreshed!");
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy() {
        super.onDestroy();
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (this.count > 0) {
            for (int i = 0; i < this.count; i++) {
                alarmManager.cancel(this.pintents[i]);
            }
        }
    }
}
