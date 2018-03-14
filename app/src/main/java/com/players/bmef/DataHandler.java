package com.players.bmef;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ArrayAdapter;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "data";
    private static final String DATABASE_PATH = "/sdcard/SmartAttendance/data/";
    private static final int DATABASE_VERSION = 1;
    private int allIds;
    private int subjectsCount;
    private ContentValues[] todaysSubjects;

    public class Schedule {
        String day;
        String time;

        Schedule(String day, String time) {
            this.day = day;
            this.time = time;
        }
    }

    public DataHandler(Context context) {
        super(context, "/sdcard/SmartAttendance/data/data", null, 1);
        new File(DATABASE_PATH).mkdirs();
        SQLiteDatabase.openOrCreateDatabase("/sdcard/SmartAttendance/data/data", null);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE subjects (id INTEGER PRIMARY KEY AUTOINCREMENT,subject_name TEXT,dept TEXT,div TEXT,type TEXT,year TEXT,roll_from INTEGER,roll_to INTEGER)");
       // db.execSQL("CREATE TABLE bajil(id INTEGER PRIMARY KEY,pass INTEGER,passques INTEGER, passans TEXT,signedup INTEGER,firstrun INTEGER)");
       // db.execSQL("INSERT INTO BAJIL (id,pass,passques,passans,signedup,firstrun) values(1,1234,0,'answer',0,1)");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS subjects");
       // db.execSQL("DROP TABLE IF EXISTS BAJIL");
        onCreate(db);
    }
    //------------>>>>>>>>>SECURITY MATE DATABSE SECTION<<<<<<<<<<<<<<<<<<<<----------------


   /* public int getpass() {
        SQLiteDatabase database = getReadableDatabase();
        Cursor c = database.rawQuery("select pass from BAJIL where id=1", null);
        if (c.getCount() <= 0 || !c.moveToFirst()) {
            return 0;
        }
        database.close();
        return c.getInt(0);
    }
    public int getQues() {
        SQLiteDatabase database = getReadableDatabase();
        Cursor c = database.rawQuery("select passques from BAJIL where id=1", null);
        if (c.getCount() <= 0 || !c.moveToFirst()) {
            return 0;
        }
        database.close();
        return c.getInt(0);
    }

    public String getAns() {
        SQLiteDatabase database = getReadableDatabase();
        Cursor c = database.rawQuery("select passans from BAJIL where id=1", null);
        if (c.getCount() <= 0 || !c.moveToFirst()) {
            return "Question not found!";
        }
        database.close();
        return c.getString(0);
    }

    public int getFirstRun() {
        SQLiteDatabase database = getReadableDatabase();
        Cursor c = database.rawQuery("select firstrun from BAJIL where id=1", null);
        if (c.getCount() <= 0 || !c.moveToFirst()) {
            return 0;
        }
        database.close();
        return c.getInt(0);
    }

    public void setPass(int a) {
        getWritableDatabase().execSQL("UPDATE BAJIL SET pass=" + a + " where id=1");
    }

    public void setQues(int a) {
        getWritableDatabase().execSQL("UPDATE BAJIL SET passques=" + a + " where id=1");
    }

    public void setAns(String a) {
        getWritableDatabase().execSQL("UPDATE BAJIL SET passans='" + a + "' where id=1");
    }

    public void setSignedUp(int a) {
        getWritableDatabase().execSQL("UPDATE BAJIL SET signedup='" + a + "' where id=1");
    }

    public void setFirstRun(int a) {
        getWritableDatabase().execSQL("UPDATE BAJIL SET firstrun='" + a + "' where id=1");
    }

    public int getSignedUp() {
        SQLiteDatabase database = getReadableDatabase();
        Cursor c = database.rawQuery("select signedup from BAJIL where id=1", null);
        if (c.getCount() <= 0 || !c.moveToFirst()) {
            return 0;
        }
        database.close();
        return c.getInt(0);
    }*/
    public void addSubject(ContentValues values, ContentValues[] schedvalues) {
        SQLiteDatabase db = getWritableDatabase();
        db.insert("subjects", null, values);
        Cursor c = db.rawQuery("SELECT MAX(id) FROM subjects", null);
        c.moveToFirst();
        int id = c.getInt(0);
        String ATTENDANCE_TABLE = "A_" + id;
        db.execSQL("CREATE TABLE " + ATTENDANCE_TABLE + " (roll_no INTEGER PRIMARY KEY)");
        int roll_from = values.getAsInteger("roll_from").intValue();
        int roll_to = values.getAsInteger("roll_to").intValue();
        for (int i = roll_from; i <= roll_to; i++) {
            db.execSQL("INSERT INTO " + ATTENDANCE_TABLE + " values(" + i + ")");
        }
        String SCHEDULE_TABLE = "S_" + id;
        db.execSQL("CREATE TABLE " + SCHEDULE_TABLE + " (day TEXT,time TEXT)");
        for (ContentValues insert : schedvalues) {
            db.insert(SCHEDULE_TABLE, null, insert);
        }
        db.close();
    }

    public void updateSubject(ContentValues values, int id, ContentValues[] schedvalues) throws Exception {
        int i;
        SQLiteDatabase db = getReadableDatabase();
        String ATTENDANCE_TABLE_NAME = "A_" + id;
        Cursor c = db.rawQuery("SELECT MIN(roll_no),MAX(roll_no) FROM " + ATTENDANCE_TABLE_NAME, null);
        c.moveToFirst();
        int old_roll_from = c.getInt(0);
        int old_roll_to = c.getInt(1);
        int new_roll_from = values.getAsInteger("roll_from").intValue();
        int new_roll_to = values.getAsInteger("roll_to").intValue();
        db.close();
        SQLiteDatabase db2 = getWritableDatabase();
        db2.update("subjects", values, "id =" + id, null);
        if (old_roll_from > new_roll_from) {
            for (i = new_roll_from; i < old_roll_from; i++) {
                db2.execSQL("INSERT INTO " + ATTENDANCE_TABLE_NAME + " (roll_no) values(" + i + ")");
            }
        }
        if (old_roll_to < new_roll_to) {
            for (i = old_roll_to + 1; i <= new_roll_to; i++) {
                db2.execSQL("INSERT INTO " + ATTENDANCE_TABLE_NAME + " (roll_no) values(" + i + ")");
            }
        }
        String SCHEDULE_TABLE = "S_" + id;
        db2.execSQL("DELETE FROM " + SCHEDULE_TABLE);
        for (ContentValues insert : schedvalues) {
            db2.insert(SCHEDULE_TABLE, null, insert);
        }
        db2.close();
    }

    public void deleteSubject(int id) throws Exception {
        SQLiteDatabase db = getReadableDatabase();
        db.rawQuery("SELECT type from subjects where id=" + id, null).moveToFirst();
        String ATTENDANCE_TABLE_NAME = "A_" + id;
        String SCHEDULE_TABLE_NAME = "S_" + id;
        db.close();
        SQLiteDatabase db2 = getWritableDatabase();
        db2.execSQL("DROP TABLE " + ATTENDANCE_TABLE_NAME);
        db2.execSQL("DROP TABLE " + SCHEDULE_TABLE_NAME);
        db2.execSQL("DELETE FROM subjects where id=" + id);
        db2.close();
    }

    public ContentValues[] getAllSubjects() {
        SQLiteDatabase db = getReadableDatabase();
        int i = 0;
        Cursor c = db.rawQuery("select * from subjects", null);
        ContentValues[] allSubjects = new ContentValues[c.getCount()];
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                ContentValues temp = new ContentValues();
                temp.put("id", Integer.valueOf(c.getInt(0)));
                temp.put("subject_name", c.getString(1));
                temp.put("dept", c.getString(2));
                temp.put("div", c.getString(3));
                temp.put("type", c.getString(4));
                temp.put("year", c.getString(5));
                temp.put("roll_from", Integer.valueOf(c.getInt(6)));
                temp.put("roll_to", Integer.valueOf(c.getInt(7)));
                allSubjects[i] = temp;
                i++;
            } while (c.moveToNext());
        }
        db.close();
        return allSubjects;
    }

    public List<Subject> getAllSubjects2() {
        List<Subject> list = new ArrayList();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("select * from subjects", null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                list.add(new Subject(c.getInt(0), c.getInt(6), c.getInt(7), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5)));
            } while (c.moveToNext());
        }
        db.close();
        return list;
    }

    public ContentValues getSubjectInfo(int id) {
        ContentValues subjectInfo = new ContentValues();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("select id,subject_name,type,roll_from,roll_to,dept,div,year from subjects where id = " + id, null);
        c.moveToFirst();
        subjectInfo.put("id", Integer.valueOf(c.getInt(0)));
        subjectInfo.put("subject_name", c.getString(1));
        subjectInfo.put("type", c.getString(2));
        subjectInfo.put("roll_from", Integer.valueOf(c.getInt(3)));
        subjectInfo.put("roll_to", Integer.valueOf(c.getInt(4)));
        subjectInfo.put("dept", c.getString(5));
        subjectInfo.put("div", c.getString(6));
        subjectInfo.put("year", c.getString(7));
        db.close();
        return subjectInfo;
    }

    public void addAttendance(ContentValues values, int[] op, String date_time) throws Exception {
        String COLUMN_NAME;
        SQLiteDatabase db = getWritableDatabase();
        int roll_from = values.getAsInteger("roll_from").intValue();
        int roll_to = values.getAsInteger("roll_to").intValue();
        String ATTENDANCE_TABLE_NAME = "A_" + values.getAsInteger("id").intValue();
        if (date_time == null) {
            COLUMN_NAME = new SimpleDateFormat("MMM_dd_yyyy_HH_mm_ss").format(new Date());
        } else {
            COLUMN_NAME = date_time;
        }
        try {
            db.execSQL("ALTER TABLE " + ATTENDANCE_TABLE_NAME + " ADD COLUMN " + COLUMN_NAME + " TEXT");
            for (int i = roll_from; i <= roll_to; i++) {
                if (op[i - 1] == 0) {
                    db.execSQL("UPDATE " + ATTENDANCE_TABLE_NAME + " SET " + COLUMN_NAME + "='P'" + "where roll_no=" + i);
                } else {
                    db.execSQL("UPDATE " + ATTENDANCE_TABLE_NAME + " SET " + COLUMN_NAME + "='A'" + "where roll_no=" + i);
                }
            }
            db.close();
        } catch (SQLiteException e) {
            db.close();
            throw e;
        }
    }

    public ArrayAdapter<String> getAttendanceDates(Context context, int id) {
        String ATTENDANCE_TABLE_NAME = "A_" + id;
        ArrayAdapter<String> adapter = new ArrayAdapter(context, R.layout.spinner_text, R.id.textView7);
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + ATTENDANCE_TABLE_NAME, null);
        adapter.add("All");
        for (int i = 1; i < c.getColumnCount(); i++) {
            adapter.add(c.getColumnName(i));
        }
        db.close();
        return adapter;
    }

    public ContentValues[] getAttendance(int id, String column_parameter) {
        String ATTENDANCE_TABLE_NAME = "A_" + id;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c;
        ContentValues[] values;
        ContentValues header;
        int i;
        int rowpointer;
        ContentValues row;
        int j;
        if (column_parameter.equals("All")) {
            c = db.rawQuery("SELECT * FROM " + ATTENDANCE_TABLE_NAME + " ORDER BY roll_no", null);
            values = new ContentValues[(c.getCount() + 1)];
            c.moveToFirst();
            header = new ContentValues();
            for (i = 0; i < c.getColumnCount(); i++) {
                header.put("column" + i, c.getColumnName(i));
            }
            System.out.println();
            values[0] = header;
            rowpointer = 1;
            do {
                row = new ContentValues();
                for (j = 0; j < c.getColumnCount(); j++) {
                    row.put("column" + j, c.getString(j));
                }
                System.out.println();
                values[rowpointer] = row;
                rowpointer++;
            } while (c.moveToNext());
            db.close();
            return values;
        }
        c = db.rawQuery("SELECT roll_no," + column_parameter + " FROM " + ATTENDANCE_TABLE_NAME + " ORDER BY roll_no", null);
        values = new ContentValues[(c.getCount() + 1)];
        c.moveToFirst();
        header = new ContentValues();
        for (i = 0; i < c.getColumnCount(); i++) {
            header.put("column" + i, c.getColumnName(i));
        }
        values[0] = header;
        rowpointer = 1;
        do {
            row = new ContentValues();
            for (j = 0; j < c.getColumnCount(); j++) {
                row.put("column" + j, c.getString(j));
            }
            values[rowpointer] = row;
            rowpointer++;
        } while (c.moveToNext());
        db.close();
        return values;
    }

    public List<ContentValues> getAttendance2(int id, Date s, Date e) {
        String ATTENDANCE_TABLE_NAME = "A_" + id;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + ATTENDANCE_TABLE_NAME + " ORDER BY roll_no", null);
        List<ContentValues> list = new ArrayList();
        for (int i = 1; i < c.getColumnCount(); i++) {
            c.moveToFirst();
            try {
                Date d = new SimpleDateFormat("MMM_dd_yyyy_hh_mm_ss").parse(c.getColumnName(i));
                Date end = e;
                if (d.compareTo(s) >= 0 && d.compareTo(end) <= 0) {
                    System.out.println(c.getColumnName(i));
                    ContentValues temp = new ContentValues();
                    temp.put("column_name", c.getColumnName(i));
                    for (int j = 0; j < c.getCount(); j++) {
                        temp.put("values" + j, c.getString(i));
                        c.moveToNext();
                    }
                    list.add(temp);
                }
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
        db.close();
        return list;
    }

    public int[] getSingleDateAttendance(int id, String COLUMN_NAME) {
        String ATTENDANCE_TABLE_NAME = "A_" + id;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT " + COLUMN_NAME + " FROM " + ATTENDANCE_TABLE_NAME, null);
        c.moveToFirst();
        int[] singleDateAttendance = new int[c.getCount()];
        int i = 0;
        do {
            if (c.getString(0) != null) {
                String temp = c.getString(0);
                if (temp.equals("P")) {
                    singleDateAttendance[i] = 0;
                } else if (temp.equals("A")) {
                    singleDateAttendance[i] = 1;
                } else {
                    singleDateAttendance[i] = 0;
                }
                i++;
            }
        } while (c.moveToNext());
        db.close();
        return singleDateAttendance;
    }

    public void updateAttendance(ContentValues values, int[] op, String COLUMN_NAME) {
        SQLiteDatabase db = getWritableDatabase();
        int roll_from = values.getAsInteger("roll_from").intValue();
        int roll_to = values.getAsInteger("roll_to").intValue();
        String ATTENDANCE_TABLE_NAME = "A_" + values.getAsInteger("id").intValue();
        String COLUMN_PARAMETER = COLUMN_NAME;
        int i = roll_from;
        while (i <= roll_to) {
            try {
                if (op[i - 1] == 0) {
                    db.execSQL("UPDATE " + ATTENDANCE_TABLE_NAME + " SET " + COLUMN_PARAMETER + "='P'" + "where roll_no=" + i);
                } else {
                    db.execSQL("UPDATE " + ATTENDANCE_TABLE_NAME + " SET " + COLUMN_PARAMETER + "='A'" + "where roll_no=" + i);
                }
                i++;
            } catch (SQLiteException e) {
                db.close();
                throw e;
            }
        }
        db.close();
    }

    public Cursor getMinMaxRollno(String COLUMN_PARAMETER, int id) {
        return getReadableDatabase().rawQuery("SELECT MIN(roll_no),MAX(roll_no)  FROM " + ("A_" + id) + " WHERE " + COLUMN_PARAMETER + "='A' OR " + COLUMN_PARAMETER + "='P'", null);
    }

    public Cursor getMinMaxRollno2(int id) {
        return getReadableDatabase().rawQuery("SELECT MIN(roll_no),MAX(roll_no)  FROM " + ("A_" + id), null);
    }

    public List<Schedule> getScheduleDataNew(int id) {
        List<Schedule> list = new ArrayList();
        String SCHEDULE_TABLE = "S_" + id;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + SCHEDULE_TABLE, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            do {
                list.add(new Schedule(cursor.getString(0), cursor.getString(1)));
            } while (cursor.moveToNext());
        }
        db.close();
        return list;
    }

    public ContentValues[] getScheduleData(int id) {
        String SCHEDULE_TABLE = "S_" + id;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + SCHEDULE_TABLE, null);
        cursor.moveToFirst();
        ContentValues[] values = new ContentValues[cursor.getCount()];
        if (cursor.getCount() > 0) {
            int i = 0;
            do {
                ContentValues temp = new ContentValues();
                temp.put("day", cursor.getString(0));
                temp.put("time", cursor.getString(1));
                values[i] = temp;
                i++;
            } while (cursor.moveToNext());
        }
        db.close();
        return values;
    }

    public int[] getAllIds() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM subjects", null);
        cursor.moveToFirst();
        int[] allIds = new int[cursor.getCount()];
        int i = 0;
        if (cursor.getCount() > 0) {
            do {
                allIds[i] = cursor.getInt(0);
                i++;
            } while (cursor.moveToNext());
        }
        db.close();
        return allIds;
    }

    public ContentValues getScheduleDetails(int id) {
        ContentValues values = new ContentValues();
        SQLiteDatabase db = getReadableDatabase();
        String SCHEDULE_TABLE = "S_" + id;
        Cursor cursor = db.rawQuery("SELECT time FROM " + SCHEDULE_TABLE + " WHERE day='" + new SimpleDateFormat("EEEE").format(new Date()) + "'", null);
        String time = new String();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                try {
                    Date d = new SimpleDateFormat("hh:mm").parse(cursor.getString(0));
                    int hours = d.getHours();
                    int minutes = d.getMinutes();
                    if (hours < 12 || hours >= 23) {
                        time = time + hours + ":" + minutes + " AM" + "\t\t";
                    } else {
                        if (hours - 12 != 0) {
                            hours -= 12;
                        }
                        time = time + hours + ":" + minutes + " PM" + "\t\t";
                    }
                } catch (Exception e) {
                }
            } while (cursor.moveToNext());
            values.put("time", time);
        }
        db.close();
        return values;
    }

    public String[] getScheduleDetailsv2(int id) {
        ContentValues values = new ContentValues();
        SQLiteDatabase db = getReadableDatabase();
        String SCHEDULE_TABLE = "S_" + id;
        Cursor cursor = db.rawQuery("SELECT time FROM " + SCHEDULE_TABLE + " WHERE day='" + new SimpleDateFormat("EEEE").format(new Date()) + "'", null);
        String[] time = new String[cursor.getCount()];
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            int i = 0;
            do {
                time[i] = cursor.getString(0);
                i++;
            } while (cursor.moveToNext());
        }
        db.close();
        return time;
    }

    public boolean deleteAttendance(int id, String COLUMN_PARAMETER) {
        try {
            SQLiteDatabase db = getReadableDatabase();
            String ATTENDANCE_TABLE = "A_" + id;
            Cursor c = db.rawQuery("SELECT * FROM " + ATTENDANCE_TABLE, null);
            String column_info = c.getColumnName(0) + " INTEGER PRIMARY KEY AUTOINCREMENT";
            String column_name = c.getColumnName(0);
            for (int i = 1; i < c.getColumnCount(); i++) {
                if (!c.getColumnName(i).equals(COLUMN_PARAMETER)) {
                    column_info = column_info + "," + c.getColumnName(i) + " text";
                    column_name = column_name + "," + c.getColumnName(i);
                }
            }
            db.execSQL("CREATE TEMPORARY TABLE backup (" + column_info + ")");
            db.execSQL("INSERT INTO backup SELECT " + column_name + " FROM " + ATTENDANCE_TABLE);
            db.execSQL("DROP TABLE " + ATTENDANCE_TABLE);
            db.execSQL("CREATE TABLE " + ATTENDANCE_TABLE + " (" + column_info + ")");
            db.execSQL("INSERT INTO " + ATTENDANCE_TABLE + " SELECT " + column_name + " FROM backup");
            db.execSQL("DROP TABLE backup");
            db.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<String> deleteCheck(int id, Date s, Date e) {
        Cursor c = getReadableDatabase().rawQuery("SELECT * FROM " + ("A_" + id) + " ORDER BY roll_no", null);
        List<String> list = new ArrayList();
        c.moveToFirst();
        for (int i = 1; i < c.getColumnCount(); i++) {
            try {
                Date d = new SimpleDateFormat("MMM_dd_yyyy_hh_mm_ss").parse(c.getColumnName(i));
                Date end = e;
                if (d.compareTo(s) >= 0 && d.compareTo(end) <= 0) {
                    System.out.println(c.getColumnName(i));
                    list.add(c.getColumnName(i));
                }
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
        return list;
    }

    public int getSubjectsCount() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("select * from subjects", null);
        int subjectsCount = c.getCount();
        c.close();
        db.close();
        return subjectsCount;
    }
}
