package jp.techinstitute.ti_046.timetablefailure;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class TableHelper extends SQLiteOpenHelper{

    public static String[] DAYS = { "月曜", "火曜", "水曜", "木曜", "金曜" };
    public static String[] TIMES = { "1", "2", "3", "4", "5", "6" };

    private static final String DB_NAME = "table.db",
            TABLE_NAME = "timetable",
            KEY_ID = "_id",
            KEY_DAY = "day",
            KEY_TIME = "time",
            KEY_NAME = "name",
            KEY_TEACHER = "teacher",
            KEY_ROOM = "room",
            KEY_HAS_ALARM = "hasAlarm", // 0 = false, 1 = true
            KEY_ALARM = "alarm";

    private static final String[] COLUMNS = {
            KEY_ID, KEY_DAY, KEY_TIME, KEY_NAME
            , KEY_TEACHER, KEY_ROOM, KEY_HAS_ALARM, KEY_ALARM };

    private static int DB_VERSION = 1;

    private static final String  SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "(" +
                    KEY_ID + " INTEGER primary key autoincrement," +
                    KEY_DAY + " TEXT," +
                    KEY_TIME + " TEXT," +
                    KEY_NAME + " TEXT," +
                    KEY_TEACHER + " TEXT," +
                    KEY_ROOM + " TEXT, " +
                    KEY_HAS_ALARM + " INTEGER, " +
                    KEY_ALARM + " INTEGER)";


    public TableHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);

        ArrayList<ClassTable> defaultList = new ArrayList<>();
        this.setDefaultTable(defaultList, db);
    }

    public ArrayList<ClassTable> setDefaultTable(ArrayList<ClassTable> list, SQLiteDatabase db) {
        for (String time : TIMES) {
            for (String day: DAYS) {
                // 各行に曜日と時間をセットして、かつ授業名にテストとしてday + timeをセット
                // TODO 授業名を空に！
                ClassTable classTable = new ClassTable(day, time, day + time, "", "", false, 0);

                ContentValues values = new ContentValues();
                values.put(KEY_DAY, classTable.getDay());
                values.put(KEY_TIME, classTable.getTime());
                values.put(KEY_NAME, classTable.getName());
                values.put(KEY_TEACHER, classTable.getTeacher());
                values.put(KEY_ROOM, classTable.getRoom());
                values.put(KEY_HAS_ALARM, classTable.hasAlarm());
                values.put(KEY_ALARM, classTable.getAlarm());
                long id = db.insert(DB_NAME, null, values);
                if (id == -1) {
                    Log.v("Database", "Failed to insert classTable");
                }
                Log.d("TABLE HELPER", classTable.getDay() + classTable.getTime());
                list.add(classTable);
            }
        }
        return list;
    }

    public void createClassTable(ClassTable classTable) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DAY, classTable.getDay());
        values.put(KEY_TIME, classTable.getTime());
        values.put(KEY_NAME, classTable.getName());
        values.put(KEY_TEACHER, classTable.getTeacher());
        values.put(KEY_ROOM, classTable.getRoom());
        values.put(KEY_HAS_ALARM, classTable.hasAlarm());
        values.put(KEY_ALARM, classTable.getAlarm());
        long id = db.insert(DB_NAME, null, values);
        if (id == -1) {
            Log.v("Database", "Failed to insert classTable");
        }
        db.close();
    }

    public ClassTable getClassTableById(int id) {
        SQLiteDatabase db = getReadableDatabase();

        // idで検索
        String where = KEY_ID + "=?";
        String[] args = { String.valueOf(id) };
        Cursor cursor = db.query(TABLE_NAME, COLUMNS, where, args
                , null, null, null, null);
        ClassTable classTable = new ClassTable();
        // 空データを作ってるから必要なし？
        if (!cursor.moveToFirst()) {
            cursor.close();
            db.close();
            return null;
        } else {

            int dayIndex = cursor.getColumnIndex(KEY_DAY);
            int timeIndex = cursor.getColumnIndex(KEY_TIME);
            int nameIndex = cursor.getColumnIndex(KEY_NAME);
            int teacherIndex = cursor.getColumnIndex(KEY_TEACHER);
            int roomIndex = cursor.getColumnIndex(KEY_ROOM);
            int hasAlarmIndex = cursor.getColumnIndex(KEY_HAS_ALARM);
            int alarmIndex = cursor.getColumnIndex(KEY_ALARM);

            // 取得したindexを元にclassTableにデータをセット
            classTable.setDay(cursor.getString(dayIndex));
            classTable.setTime(cursor.getString(timeIndex));
            classTable.setName(cursor.getString(nameIndex));
            classTable.setTeacher(cursor.getString(teacherIndex));
            classTable.setRoom(cursor.getString(roomIndex));
            classTable.setAlarm(cursor.getInt(alarmIndex));

            // hasAlarmだけはbooleanなので別処理
            switch (cursor.getInt(hasAlarmIndex)) {
                case 0:
                    classTable.setHasAlarm(false);
                    break;
                case 1:
                    classTable.setHasAlarm(true);
                    break;
            }
        }
        cursor.close();
        db.close();

        return classTable;
    }

    public ClassTable getClassTable(String day, String time) {
        SQLiteDatabase db = getReadableDatabase();

        // idで検索
        String where = KEY_ID + "=?";
        int id = getId(day, time);
        String[] args = { String.valueOf(id) };
        Cursor cursor = db.query(TABLE_NAME, COLUMNS, where, args
                , null, null, null, null);
        ClassTable classTable = new ClassTable();



        // index取得
        int dayIndex = cursor.getColumnIndex(KEY_DAY);
        int timeIndex = cursor.getColumnIndex(KEY_TIME);
        int nameIndex = cursor.getColumnIndex(KEY_NAME);
        int teacherIndex = cursor.getColumnIndex(KEY_TEACHER);
        int roomIndex = cursor.getColumnIndex(KEY_ROOM);
        int hasAlarmIndex = cursor.getColumnIndex(KEY_HAS_ALARM);
        int alarmIndex = cursor.getColumnIndex(KEY_ALARM);

        // 取得したindexを元にclassTableにデータをセット
        classTable.setDay(cursor.getString(dayIndex));
        classTable.setTime(cursor.getString(timeIndex));
        classTable.setName(cursor.getString(nameIndex));
        classTable.setTeacher(cursor.getString(teacherIndex));
        classTable.setRoom(cursor.getString(roomIndex));
        classTable.setAlarm(cursor.getInt(alarmIndex));

        // hasAlarmだけはbooleanなので別処理
        switch (cursor.getInt(hasAlarmIndex)) {
            case 0:
                classTable.setHasAlarm(false);
                break;
            case 1:
                classTable.setHasAlarm(true);
                break;
        }
        cursor.close();
        db.close();

        return classTable;
    }

    public int getId(String day, String time) {
        int parseDay = 0;
        int id;
        switch (day) {
            case "月曜":
                parseDay = 0;
                break;
            case "火曜":
                parseDay = 1;
                break;
            case "水曜":
                parseDay = 2;
                break;
            case "木曜":
                parseDay = 3;
                break;
            case "金曜":
                parseDay = 4;
                break;
        }
        id = DAYS.length * parseDay + Integer.parseInt(time);
        return id;
    }

    public String[] getDayAndTime(int id) {
        String[] dayAndTime = new String[2];
        int day = id % 6;
        int time = (int)Math.ceil(id / 5);
        dayAndTime = new String[]{ Integer.toString(day), Integer.toString(time) };
        return dayAndTime;
    }

    public void updateClassTable(ClassTable classTable) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, classTable.getName());
        values.put(KEY_TEACHER, classTable.getTeacher());
        values.put(KEY_ROOM, classTable.getRoom());
        values.put(KEY_HAS_ALARM, classTable.hasAlarm());
        values.put(KEY_ALARM, classTable.getAlarm());

        String where = KEY_ID + "=?";
        int id = getId(classTable.getDay(), classTable.getTime());
        String[] args = { String.valueOf(id) };

        int count = db.update(TABLE_NAME, values, where, args);
        if (count == 0) {
            Log.v("Edit", "Failed to update");
        }
        db.close();
    }

    public void deleteClassTable(String day, String time) {
        // make an empty ClassTable and set it day and time
        ClassTable classTable = new ClassTable();
        classTable.setDay(day);
        classTable.setTime(time);
        // 空のデータに更新することで、削除による行と行の間に空行回避
        updateClassTable(classTable);
    }

    public void dropTable() {
        String dropSQL = "DROP TABLE " + TABLE_NAME;
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.execSQL(dropSQL);
        } catch (SQLException e) {
            Log.e("ERROR", e.toString());
        }
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }
}
