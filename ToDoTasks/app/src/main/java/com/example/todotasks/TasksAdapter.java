package com.example.todotasks;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDateTime;
import java.util.ArrayList;


public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TaskViewHolder> {

    private final TaskRecyclerView taskRecyclerView;
    private Context context;
    private ArrayList<TaskModel> taskModels;
    private int layout;

    public TasksAdapter(TaskRecyclerView taskRecyclerView, Context context, ArrayList<TaskModel> taskModels, int layout) {
        this.taskRecyclerView = taskRecyclerView;
        this.context = context;
        this.taskModels = taskModels;
        this.layout = layout;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layout, parent,false);
        return new TaskViewHolder(view, taskRecyclerView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.title.setText(taskModels.get(position).getTitle());
        LocalDateTime dateTime =  taskModels.get(position).getDueDate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.dueDate.setText(dateTime.getYear()+"-"+dateTime.getMonth().getValue()+"-"+dateTime.getDayOfMonth()+" | "+dateTime.getHour()+":"+dateTime.getMinute());
        }
        if(taskModels.get(position).isStatus()){
            int color = ContextCompat.getColor(context, R.color.done);
            holder.statusTV.setText("Completed");
            holder.statusTV.setTextColor(color);
        }else{
            int color = ContextCompat.getColor(context, R.color.progress);
            holder.statusTV.setText("In progress");
            holder.statusTV.setTextColor(color);
        }
    }

    @Override
    public int getItemCount() {
        return taskModels.size();
    }


    ////////////////!--------------------------------------------------------!////////////////
    public  static class TaskViewHolder extends RecyclerView.ViewHolder{
        TextView title, dueDate, statusTV;
        public TaskViewHolder(@NonNull View itemView , TaskRecyclerView taskRecyclerView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            dueDate =itemView.findViewById(R.id.dueDate);
            statusTV = itemView.findViewById(R.id.status_tv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (taskRecyclerView != null){
                        int pos = getAdapterPosition();
                        if (pos!= RecyclerView.NO_POSITION){
                            taskRecyclerView.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
    ////////////////!--------------------------------------------------------!////////////////
}