package com.players.bmef;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AttendanceActivity extends AppCompatActivity implements OnClickListener {
    String COLUMN_NAME;
    CardView card;
    CoordinatorLayout coordinatorLayout;
    String date_time;
    int flag = 0;
    ImageButton[] ib;
    int id;
    LinearLayout main;
    FrameLayout mainlayout;
    ScrollView mainsub;
    String[] month = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    int new_day;
    int new_hour;
    int new_minute;
    int new_month;
    int new_year;
    int[] op;
    String preactivity;
    int resultCode = 0;
    int rollFrom;
    int rollTo;
    int size;
    int c=0;

    class C02201 implements DialogInterface.OnClickListener {
        C02201() {
        }

        public void onClick(DialogInterface dialog, int which) {
            AttendanceActivity.this.finish();
        }
    }

    class C02212 implements DialogInterface.OnClickListener {
        C02212() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    }

    class C02223 implements DialogInterface.OnClickListener {
        C02223() {
        }

        public void onClick(DialogInterface dialog, int which) {
            AttendanceActivity.this.finish();
        }
    }

    class C02234 implements DialogInterface.OnClickListener {
        C02234() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    }

    class C02267 implements DialogInterface.OnClickListener {
        C02267() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    }

    public class AttendanceSaveOrUpdate extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog;

        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog = new ProgressDialog(AttendanceActivity.this);
            this.dialog.setMessage("Working..");
            this.dialog.show();
        }

        protected Void doInBackground(Void... params) {
            ContentValues values = new ContentValues();
            values.put("roll_from", Integer.valueOf(AttendanceActivity.this.rollFrom));
            values.put("roll_to", Integer.valueOf(AttendanceActivity.this.rollTo));
            values.put("id", Integer.valueOf(AttendanceActivity.this.id));
            DataHandler handler = new DataHandler(AttendanceActivity.this);
            if (AttendanceActivity.this.preactivity.equals("ShowRecords")) {
                try {
                    handler.updateAttendance(values, AttendanceActivity.this.op, AttendanceActivity.this.COLUMN_NAME);
                    AttendanceActivity.this.resultCode = 2;
                } catch (Exception e) {
                    AttendanceActivity.this.resultCode = 3;
                    System.out.println(e.getMessage());
                }
            } else {
                try {
                    handler.addAttendance(values, AttendanceActivity.this.op, AttendanceActivity.this.date_time);
                    AttendanceActivity.this.resultCode = 1;
                } catch (Exception e2) {
                    AttendanceActivity.this.resultCode = 3;
                    System.out.println(e2.getMessage());
                }
            }
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            this.dialog.cancel();
            switch (AttendanceActivity.this.resultCode) {
                case 3:
                    Snackbar.make(AttendanceActivity.this.coordinatorLayout, (CharSequence) "Seems like you have already taken attendance for selected date n time", 0).show();
                    break;
            }
            AttendanceActivity.this.setResult(-1, new Intent());
            AttendanceActivity.this.finish();
        }
    }

    public class UI extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog;

        class C02271 implements Runnable {
            C02271() {
            }

            public void run() {
                UI.this.initUI();
            }
        }

        class C02322 implements OnClickListener {

            class C02281 implements DialogInterface.OnClickListener {
                C02281() {
                }

                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            }

            class C02302 implements DialogInterface.OnClickListener {

                class C02291 implements Runnable {
                    C02291() {
                    }

                    public void run() {
                        new AttendanceSaveOrUpdate().execute(new Void[0]);
                    }
                }

                C02302() {
                }

                public void onClick(DialogInterface dialog, int which) {
                    AttendanceActivity.this.runOnUiThread(new C02291());
                }
            }

            class C02313 implements Runnable {
                C02313() {
                }

                public void run() {
                    new AttendanceSaveOrUpdate().execute(new Void[0]);
                }
            }

            C02322() {
            }

            public void onClick(View v) {
                if (AttendanceActivity.this.preactivity.equals("ShowRecords")) {
                    Builder mydialog = new Builder(AttendanceActivity.this);
                    mydialog.setTitle(R.string.alert);
                    mydialog.setMessage(R.string.attendance_update_alert);
                    mydialog.setPositiveButton(R.string.no, new C02281());
                    mydialog.setNegativeButton(R.string.yes, new C02302());
                    mydialog.show();
                    Toast.makeText(AttendanceActivity.this,c , Toast.LENGTH_LONG);
                    return ;
                }
                AttendanceActivity.this.runOnUiThread(new C02313());
            }
        }

        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog = new ProgressDialog(AttendanceActivity.this);
            this.dialog.setMessage("Working..");
            this.dialog.show();
        }

        protected Void doInBackground(Void... params) {
            AttendanceActivity.this.runOnUiThread(new C02271());
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            this.dialog.cancel();
        }

        public void initUI() {
            ((FloatingActionButton) AttendanceActivity.this.findViewById(R.id.save)).setOnClickListener(new C02322());
            AttendanceActivity.this.main = (LinearLayout) AttendanceActivity.this.findViewById(R.id.main);
            AttendanceActivity.this.size = AttendanceActivity.this.getResources().getDisplayMetrics().widthPixels;
            AttendanceActivity.this.ib = new ImageButton[AttendanceActivity.this.rollTo];
            AttendanceActivity.this.op = new int[AttendanceActivity.this.rollTo];
            int sublay = (AttendanceActivity.this.rollTo - AttendanceActivity.this.rollFrom) / 8;
            int s1 = AttendanceActivity.this.rollFrom - 1;
            int s2 = AttendanceActivity.this.rollFrom - 1;
            for (int i = 0; i <= sublay; i++) {
                LinearLayout buttonlay = new LinearLayout(AttendanceActivity.this);
                buttonlay.setOrientation(LinearLayout.HORIZONTAL);
                buttonlay.setGravity(1);
                AttendanceActivity.this.main.addView(buttonlay);
                int j = s1;
                while (j < AttendanceActivity.this.rollTo) {
                    if (j == s1 + 8 && j != 0 && j != s1) {
                        s1 = j;
                        break;
                    }
                    AttendanceActivity.this.ib[j] = new ImageButton(AttendanceActivity.this);
                    AttendanceActivity.this.ib[j].setPadding(2, 0, 2, 0);
                    AttendanceActivity.this.ib[j].setOnClickListener(AttendanceActivity.this);
                    AttendanceActivity.this.ib[j].setId(j);
                    AttendanceActivity.this.ib[j].setBackgroundResource(R.drawable.pr);
                    AttendanceActivity.this.ib[j].setLayoutParams(new LayoutParams(AttendanceActivity.this.size / 9, AttendanceActivity.this.size / 9));
                    buttonlay.addView(AttendanceActivity.this.ib[j]);
                    j++;
                }
                LinearLayout rolllay = new LinearLayout(AttendanceActivity.this);
                rolllay.setOrientation(LinearLayout.HORIZONTAL);
                rolllay.setGravity(1);
                AttendanceActivity.this.main.addView(rolllay);
                int k = s2;
                while (k < AttendanceActivity.this.rollTo) {
                    if (k == s2 + 8 && k != 0 && k != s2) {
                        s2 = k;
                        break;
                    }
                    TextView temp = new TextView(AttendanceActivity.this);
                    temp.setGravity(17);
                    temp.setTextSize(16.0f);
                    temp.setPadding(2, 0, 2, 2);
                    temp.setLayoutParams(new LayoutParams(AttendanceActivity.this.size / 9, AttendanceActivity.this.size / 9));
                    temp.setText(Integer.toString(k + 1));
                    rolllay.addView(temp);
                    k++;
                }
            }
            View view = new View(AttendanceActivity.this);
            view.setLayoutParams(new LayoutParams(-1, 130));
            AttendanceActivity.this.main.addView(view);
            if (AttendanceActivity.this.preactivity.equals("ShowRecords")) {
                AttendanceActivity.this.getSupportActionBar().setTitle((int) R.string.edit_attendance);
                loadAttendance();
            }
        }

        private void loadAttendance() {
            int[] result = new DataHandler(AttendanceActivity.this).getSingleDateAttendance(AttendanceActivity.this.id, AttendanceActivity.this.COLUMN_NAME);
            int rindex = 0;
            for (int j = AttendanceActivity.this.rollFrom - 1; j < AttendanceActivity.this.rollTo; j++) {
                AttendanceActivity.this.op[j] = result[rindex];
                rindex++;
            }
            for (int i = AttendanceActivity.this.rollFrom - 1; i < AttendanceActivity.this.rollTo; i++) {
                if (AttendanceActivity.this.op[i] == 0) {
                    AttendanceActivity.this.ib[i].setBackgroundResource(R.drawable.pr);
                } else {
                    AttendanceActivity.this.ib[i].setBackgroundResource(R.drawable.ab);
                }
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.attendance_layout);
     //   setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setTitle((int) R.string.take_attendance);
        this.coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinateLayout);
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator((int) R.drawable.ic_close_white);
        ab.setDisplayHomeAsUpEnabled(true);
        Bundle basket = getIntent().getExtras();
        this.rollFrom = basket.getInt("roll_from");
        this.rollTo = basket.getInt("roll_to");
        System.out.println(this.rollFrom + "\t\t" + this.rollTo);
        this.id = basket.getInt("id");
        this.preactivity = basket.getString("preactivity");
        this.COLUMN_NAME = basket.getString("column_parameter");
        new UI().execute(new Void[0]);
    }

    public void onBackPressed() {
        Builder dialog = new Builder(this);
        dialog.setTitle(R.string.alert);
        dialog.setMessage(R.string.attendance_save_alert);
        dialog.setNegativeButton(R.string.yes, new C02201());
        dialog.setPositiveButton(R.string.no, new C02212());
        dialog.show();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.attendance_menu, menu);
        if (this.preactivity.equals("ShowRecords")) {
            menu.removeItem(R.id.datetime);
        }
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int i;
        switch (item.getItemId()) {
            case 16908332:
                Builder dialog = new Builder(this);
                dialog.setTitle(R.string.alert);
                dialog.setMessage(R.string.attendance_save_alert);
                dialog.setNegativeButton(R.string.yes, new C02223());
                dialog.setPositiveButton(R.string.no, new C02234());
                dialog.show();
                break;
            case R.id.datetime:
                final Dialog d = new Dialog(this);
                d.setTitle(R.string.select_date_time);
                d.setContentView(R.layout.datetimepicker);
                final DatePicker datePicker = (DatePicker) d.findViewById(R.id.datePicker);
                final TimePicker timePicker = (TimePicker) d.findViewById(R.id.timePicker);
                Button set = (Button) d.findViewById(R.id.set);
                Button cancel = (Button) d.findViewById(R.id.cancel);
                if (this.flag == 1) {
                    timePicker.setCurrentHour(Integer.valueOf(this.new_hour));
                    timePicker.setCurrentMinute(Integer.valueOf(this.new_minute));
                    datePicker.updateDate(this.new_year, this.new_month, this.new_day);
                }
                set.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                     //   c++;
                        AttendanceActivity.this.new_hour = timePicker.getCurrentHour().intValue();
                        AttendanceActivity.this.new_minute = timePicker.getCurrentMinute().intValue();
                        AttendanceActivity.this.new_year = datePicker.getYear();
                        AttendanceActivity.this.new_month = datePicker.getMonth();
                        AttendanceActivity.this.new_day = datePicker.getDayOfMonth();
                        Calendar c = Calendar.getInstance();
                        c.set(Calendar.YEAR, AttendanceActivity.this.new_year);
                        c.set(Calendar.MONTH, AttendanceActivity.this.new_month);
                        c.set(Calendar.DAY_OF_MONTH, AttendanceActivity.this.new_day);
                        c.set(Calendar.HOUR_OF_DAY, AttendanceActivity.this.new_hour);
                        c.set(Calendar.SECOND, AttendanceActivity.this.new_minute);
                        Date date = c.getTime();
                        AttendanceActivity.this.date_time = new SimpleDateFormat("MMM_dd_yyyy_HH_mm_ss").format(date);
                        AttendanceActivity.this.showDateDialog(new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a").format(date));
                        AttendanceActivity.this.flag = 1;
                        d.cancel();
                    }
                });
                cancel.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        Snackbar.make(AttendanceActivity.this.coordinatorLayout, (int) R.string.discarded, -1).show();
                        d.cancel();
                    }
                });
                d.show();
                break;
            case R.id.action_mark_all:
                for (i = this.rollFrom - 1; i < this.rollTo; i++) {
                    this.ib[i].setBackgroundResource(R.drawable.pr);
                    this.op[i] = 0;
                }
                Snackbar.make(this.coordinatorLayout, (int) R.string.mark_p, -1).show();
                break;
            case R.id.action_clear_all:
                for (i = this.rollFrom - 1; i < this.rollTo; i++) {
                    this.ib[i].setBackgroundResource(R.drawable.ab);
                    this.op[i] = 1;
                }
                Snackbar.make(this.coordinatorLayout, (int) R.string.mark_a, -1).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDateDialog(String temp) {
        Builder mydialog = new Builder(this);
        mydialog.setTitle(R.string.current_date_time);
        mydialog.setMessage(temp);
        mydialog.setPositiveButton("OK", new C02267());
        mydialog.show();
    }

    public void onClick(View arg0) {
        int id = arg0.getId();
        ImageButton temp = (ImageButton) findViewById(id);
        if (this.op[id] % 2 == 0) {
            this.op[id] = 1;
            temp.setBackgroundResource(R.drawable.ab);
            return;
        }
        this.op[id] = 0;
        temp.setBackgroundResource(R.drawable.pr);
    }
}
