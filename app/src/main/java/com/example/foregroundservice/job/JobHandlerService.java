package com.example.foregroundservice.job;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * 保活 （一）- Job Service ，JobScheduler
 * https://www.jianshu.com/p/e33818032531
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class JobHandlerService extends JobService {

    private JobScheduler mJobScheduler;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.e("JobHandlerService  onStartCommand");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
            JobInfo.Builder builder = new JobInfo.Builder(startId++,
                    new ComponentName(getPackageName(), JobHandlerService.class.getName()));

            builder.setPeriodic((1000 * 60 * 2));//设置间隔时间

            builder.setRequiresCharging(true);// 设置是否充电的条件,默认false

            builder.setRequiresDeviceIdle(true);// 设置手机是否空闲的条件,默认false

            builder.setPersisted(true);//设备重启之后你的任务是否还要继续执行

            if (mJobScheduler.schedule(builder.build()) <= 0) {
                Logger.e("JobHandlerService  工作失败");
            } else {
                Logger.e("JobHandlerService  工作成功");
            }
        }
        return START_STICKY;
    }


    @Override
    public boolean onStartJob(JobParameters params) {
        Logger.e("JobHandlerService  服务启动");
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Logger.e("JobHandlerService  服务停止");
        return false;
    }


    // 服务是否运行
    public boolean isServiceRunning(String serviceName) {
        boolean isRunning = false;
        ActivityManager am = (ActivityManager) this
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> lists = am.getRunningAppProcesses();
        // 获取运行服务再启动
        for (ActivityManager.RunningAppProcessInfo info : lists) {
            System.out.println(info.processName);
            if (info.processName.equals(serviceName)) {
                isRunning = true;
            }
        }
        return isRunning;
    }
}


