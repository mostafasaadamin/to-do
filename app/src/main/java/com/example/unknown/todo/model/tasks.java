package com.example.unknown.todo.model;

/**
 * Created by unknown on 6/1/2018.
 */

public class tasks
{
    public static final String TABLE_NAME = "tasks";
    public static final String Task_ID = "ID";
    public static final String Task_title= "name";
    public static final String Task_description = "description";
    public static final String Task_time = "time";
    public static final String Task_date= "date";
    public static final String Task_status="Status";
    String  ID;
    String  title;
    String  description;
    String  time;
    String  date;
    String taskStatus;
    public  tasks(){}
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public tasks(String ID, String title, String description, String time, String date, String taskStatus) {
        this.ID = ID;
        this.title = title;
        this.description = description;
        this.time = time;
        this.date = date;
        this.taskStatus = taskStatus;
    }


    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + Task_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + Task_title + " TEXT NOT NULL,"
                    + Task_description + " TEXT NOT NULL,"
                    + Task_date + " TEXT NOT NULL,"
                    + Task_time + " TEXT NOT NULL,"
                    + Task_status + " TEXT NOT NULL"
                    + ")";

}
