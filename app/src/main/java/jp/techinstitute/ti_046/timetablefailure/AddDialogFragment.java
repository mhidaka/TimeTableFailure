package jp.techinstitute.ti_046.timetablefailure;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class AddDialogFragment extends DialogFragment {
    private static final String ARG_CLASS_ID = "class_id";

    private int class_id;

    public static AddDialogFragment newInstance(int class_id) {
        AddDialogFragment fragment = new AddDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CLASS_ID, class_id);
        fragment.setArguments(args);
        return fragment;
    }

    public AddDialogFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_add_dialog, container, false);
        Button button = (Button) rootView.findViewById(R.id.btn_add);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 新規授業作成処理
            }
        });
        return rootView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        return builder.create();
    }
}
