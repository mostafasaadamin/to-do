package com.example.unknown.todo.Reciver;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.example.unknown.todo.Databasepakage.Database;
import com.example.unknown.todo.R;
import com.example.unknown.todo.Tasks;

public class alarmreciever extends BroadcastReceiver {
String description="";

    @Override
    public void onReceive(Context context, Intent intent) {
        int taskid=intent.getExtras().getInt("taskid");
        if(taskid!=-1) {
            Intent i = new Intent(context, Tasks.class);
            description = intent.getExtras().getString("description");
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Database db = new Database(context);
            String id_for_database=String.valueOf(taskid);
            db.updateTaskStatus("done", id_for_database);
            PendingIntent pen = PendingIntent.getActivity(context, intent.getExtras().getInt("id"), i, PendingIntent.FLAG_ONE_SHOT);
            NotificationCompat.Builder no = new NotificationCompat.Builder(context, "M_CH_ID");
            no.setBadgeIconType(R.drawable.done);
            no.setContentTitle(intent.getExtras().getString("tasktitle"));
            no.setContentText(description);
            no.setSmallIcon(R.drawable.done);
            no.setContentIntent(pen);
            no.setAutoCancel(true);
            NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            nm.notify(0, no.build());
            Intent m_service=new Intent(context,mediaservice.class);
            context.startService(m_service);
            Intent open_task=new Intent(context,Tasks.class);
            open_task.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            open_task.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(open_task);
        }
    }
}


