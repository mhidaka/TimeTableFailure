package jp.techinstitute.ti_046.timetablefailure;

import android.widget.EditText;

public class BlankTextNotify {

    private EditText editText = null;
    private InputTextListener listener = null;

    public BlankTextNotify(EditText editText) {
        this.editText = editText;
    }

    public boolean containsText(){
        boolean hasText = false;
        if(this.listener != null){
            if(this.editText.getText().toString().equals("")) {
                // テキストが入力されていない場合の通知を行う
                listener.noInputText();
                hasText = false;
            }else{
                // テキストが入力されている場合の通知を行う
                listener.inputText();
                hasText = true;
            }
        }
        return hasText;
    }

    public void setListener(InputTextListener listener) {
        this.listener = listener;
    }

    public void removeListener() {
        this.listener = null;
    }
}
