package com.example.unknown.todo.Adapters;


import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.support.v7.widget.CardView;
import android.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.unknown.todo.Addnewtask;
import com.example.unknown.todo.Databasepakage.Database;
import com.example.unknown.todo.R;
import com.example.unknown.todo.Reciver.alarmreciever;
import com.example.unknown.todo.Reciver.remindonehoure;
import com.example.unknown.todo.ViewModels.taskholder;
import com.example.unknown.todo.model.tasks;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class loadtasks extends RecyclerView.Adapter<taskholder> {
    ArrayList<tasks> catList = new ArrayList<>();
    private long timeCountInMilliSeconds = 0;
    Context context;
    int previous_color = 0;
    CountDownTimer countDownTimer;

    public loadtasks(ArrayList<tasks> catList, Context context) {
        this.catList = catList;
        this.context = context;
    }

    @Override
    public taskholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View form = LayoutInflater.from(parent.getContext()).inflate(R.layout.taskcard, parent, false);
        taskholder sesstion = new taskholder(form);
        return sesstion;
    }

    @Override
    public void onBindViewHolder(final taskholder holder, final int position) {
        final tasks po = catList.get(position);
        holder.title.setText(po.getTitle());
        holder.date.setText(po.getDate().concat(po.getTime()));
        holder.card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
            setupcontextmenu(holder.card,po.getID(),position);
                return false;
            }
        });
        int[] androidColors = context.getResources().getIntArray(R.array.androidcolors);
        if (previous_color < androidColors.length - 1) {
            previous_color++;
        } else {
            previous_color = 0;
        }
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    showtimerpopup(holder.card,po.getDate(),po.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        int randomAndroidColor = androidColors[previous_color];
        holder.card.setBackgroundColor(randomAndroidColor);
        String status = po.getTaskStatus();
        try {
            setTimerValues(po.getDate(), po.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (status.equals("done")) {
            holder.Status.setImageResource(R.drawable.done);
        }
    }
    private void setupcontextmenu(CardView card, final String note_id, final int position) {
        PopupMenu popup = new PopupMenu(context, card);
        //inflating menu from xml resource
        popup.inflate(R.menu.context);
        //adding click listener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_delete_note:
                        cancelAlarm(note_id,position);
                        break;
                }
                return false;
            }
        });
        popup.show();
    }

    private void cancelAlarm(final String note_id,int pos) {
       int id=Integer.parseInt(note_id);
        AlarmManager objAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent alamShowIntent = new Intent(context, alarmreciever.class);
        alamShowIntent.putExtra("id", -1);
        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, id, alamShowIntent, 0);
        objAlarmManager.cancel(alarmPendingIntent);
        //cancel Alarm before task with one houre
               Intent onehoure = new Intent(context, remindonehoure.class);
               onehoure.putExtra("id",-1);
               PendingIntent one_houre_remain_PendingIntent = PendingIntent.getBroadcast(context, id + id, onehoure, 0);
               objAlarmManager.cancel( one_houre_remain_PendingIntent);
               MDToast mdToast = MDToast.makeText(context, context.getResources().getString(R.string.taskdeleted), 1000, MDToast.TYPE_SUCCESS);
              mdToast.show();
              deletenotefromdatabase(note_id,pos);
    }
    private void deletenotefromdatabase(String note_id,int pos) {
        int id=Integer.parseInt(note_id);
         Database db;
         tasks t=new tasks();
         db=new Database(context);
         db.deleteNote(t,note_id);
        catList.remove(pos);
        notifyItemRemoved(pos);
        notifyItemRangeRemoved(pos,getItemCount());
    }
    private void showtimerpopup(CardView holder,String date,String time) throws ParseException {
        Dialog alertDialog = new Dialog(holder.getContext());
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        final View dialogView = inflater.inflate(R.layout.popupfortimer, null);
        ProgressBar bar = dialogView.findViewById(R.id.barfortimer);
        TextView timetext= dialogView.findViewById(R.id. timetext);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(dialogView);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        setTimerValues(date,time);
            setProgressBarValues(bar);
           startCountDownTimer(timetext,bar);
    }
    @Override
    public int getItemCount() {
        return catList.size();
    }
    private void setTimerValues(String date, String time) throws ParseException {
        String myDate = date.concat(" ").concat(time);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date D_T = sdf.parse(myDate);
        long C_time = System.currentTimeMillis();
        timeCountInMilliSeconds = (D_T.getTime() - C_time);
    }
    private void startCountDownTimer(final TextView text,final ProgressBar bar) {
        countDownTimer = new CountDownTimer(timeCountInMilliSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                text.setText(hmsTimeFormatter(millisUntilFinished));
                bar.setProgress((int) (millisUntilFinished / 1000));
            }
            @Override
            public void onFinish() {
              text.setText(hmsTimeFormatter(timeCountInMilliSeconds));
                setProgressBarValues(bar);
            }

        }.start();
        countDownTimer.start();
    }
    private void setProgressBarValues(ProgressBar bar) {
        bar.setMax((int) timeCountInMilliSeconds / 1000);
        bar.setProgress((int) timeCountInMilliSeconds / 1000);
    }
    private String hmsTimeFormatter(long different) {
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;
        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;
        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;
        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;
        long elapsedSeconds = different / secondsInMilli;

        String dhms = elapsedDays + "d:" + elapsedHours + "H:" + elapsedMinutes + "M:" + elapsedSeconds + "S";

        return dhms;


    }
}