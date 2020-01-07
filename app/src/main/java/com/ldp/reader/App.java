package com.ldp.reader;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by ldp on 17-4-15.
 */

public class App extends Application {
    private static Context sInstance;


    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        CrashReport.initCrashReport(getApplicationContext(), "ab86f05cf4", true);
//        startService(new Intent(getContext(), DownloadService.class));

        // 初始化内存分析工具
        if (!LeakCanary.isInAnalyzerProcess(this)) {
            LeakCanary.install(this);
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static Context getContext(){
        return sInstance;
    }
}