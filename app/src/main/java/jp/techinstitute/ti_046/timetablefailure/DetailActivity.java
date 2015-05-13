package jp.techinstitute.ti_046.timetablefailure;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class DetailActivity extends ActionBarActivity
        implements CompoundButton.OnCheckedChangeListener,
        TimePickerDialog.OnTimeSetListener {

    public static final String TAG_CLASS_ID = "class_id";
    public static final String TAG_WANTS_ALARM = "wantsAlarm";
    private final int[] DEFAULT_TIME = {0, 0};
    private int class_id;
    private boolean wantsAlarm;
    private View rootView;

    private TimePickerDialog timePickerDialog;

    private TableHelper helper;

    private ClassTable classTable;
    private TextView alarmTextView;
    private Switch alarmSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Intentからid取得
        Intent intent = getIntent();
        class_id = intent.getIntExtra(TAG_CLASS_ID, 0);
        wantsAlarm = intent.getBooleanExtra(TAG_WANTS_ALARM, false);

        // TextView取得
        TextView text_day = (TextView) findViewById(R.id.text_day);
        TextView text_time = (TextView) findViewById(R.id.text_time);
        TextView text_name = (TextView) findViewById(R.id.text_name);
        TextView text_teacher = (TextView) findViewById(R.id.text_teacher);
        TextView text_room = (TextView) findViewById(R.id.text_room);

        helper = new TableHelper(this);
        classTable = helper.getClassTableById(class_id);
        String[] detail = {classTable.getDay(), classTable.getTime(), classTable.getName()
                , classTable.getTeacher(), classTable.getRoom()};
        TextView[] textViews = {text_day, text_time, text_name, text_teacher, text_room};
        for (int i = 0; i < detail.length; i++) {
            textViews[i].setText(detail[i]);
        }

        alarmSwitch = (Switch) findViewById(R.id.switch_alarm);
        alarmSwitch.setChecked(classTable.hasAlarm());
        if (wantsAlarm) {
            alarmSwitch.setChecked(true);
            timePickerDialog = new TimePickerDialog(this, this, 0, 0, true);
            timePickerDialog.show();
            wantsAlarm = false;
        }
        // TimePickerDialogで更新されたClassTable取得
        classTable = helper.getClassTableById(class_id);

        alarmSwitch = (Switch) findViewById(R.id.switch_alarm);
        alarmSwitch.setChecked(classTable.hasAlarm());
        alarmTextView = (TextView) findViewById(R.id.text_alarm);
        // classTableがAlarmを持つなら、TextViewでセット時間表示
        if (classTable.hasAlarm()) {
            alarmTextView.setText(classTable.getAlarmHour() + "時"
                    + classTable.getAlarmMinute() + "分にアラームをセットしています");
        } else {
            alarmTextView.setText("アラームは設定されていません");
        }
        alarmSwitch.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        classTable = helper.getClassTableById(class_id);
        if (isChecked && timePickerDialog == null) {
            // alarmSwitchがONでかつDialog表示中でなければ
            // アラームセット用のダイアログ表示
            timePickerDialog = new TimePickerDialog(DetailActivity.this, this
                    , DEFAULT_TIME[0], DEFAULT_TIME[1], true);
            timePickerDialog.show();
        } else {
            alarmTextView = (TextView) findViewById(R.id.text_alarm);
            alarmTextView.setText("アラームは設定されていません");
        }
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        // classTable更新
        classTable.setHasAlarm(true);
        classTable.setAlarmHour(hour);
        classTable.setAlarmMinute(minute);
        ClassTable newClassTable = helper.updateClassTable(classTable);
        // 更新したnewClassTableをthis.classTaleにセット
        classTable = newClassTable;

        // alarmに関するUI更新
        alarmSwitch.setChecked(classTable.hasAlarm());
        if (alarmSwitch.isChecked()) {
            alarmTextView = (TextView) findViewById(R.id.text_alarm);
            alarmTextView.setText(classTable.getDay() + "日の"
                    + Integer.toString(hour) + "時"
                    + Integer.toString(minute) + "分にアラームをセットしています");
        }
        Toast.makeText(DetailActivity.this, classTable.getDay() + "日の"
                + Integer.toString(hour) + "時"
                + Integer.toString(minute) + "分にアラームをセットされました！", Toast.LENGTH_SHORT).show();
    }
}