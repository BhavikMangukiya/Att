package com.players.bmef;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.players.bmef.DataHandler.Schedule;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MyFragment extends Fragment {
    Context context = getContext();
    String day;
    LinearLayout emptyView;

    public static class ScheduleDetails {
        String subject_name;
        String timings;

        ScheduleDetails(String subject_name, String timings) {
            this.subject_name = subject_name;
            this.timings = timings;
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.myfragment, container, false);
        this.day = getArguments().get("day").toString();
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        this.emptyView = (LinearLayout) v.findViewById(R.id.empty);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.context);
        List<ScheduleDetails> scheduleData = new ArrayList();
        DataHandler handler = new DataHandler(getActivity());
        List<Subject> subjects = handler.getAllSubjects2();
        for (int i = 0; i < subjects.size(); i++) {
            List<Schedule> data = handler.getScheduleDataNew(((Subject) subjects.get(i)).sid);
            StringBuilder temp = new StringBuilder();
            String subject_name = ((Subject) subjects.get(i)).subject_name;
            for (int j = 0; j < data.size(); j++) {
                if (((Schedule) data.get(j)).day.equals(this.day)) {
                    try {
                        temp.append(new SimpleDateFormat("hh:mm a").format(new SimpleDateFormat("HH:mm").parse(((Schedule) data.get(j)).time)) + "\t\t");
                    } catch (Exception e) {
                        temp.append("0:0\t\t");
                    }
                }
            }
            ScheduleDetails scheduleDetails = new ScheduleDetails(subject_name, temp.toString());
            if (temp.length() > 0) {
                scheduleData.add(scheduleDetails);
            }
        }
        recyclerView.setLayoutManager(linearLayoutManager);
        if (scheduleData.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            this.emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            this.emptyView.setVisibility(View.GONE);
            recyclerView.setAdapter(new ScheduleAdapter(scheduleData, this.context));
        }
        return v;
    }
}
