package com.example.unknown.todo;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.example.unknown.todo.Adapters.loadtasks;
import com.example.unknown.todo.Databasepakage.Database;
import com.example.unknown.todo.Reciver.alarmreciever;
import com.example.unknown.todo.Reciver.mediaservice;
import com.example.unknown.todo.Reciver.remindonehoure;
import com.example.unknown.todo.model.tasks;
import com.valdesekamdem.library.mdtoast.MDToast;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Tasks extends AppCompatActivity {
    ArrayList<tasks> task_list=new ArrayList<>();
    RecyclerView taskrecycler;
    loadtasks adapter;
       private Database db;
       TextView emptytext;
       ImageView emptynote;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        taskrecycler=findViewById(R.id.addtask);
        emptytext=findViewById(R.id.emptytext);
        emptynote=findViewById(R.id.empty);
        Toolbar toolbar =findViewById(R.id.toolbar);
        taskrecycler.setLayoutManager(new LinearLayoutManager(Tasks.this));
        taskrecycler.setHasFixedSize(true);
        setSupportActionBar(toolbar);
        FloatingActionButton fab =findViewById(R.id.fab);
        db=new Database(this);
        getAllTasks();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           Intent i=new Intent(Tasks.this,Addnewtask.class);
           startActivity(i);
            }
        });
    }
    private void getAllTasks() {
        task_list=db.getAllNotes();
        if(task_list.size()>0) {
            adapter = new loadtasks(task_list, getApplicationContext());
            taskrecycler.setAdapter(adapter);
            emptytext.setVisibility(View.INVISIBLE);
            emptynote.setVisibility(View.INVISIBLE);
        }else
            {
                MDToast mdToast = MDToast.makeText(Tasks.this, "No Tasks Available", 1000, MDToast.TYPE_ERROR);
                mdToast.show();
                emptytext.setVisibility(View.VISIBLE);
                emptynote.setVisibility(View.VISIBLE);
            }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_or_search, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.action_refresh)
        {
            getAllTasks();
        }
        else if(id==R.id.action_share)
        {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Hey, download this app! set link here");
            startActivity(shareIntent);
        }else if(id==R.id.action_rate)
        {
            Uri uri = Uri.parse("market://details?id=" + getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            goToMarket.addFlags(
                    Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                 Uri.parse("http://play.google.com/store/apps/details?id=" +getPackageName())));
            }
        }else if(id==R.id.action_alarm)
        {
          Intent i=new Intent(getApplicationContext(),mediaservice.class);
          stopService(i);
        }else if(id==R.id.action_setting)
        {
            Intent i=new Intent( Tasks.this,Settingactivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}
