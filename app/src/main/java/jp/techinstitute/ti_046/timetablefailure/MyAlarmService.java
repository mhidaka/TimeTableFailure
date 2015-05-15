package jp.techinstitute.ti_046.timetablefailure;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MyAlarmService extends Service {

    private int id;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        id = intent.getIntExtra(DetailActivity.TAG_CLASS_ID, 0);
        return super.onStartCommand(intent, flags, startId);
    }

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
            Intent alarmBroadcast = new Intent();
            alarmBroadcast.setAction("MyAlarmAction");
            alarmBroadcast.putExtra(DetailActivity.TAG_CLASS_ID, id);
            sendBroadcast(alarmBroadcast);
            MyAlarmService.this.stopSelf();
            Log.v(MyAlarmService.class.getSimpleName(), "SERVICE STOPPED");
        }
    };
}
