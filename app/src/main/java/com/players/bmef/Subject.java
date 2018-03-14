package com.players.bmef;

public class Subject {
    String dept;
    String div;
    int roll_from;
    int roll_to;
    int sid;
    String subject_name;
    String type;
    String year;

    Subject(int x, int y, int z, String a, String b, String c, String d, String e) {
        this.sid = x;
        this.roll_from = y;
        this.roll_to = z;
        this.subject_name = a;
        this.dept = b;
        this.div = c;
        this.type = d;
        this.year = e;
    }
}
