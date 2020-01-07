package com.ldp.reader.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.util.Log;

import com.ldp.reader.App;
import com.ldp.reader.utils.SchedulerUtils;
import com.ldp.reader.utils.SharedPreUtils;


/**
 * @author ldp
 */
public class RefreshJobService extends JobService {
    private static final String TAG = RefreshJobService.class.getSimpleName();
    private  static String REFRESH_JOB_SERVICE_STATUS = "refresh_job_service_status";

    private volatile static RefreshJobService service;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        service = this;
        SharedPreUtils.getInstance().putBoolean(REFRESH_JOB_SERVICE_STATUS,true);

//        Vibrator vibrator = (Vibrator) App.getContext().getSystemService(App.getContext().VIBRATOR_SERVICE);
//        vibrator.vibrate(400);
        Log.d(TAG,"onStartJob+振动");
        if(!RefreshService.isServiceRunning()){
            Log.d(TAG,"RefreshService未启动");

            SchedulerUtils.cancelAlarm(App.getContext());
            SchedulerUtils.setWakeAtTime(App.getContext(),0);
        }
        SchedulerUtils.cancelJobScheduler();
        SchedulerUtils.setJobScheduler();

//        checkUpdate();
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        service = null;
        SharedPreUtils.getInstance().putBoolean(REFRESH_JOB_SERVICE_STATUS,false);

        return false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SharedPreUtils.getInstance().putBoolean(REFRESH_JOB_SERVICE_STATUS,true);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        service = null;
        SharedPreUtils.getInstance().putBoolean(REFRESH_JOB_SERVICE_STATUS,false);

        super.onDestroy();
    }


    public static  boolean isServiceRunning() {
        return  SharedPreUtils.getInstance().getBoolean(REFRESH_JOB_SERVICE_STATUS,false);

    }

}
