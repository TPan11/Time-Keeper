package com.btbsolutions.timekeeper.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.btbsolutions.timekeeper.R;

import java.util.List;

public class MetricQAdapter extends RecyclerView.Adapter<MetricQAdapter.MetricQAdapterItemViewHolder> {

    List<String> mTaskList;
    private final LayoutInflater mInflater;

    public MetricQAdapter(Context context){
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MetricQAdapterItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = mInflater.inflate(R.layout.recyclerview_display_tasks, viewGroup, false);
        return new MetricQAdapterItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MetricQAdapterItemViewHolder metricQAdapterItemViewHolder, int i) {
        if (mTaskList != null){
            metricQAdapterItemViewHolder.metric_item.setText(mTaskList.get(i));
        }
    }

    @Override
    public int getItemCount() {
        if (mTaskList != null){
            return mTaskList.size();
        }
        return 0;
    }

    public void setTaskList(List<String> taskList){
        mTaskList = taskList;
    }

    public class MetricQAdapterItemViewHolder extends RecyclerView.ViewHolder {
        TextView metric_item;

        public MetricQAdapterItemViewHolder(@NonNull View itemView) {
            super(itemView);
            metric_item = itemView.findViewById(R.id.metric_item);
        }
    }
}
