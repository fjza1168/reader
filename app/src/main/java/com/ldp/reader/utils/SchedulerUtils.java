package com.ldp.reader.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;

import com.ldp.reader.App;
import com.ldp.reader.service.RefreshJobService;
import com.ldp.reader.service.RefreshService;

import static android.app.job.JobInfo.BACKOFF_POLICY_LINEAR;
import static android.app.job.JobInfo.NETWORK_TYPE_UNMETERED;

/**
 * @author ldp
 */
public class SchedulerUtils {
    private static PendingIntent pi;
    private static AlarmManager alarm;
    private static JobInfo.Builder builder;
    private static JobScheduler jobScheduler;
    public static void setWakeAtTime(Context cxt, int delay) {
         pi = PendingIntent.getService(cxt, 0, new Intent(cxt, RefreshService.class), PendingIntent.FLAG_UPDATE_CURRENT);
         alarm = (AlarmManager) cxt.getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            //Android 6，针对省电优化
            alarm.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + delay, pi);

        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            //Android 4.4，针对set不准确
            alarm.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + delay, pi);
        }
        else {
            alarm.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + delay, pi);
        }
    }

    public static void cancelAlarm(Context cxt) {
        alarm = (AlarmManager) cxt.getSystemService(Context.ALARM_SERVICE);
        pi = PendingIntent.getService(cxt, 0, new Intent(cxt, RefreshService.class), PendingIntent.FLAG_UPDATE_CURRENT);

        if (alarm != null) {
            alarm.cancel(pi);
        }
    }

    public static void setJobScheduler() {
        int jobId = App.getContext().getPackageName().hashCode();
        builder = new JobInfo.Builder(jobId, new ComponentName(App.getContext(), RefreshJobService.class))
                .setPersisted(true)
                .setBackoffCriteria(30000, BACKOFF_POLICY_LINEAR)
                .setRequiresDeviceIdle(false)
//                .setMinimumLatency(JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS)
                .setMinimumLatency(5*60*1000)
//                .setMinimumLatency(1*60*1000)

                .setOverrideDeadline(JobInfo.MAX_BACKOFF_DELAY_MILLIS)
                .setRequiredNetworkType(NETWORK_TYPE_UNMETERED);


        jobScheduler = (JobScheduler) App.getContext().getSystemService(Context.JOB_SCHEDULER_SERVICE);

        assert jobScheduler != null;
        jobScheduler.cancel(jobId);
        jobScheduler.schedule(builder.build());
    }


    public static  void cancelJobScheduler() {
        int jobId = App.getContext().getPackageName().hashCode();
        jobScheduler = (JobScheduler) App.getContext().getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.cancel(jobId);
    }

}