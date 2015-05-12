package jp.techinstitute.ti_046.timetablefailure;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


public class DetailActivity extends ActionBarActivity implements CompoundButton.OnCheckedChangeListener {

    private final String ARG_CLASS_ID = "class_id";
    private final int[] DEFAULT_TIME = { 7, 0 };
    private int class_id;

    private TableHelper helper;

    private FragmentManager manager;
    private ClassTable classTable;
    private TextView alertTextView;

    private static final String SET_ALARM_FRAGMENT_TAG = SetAlarmFragment.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        manager = getSupportFragmentManager();

        // Intentからid取得
        Intent intent = getIntent();
        class_id = intent.getIntExtra(ARG_CLASS_ID, 0);

        // TextView取得
        TextView text_day = (TextView) findViewById(R.id.text_day);
        TextView text_time = (TextView) findViewById(R.id.text_time);
        TextView text_name = (TextView) findViewById(R.id.text_name);
        TextView text_teacher = (TextView) findViewById(R.id.text_teacher);
        TextView text_room = (TextView) findViewById(R.id.text_room);

        helper = new TableHelper(this);
        classTable = helper.getClassTableById(class_id);
        String[] detail = { classTable.getDay(), classTable.getTime(), classTable.getName()
                , classTable.getTeacher(), classTable.getRoom() };
        TextView[] textViews = { text_day, text_time, text_name, text_teacher, text_room };
        for (int i = 0; i < detail.length; i++) {
            textViews[i].setText(detail[i]);
        }

        Switch alarm_switch = (Switch) findViewById(R.id.switch_alarm);
        alarm_switch.setChecked(classTable.hasAlarm());
        alertTextView = (TextView) findViewById(R.id.text_alarm);
        // classTableがAlarmを持つなら、TextViewでセット時間表示
        if (alarm_switch.isChecked()) {
            alertTextView.setText(classTable.getAlarmHour() + "時"
                    + classTable.getAlarmMinute() + "分にアラームをセットしています");
        } else {
            alertTextView.setText("アラームは設定されていません");
        }
        alarm_switch.setOnCheckedChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        classTable = helper.getClassTableById(class_id);
        if (isChecked) {
            // アラームセット用のダイアログ表示
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hour, int minute) {

                    // classTable更新
                    classTable.setHasAlarm(true);
                    classTable.setAlarmHour(hour);
                    classTable.setAlarmMinute(minute);
                    helper.updateClassTable(classTable);

                    // alertTextView UI更新
                    alertTextView.refreshDrawableState();
                    alertTextView.setText(classTable.getDay() + "日の"
                            + Integer.toString(hour) + "時"
                            + Integer.toString(minute) + "分にアラームをセットしています");

                    Toast.makeText(DetailActivity.this, classTable.getDay() + "日の"
                            + Integer.toString(hour) + "時"
                            + Integer.toString(minute) + "分にアラームをセットされました！", Toast.LENGTH_SHORT).show();
                }
            }, DEFAULT_TIME[0], DEFAULT_TIME[1], true);
            timePickerDialog.show();
        } else {
            // classTable更新
            classTable.setHasAlarm(false);
            classTable.setAlarmHour(0);
            classTable.setAlarmMinute(0);
            helper.updateClassTable(classTable);

            // alertTextView UI更新
            alertTextView.refreshDrawableState();
            alertTextView.setText("アラームはセットされていません");
        }
    }
}
