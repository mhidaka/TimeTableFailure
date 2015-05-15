package jp.techinstitute.ti_046.timetablefailure;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Spinner;

public class MainActivity extends ActionBarActivity implements InputTextListener {

    private final String MAIN_FRAGMENT_TAG = MainFragment.class.getName();
    // TODO クラス変数である必要がないものもたくさんあるので見なおしてみて
    private BlankTextNotify notify = null;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private MainFragment mainFragment;
    private TableHelper helper;
    private boolean hasAlarm;
    private ClassTable classTable;
    private int class_id; // TODO classIdのほうが命名規則一致してる
    private View rootView;
    private Intent intent;

    private Spinner spinnerDay, spinnerTime;
    private EditText editName, editTeacher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        helper = new TableHelper(MainActivity.this);
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        mainFragment = new MainFragment();
        if (savedInstanceState == null) {
            // TODO managerもtranscationもここでしか使ってないなら getSupportFragmentManager().beginTransaction().add(...)でよくないかな
            transaction.add(R.id.container, mainFragment
                    , MAIN_FRAGMENT_TAG).commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // TODO 以下のクラス変数はここに書くこと（メソッド変数としての機能しか必要としていない）
        // private Spinner spinnerDay, spinnerTime;
        // private EditText editName, editTeacher;

        switch (id) {
            case R.id.action_add_class_table:
                AlertDialog.Builder addTableBuilder = new AlertDialog.Builder(this);
                // Layout及びView取得、Dialogにセット
                // TODO: もっとすっきり取得したい 名前短くするといいよ。
                rootView = getLayoutInflater().inflate(R.layout.layout_add_dialog, null);
                spinnerDay = (Spinner) rootView.findViewById(R.id.spinner_day);
                spinnerTime = (Spinner) rootView.findViewById(R.id.spinner_time);
                editName = (EditText) rootView.findViewById(R.id.edit_name);
                editTeacher = (EditText) rootView.findViewById(R.id.edit_teacher);
                addTableBuilder.setView(rootView);

                notify = new BlankTextNotify(editName);
                notify.setListener(MainActivity.this);

                // Software Keyboardを、それ以外の部分をクリックで隠す
                InputMethodManager inputMethodManager =
                        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(rootView.getWindowToken(), 0);

                addTableBuilder.setTitle("新規授業登録");
                addTableBuilder.setPositiveButton("追加", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // editName空だとダメ！
                        boolean hasText = notify.containsText();

                        if (hasText) {
                            // TODO: もっとすっきり書きたい
                            String day = spinnerDay.getSelectedItem().toString();
                            String time = spinnerTime.getSelectedItem().toString();
                            String name = editName.getText().toString();
                            String teacher = editTeacher.getText().toString();
                            class_id = helper.getId(day, time);
                            classTable = new ClassTable(day, time, name, teacher, "room", false, 0, 0);
                            // Databaseを更新
                            helper.updateClassTable(classTable);
                            Log.d("createClassTable:", classTable.getDay()
                                    + classTable.getTime() + classTable.getName());

                            AlertDialog.Builder askDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                            askDialogBuilder.setMessage("この授業にアラームをセットしますか？");
                            askDialogBuilder.setPositiveButton("はい", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    hasAlarm = true;
                                    intent = new Intent(MainActivity.this, DetailActivity.class);
                                    intent.putExtra(DetailActivity.TAG_CLASS_ID, class_id);
                                    intent.putExtra(DetailActivity.TAG_WANTS_ALARM, hasAlarm);
                                    startActivity(intent);
                                }
                            });
                            askDialogBuilder.setNegativeButton("いいえ", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    hasAlarm = false;
                                    intent = new Intent(MainActivity.this, DetailActivity.class);
                                    intent.putExtra(DetailActivity.TAG_CLASS_ID, class_id);
                                    intent.putExtra(DetailActivity.TAG_WANTS_ALARM, hasAlarm);
                                    startActivity(intent);
                                }
                            });

                            askDialogBuilder.show();
                        }
                    }
                });
                addTableBuilder.show();
                break;
            case R.id.action_drop_class_table: // TODO addの反対はdelete。名前をdropからdeleteに変えた方がいい
                AlertDialog.Builder deleteDialogBuilder = new AlertDialog.Builder(this);
                deleteDialogBuilder.setTitle("時間割を削除");
                deleteDialogBuilder.setMessage("ほんとうに時間割をすべて削除しますか？");
                deleteDialogBuilder.setPositiveButton("削除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        helper.dropTable();

                        Log.d("dropClassTable: ", "the table is dropped successfully");
                    }
                });
                deleteDialogBuilder.show();
                break;
            case R.id.action_settings:
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void noInputText() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("授業が登録できません！");
        builder.setMessage("授業名が空欄です");
        builder.create().show();
    }

    @Override
    public void inputText() {
        // 何もしない
    }
}