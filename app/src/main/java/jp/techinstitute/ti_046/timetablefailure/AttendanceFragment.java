package jp.techinstitute.ti_046.timetablefailure;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TimePicker;

/**
 *  絶対newInstance()で呼び出すこと！
 **/
public class AttendanceFragment extends android.support.v4.app.Fragment {

    private OnFragmentInteractionListener mListener;

    private int id;
    private ClassTable classTable;
    private TableHelper helper;
    private Bundle args;

    public static AttendanceFragment newInstance(int id) {
        AttendanceFragment fragment = new AttendanceFragment();
        Bundle args = new Bundle();
        args.putInt(DetailActivity.TAG_CLASS_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    public AttendanceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            // getArguments()がnull返したらNullPointerException投げるように
            args = getArguments();
        } catch (NullPointerException e) {
            throw new NullPointerException(this.getClass().getSimpleName()
                    + " must be instanced with id of ClassTable as its argument");
        }
        id = getArguments().getInt(DetailActivity.TAG_CLASS_ID);
        helper = new TableHelper(getActivity());
        classTable = helper.getClassTableById(id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_attendance, container, false);

        NumberPicker attendPicker = (NumberPicker) rootView.findViewById(R.id.picker_attend);
        NumberPicker absentPicker = (NumberPicker) rootView.findViewById(R.id.picker_absent);
        NumberPicker latePicker = (NumberPicker) rootView.findViewById(R.id.picker_late);

        // TODO: DBから出席状況についてのデータ取ってきてNumberPickerらにセット

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
