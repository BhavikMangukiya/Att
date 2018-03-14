package com.players.bmef;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.internal.view.SupportMenu;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ShowRecords extends AppCompatActivity {
    Button f1547b;
    CoordinatorLayout coordinatorLayout;
    Button date;
    LinearLayout empty;
    Date end_date;
    FloatingActionButton fab;
    HorizontalScrollView hsv;
    int id;
    private Menu mymenu;
    int roll_from;
    int roll_to;
    Date start_date;
    Toolbar toolbar;
    private static final String path = "/sdcard/SmartAttendance/data/";

    class C02431 implements OnClickListener {
        C02431() {
        }

        public void onClick(View v) {
            DataHandler dataHandler = new DataHandler(ShowRecords.this);
            List<ContentValues> result = dataHandler.getAttendance2(ShowRecords.this.id, ShowRecords.this.start_date, ShowRecords.this.end_date);
            ContentValues values = dataHandler.getSubjectInfo(ShowRecords.this.id);
            String file_name = values.get("id") + "_" + values.get("subject_name") + "_" + values.get("dept") + "_" + values.get("div") + "_" + values.get("type");
            if (result.size() > 0) {
                ShowRecords.this.export(file_name, result);
            } else {
                Snackbar.make(v, (int) R.string.export_alert_1, -1).show();
            }
        }
    }

    class C02442 implements OnClickListener {
        C02442() {
        }

        public void onClick(View v) {
            new Activity().openContextMenu(v);
        }
    }

    class C02485 implements OnDateSetListener {
        C02485() {
        }

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Date s = new Date(year - 1900, monthOfYear, dayOfMonth, 0, 0, 0);
            Date e = new Date(year - 1900, monthOfYear, dayOfMonth, 23, 59, 59);
            final DataHandler dataHandler = new DataHandler(ShowRecords.this);
            final List<String> result = dataHandler.deleteCheck(ShowRecords.this.id, s, e);
            CharSequence[] columns = (String[]) result.toArray(new String[result.size()]);
            for (int i = 0; i < columns.length; i++) {
                try {
                    columns[i] = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a").format(new SimpleDateFormat("MMM_dd_yyyy_HH_mm_ss").parse((String) columns[i]));
                } catch (Exception e2) {
                }
            }
            Builder builder = new Builder(ShowRecords.this);
            builder.setTitle((CharSequence) "More than one record found. Select any one.");
            builder.setItems(columns, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (dataHandler.deleteAttendance(ShowRecords.this.id, ((String[]) result.toArray(new String[result.size()]))[which])) {
                        Snackbar.make(ShowRecords.this.coordinatorLayout, (int) R.string.attendance_deleted, 0).show();
                        if (ShowRecords.this.start_date != null && ShowRecords.this.end_date != null) {
                            new FetchAttendance().execute(new Date[]{ShowRecords.this.start_date, ShowRecords.this.end_date});
                            return;
                        }
                        return;
                    }
                    Snackbar.make(ShowRecords.this.coordinatorLayout, (CharSequence) "Something went wrong", -1).show();
                }
            });
            builder.setInverseBackgroundForced(true);
            builder.create();
            if (result.size() > 0) {
                builder.show();
            } else {
                Snackbar.make(ShowRecords.this.coordinatorLayout, (CharSequence) "No attendance was taken on this date.", -1).show();
            }
        }
    }

    class C02506 implements OnDateSetListener {
        C02506() {
        }

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            final List<String> result = new DataHandler(ShowRecords.this).deleteCheck(ShowRecords.this.id, new Date(year - 1900, monthOfYear, dayOfMonth, 0, 0, 0), new Date(year - 1900, monthOfYear, dayOfMonth, 23, 59, 59));
            CharSequence[] columns = (String[]) result.toArray(new String[result.size()]);
            for (int i = 0; i < columns.length; i++) {
                try {
                    columns[i] = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a").format(new SimpleDateFormat("MMM_dd_yyyy_HH_mm_ss").parse((String) columns[i]));
                } catch (Exception e) {
                }
            }
            if (result.size() > 0) {
                Builder builder = new Builder(ShowRecords.this);
                builder.setTitle((CharSequence) "More than one record found. Select any one.");
                builder.setItems(columns, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String[] original = (String[]) result.toArray(new String[result.size()]);
                        Cursor c = new DataHandler(ShowRecords.this).getMinMaxRollno(original[which], ShowRecords.this.id);
                        c.moveToFirst();
                        Bundle bundle = new Bundle();
                        bundle.putString("preactivity", "ShowRecords");
                        bundle.putInt("id", ShowRecords.this.id);
                        bundle.putInt("roll_from", c.getInt(0));
                        bundle.putInt("roll_to", c.getInt(1));
                        bundle.putString("column_parameter", original[which]);
                        Intent intent = new Intent(ShowRecords.this, AttendanceActivity.class);
                        intent.putExtras(bundle);
                        ShowRecords.this.startActivityForResult(intent, 7);
                    }
                });
                builder.setInverseBackgroundForced(true);
                builder.create();
                builder.show();
                return;
            }
            Snackbar.make(ShowRecords.this.coordinatorLayout, (CharSequence) "No attendance was taken on this date.", -1).show();
        }
    }

    public class FetchAttendance extends AsyncTask<Date, Void, Boolean> {
        ProgressDialog dialog;
        DataHandler handler = new DataHandler(ShowRecords.this);
        LinearLayout root;

        protected void onPreExecute() {
            super.onPreExecute();
            ShowRecords.this.hsv.removeAllViews();
            this.dialog = new ProgressDialog(ShowRecords.this);
            this.dialog.setMessage("Loading...");
            this.dialog.show();
        }

        protected Boolean doInBackground(Date... params) {
            List<ContentValues> result = this.handler.getAttendance2(ShowRecords.this.id, params[0], params[1]);
            if (result.size() <= 0) {
                return Boolean.valueOf(true);
            }
            fillTable(result);
            return Boolean.valueOf(false);
        }

        private void fillTable(List<ContentValues> result) {
            int j;
            int i;
            Cursor tempc = this.handler.getMinMaxRollno2(ShowRecords.this.id);
            tempc.moveToFirst();
            int min = tempc.getInt(0);
            int max = tempc.getInt(1);
            this.root = new LinearLayout(ShowRecords.this);
            this.root.setOrientation(LinearLayout.HORIZONTAL);
            View ws = new LinearLayout(ShowRecords.this);
            ws.setLayoutParams(new LayoutParams(4, -1));
            ws.setBackgroundColor(Color.parseColor("#FFFFFF"));
            this.root.addView(ws);
            int[] p = new int[(((ContentValues) result.get(0)).size() + 1)];
            int[] a = new int[(((ContentValues) result.get(0)).size() + 1)];
            LinearLayout l = new LinearLayout(ShowRecords.this);
            l.setOrientation(LinearLayout.VERTICAL);
            ContentValues c = (ContentValues) result.get(0);
            View view = new View(ShowRecords.this);
            view.setLayoutParams(new LayoutParams(-1, 4));
            view.setBackgroundColor(Color.parseColor("#FFFFFF"));
            l.addView(view);
            TextView cname = new TextView(ShowRecords.this);
            cname.setText("Roll no");
            cname.setPadding(30, 10, 30, 10);
            cname.setGravity(17);
            cname.setBackgroundColor(Color.parseColor("#607D8B"));
            l.addView(cname);
            for (j = min; j <= max; j++) {
                view = new View(ShowRecords.this);
                view.setLayoutParams(new LayoutParams(-1, 4));
                view.setBackgroundColor(Color.parseColor("#FFFFFF"));
                l.addView(view);
                TextView t = new TextView(ShowRecords.this);
                t.setText(Integer.toString(j));
                t.setPadding(30, 10, 30, 10);
                t.setGravity(17);
                t.setBackgroundColor(Color.parseColor("#607D8B"));
                l.addView(t);
            }
            view = new View(ShowRecords.this);
            view.setLayoutParams(new LayoutParams(-1, 4));
            view.setBackgroundColor(Color.parseColor("#FFFFFF"));
            l.addView(view);
            this.root.addView(l);
            for (i = 0; i < a.length; i++) {
                a[i] = 0;
                p[i] = 0;
            }
            TextView t;
            for (i = 0; i < result.size(); i++) {
                ws = new LinearLayout(ShowRecords.this);
                ws.setLayoutParams(new LayoutParams(4, -1));
                ws.setBackgroundColor(Color.parseColor("#FFFFFF"));
                this.root.addView(ws);
                l = new LinearLayout(ShowRecords.this);
                l.setOrientation(LinearLayout.VERTICAL);
                view = new View(ShowRecords.this);
                view.setLayoutParams(new LayoutParams(-1, 4));
                view.setBackgroundColor(Color.parseColor("#FFFFFF"));
                l.addView(view);
                c = (ContentValues) result.get(i);
                cname = new TextView(ShowRecords.this);
                try {
                    cname.setText(new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a").format(new SimpleDateFormat("MMM_dd_yyyy_HH_mm_ss").parse(c.getAsString("column_name"))));
                } catch (Exception e) {
                }
                cname.setPadding(30, 10, 30, 10);
                cname.setGravity(17);
                cname.setBackgroundColor(Color.parseColor("#607D8B"));
                l.addView(cname);
                for (j = 0; j < c.size() - 1; j++) {
                    view = new View(ShowRecords.this);
                    view.setLayoutParams(new LayoutParams(-1, 4));
                    view.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    l.addView(view);
                    t = new TextView(ShowRecords.this);
                    String temp = c.getAsString("values" + j);
                    t.setPadding(30, 10, 30, 10);
                    t.setGravity(17);
                    if (temp != null) {
                        if (temp.equals("P")) {
                            t.setBackgroundColor(Color.parseColor("#81C784"));
                            p[j] = p[j] + 1;
                        }
                        if (temp.equals("A")) {
                            t.setBackgroundColor(Color.parseColor("#EF5350"));
                            a[j] = a[j] + 1;
                            t.setTextColor(SupportMenu.CATEGORY_MASK);
                        }
                    }
                    l.addView(t);
                }
                view = new View(ShowRecords.this);
                view.setLayoutParams(new LayoutParams(-1, 4));
                view.setBackgroundColor(Color.parseColor("#FFFFFF"));
                l.addView(view);
                this.root.addView(l);
            }
            ws = new LinearLayout(ShowRecords.this);
            ws.setLayoutParams(new LayoutParams(4, -1));
            ws.setBackgroundColor(Color.parseColor("#FFFFFF"));
            this.root.addView(ws);
            l = new LinearLayout(ShowRecords.this);
            l.setOrientation(LinearLayout.VERTICAL);
            view = new View(ShowRecords.this);
            view.setLayoutParams(new LayoutParams(-1, 4));
            view.setBackgroundColor(Color.parseColor("#FFFFFF"));
            l.addView(view);
            c = (ContentValues) result.get(0);
            cname = new TextView(ShowRecords.this);
            cname.setText("Present count");
            cname.setPadding(30, 10, 30, 10);
            cname.setGravity(17);
            cname.setBackgroundColor(Color.parseColor("#607D8B"));
            l.addView(cname);
            for (j = 0; j < c.size() - 1; j++) {
                view = new View(ShowRecords.this);
                view.setLayoutParams(new LayoutParams(-1, 4));
                view.setBackgroundColor(Color.parseColor("#FFFFFF"));
                l.addView(view);
                t = new TextView(ShowRecords.this);
                t.setText(Integer.toString(p[j]));
                t.setPadding(30, 10, 30, 10);
                t.setGravity(17);
                t.setBackgroundColor(Color.parseColor("#607D8B"));
                l.addView(t);
            }
            view = new View(ShowRecords.this);
            view.setLayoutParams(new LayoutParams(-1, 4));
            view.setBackgroundColor(Color.parseColor("#FFFFFF"));
            l.addView(view);
            this.root.addView(l);
            ws = new LinearLayout(ShowRecords.this);
            ws.setLayoutParams(new LayoutParams(4, -1));
            ws.setBackgroundColor(Color.parseColor("#FFFFFF"));
            this.root.addView(ws);
            l = new LinearLayout(ShowRecords.this);
            l.setOrientation(LinearLayout.VERTICAL);
            c = (ContentValues) result.get(0);
            view = new View(ShowRecords.this);
            view.setLayoutParams(new LayoutParams(-1, 4));
            view.setBackgroundColor(Color.parseColor("#FFFFFF"));
            l.addView(view);
            cname = new TextView(ShowRecords.this);
            cname.setText("Absent count");
            cname.setPadding(30, 10, 30, 10);
            cname.setGravity(17);
            cname.setBackgroundColor(Color.parseColor("#607D8B"));
            l.addView(cname);
            for (j = 0; j < c.size() - 1; j++) {
                view = new View(ShowRecords.this);
                view.setLayoutParams(new LayoutParams(-1, 4));
                view.setBackgroundColor(Color.parseColor("#FFFFFF"));
                l.addView(view);
                t = new TextView(ShowRecords.this);
                t.setText(Integer.toString(a[j]));
                t.setPadding(30, 10, 30, 10);
                t.setGravity(17);
                t.setBackgroundColor(Color.parseColor("#607D8B"));
                l.addView(t);
            }
            view = new View(ShowRecords.this);
            view.setLayoutParams(new LayoutParams(-1, 4));
            view.setBackgroundColor(Color.parseColor("#FFFFFF"));
            l.addView(view);
            this.root.addView(l);
            ws = new LinearLayout(ShowRecords.this);
            ws.setLayoutParams(new LayoutParams(4, -1));
            ws.setBackgroundColor(Color.parseColor("#FFFFFF"));
            this.root.addView(ws);
            l = new LinearLayout(ShowRecords.this);
            l.setOrientation(LinearLayout.VERTICAL);
            c = (ContentValues) result.get(0);
            view = new View(ShowRecords.this);
            view.setLayoutParams(new LayoutParams(-1, 4));
            view.setBackgroundColor(Color.parseColor("#FFFFFF"));
            l.addView(view);
            cname = new TextView(ShowRecords.this);
            cname.setText("Percentage");
            cname.setPadding(30, 10, 30, 10);
            cname.setGravity(17);
            cname.setBackgroundColor(Color.parseColor("#607D8B"));
            l.addView(cname);
            for (j = 0; j < c.size() - 1; j++) {
                view = new View(ShowRecords.this);
                view.setLayoutParams(new LayoutParams(-1, 4));
                view.setBackgroundColor(Color.parseColor("#FFFFFF"));
                l.addView(view);
                t = new TextView(ShowRecords.this);
                t.setText(Double.toString(Math.floor(100.0d * ((double) (100.0f * (((float) p[j]) / ((float) (p[j] + a[j])))))) / 100.0d) + "%");
                t.setPadding(30, 10, 30, 10);
                t.setGravity(17);
                t.setBackgroundColor(Color.parseColor("#607D8B"));
                l.addView(t);
            }
            view = new View(ShowRecords.this);
            view.setLayoutParams(new LayoutParams(-1, 4));
            view.setBackgroundColor(Color.parseColor("#FFFFFF"));
            l.addView(view);
            this.root.addView(l);
            ws = new LinearLayout(ShowRecords.this);
            ws.setLayoutParams(new LayoutParams(4, -1));
            ws.setBackgroundColor(Color.parseColor("#FFFFFF"));
            this.root.addView(ws);
        }

        protected void onPostExecute(Boolean aVoid) {
            super.onPostExecute(aVoid);
            View vsv = (View) ShowRecords.this.hsv.getParent();
            if (aVoid.booleanValue()) {
                ShowRecords.this.empty.setVisibility(View.VISIBLE);
                vsv.setVisibility(View.GONE);
            } else {
                ShowRecords.this.empty.setVisibility(View.GONE);
                vsv.setVisibility(View.VISIBLE);
                ShowRecords.this.hsv.addView(this.root);
            }
            this.dialog.cancel();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.id = getIntent().getExtras().getInt("id");
        this.roll_from = getIntent().getExtras().getInt("roll_from");
        this.roll_to = getIntent().getExtras().getInt("roll_to");
        initUI();
    }

    private void initUI() {
        setContentView((int) R.layout.record_sheet_layout);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinateLayout);
        this.empty = (LinearLayout) findViewById(R.id.empty);
        this.hsv = (HorizontalScrollView) findViewById(R.id.hsv);
//        setSupportActionBar(this.toolbar);
        getSupportActionBar().setTitle((int) R.string.show_records);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.fab = (FloatingActionButton) findViewById(R.id.fab);
        this.fab.setOnClickListener(new C02431());
        this.date = (Button) findViewById(R.id.date_range);
        registerForContextMenu(this.date);
        this.date.setOnClickListener(new C02442());
        showAllTimeAttendance();
    }

    private void showAllTimeAttendance() {
        Calendar c = Calendar.getInstance();
        this.start_date = new Date(0, 0, 1, 0, 0, 0);
        this.end_date = new Date(c.get(Calendar.YEAR) - 1900, c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        this.date.setText(R.string.all_time);
        new FetchAttendance().execute(new Date[]{this.start_date, this.end_date});
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, 1, 1, R.string.today);
        menu.add(0, 2, 2, R.string.yesterday);
        menu.add(0, 3, 3, R.string.last_seven);
        menu.add(0, 4, 4, R.string.last_thirty);
        menu.add(0, 5, 5, R.string.all_time);
        menu.add(0, 6, 6, R.string.custom_range);
    }

    public boolean onContextItemSelected(MenuItem item) {
        Calendar c = Calendar.getInstance();
        StringBuilder sb = new StringBuilder();
        switch (item.getItemId()) {
            case 1:
                this.start_date = new Date(c.get(Calendar.YEAR) - 1900, c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
                this.end_date = new Date(c.get(Calendar.YEAR) - 1900, c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
                this.date.setText(R.string.today);
                new FetchAttendance().execute(new Date[]{this.start_date, this.end_date});
                break;
            case 2:
                c.add(Calendar.DAY_OF_MONTH, -1);
                this.start_date = new Date(c.get(Calendar.YEAR) - 1900, c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
                this.end_date = new Date(c.get(Calendar.YEAR) - 1900, c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
                this.date.setText(R.string.yesterday);
                new FetchAttendance().execute(new Date[]{this.start_date, this.end_date});
                break;
            case 3:
                c.add(Calendar.DAY_OF_MONTH, -1);
                this.end_date = new Date(c.get(Calendar.YEAR) - 1900, c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
                c.add(Calendar.DAY_OF_MONTH, -6);
                this.start_date = new Date(c.get(Calendar.YEAR) - 1900, c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
                sb.append(new SimpleDateFormat("MMM dd, yyyy").format(this.start_date) + " - " + new SimpleDateFormat("MMM dd, yyyy").format(this.end_date));
                this.date.setText(sb.toString());
                new FetchAttendance().execute(new Date[]{this.start_date, this.end_date});
                break;
            case 4:
                c.add(Calendar.DAY_OF_MONTH, -1);
                this.end_date = new Date(c.get(Calendar.YEAR) - 1900, c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
                c.add(Calendar.DAY_OF_MONTH, -29);
                this.start_date = new Date(c.get(Calendar.YEAR) - 1900, c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
                sb.append(new SimpleDateFormat("MMM dd, yyyy").format(this.start_date) + " - " + new SimpleDateFormat("MMM dd, yyyy").format(this.end_date));
                this.date.setText(sb.toString());
                new FetchAttendance().execute(new Date[]{this.start_date, this.end_date});
                break;
            case 5:
                this.start_date = new Date(0, 0, 1, 0, 0, 0);
                this.end_date = new Date(c.get(Calendar.YEAR) - 1900, c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
                this.date.setText(R.string.all_time);
                new FetchAttendance().execute(new Date[]{this.start_date, this.end_date});
                break;
            case 6:
                final Dialog d = new Dialog(this);
                d.setTitle(R.string.custom_range);
                d.setContentView(R.layout.date_range);
                d.show();
                Button cancel = (Button) d.findViewById(R.id.cancel);
                final DatePicker start = (DatePicker) d.findViewById(R.id.datePicker1);
                final DatePicker end = (DatePicker) d.findViewById(R.id.datePicker2);
                ((Button) d.findViewById(R.id.ok)).setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        ShowRecords.this.start_date = new Date(start.getYear() - 1900, start.getMonth(), start.getDayOfMonth(), 0, 0, 0);
                        ShowRecords.this.end_date = new Date(end.getYear() - 1900, end.getMonth(), end.getDayOfMonth(), 23, 59, 59);
                        if (ShowRecords.this.start_date.compareTo(ShowRecords.this.end_date) == -1) {
                            StringBuilder sb = new StringBuilder();
                            sb.append(new SimpleDateFormat("MMM dd, yyyy").format(ShowRecords.this.start_date) + " - " + new SimpleDateFormat("MMM dd, yyyy").format(ShowRecords.this.end_date));
                            ShowRecords.this.date.setText(sb.toString());
                            new FetchAttendance().execute(new Date[]{ShowRecords.this.start_date, ShowRecords.this.end_date});
                            d.cancel();
                            return;
                        }
                        Snackbar.make(ShowRecords.this.coordinatorLayout, (CharSequence) "Start date should be less than the end date!", -1).show();
                        d.cancel();
                    }
                });
                cancel.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        d.cancel();
                    }
                });
                break;
        }
        return super.onContextItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.show_records_menu, menu);
        this.mymenu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                finish();
                break;
            case R.id.edit:
                Calendar c1 = Calendar.getInstance();
                new DatePickerDialog(this, new C02506(), c1.get(Calendar.YEAR), c1.get(Calendar.MONTH), c1.get(Calendar.DAY_OF_MONTH)).show(); break;
            case R.id.delete:
                Calendar c = Calendar.getInstance();
                new DatePickerDialog(this, new C02485(), c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onPause() {
        super.onPause();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 7 && resultCode == -1) {
            new FetchAttendance().execute(new Date[]{this.start_date, this.end_date});
            Snackbar.make(this.coordinatorLayout, (int) R.string.attendance_updated, 0).show();
        }
    }

        private void export(String filename, List<ContentValues> contentValues) {
        final String path = Environment.getExternalStorageDirectory().toString() + "/SmartAttendance/export/";
        final String file_name = filename;
        File f = new File(path);
        if (!f.exists()) {
            f.mkdirs();
        }
        try {
            FileWriter writer = new FileWriter(new File(path + file_name + ".csv"), false);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Roll no,");
            int i = 0;
            while (i < contentValues.size()) {
                try {
                    stringBuilder.append(new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a").format(new SimpleDateFormat("MMM_dd_yyyy_HH_mm_ss").parse(((ContentValues) contentValues.get(i)).get("column_name").toString())) + ",");
                    i++;
                } catch (Exception e) {
                }
            }
            stringBuilder.append("Present count,Absent count,Percentage,");
            stringBuilder.append("\n");
            writer.append(stringBuilder.toString());
            Cursor cursor = new DataHandler(this).getMinMaxRollno2(this.id);
            cursor.moveToFirst();
            int min = cursor.getInt(0);
            for (i = 0; i < ((ContentValues) contentValues.get(0)).size() - 1; i++) {
                StringBuilder stringBuilder1 = new StringBuilder();
                stringBuilder1.append((i + min) + ",");
                int a = 0;
                int p = 0;
                for (int j = 0; j < contentValues.size(); j++) {
                    String temp = ((ContentValues) contentValues.get(j)).getAsString("values" + i);
                    if (temp != null) {
                        if (temp.equals("P")) {
                            p++;
                        }
                        if (temp.equals("A")) {
                            a++;
                        }
                    }
                    stringBuilder1.append(((ContentValues) contentValues.get(j)).get("values" + i) + ",");
                }
                stringBuilder1.append(p + "," + a + "," + (Math.floor(100.0d * ((double) (100.0f * (((float) p) / ((float) (p + a)))))) / 100.0d) + ",");
                stringBuilder1.append("\n");
                writer.append(stringBuilder1.toString());
            }
            writer.flush();
            writer.close();
            Snackbar.make(this.coordinatorLayout, getResources().getString(R.string.export_snack) + path, 0).setAction((CharSequence) "Open", new OnClickListener() {
                public void onClick(View v) {
                    Uri path = Uri.fromFile(new File(Environment.getExternalStorageDirectory().toString()+"/SmartAttendance/export/" + file_name + ".csv"));
                    Intent intent = new Intent("android.intent.action.VIEW");
                    intent.setFlags(67108864);
                    intent.setDataAndType(path, "text/csv");
                    try {
                        ShowRecords.this.startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        System.out.println();
                        Snackbar.make(ShowRecords.this.coordinatorLayout, (CharSequence) "Something went wrong!", -1).show();
                    }
                }
            }).show();
        } catch (Exception e2) {
        }
    }
}
