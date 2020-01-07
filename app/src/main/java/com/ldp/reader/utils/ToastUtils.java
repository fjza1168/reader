package com.ldp.reader.utils;

import android.widget.Toast;

import com.ldp.reader.App;

/**
 * Created by ldp on 17-5-11.
 */

public class ToastUtils {

    public static void show(String msg){
        Toast.makeText(App.getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
