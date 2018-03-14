package com.players.bmef;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.players.bmef.DataHandler.Schedule;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class MyAdapter extends Adapter<MyAdapter.SubjectViewHolder> {
    private String[] MONTH = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    String[] colors;
    Context context;
    private int lastPosition = -1;
    List<Subject> subjects;

    public class SubjectViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        ImageButton delete;
        TextView dline1;
        TextView dline2;
        ImageButton edit;
        ImageButton excl;
        RelativeLayout optionsLayout;
        TextView schedule;
        LinearLayout scheduleLayout;
        TextView subject_name;
        ImageButton take_attendance;
        RelativeLayout titleLayout;
        TextView type;
        ImageButton view_records;

        SubjectViewHolder(View itemView) {
            super(itemView);
            this.cv = (CardView) itemView.findViewById(R.id.mycard);
            this.subject_name = (TextView) itemView.findViewById(R.id.subject_name);
            this.type = (TextView) itemView.findViewById(R.id.type);
            this.dline1 = (TextView) itemView.findViewById(R.id.d_line1);
            this.dline2 = (TextView) itemView.findViewById(R.id.d_line2);
            this.schedule = (TextView) itemView.findViewById(R.id.s_data);
            this.scheduleLayout = (LinearLayout) itemView.findViewById(R.id.scheduleLayout);
            this.optionsLayout = (RelativeLayout) itemView.findViewById(R.id.optionsLayout);
            this.titleLayout = (RelativeLayout) itemView.findViewById(R.id.frameLayout);
            this.take_attendance = (ImageButton) itemView.findViewById(R.id.takeAttendance);
            this.view_records = (ImageButton) itemView.findViewById(R.id.viewRecords);
            this.edit = (ImageButton) itemView.findViewById(R.id.edit);
            this.delete = (ImageButton) itemView.findViewById(R.id.delete);
            this.excl = (ImageButton) itemView.findViewById(R.id.excl);
            this.take_attendance.setOnClickListener((OnClickListener) MyAdapter.this.context);
            this.view_records.setOnClickListener((OnClickListener) MyAdapter.this.context);
            this.edit.setOnClickListener((OnClickListener) MyAdapter.this.context);
            this.delete.setOnClickListener((OnClickListener) MyAdapter.this.context);
            this.excl.setOnClickListener((OnClickListener) MyAdapter.this.context);
        }
    }

    public MyAdapter(List mylist, Context c) {
        this.subjects = mylist;
        this.context = c;
        this.colors = c.getResources().getStringArray(R.array.colors);
    }

    public SubjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SubjectViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.scards, parent, false));
    }


   public void onBindViewHolder(SubjectViewHolder holder, int i) {
       // holder.titleLayout.setBackgroundColor(Color.parseColor(this.colors[i % 5]));
       // holder.optionsLayout.setBackgroundColor(Color.parseColor(this.colors[i % 5]));
      //  holder.scheduleLayout.setBackgroundColor(Color.parseColor(this.colors[i % 5]));
        holder.subject_name.setText(((Subject) this.subjects.get(i)).subject_name);
        holder.type.setText(setType(((Subject) this.subjects.get(i)).type));
        holder.cv.setId(((Subject) this.subjects.get(i)).sid);
        TextView textView = holder.dline1;
        TextView textView2 = textView;
        textView2.setText(((Subject) this.subjects.get(i)).year + " " + this.context.getResources().getString(R.string.yearorstd));
        textView = holder.dline2;
        textView2 = textView;
        textView2.setText(((Subject) this.subjects.get(i)).dept + "\t\t" + ((Subject) this.subjects.get(i)).div);
        List<Schedule> list = new DataHandler(this.context).getScheduleDataNew(((Subject) this.subjects.get(i)).sid);
        String[] LOCALE_DAY = this.context.getResources().getStringArray(R.array.spinnerItems);
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int childCount = holder.scheduleLayout.getChildCount();
        if (childCount > 3) {
            holder.scheduleLayout.removeViewsInLayout(3, childCount - 3);
        }
        holder.schedule.setVisibility(View.VISIBLE);
        if (list.size() >= 1) {
            for (int j = 0; j < 7; j++) {
                View v = inflater.inflate(R.layout.row, null);
                TextView day = (TextView) v.findViewById(R.id.day);
                TextView time = (TextView) v.findViewById(R.id.time);
                String temp = new String();
                for (int k = 0; k < list.size(); k++) {
                    if (((Schedule) list.get(k)).day.equals(this.MONTH[j])) {
                        try {
                            temp = temp + new SimpleDateFormat("hh:mm a").format(new SimpleDateFormat("HH:mm").parse(((Schedule) list.get(k)).time)) + "\t\t";
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (temp.length() > 0) {
                    holder.schedule.setVisibility(View.GONE);
                    day.setText(LOCALE_DAY[j]);
                    time.setText(temp);
                    holder.scheduleLayout.addView(v);
                }
            }
        }
        ((View) holder.delete.getParent()).setId(i);
        setAnimation(holder.cv, i);
    }

    private String setType(String type) {
        if (type.equals("Lecture")) {
            return this.context.getResources().getString(R.string.lecture);
        }
        if (type.equals("Practical")) {
            return this.context.getResources().getString(R.string.practical);
        }
        if (type.equals("Seminar")) {
            return this.context.getResources().getString(R.string.seminar);
        }
        if (type.equals("Workshop")) {
            return this.context.getResources().getString(R.string.workshop);
        }
        return this.context.getResources().getString(R.string.exam);
    }

    public int getItemCount() {
        return this.subjects.size();
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > this.lastPosition) {
            viewToAnimate.startAnimation(AnimationUtils.loadAnimation(this.context, R.anim.card_animation));
            this.lastPosition = position;
        }
    }
}
