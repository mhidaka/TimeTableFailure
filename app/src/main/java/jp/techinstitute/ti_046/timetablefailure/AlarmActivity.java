package jp.techinstitute.ti_046.timetablefailure;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;


public class AlarmActivity extends ActionBarActivity {

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
        setContentView(R.layout.activity_alarm);

        Button btnWakeUp = (Button) findViewById(R.id.button_wakeUp);
        Button btnBeg = (Button) findViewById(R.id.button_beg);
        Button btnDame = (Button) findViewById(R.id.button_dame);

        // 起きるボタン押したらアラーム停止
        btnWakeUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopAndRelease();
            }
        });
        // あと五分ボタン押したら五分後に再度アラームセット
        btnBeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        // もうだめボタン押したらTwitterに投稿
        btnDame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "もうだめ。" +  "代返plz...");
                String appName = "twitter";


                // Twitter投稿。Twitterという名前からアプリを起動する
                PackageManager packageManager = getPackageManager();
                List<?> activityList = packageManager.queryIntentActivities(shareIntent, 0);
                int len = activityList.size();
                for (int i = 0; i < len; i++) {
                    ResolveInfo app = (ResolveInfo) activityList.get(i);
                    if ((app.activityInfo.name.contains(appName))) {
                        ActivityInfo activity = app.activityInfo;
                        ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
                        shareIntent.setComponent(name);
                        startActivity(shareIntent);
                        break;
                    }
                }
            }
        });

        Log.v("通知ログ", "create");
        // スリープ状態から復帰する
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Toast.makeText(this, "アラーム！", Toast.LENGTH_SHORT).show();
    }
}
