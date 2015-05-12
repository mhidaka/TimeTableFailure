package jp.techinstitute.ti_046.timetablefailure;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;

public class SetAlarmFragment extends android.support.v4.app.Fragment {

    private static final String ARG_CLASS_ID = "class_id";
    private int class_id;
    private TableHelper helper;

    public static SetAlarmFragment newInstance(int class_id) {
        SetAlarmFragment fragment = new SetAlarmFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CLASS_ID, class_id);
        fragment.setArguments(args);
        return fragment;
    }

    public SetAlarmFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        helper = new TableHelper(getActivity());
        ClassTable classTable = helper.getClassTableById(class_id);

        View rootView = inflater.inflate(R.layout.fragment_set_alarm, container, false);
        final TimePicker timePicker = (TimePicker) rootView.findViewById(R.id.time_picker);
        Button button = (Button) rootView.findViewById(R.id.btn_alarm);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hour = timePicker.getCurrentHour();
                int minute = timePicker.getCurrentMinute();

            }
        });
        return rootView;
    }
}
