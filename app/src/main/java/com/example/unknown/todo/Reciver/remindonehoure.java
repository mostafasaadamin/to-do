package com.example.unknown.todo.Reciver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.example.unknown.todo.R;
import com.example.unknown.todo.Tasks;

/**
 * Created by unknown on 7/5/2018.
 */

public class remindonehoure extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int id= intent.getExtras().getInt("id");
        if(id!=-1) {
            Intent i = new Intent(context, Tasks.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pen = PendingIntent.getActivity(context,id, i, PendingIntent.FLAG_ONE_SHOT);
            NotificationCompat.Builder no = new NotificationCompat.Builder(context, "M_CH_ID");
            Uri sound = Uri.parse("android.resource://" + context.getPackageName() + "/raw/sound");
            no.setSound(sound);
            no.setBadgeIconType(R.drawable.status);
            no.setContentTitle(intent.getExtras().getString("tasktitle"));
            no.setContentText(context.getResources().getString(R.string.starttask));
            no.setSmallIcon(R.drawable.status);
            no.setAutoCancel(true);
            no.setContentIntent(pen);
            NotificationManager nm =(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            nm.notify(0, no.build());
        }

    }
}
