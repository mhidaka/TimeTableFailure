package jp.techinstitute.ti_046.timetablefailure;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class DetailFragment extends android.support.v4.app.Fragment {

    private static final String ARG_CLASS_ID = "class_id";

    private int class_id;

    public static DetailFragment newInstance(int class_id) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CLASS_ID, class_id);
        fragment.setArguments(args);
        return fragment;
    }

    public DetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            class_id = getArguments().getInt(ARG_CLASS_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.listView);

        TableHelper helper = new TableHelper(getActivity());
        ClassTable classTable = helper.getClassTableById(class_id);
        String[] detail = new String[]{ classTable.getDay(), classTable.getTime()
                , classTable.getName(), classTable.getTeacher(), classTable.getRoom() };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity()
                , android.R.layout.simple_list_item_1, detail);
        listView.setAdapter(adapter);
        return rootView;
    }
}
