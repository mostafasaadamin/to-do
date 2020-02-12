package com.example.unknown.todo;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.unknown.todo.Adapters.loadtasks;
import com.example.unknown.todo.Databasepakage.Database;
import com.example.unknown.todo.Reciver.alarmreciever;
import com.example.unknown.todo.Reciver.remindonehoure;
import com.example.unknown.todo.model.tasks;
import com.valdesekamdem.library.mdtoast.MDToast;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Addnewtask extends AppCompatActivity {
    SharedPreferences sharedPreference =null;
    EditText tasktitle,taskdescription,tasktime,taskdate;
    int YEAR=0,MONTH=0,HOURE=0,MINUTE=0,DAY=0,SECOUND=0;
    String date="",time="";
    java.util.Calendar dateTime = java.util.Calendar.getInstance();
    String taskstatus="incomplete";
    AVLoadingIndicatorView avi;
    private Database db;
    ArrayList<tasks> task_list=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnewtask);
        db=new Database(this);
        tasktitle =findViewById(R.id.tasktitle);
        avi=findViewById(R.id.loader);
        taskdescription= findViewById(R.id.taskdescription);
        tasktime= findViewById(R.id.tasktime);
        taskdate= findViewById(R.id.taskdate);
        taskdate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
         @Override
         public void onFocusChange(View view, boolean b) {
             if (b) {
                 showDATEdialog();
             }
     }
     });
        tasktime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    showtimedialog();
                }
            }
        });

    }
    private void showtimedialog() {
        new TimePickerDialog(this, t, dateTime.get(java.util.Calendar.HOUR_OF_DAY), dateTime.get(java.util.Calendar.MINUTE), true).show();
    }
    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dateTime.set(java.util.Calendar.HOUR_OF_DAY, hourOfDay);
            dateTime.set(java.util.Calendar.MINUTE, minute);
            String timeComp = dateTime.get(java.util.Calendar.HOUR_OF_DAY) + ":" + dateTime.get(java.util.Calendar.MINUTE) + ":" + dateTime.get(java.util.Calendar.SECOND);
            time=timeComp;
            HOURE=hourOfDay;
            MINUTE=minute;
            SECOUND=dateTime.get(java.util.Calendar.SECOND);
            tasktime.setText(time);
        }
    };
    private void showDATEdialog() {
        new DatePickerDialog(this, d, dateTime.get(java.util.Calendar.YEAR), dateTime.get(java.util.Calendar.MONTH), dateTime.get(java.util.Calendar.DAY_OF_MONTH)).show();
    }
    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateTime.set(java.util.Calendar.YEAR, year);
            dateTime.set(java.util.Calendar.MONTH, monthOfYear);
            dateTime.set(java.util.Calendar.DAY_OF_MONTH, dayOfMonth);
            SimpleDateFormat dateOnly = new SimpleDateFormat("MM/dd/yyyy");
            date=dateOnly.format(dateTime.getTime()).toString();
            taskdate.setText(date);
            DAY=dayOfMonth;
            YEAR=year;
            MONTH=monthOfYear;

        }
    };
    private void CreateAlarmAndSetTask(int year, int month, int day, int houre, int minute,int secound,int RowID,String title,int TaskID,String description) {
        sharedPreference=getApplication().getSharedPreferences("userSetting", Context.MODE_PRIVATE);
        String time_in_minutes=sharedPreference.getString("time", "1");
        int time=Integer.parseInt(time_in_minutes);
        AlarmManager objAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Calendar objCalendar = Calendar.getInstance();
        objCalendar.set(Calendar.YEAR, year);
        objCalendar.set(Calendar.MONTH, month);
        objCalendar.set(Calendar.DAY_OF_MONTH,day);
        objCalendar.set(Calendar.HOUR_OF_DAY, houre);
        objCalendar.set(Calendar.MINUTE, minute);
        objCalendar.set(Calendar.SECOND, secound);
        Intent alamShowIntent = new Intent(getApplicationContext(),alarmreciever.class);
        alamShowIntent.putExtra("tasktitle",title);
        alamShowIntent.putExtra("id",RowID);
        alamShowIntent.putExtra("taskid",TaskID);
        alamShowIntent.putExtra("description",description);
        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), RowID,alamShowIntent,0 );
        objAlarmManager.set(AlarmManager.RTC_WAKEUP,objCalendar.getTimeInMillis(), alarmPendingIntent);
        //Setting Alarm before task with one houre
        long actual_time=objCalendar.getTimeInMillis()-System.currentTimeMillis();
        if(actual_time>=(time*60*1000)) {
            Intent onehoure = new Intent(this, remindonehoure.class);
            onehoure.putExtra("remain_one_houre", title);
            onehoure.putExtra("id", RowID + TaskID);
            PendingIntent one_houre_remain_PendingIntent = PendingIntent.getBroadcast(Addnewtask.this, RowID + TaskID, onehoure, 0);
            objAlarmManager.set(AlarmManager.RTC_WAKEUP, objCalendar.getTimeInMillis() -(time* 60 * 1000), one_houre_remain_PendingIntent);
        }
    }
    private void getAllTasks() {
        task_list=db.getAllNotes();
    }
    public void savetask(View view) {
        if (!TextUtils.isEmpty(tasktitle.getText().toString())&&!TextUtils.isEmpty(taskdescription.getText().toString())&&!TextUtils.isEmpty(time)&&!TextUtils.isEmpty(date)){
            //add task info to database
            avi.show();
            long idd =db.insertProduct(tasktitle.getText().toString().trim(),taskdescription.getText().toString().trim(),tasktime.getText().toString().trim(),taskdate.getText().toString().trim(),taskstatus);
            avi.hide();
            MDToast mdToast = MDToast.makeText(Addnewtask.this, getResources().getString(R.string.successfully), 1000, MDToast.TYPE_SUCCESS);
            mdToast.show();
            getAllTasks();
            int row_index=Integer.parseInt(String.valueOf(idd));
            CreateAlarmAndSetTask(YEAR,MONTH,DAY,HOURE,MINUTE,SECOUND,row_index,tasktitle.getText().toString(),Integer.parseInt(task_list.get(task_list.size()-1).getID()),taskdescription.getText().toString());
        }
        else
        {
            MDToast mdToast = MDToast.makeText(Addnewtask.this, getResources().getString(R.string.fill), 1000, MDToast.TYPE_WARNING);
            mdToast.show();
        }
    }
}
