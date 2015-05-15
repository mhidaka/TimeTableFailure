package jp.techinstitute.ti_046.timetablefailure;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;


// TODO: 出席状況に関するカラム作成
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
            KEY_ALARM_HOUR = "alarmHour",
            KEY_ALARM_MINUTE = "alarmMinute";

    private static final String[] COLUMNS = {
            KEY_ID, KEY_DAY, KEY_TIME, KEY_NAME
            , KEY_TEACHER, KEY_ROOM, KEY_HAS_ALARM, KEY_ALARM_HOUR, KEY_ALARM_MINUTE };

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
                    KEY_ALARM_HOUR + " INTEGER, " +
                    KEY_ALARM_MINUTE + " INTEGER)";


    public TableHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);

        ArrayList<ClassTable> list = new ArrayList<>();
        setDefaultTable(list, db);
    }

    // TODO privateでよくない？
    public ArrayList<ClassTable> setDefaultTable(ArrayList<ClassTable> list, SQLiteDatabase db) {
        for (String time : TIMES) {
            for (String day: DAYS) {
                // 各行に曜日と時間をセットして、かつ授業名にテストとしてday + timeをセット
                // TODO 授業名を空に！
                ClassTable classTable = new ClassTable(day, time, day + time, " ", " ", false, 0, 0);
                createClassTable(classTable, db);
                list.add(classTable);
                Log.d("DEFAULT SET", "DONE" + classTable.getName());
            }
        }
        return list;
    }

    // TODO privateでよくない？
    public void createClassTable(ClassTable classTable, SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(KEY_DAY, classTable.getDay());
        values.put(KEY_TIME, classTable.getTime());
        values.put(KEY_NAME, classTable.getName());
        values.put(KEY_TEACHER, classTable.getTeacher());
        values.put(KEY_ROOM, classTable.getRoom());
        values.put(KEY_ALARM_HOUR, classTable.getAlarmHour());
        values.put(KEY_ALARM_MINUTE, classTable.getAlarmMinute());

        // hasAlarmだけbooleanなので別処理
        if (classTable.hasAlarm()) {
            values.put(KEY_HAS_ALARM, 1); // true
        } else if (!classTable.hasAlarm()) {
            values.put(KEY_HAS_ALARM, 0); // false
        }

        long id = db.insert(TABLE_NAME, null, values);
        if (id == -1) {
            Log.v("Database", "Failed to insert classTable");
        }
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

            // index取得
            int idIndex = cursor.getColumnIndex(KEY_ID);
            int dayIndex = cursor.getColumnIndex(KEY_DAY);
            int timeIndex = cursor.getColumnIndex(KEY_TIME);
            int nameIndex = cursor.getColumnIndex(KEY_NAME);
            int teacherIndex = cursor.getColumnIndex(KEY_TEACHER);
            int roomIndex = cursor.getColumnIndex(KEY_ROOM);
            int hasAlarmIndex = cursor.getColumnIndex(KEY_HAS_ALARM);
            int alarmHourIndex = cursor.getColumnIndex(KEY_ALARM_HOUR);
            int alarmMinuteIndex = cursor.getColumnIndex(KEY_ALARM_MINUTE);

            // 取得したindexを元にclassTableにデータをセット
            classTable.setId(cursor.getInt(idIndex));
            classTable.setDay(cursor.getString(dayIndex));
            classTable.setTime(cursor.getString(timeIndex));
            classTable.setName(cursor.getString(nameIndex));
            classTable.setTeacher(cursor.getString(teacherIndex));
            classTable.setRoom(cursor.getString(roomIndex));
            classTable.setAlarmHour(cursor.getInt(alarmHourIndex));
            classTable.setAlarmMinute(cursor.getInt(alarmMinuteIndex));

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
        int id = getId(day, time);
        return getClassTableById(id);
    }

    public int getId(String day, String time) {
        int intDay = 0;
        int id;
        switch (day) {
            case "月曜":
                intDay = 1;
                break;
            case "火曜":
                intDay = 2;
                break;
            case "水曜":
                intDay = 3;
                break;
            case "木曜":
                intDay = 4;
                break;
            case "金曜":
                intDay = 5;
                break;
        }
        // id = (時間 - 1) * 曜日数(ここでは5) + 曜日(月曜から順に1, 2, 3, 4, 5)
        id = ((Integer.parseInt(time) - 1) * DAYS.length) + intDay;
        return id;
    }

    public String[] getDayAndTime(int id) {
        String[] dayAndTime;
        int day = id % 6;
        int time = (int)Math.floor(id / 5);
        dayAndTime = new String[]{ DAYS[day - 1], TIMES[time - 1] };
        return dayAndTime;
    }

    // TODO oldじゃなくてcurrentでは？
    public ClassTable updateClassTable(ClassTable oldClassTable) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, oldClassTable.getName());
        values.put(KEY_TEACHER, oldClassTable.getTeacher());
        values.put(KEY_ROOM, oldClassTable.getRoom());
        values.put(KEY_ALARM_HOUR, oldClassTable.getAlarmHour());
        values.put(KEY_ALARM_MINUTE, oldClassTable.getAlarmMinute());

        // hasAlarmだけbooleanなので別処理
        if (oldClassTable.hasAlarm()) {
            values.put(KEY_HAS_ALARM, 1);
        } else if (!oldClassTable.hasAlarm()) {
            values.put(KEY_HAS_ALARM, 0);
        }

        // idから検索してupdate
        String where = KEY_ID + "=?";
        int id = getId(oldClassTable.getDay(), oldClassTable.getTime());
        String[] args = { String.valueOf(id) };

        int count = db.update(TABLE_NAME, values, where, args);
        if (count == 0) {
            Log.v("Edit", "Failed to update");
        }
        db.close();

        return getClassTableById(id);
    }

    public void deleteClassTable(int id) {
        // make an empty ClassTable and set it day and time
        ClassTable oldClassTable = getClassTableById(id);
        ClassTable newClassTable = new ClassTable(oldClassTable.getDay(), oldClassTable.getTime());
        newClassTable.setName("Deleted");
        // 空のデータに更新することで、削除による行と行の間に空行回避
        updateClassTable(newClassTable);
    }

    public void dropTable() {
        String dropSQL = "DROP TABLE " + TABLE_NAME;
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.execSQL(dropSQL);
        } catch (SQLException e) {
            Log.e("ERROR", e.toString());
        }
        // デフォルトデータベース作成
        db.execSQL(SQL_CREATE_TABLE);
        ArrayList<ClassTable> list = new ArrayList<>();
        setDefaultTable(list, db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }
}
