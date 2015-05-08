package jp.techinstitute.ti_046.timetablefailure;

public class ClassTable {
    private String day;
    private String time;
    private String name;
    private String room;
    private String teacher;
    private boolean hasAlarm;
    private int alarm;

    public ClassTable(String day, String time, String name, String room, String teacher, boolean hasAlarm, int alarm) {
        this.day = day;
        this.time = time;
        this.name = name;
        this.room = room;
        this.teacher = teacher;
        this.hasAlarm = hasAlarm;
        this.alarm = alarm;
    }

    public ClassTable(String day, String time) {
        this.day = day;
        this.time = time;
        this.name = "test_name";
        this.room = "test_room";
        this.teacher = "test_teacher";
        this.hasAlarm = false;
        this.alarm = 0;
    }

    public ClassTable() {
        this.day = "";
        this.time = "";
        this.name = "test_name";
        this.room = "test_room";
        this.teacher = "test_teacher";
        this.hasAlarm = false;
        this.alarm = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public boolean hasAlarm() {
        return hasAlarm;
    }

    public void setHasAlarm(boolean hasAlarm) {
        this.hasAlarm = hasAlarm;
    }

    public int getAlarm() {
        return alarm;
    }

    public void setAlarm(int alarm) {
        this.alarm = alarm;
    }
}
