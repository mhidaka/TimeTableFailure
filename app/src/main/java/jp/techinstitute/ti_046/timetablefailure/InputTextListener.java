package jp.techinstitute.ti_046.timetablefailure;

import android.app.Dialog;

import java.util.EventListener;

// TODO: 名前これでいいか？
public interface InputTextListener extends EventListener {
    // 文字が入力されていない状態通知
    public void noInputText();
    // 文字が入力されてる状態通知
    public void inputText();
}
