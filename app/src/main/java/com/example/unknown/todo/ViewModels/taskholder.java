package com.example.unknown.todo.ViewModels;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.unknown.todo.R;


/**
 * Created by unknown on 7/4/2018.
 */

public class taskholder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
public TextView title,date,time;
public ImageView Status;
public CardView card;
    public taskholder(View itemView) {
        super(itemView);
    title=itemView.findViewById(R.id.task_title);
    date=itemView.findViewById(R.id.dateandtime);
    Status=itemView.findViewById(R.id.status);
    card=itemView.findViewById(R.id.cardd);
    }
    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        contextMenu.add(0,0,getAdapterPosition(),"Delete Note");
    }
}
