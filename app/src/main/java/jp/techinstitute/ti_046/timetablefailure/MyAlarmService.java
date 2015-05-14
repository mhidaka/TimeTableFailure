package jp.techinstitute.ti_046.timetablefailure;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MyAlarmService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Thread thread = new Thread(null, mTask, "MyAlarmServiceThread");
        thread.start();
        Log.v("MyAlarmServiceログ","スレッド開始");
    }

    Runnable mTask = new Runnable() {
        @Override
        public void run() {
            Intent alarmBroadcast = new Intent(); // ここでアラーム受け取るActivity指定
            alarmBroadcast.setAction("MyAlarmAction"); // sendMyMessage
            sendBroadcast(alarmBroadcast);
            Log.v("MyAlarmServiceログ", "通知画面起動メッセージを送った");

        }
    };
}
