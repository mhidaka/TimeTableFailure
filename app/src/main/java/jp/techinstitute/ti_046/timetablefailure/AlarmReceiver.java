package jp.techinstitute.ti_046.timetablefailure;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("レシーバログ", "action: " + intent.getAction());
        int id = intent.getIntExtra(DetailActivity.TAG_CLASS_ID, 0);
        // 起動するActivity指定
        Intent notification = new Intent(context, AlarmActivity.class);
        notification.putExtra(DetailActivity.TAG_CLASS_ID, id);
        Log.d(this.getClass().getName(), Integer.toString(id));
        // 画面起動用
        notification.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(notification);
    }
}
