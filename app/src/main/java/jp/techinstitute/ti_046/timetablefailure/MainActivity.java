package jp.techinstitute.ti_046.timetablefailure;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

    private TableHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        helper = new TableHelper(MainActivity.this);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MainFragment())
                    .commit();
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

        switch (id) {
            case R.id.action_add_class_table:
                // AlertDialog to create new ClassTable
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                // set dialog layout of add_fragment_dialog
                View rootView = getLayoutInflater().inflate(R.layout.fragment_add_dialog, null);
                // get views in rootView
                // findViewByIdはBuilder.setView後は使えないので要注意
                final Spinner spinnerDay = (Spinner) rootView.findViewById(R.id.spinner_day);
                final Spinner spinnerTime = (Spinner) rootView.findViewById(R.id.spinner_time);
                final EditText editName = (EditText) rootView.findViewById(R.id.edit_name);
                final EditText editTeacher = (EditText) rootView.findViewById(R.id.edit_teacher);

                builder.setView(rootView);
                builder.setTitle("新規授業登録");
                builder.setPositiveButton("追加", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ClassTable classTable = new ClassTable();
                        classTable.setDay(getSpinnerItemSelected(spinnerDay));
                        classTable.setTime(getSpinnerItemSelected(spinnerTime));
                        classTable.setName(getStringOfEditText(editName));
                        classTable.setTeacher(getStringOfEditText(editTeacher));
                        helper.createClassTable(classTable, helper.getWritableDatabase());

                        // TODO: もし追加した授業がその日の最初の授業なら、アラームセットするかダイアログ

                        Toast.makeText(MainActivity.this, classTable.getName()
                                + "が時間割に追加されました。", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
                break;
            case R.id.action_drop_class_table:
                break;
            case R.id.action_settings:
        }

        return super.onOptionsItemSelected(item);
    }

    private String getSpinnerItemSelected(Spinner spinner) {
        return spinner.getSelectedItem().toString();
    }

    private String getStringOfEditText(EditText editText) {
        SpannableStringBuilder spannableStringBuilder = (SpannableStringBuilder) editText.getText();
        return spannableStringBuilder.toString();
    }
}
