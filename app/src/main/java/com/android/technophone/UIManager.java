package com.android.technophone;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by User on 27.06.2017.
 */

public class UIManager extends Toast {

    Context mContext;

    public UIManager(Context context) {
        super(context);
        mContext = context;
    }

    public void showToast(String message){
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
    }
}
