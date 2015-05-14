package jp.techinstitute.ti_046.timetablefailure;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

public class MyAlarmManager {

    private Context context;
    private AlarmManager alarmManager;
    private PendingIntent alarmSender;

    private static final String TAG = MyAlarmManager.class.getName();

    public MyAlarmManager(Context context) {
        this.context = context;
        alarmManager = (AlarmManager)
                context.getSystemService(Context.ALARM_SERVICE);
        Log.v(TAG, "初期化完了");
    }

    public void addAlarm(int alarmHour, int alarmMinute) {
        alarmSender = this.getPendingIntent();

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());

        // アラームの時間セット
        cal.set(Calendar.HOUR_OF_DAY, alarmHour);
        cal.set(Calendar.MINUTE, alarmMinute);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        // 過去だったら来週？TODO: ダイアログでも出す？？
        if(cal.getTimeInMillis() < System.currentTimeMillis()){
            cal.add(Calendar.DAY_OF_YEAR, 7);
        }

        Toast.makeText(context,
                String.format("%02d時%02d分に起こします", alarmHour, alarmMinute)
                , Toast.LENGTH_LONG).show();
        alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), alarmSender);
        Log.v(TAG, "addAlarm()" + cal.getTimeInMillis() + "ms");
    }

    public void cancelAlarm() {
        Log.d(TAG, "cancelAlarm()");
        alarmManager.cancel(alarmSender);
    }

    private PendingIntent getPendingIntent() {
        // アラーム時に起動するアプリケーションを登録
        Intent intent = new Intent(context, MyAlarmService.class);
        PendingIntent pendingIntent = PendingIntent
                .getService(context, PendingIntent.FLAG_ONE_SHOT
                        , intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }
}
