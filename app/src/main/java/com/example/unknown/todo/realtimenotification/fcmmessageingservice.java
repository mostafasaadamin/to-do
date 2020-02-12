package com.example.unknown.todo.realtimenotification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.example.unknown.todo.R;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by unknown on 6/17/2018.
 */

public class fcmmessageingservice extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String title=remoteMessage.getNotification().getTitle();
        String message=remoteMessage.getNotification().getBody();
        Intent i=new Intent(this,Tasks.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pen=PendingIntent.getActivity(this,0,i, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder no=new NotificationCompat.Builder(this,"M_CH_ID");
        Uri sound = Uri.parse("android.resource://"+getPackageName()+"/raw/sound");
        no.setSound(sound);
        no.setBadgeIconType(R.drawable.status);
        no.setContentTitle(title);
        no.setContentText(message);
        no.setSmallIcon(R.drawable.status);
        no.setAutoCancel(true);
        no.setContentIntent(pen);
        NotificationManager nm=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(0,no.build());
        super.onMessageReceived(remoteMessage);

    }
}
