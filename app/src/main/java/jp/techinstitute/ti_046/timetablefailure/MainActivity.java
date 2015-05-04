package jp.techinstitute.ti_046.timetablefailure;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
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

    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            GridView grid = (GridView) rootView.findViewById(R.id.grid);
            ArrayList<ClassTable> list = new ArrayList<>();
            this.setDefaultList(list); // listに月1から金6までセット
            GridAdapter adapter = new GridAdapter(getActivity(), 0, list); // GridAdapterの第二引数はlayout resource id
            grid.setAdapter(adapter);
            grid.setOnItemLongClickListener(adapter);
            return rootView;
        }

        public ArrayList<ClassTable> setDefaultList(ArrayList<ClassTable> list) {
            for (String time : TableHelper.TIMES) {
                for (String day: TableHelper.DAYS) {
                    ClassTable classTable = new ClassTable(day, time, day + time, "", "", false, 0);
                    list.add(classTable);
                }
            }
            return list;
        }
    }

    public static class GridAdapter extends ArrayAdapter<ClassTable>
            implements AdapterView.OnItemClickListener,
            AdapterView.OnItemLongClickListener {

        public GridAdapter(Context context, int resource, List<ClassTable> list) {
            super(context, resource, list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ClassTable classTable = getItem(position);
            TextView textView = new TextView(getContext());

            // textViewの高さを、”親Viewの高さ/時限数”にセットして、背景にborder_grid指定
            int parentHeight = parent.getHeight();
            textView.setHeight(parentHeight / TableHelper.TIMES.length);
            textView.setBackgroundResource(R.drawable.border_grid);

            textView.setText(classTable.getName());

            return textView;
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            // TODO 授業詳細画面に飛ばす
        }

        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, final View view, int position, final long id) {
            final TableHelper helper = new TableHelper(getContext());
            String[] menu = new String[] { "追加", "編集", "削除" };
            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setItems(menu, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    switch (which) {
                        case (0):
                            // 追加処理
                            break;
                        case (1):
                            // 編集処理
                            break;
                        case (2):
                            // 削除処理
                            final AlertDialog.Builder deleteBuilder = new AlertDialog.Builder(getContext());
                            deleteBuilder.setTitle(((TextView)view).getText() + "を削除しますか？");
                            deleteBuilder.setPositiveButton("削除", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    int classId = (int) id;
                                    // TableHelperのgetDayAndTimeでidから曜日と時間を取得し、それに対応する授業削除
                                    String[] day_time = helper.getDayAndTime(classId);
                                    helper.deleteClassTable(day_time[0], day_time[1]);
                                }
                            });
                            deleteBuilder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            deleteBuilder.show();
                            break;
                    }
                }
            });
            builder.show();
            return true;
        }
    }

}
