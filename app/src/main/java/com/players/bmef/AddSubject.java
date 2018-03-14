package com.players.bmef;

import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddSubject extends AppCompatActivity implements OnClickListener {
    Button add;
    Button addnew;
    RadioButton c1;
    RadioButton c2;
    RadioButton c3;
    RadioButton c4;
    RadioButton c5;
    int count = 0;
    String[] days = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    EditText etDepartment;
    EditText etDivision;
    EditText etRollFrom;
    EditText etRollTo;
    EditText etSubject;
    TextView etYear;
    FloatingActionButton fab;
    DataHandler handler = new DataHandler(this);
    int id;
    InputMethodManager imm;
    int index = 0;
    LinearLayout layout;
    Button minus;
    String preactivity;
    Toast f1546t;
    String time;
    String[] year = new String[]{"1st", "2nd", "3rd", "4th", "5th", "6th", "7th", "8th", "9th", "10th", "11th", "12th"};

    class C02111 implements OnClickListener {
        C02111() {
        }

        public void onClick(View v) {
            if (AddSubject.this.count < 99) {
                AddSubject.this.scheduleUIToggle();
            } else {
                Toast.makeText(AddSubject.this, "Sorry, you can't add more. You have reached a limit.", Toast.LENGTH_LONG).show();
            }
        }
    }

    class C02143 implements OnClickListener {
        C02143() {
        }

        public void onClick(View v) {
            ((View) v.getParent()).setVisibility(View.GONE);
            AddSubject.this.addnew.setVisibility(View.VISIBLE);
        }
    }

    class C02154 implements DialogInterface.OnClickListener {
        C02154() {
        }

        public void onClick(DialogInterface dialog, int which) {
            AddSubject.this.finish();
        }
    }

    class C02165 implements DialogInterface.OnClickListener {
        C02165() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    }

    class C02176 implements DialogInterface.OnClickListener {
        C02176() {
        }

        public void onClick(DialogInterface dialog, int which) {
            AddSubject.this.finish();
        }
    }

    class C02187 implements DialogInterface.OnClickListener {
        C02187() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    }

    class LoadSubjectDetails extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;
        ContentValues[] schedulevalues;
        ContentValues values;

        class C02191 implements Runnable {
            C02191() {
            }

            public void run() {
                DataHandler handler = new DataHandler(AddSubject.this);
                LoadSubjectDetails.this.values = handler.getSubjectInfo(AddSubject.this.id);
                LoadSubjectDetails.this.schedulevalues = handler.getScheduleData(AddSubject.this.id);
                AddSubject.this.etSubject.setText(LoadSubjectDetails.this.values.getAsString("subject_name"));
                AddSubject.this.etDepartment.setText(LoadSubjectDetails.this.values.getAsString("dept"));
                AddSubject.this.etDivision.setText(LoadSubjectDetails.this.values.getAsString("div"));
                String asString = LoadSubjectDetails.this.values.getAsString("type");
                int i = -1;
                switch (asString.hashCode()) {
                    case -651160879:
                        if (asString.equals("Seminar")) {
                            i = 2;
                            break;
                        }
                        break;
                    case 2174015:
                        if (asString.equals("Exam")) {
                            i = 4;
                            break;
                        }
                        break;
                    case 100406695:
                        if (asString.equals("Workshop")) {
                            i = 3;
                            break;
                        }
                        break;
                    case 1382598133:
                        if (asString.equals("Practical")) {
                            boolean z = true;
                            break;
                        }
                        break;
                    case 1717347678:
                        if (asString.equals("Lecture")) {
                            i = 0;
                            break;
                        }
                        break;
                }
                switch (i) {
                    case 0:
                        AddSubject.this.c1.setChecked(true);
                        break;
                    case 1:
                        AddSubject.this.c2.setChecked(true);
                        break;
                    case 2:
                        AddSubject.this.c3.setChecked(true);
                        break;
                    case 3:
                        AddSubject.this.c4.setChecked(true);
                        break;
                    case 4:
                        AddSubject.this.c5.setChecked(true);
                        break;
                }
                AddSubject.this.etYear.setText(LoadSubjectDetails.this.values.getAsString("year"));
                asString = LoadSubjectDetails.this.values.getAsString("year");
                i = -1;
                boolean z;
                switch (asString.hashCode()) {
                    case 50770:
                        if (asString.equals("1st")) {
                            i = 0;
                            break;
                        }
                        break;
                    case 51560:
                        if (asString.equals("2nd")) {
                            z = true;
                            break;
                        }
                        break;
                    case 52645:
                        if (asString.equals("3rd")) {
                            i = 2;
                            break;
                        }
                        break;
                    case 53672:
                        if (asString.equals("4th")) {
                            i = 3;
                            break;
                        }
                        break;
                    case 54633:
                        if (asString.equals("5th")) {
                            i = 4;
                            break;
                        }
                        break;
                    case 55594:
                        if (asString.equals("6th")) {
                            i = 5;
                            break;
                        }
                        break;
                    case 56555:
                        if (asString.equals("7th")) {
                            i = 6;
                            break;
                        }
                        break;
                    case 57516:
                        if (asString.equals("8th")) {
                            i = 7;
                            break;
                        }
                        break;
                    case 58477:
                        if (asString.equals("9th")) {
                            i = 8;
                            break;
                        }
                        break;
                    case 1509587:
                        if (asString.equals("10th")) {
                            i = 9;
                            break;
                        }
                        break;
                    case 1510548:
                        if (asString.equals("11th")) {
                            i = 10;
                            break;
                        }
                        break;
                    case 1511509:
                        if (asString.equals("12th")) {
                            i = 11;
                            break;
                        }
                        break;
                }
                switch (i) {
                    case 0:
                        AddSubject.this.index = 0;
                        break;
                    case 1:
                        AddSubject.this.index = 1;
                        break;
                    case 2:
                        AddSubject.this.index = 2;
                        break;
                    case 3:
                        AddSubject.this.index = 3;
                        break;
                    case 4:
                        AddSubject.this.index = 4;
                        break;
                    case 5:
                        AddSubject.this.index = 5;
                        break;
                    case 6:
                        AddSubject.this.index = 6;
                        break;
                    case 7:
                        AddSubject.this.index = 7;
                        break;
                    case 8:
                        AddSubject.this.index = 8;
                        break;
                    case 9:
                        AddSubject.this.index = 9;
                        break;
                    case 10:
                        AddSubject.this.index = 10;
                        break;
                    case 11:
                        AddSubject.this.index = 11;
                        break;
                }
                AddSubject.this.etRollFrom.setText(LoadSubjectDetails.this.values.getAsString("roll_from"));
                AddSubject.this.etRollTo.setText(LoadSubjectDetails.this.values.getAsString("roll_to"));
                for (int i2 = 1; i2 <= LoadSubjectDetails.this.schedulevalues.length; i2++) {
                    AddSubject.this.scheduleUIToggle();
                    Button b = (Button) AddSubject.this.findViewById(i2 + 100);
                    try {
                        b.setText(new SimpleDateFormat("hh:mm a").format(new SimpleDateFormat("HH:mm").parse(LoadSubjectDetails.this.schedulevalues[i2 - 1].getAsString("time"))));
                    } catch (Exception e) {
                        b.setText("12:30 PM");
                    }
                    Spinner s = (Spinner) AddSubject.this.findViewById(i2 + Callback.DEFAULT_DRAG_ANIMATION_DURATION);
                    asString = LoadSubjectDetails.this.schedulevalues[i2 - 1].getAsString("day");
                    i = -1;
                    switch (asString.hashCode()) {
                        case -2049557543:
                            if (asString.equals("Saturday")) {
                                i = 5;
                                break;
                            }
                            break;
                        case -1984635600:
                            if (asString.equals("Monday")) {
                                i = 0;
                                break;
                            }
                            break;
                        case -1807319568:
                            if (asString.equals("Sunday")) {
                                i = 6;
                                break;
                            }
                            break;
                        case -897468618:
                            if (asString.equals("Wednesday")) {
                                i = 2;
                                break;
                            }
                            break;
                        case 687309357:
                            if (asString.equals("Tuesday")) {
                                z = true;
                                break;
                            }
                            break;
                        case 1636699642:
                            if (asString.equals("Thursday")) {
                                i = 3;
                                break;
                            }
                            break;
                        case 2112549247:
                            if (asString.equals("Friday")) {
                                i = 4;
                                break;
                            }
                            break;
                    }
                    switch (i) {
                        case 0:
                            s.setSelection(0);
                            break;
                        case 1:
                            s.setSelection(1);
                            break;
                        case 2:
                            s.setSelection(2);
                            break;
                        case 3:
                            s.setSelection(3);
                            break;
                        case 4:
                            s.setSelection(4);
                            break;
                        case 5:
                            s.setSelection(5);
                            break;
                        case 6:
                            s.setSelection(6);
                            break;
                        default:
                            break;
                    }
                }
            }
        }

        LoadSubjectDetails() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
            this.pd = new ProgressDialog(AddSubject.this);
            this.pd.setMessage("Working...");
            this.pd.setCancelable(false);
            this.pd.show();
        }

        protected Void doInBackground(Void... params) {
            AddSubject.this.runOnUiThread(new C02191());
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            this.pd.cancel();
        }
    }

    class Subject extends AsyncTask<Void, Void, Void> {
        String dept;
        String div;
        ProgressDialog pd;
        int result;
        int rollfrom;
        int rollto;
        private ContentValues[] scheduleData;
        String subject_name;
        String type;
        String year;

        Subject() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
            this.pd = new ProgressDialog(AddSubject.this);
            this.pd.setMessage("Working...");
            this.pd.setCancelable(false);
            this.pd.show();
        }

        protected Void doInBackground(Void... params) {
            int c = getData();
            if (c == 1) {
                ContentValues values = new ContentValues();
                values.put("subject_name", this.subject_name);
                values.put("dept", this.dept);
                values.put("div", this.div);
                values.put("type", this.type);
                values.put("year", this.year);
                values.put("roll_from", Integer.valueOf(this.rollfrom));
                values.put("roll_to", Integer.valueOf(this.rollto));
                ContentValues[] schedvalues = getScheduleData();
                Intent resultIntent;
                if (AddSubject.this.preactivity.equals("UpdateSubject")) {
                    try {
                        AddSubject.this.handler.updateSubject(values, AddSubject.this.id, schedvalues);
                        resultIntent = new Intent();
                        AddSubject.this.setResult(-1, resultIntent);
                        resultIntent.putExtra("result", 2);
                        resultIntent.putExtra("position", AddSubject.this.getIntent().getIntExtra("position", 0));
                        AddSubject.this.finish();
                    } catch (Exception e) {
                        this.result = 3;
                        System.out.println(e);
                        e.printStackTrace();
                    }
                } else {
                    try {
                        ////////////////////-----capacity vadharva mate
                        if (AddSubject.this.handler.getSubjectsCount() >= 7) {
                            Snackbar.make(AddSubject.this.fab, (CharSequence) "You cant add more subjects. Buy Pro version!", -1).show();
                        } else {
                            AddSubject.this.handler.addSubject(values, schedvalues);
                            resultIntent = new Intent();
                            AddSubject.this.setResult(-1, resultIntent);
                            resultIntent.putExtra("result", 1);
                            AddSubject.this.finish();
                        }
                    } catch (Exception e2) {
                        this.result = 3;
                        e2.printStackTrace();
                    }
                }
            } else if (c == 2) {
                this.result = 4;
            } else {
                this.result = 5;
            }
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast toast;
            switch (this.result) {
                case 3:
                    toast = AddSubject.this.f1546t;
                    Toast.makeText(AddSubject.this, "Something went wrong!", Toast.LENGTH_LONG).show();
                    break;
                case 4:
                    toast = AddSubject.this.f1546t;
                    Toast.makeText(AddSubject.this, R.string.rollno_alert, Toast.LENGTH_LONG).show();
                    break;
                case 5:
                    toast = AddSubject.this.f1546t;
                    Toast.makeText(AddSubject.this, R.string.unfilled_alert, Toast.LENGTH_LONG).show();
                    break;
            }
            this.pd.cancel();
        }

        int getData() {
            if (AddSubject.this.etSubject.getText().toString().length() <= 0 || AddSubject.this.etDepartment.getText().toString().length() <= 0 || AddSubject.this.etDivision.getText().toString().length() <= 0 || AddSubject.this.etRollFrom.getText().toString().length() <= 0 || AddSubject.this.etRollTo.getText().toString().length() <= 0) {
                return 0;
            }
            this.subject_name = AddSubject.this.etSubject.getText().toString();
            this.dept = AddSubject.this.etDepartment.getText().toString();
            this.div = AddSubject.this.etDivision.getText().toString();
            if (AddSubject.this.c1.isChecked()) {
                this.type = "Lecture";
            } else if (AddSubject.this.c2.isChecked()) {
                this.type = "Practical";
            } else if (AddSubject.this.c3.isChecked()) {
                this.type = "Seminar";
            } else if (AddSubject.this.c4.isChecked()) {
                this.type = "Workshop";
            } else {
                this.type = "Exam";
            }
            this.year = AddSubject.this.etYear.getText().toString();
            this.rollfrom = Integer.parseInt(AddSubject.this.etRollFrom.getText().toString());
            this.rollto = Integer.parseInt(AddSubject.this.etRollTo.getText().toString());
            if (this.rollfrom >= this.rollto || this.rollfrom <= 0) {
                return 2;
            }
            return 1;
        }

        public ContentValues[] getScheduleData() {
            int actualcount = 0;
            System.out.println(AddSubject.this.count);
            for (int i = 1; i <= AddSubject.this.count; i++) {
                Button temp = (Button) AddSubject.this.findViewById(i + 100);
                View v = (View) temp.getParent();
                if (temp.getText().toString().length() > 0 && v.getVisibility() != View.GONE) {
                    actualcount++;
                }
            }
            ContentValues[] scheduleData = new ContentValues[actualcount];
            for (int j = 1; j <= actualcount; j++) {
                Button b = (Button) AddSubject.this.findViewById(j + 100);
                Spinner s = (Spinner) AddSubject.this.findViewById(j + Callback.DEFAULT_DRAG_ANIMATION_DURATION);
                if (((View) b.getParent()).getVisibility() != View.GONE) {
                    ContentValues c = new ContentValues();
                    try {
                        c.put("time", new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("hh:mm a").parse(b.getText().toString())));
                    } catch (Exception e) {
                        c.put("time", "0:0");
                        System.out.println("caught");
                    }
                    c.put("day", AddSubject.this.days[s.getSelectedItemPosition()]);
                    scheduleData[j - 1] = c;
                }
            }
            return scheduleData;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.add_subject_layout);
        this.time = new SimpleDateFormat("hh:mm aa").format(new Date());
        initUI();
        this.preactivity = getIntent().getExtras().getString("preactivity");
        if (this.preactivity.equals("UpdateSubject")) {
            getSupportActionBar().setTitle((int) R.string.edit_subject);
            this.id = getIntent().getExtras().getInt("id");
            new LoadSubjectDetails().execute(new Void[0]);
            return;
        }
        System.out.println("No");
    }

    private void initUI() {
        this.imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
     //   setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setTitle((int) R.string.add_subject);
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator((int) R.drawable.ic_close_white);
        ab.setDisplayHomeAsUpEnabled(true);
        this.fab = (FloatingActionButton) findViewById(R.id.fabsave);
        this.fab.setOnClickListener(this);
        this.layout = (LinearLayout) findViewById(R.id.schedule_layout);
        this.addnew = (Button) findViewById(R.id.addnew);
        this.addnew.setOnClickListener(new C02111());
        this.c1 = (RadioButton) findViewById(R.id.checkL);
        this.c2 = (RadioButton) findViewById(R.id.checkP);
        this.c3 = (RadioButton) findViewById(R.id.checkS);
        this.c4 = (RadioButton) findViewById(R.id.checkW);
        this.c5 = (RadioButton) findViewById(R.id.checkT);
        this.etSubject = (EditText) findViewById(R.id.etSubName);
        this.etDepartment = (EditText) findViewById(R.id.etDept);
        this.etDivision = (EditText) findViewById(R.id.etDiv);
        this.etYear = (TextView) findViewById(R.id.year);
        this.add = (Button) findViewById(R.id.inc);
        this.add.setOnClickListener(this);
        this.minus = (Button) findViewById(R.id.dec);
        this.minus.setOnClickListener(this);
        this.etRollFrom = (EditText) findViewById(R.id.etNoStudF);
        this.etRollTo = (EditText) findViewById(R.id.etNoStudTo);
    }

    private void scheduleUIToggle() {
        this.count++;
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams params = new LayoutParams(-1, -2);
        params.gravity = Gravity.CENTER_VERTICAL;
        params.weight = 1.0f;
        final Button b = new Button(this);
        b.setId(this.count + 100);
        b.setText(this.time);
        b.setLayoutParams(params);
        b.setOnClickListener(new OnClickListener() {

            class C02121 implements OnTimeSetListener {
                C02121() {
                }

                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    try {
                        b.setText(new SimpleDateFormat("hh:mm a").format(new SimpleDateFormat("HH:mm").parse(hourOfDay + ":" + minute)));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

            public void onClick(View v) {
                showTimePicker();
            }

            private void showTimePicker() {
                Calendar c = Calendar.getInstance();
                new TimePickerDialog(AddSubject.this, new C02121(), c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false).show();
            }
        });
        l.addView(b);
        Spinner s = new Spinner(this);
        s.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.spinnerItems)));
        s.setId(this.count + Callback.DEFAULT_DRAG_ANIMATION_DURATION);
        s.setLayoutParams(params);
        l.addView(s);
        LayoutParams params2 = new LayoutParams(-2, -2);
        params2.gravity = Gravity.CENTER_VERTICAL;
        params2.leftMargin = 10;
        params2.rightMargin = 10;
        ImageButton v = new ImageButton(this);
        v.setBackgroundResource(R.drawable.ic_close_white_24dp);
        v.setOnClickListener(new C02143());
        v.setLayoutParams(params2);
        l.addView(v);
        this.layout.addView(l);
    }

    public void onBackPressed() {
        Builder dialog = new Builder(this);
        dialog.setTitle(R.string.alert);
        dialog.setMessage(R.string.subject_save_alert);
        dialog.setNegativeButton(R.string.yes, new C02154());
        dialog.setPositiveButton(R.string.no, new C02165());
        dialog.show();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                Builder dialog = new Builder(this);
                dialog.setTitle(R.string.alert);
                dialog.setMessage(R.string.subject_save_alert);
                dialog.setNegativeButton(R.string.yes, new C02176());
                dialog.setPositiveButton(R.string.no, new C02187());
                dialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.fabsave) {
            this.imm.toggleSoftInput(0, 0);
            new Subject().execute(new Void[0]);
        }
        if (v.getId() == R.id.dec && this.index < 11) {
            this.index++;
            this.etYear.setText(this.year[this.index]);
        }
        if (v.getId() == R.id.inc && this.index > 0) {
            this.index--;
            this.etYear.setText(this.year[this.index]);
        }
    }
}
