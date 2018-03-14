package com.players.bmef;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat.Builder;
import java.util.Random;

public class AlarmReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        System.out.println("Hello world");
        int id = intent.getIntExtra("id", 1);
        DataHandler handler = new DataHandler(context);
        ContentValues values = handler.getSubjectInfo(id);
        handler.close();
        String title = values.getAsString("subject_name") + " " + values.getAsString("type");
        String body = values.getAsString("year") + " " + context.getResources().getString(R.string.yearorstd) + " " + values.getAsString("dept") + " " + values.getAsString("div");
        Intent notificationIntent = new Intent(context, HomeActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, new Random().nextInt(100) + id, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Resources res = context.getResources();
        Builder builder = new Builder(context);
        builder.setContentIntent(contentIntent).setSmallIcon(R.drawable.n_icon).setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.sa_icon)).setWhen(System.currentTimeMillis()).setAutoCancel(true).setContentTitle(title).setContentText(body);
        nm.notify(new Random().nextInt(100), builder.build());
    }
}
