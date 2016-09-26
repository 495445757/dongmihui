package com.dongmihui.ui.dialog;

import android.app.ProgressDialog;

/**
 * Created by Administrator on 2016/7/5.
 */
public interface DialogControl {
    public abstract void hideWaitDialog();

    public abstract ProgressDialog showWaitDialog();

    public abstract ProgressDialog showWaitDialog(int resid);

    public abstract ProgressDialog showWaitDialog(String text);
}
