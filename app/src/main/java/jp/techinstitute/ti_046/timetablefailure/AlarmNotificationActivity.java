package jp.techinstitute.ti_046.timetablefailure;

import android.app.KeyguardManager;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.PowerManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;


public class AlarmNotificationActivity extends ActionBarActivity {

    MediaPlayer player;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopAndRelease();
    }

    private void stopAndRelease() {
        if (player != null) {
            player.stop();
            player.release();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (player == null) {
            player = MediaPlayer.create(this, R.raw.alarm);
        }
        player.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_notification);
        Log.v("通知ログ", "create");
        // スリープ状態から復帰する
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Toast.makeText(this, "アラーム！", Toast.LENGTH_SHORT).show();
    }
}
