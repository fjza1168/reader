package com.ldp.reader;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.blankj.utilcode.util.AppUtils;
import com.ldp.reader.utils.EncryptUtils;
import com.ldp.reader.utils.MD5Utils;
import com.mob.MobSDK;
import com.tencent.bugly.crashreport.CrashReport;


/**
 * Created by ldp on 17-4-15.
 */

public class App extends Application {
    private static final String TAG   = App.class.getSimpleName();
    private static Context sInstance;


    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        CrashReport.initCrashReport(getApplicationContext(), "ab86f05cf4", true);
//        startService(new Intent(getContext(), DownloadService.class));
        // 初始化内存分析工具

        MobSDK.submitPolicyGrantResult(true, null);
        getCertificateMD5();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static Context getContext(){
        return sInstance;
    }

    private void getCertificateMD5()
    {
//        Log.d(TAG, "getCertificateMD5: " + AppUtils.getAppSignaturesMD5());
    }
}