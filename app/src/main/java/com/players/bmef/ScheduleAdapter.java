package com.players.bmef;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.players.bmef.MyFragment.ScheduleDetails;
import java.util.List;

public class ScheduleAdapter extends Adapter<ScheduleAdapter.ScheduleHolder> {
    Context context;
    List<ScheduleDetails> scheduleData;

    public class ScheduleHolder extends ViewHolder {
        TextView sdata;
        TextView sname;

        public ScheduleHolder(View itemView) {
            super(itemView);
            this.sname = (TextView) itemView.findViewById(R.id.subject_name);
            this.sdata = (TextView) itemView.findViewById(R.id.schedule_data);
        }
    }

    public ScheduleAdapter(List<ScheduleDetails> scheduleData, Context context) {
        this.scheduleData = scheduleData;
        this.context = context;
    }

    public ScheduleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ScheduleHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.schedulecards, parent, false));
    }

    public void onBindViewHolder(ScheduleHolder holder, int position) {
        holder.sname.setText(((ScheduleDetails) this.scheduleData.get(position)).subject_name);
        holder.sdata.setText(((ScheduleDetails) this.scheduleData.get(position)).timings);
    }

    public int getItemCount() {
        return this.scheduleData.size();
    }
}
