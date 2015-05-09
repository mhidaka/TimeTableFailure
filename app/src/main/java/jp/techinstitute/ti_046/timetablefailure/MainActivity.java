package jp.techinstitute.ti_046.timetablefailure;

import android.app.AlertDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                ViewGroup container = (ViewGroup) findViewById(R.id.add_fragment_container);
                builder.setView(getLayoutInflater().inflate(R.layout.fragment_add_dialog, container));
                builder.setTitle("新規授業登録");
                builder.show();
                break;
            case R.id.action_drop_class_table:
                break;
            case R.id.action_settings:
        }

        return super.onOptionsItemSelected(item);
    }
}
