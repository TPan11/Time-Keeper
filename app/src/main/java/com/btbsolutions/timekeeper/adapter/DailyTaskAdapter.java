package com.btbsolutions.timekeeper.adapter;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.btbsolutions.timekeeper.R;
import com.btbsolutions.timekeeper.utility.DailyTasks;
import com.btbsolutions.timekeeper.utility.ToDoRepository;

import java.util.List;

public class DailyTaskAdapter extends RecyclerView.Adapter<DailyTaskAdapter.DailyTaskViewHolder> {

    private final LayoutInflater mInflater;
    private Context mContext;
    ToDoRepository mRepo;
    private List<DailyTasks> mDailyTaskItems;

    public DailyTaskAdapter(Context ctx, Application application){
        mInflater = LayoutInflater.from(ctx);
        this.mContext = ctx;
        mRepo = new ToDoRepository(application);
    }

    @NonNull
    @Override
    public DailyTaskViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View itemView = mInflater.inflate(R.layout.recyclerview_dailytask, viewGroup, false);
        return new DailyTaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyTaskViewHolder dailyTaskViewHolder, int position) {
        if (mDailyTaskItems != null) {
            DailyTasks dt = mDailyTaskItems.get(position);
            dailyTaskViewHolder.tv_timeline_starttime.setText(dt.getStrtTime());
            dailyTaskViewHolder.tv_timeline_task.setText(dt.getTasks());
            dailyTaskViewHolder.tv_timeline_duration.setText(String.valueOf(dt.getDuration()));
            dailyTaskViewHolder.tv_timeline_metric.setText(dt.getMetricQ());
            dailyTaskViewHolder.tv_timeline_endtime.setText(dt.getEndTime());
        }
    }

    @Override
    public int getItemCount() {
        if(mDailyTaskItems != null){
            return mDailyTaskItems.size();
        }
        return 0;
    }

    public void setItems(List<DailyTasks> words){
        mDailyTaskItems = words;
        notifyDataSetChanged();
    }

    class DailyTaskViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_timeline_task;
        private TextView tv_timeline_duration;
        private TextView tv_timeline_metric;
        private TextView tv_timeline_starttime;
        private TextView tv_timeline_endtime;

        DailyTaskViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_timeline_starttime = itemView.findViewById(R.id.tv_timeline_starttime);
            tv_timeline_task = itemView.findViewById(R.id.tv_timeline_task);
            tv_timeline_duration = itemView.findViewById(R.id.tv_timeline_duration);
            tv_timeline_metric = itemView.findViewById(R.id.tv_timeline_metric);
            tv_timeline_endtime = itemView.findViewById(R.id.tv_timeline_endtime);
        }
    }
}
