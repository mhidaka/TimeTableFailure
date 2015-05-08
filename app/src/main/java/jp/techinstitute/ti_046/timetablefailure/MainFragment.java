package jp.techinstitute.ti_046.timetablefailure;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends android.support.v4.app.Fragment {

    private TableHelper helper;
    private ArrayList<ClassTable> list;
    private GridAdapter adapter;

    public MainFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        helper = new TableHelper(getActivity());
        list = setDefaultClassTable(helper); // listに月1から金6までセット
        adapter = new GridAdapter(getActivity(), 0, list); // GridAdapterの第二引数はlayout resource id
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        GridView grid = (GridView) rootView.findViewById(R.id.grid);
        list = setDefaultClassTable(helper); // listに月1から金6までセット
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(adapter);
        grid.setOnItemLongClickListener(adapter);
        return rootView;
    }

    public ArrayList<ClassTable> setDefaultClassTable(TableHelper helper) {
        ArrayList<ClassTable> list = new ArrayList<>();
        for (String time : TableHelper.TIMES) {
            for (String day: TableHelper.DAYS) {
                ClassTable classTable = new ClassTable(day, time, day + time, "", "", false, 0);
                helper.createClassTable(classTable, helper.getWritableDatabase());
                Log.d("DEFAULT SET", "DONE" + classTable.getName());
                list.add(classTable);
            }
        }
        return list;
    }

    private class GridAdapter extends ArrayAdapter<ClassTable>
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
            // class_idにクリックされた授業のidをセットし、Fragmentに渡す
            int class_id = position + 1;
            DetailFragment detailFragment = DetailFragment.newInstance(class_id);
            // Fragment入れ替え
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.container, detailFragment);
            // 戻るボタンで戻り可能にする
            transaction.addToBackStack(null);
            transaction.commit();
            // TODO テスト用のToastでid表示を消す
            Toast.makeText(getActivity(), class_id + " was touched", Toast.LENGTH_SHORT).show();
        }

        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, final View view, final int position, long id) {
            final TableHelper helper = new TableHelper(getContext());
            String[] menu = new String[] { "追加", "削除" };
            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setItems(menu, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    switch (which) {
                        case (0):
                            // 追加処理
                            break;
                        case (1):
                            // 削除処理
                            final AlertDialog.Builder deleteBuilder = new AlertDialog.Builder(getContext());
                            deleteBuilder.setTitle(((TextView)view).getText() + "を削除しますか？");
                            deleteBuilder.setPositiveButton("削除", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    int classId = position + 1;
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


