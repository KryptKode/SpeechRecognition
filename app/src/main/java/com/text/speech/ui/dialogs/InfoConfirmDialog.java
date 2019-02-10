package com.text.speech.ui.dialogs;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;


import com.text.speech.R;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

public class InfoConfirmDialog extends LovelyStandardDialog implements View.OnClickListener {
    private InfoListener listener;

    public InfoConfirmDialog(Context context, String title, String message, @DrawableRes int topIcon, InfoListener listener) {
        super(context, ButtonLayout.HORIZONTAL);
        this.listener = listener;
        setTopColorRes(R.color.colorPrimary);

        setIcon(topIcon == 0 ? R.drawable.ic_info_white : topIcon);
        setTitle(TextUtils.isEmpty(title) ? context.getString(R.string.info) : title);
        setMessage(TextUtils.isEmpty(message) ? context.getString(R.string.info_msg) : message);
        setPositiveButton(android.R.string.ok, this);
        /*setNegativeButton(android.R.string.cancel, v -> {
            dismiss();
        });*/


    }

    @Override
    public void onClick(View v) {
        dismiss();
        listener.onConfirm();
    }


    public static  void showDialog(Context context, String title, String message, @DrawableRes int topIcon, InfoListener listener){
        InfoConfirmDialog loadingDialog = new InfoConfirmDialog(context, title, message, topIcon, listener);
        loadingDialog.show();
    }

    public static  void showDialog(Context context, @StringRes int title, @StringRes int message, @DrawableRes int topIcon, InfoListener listener){
        InfoConfirmDialog loadingDialog = new InfoConfirmDialog(context, context.getString(title), context.getString(message), topIcon, listener);
        loadingDialog.show();
    }

    public static  void showDialog(Context context, InfoListener listener){
        InfoConfirmDialog loadingDialog = new InfoConfirmDialog(context, null, null, 0, listener);
        loadingDialog.show();
    }

    public interface InfoListener {
        void onConfirm();
    }
}
