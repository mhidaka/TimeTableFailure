package jp.techinstitute.ti_046.timetablefailure;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class AlarmActivity extends ActionBarActivity {

    private final int BEGGING_MINUTES = 5;
    private MediaPlayer player;
    private int id;
    private TableHelper helper;
    private ClassTable classTable;
    private Intent intent;

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
        helper = new TableHelper(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        // アラームをセットした授業のid取得
        id = getIntent().getIntExtra(DetailActivity.TAG_CLASS_ID, 0);
        classTable = helper.getClassTableById(id);

        Button btnWakeUp = (Button) findViewById(R.id.button_wakeUp);
        Button btnBeg = (Button) findViewById(R.id.button_beg);
        Button btnDame = (Button) findViewById(R.id.button_dame);

        // 起きるボタン押したらアラーム停止してDetailActivityの授業詳細画面へ
        btnWakeUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopAndRelease();
                intent = getIntentToDetail(id);
                startActivity(intent);
            }
        });
        // あと5分ボタン押したらアラーム止めて、5分後に再度アラームセット
        btnBeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopAndRelease();
                int[] beggingAlarmTime = { classTable.getAlarmHour()
                        , classTable.getAlarmMinute() + BEGGING_MINUTES };
                MyAlarmManager alarmManager = new MyAlarmManager(AlarmActivity.this, id);
                alarmManager.addAlarm(beggingAlarmTime[0], beggingAlarmTime[1]);
            }
        });
        // もうだめボタン押したらTwitterに投稿
        btnDame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AlarmActivity.this);
                builder.setMessage("ほんとにいいの？\nダメ人間だよ？");
                builder.setPositiveButton("はい、私はダメ人間です", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        stopAndRelease();
                        findTwitterAppsAndPost();
                    }
                });
                builder.setNegativeButton("頑張る", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        stopAndRelease();
                        intent = getIntentToDetail(id);
                        startActivity(intent);
                    }
                });
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

    public void findTwitterAppsAndPost() {
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

    public Intent getIntentToDetail(int id) {
        Intent intent = new Intent(AlarmActivity.this, DetailActivity.class);
        intent.putExtra(DetailActivity.TAG_CLASS_ID, id);
        return intent;
    }
}
