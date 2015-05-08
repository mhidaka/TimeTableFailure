package jp.techinstitute.ti_046.timetablefailure;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;


public class MyDialogFragment extends DialogFragment {
    private static final String ARG_CLASS_NAME = "param1";
    private static final String ARG_CLASS_ID = "param2";
    private String class_name;
    private int class_id;

    public static MyDialogFragment newInstance(String name, int id) {
        MyDialogFragment fragment = new MyDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CLASS_NAME, name);
        args.putInt(ARG_CLASS_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    public MyDialogFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            class_name = getArguments().getString(ARG_CLASS_NAME);
            class_id = getArguments().getInt(ARG_CLASS_ID);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder deleteBuilder = new AlertDialog.Builder(getActivity());
        deleteBuilder.setTitle(class_name + "を削除しますか？");
        deleteBuilder.setPositiveButton("削除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                TableHelper helper = new TableHelper(getActivity());
                // TableHelperのgetDayAndTimeでidから曜日と時間を取得し、それに対応する授業削除
                String[] day_time = helper.getDayAndTime(class_id);
                helper.deleteClassTable(day_time[0], day_time[1]);
                getFragmentManager().beginTransaction().replace(R.id.container, new MainFragment());
            }
        });
        deleteBuilder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        return deleteBuilder.create();
    }
}
