package com.ldp.reader.service;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import android.util.Log;

import com.ldp.reader.App;
import com.ldp.reader.R;
import com.ldp.reader.RxBus;
import com.ldp.reader.model.bean.BookChapterBean;
import com.ldp.reader.model.bean.BookDetailBean;
import com.ldp.reader.model.bean.CollBookBean;
import com.ldp.reader.model.bean.DownloadTaskBean;
import com.ldp.reader.model.bean.BookDetailBeanInBiquge;
import com.ldp.reader.model.bean.packages.BookChapterPackageByBiquge;
import com.ldp.reader.model.local.BookRepository;
import com.ldp.reader.model.remote.RemoteRepository;
import com.ldp.reader.ui.activity.ReadActivity;
import com.ldp.reader.utils.SchedulerUtils;
import com.ldp.reader.utils.MD5Utils;
import com.ldp.reader.utils.NetworkUtils;

import com.ldp.reader.utils.SharedPreUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.ldp.reader.ui.activity.ReadActivity.EXTRA_COLL_BOOK;
import static com.ldp.reader.ui.activity.ReadActivity.EXTRA_IS_COLLECTED;
import static java.util.concurrent.Executors.newSingleThreadExecutor;

public class RefreshService extends Service {
    private volatile static RefreshService service;
    private  static String REFRESH_SERVICE_STATUS = "refresh_service_status";

    private  static String TAG = RefreshService.class.getSimpleName();
    String id = "novel_update";
    String name="小说更新";

    PowerManager powerManager;
    PowerManager.WakeLock localWakeLock;
    Disposable disposable;


    @Override
    public void onCreate() {
        Log.d(TAG,"+创建服务");
        service = this;
        SharedPreUtils.getInstance().putBoolean(REFRESH_SERVICE_STATUS,true);
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        service = this;
        Log.d(TAG,"+启动服务");
        SharedPreUtils.getInstance().putBoolean(REFRESH_SERVICE_STATUS,true);

//        SchedulerUtils.setWakeAtTime(App.getContext(),10*60*1000);
        SchedulerUtils.setWakeAtTime(App.getContext(),10*60*1000);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        }
        localWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, this.getClass().getCanonicalName());
        if(!localWakeLock.isHeld()){
            ;// 申请设备电源锁
            localWakeLock.acquire(24*60*60*1000L /*10 minutes*/);
        }
        if (!RefreshJobService.isServiceRunning()) {
            Log.d(TAG,"+RefreshJobService未启动");
            SchedulerUtils.setJobScheduler();
        }

                if ( !NetworkUtils.isWifiConnected(App.getContext()) ) {

                    return START_STICKY ;
                }

                List<CollBookBean> collBookBeans = BookRepository
                        .getInstance().getCollBooks();
                if (collBookBeans == null || collBookBeans.isEmpty()){
                    return START_STICKY;
                }
        Log.d(TAG, "onStartCommand: " +collBookBeans );
                if(null == disposable){
                    disposable = Observable.interval(0,10, TimeUnit.MINUTES).subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            Log.d(TAG, "accept: "+ aLong);
                            if ( !NetworkUtils.isWifiConnected(App.getContext()) ) {

                                return  ;
                            }
//                            checkUpdate(collBookBeans);

                        }
                    });
                }




//                Single.concat(observables)
//                        .observeOn(Schedulers.io())
//                        .subscribeOn(Schedulers.io())
//                        .subscribe(new Consumer<BookDetailBean>() {
//                            @Override
//                            public void accept(BookDetailBean bookDetailBean) throws Exception {
//
//                                CollBookBean oldCollBook = collBooks.get(0);
//                                CollBookBean newCollBook = bookDetailBean.getCollBookBean();
//                                for (CollBookBean collBookItem : collBooks) {
//                                    if (collBookItem.getTitle().equals(newCollBook.getTitle())) {
//                                        oldCollBook = collBookItem;
//                                        break;
//                                    }
//                                }
//
//                                Log.d("+?2缓存", oldCollBook.getLastChapter() + "   " + newCollBook.getTitle() + newCollBook.getLastChapter());
//                                //如果是oldBook是update状态，或者newCollBook与oldBook章节数不同
//
//
//                                if (!oldCollBook.getLastChapter().equals(newCollBook.getLastChapter())) {
//                                    Log.d("+更新书籍", newCollBook.getTitle() + newCollBook.getLastChapter());
//                                    newCollBook.setUpdate(true);
//                                    showNotification(newCollBook);
//
//                                    Vibrator vibrator = (Vibrator) App.getContext().getSystemService(App.getContext().VIBRATOR_SERVICE);
//                                    vibrator.vibrate(4000);
//                                } else {
//                                    Log.d("+?3缓存", "运行");
//                                    newCollBook.setUpdate(false);
//                                }
//                                newCollBook.setLastRead(oldCollBook.getLastRead());
//                                newCollBooksMerge.add(newCollBook);
//                                Log.d("+?4缓存", "运行");
//                            }
//                        }, new Consumer<Throwable>() {
//                            @Override
//                            public void accept(Throwable throwable) throws Exception {
//                                Log.e("+更新请求错误",throwable.getMessage() + throwable.toString());
//
//                            }
//                        }, new Action() {
//                            @Override
//                            public void run() throws Exception {
//                                Log.d("+?5缓存", "运行");
//                                updateCategory(newCollBooksMerge);
//                                //异步存储到数据库中
//                                BookRepository.getInstance().saveCollBooks(newCollBooksMerge);
//                                Log.d("+?5缓存", "运行");
//
//                            }
//                        });




        return START_STICKY_COMPATIBILITY;
    }

    private void checkUpdate(List<CollBookBean> collBookBeans) {
        List<CollBookBean> collBooks = new ArrayList<>(collBookBeans);
        List<Single<BookDetailBean>> observables = new ArrayList<>(collBooks.size());
        List<Single<BookDetailBeanInBiquge>> observablesInBiquge = new ArrayList<>();

        Iterator<CollBookBean> it = collBooks.iterator();
        while (it.hasNext()){
            CollBookBean collBook = it.next();
            //删除本地文件
            if (collBook.isLocal()) {
                it.remove();
            }
            else {
//                        observables.add(RemoteRepository.getInstance().getBookDetail(collBook.get_id()));
//                        if (null == collBook.getBookIdInBiquge()|| collBook.getBookIdInBiquge().isEmpty()) {
//
//                            continue;
//                        }

                Log.d(TAG,"+笔趣阁ID "+  collBook.getBookIdInBiquge());
//                observablesInBiquge.add(RemoteRepository.getInstance().getBookDetailByBiquge(collBook.get_id()));
            }
        }
        Log.d(TAG, "onStartCommand: " +"请求网络前" );

        List<CollBookBean> newCollBooksMerge = new ArrayList<CollBookBean>(observables.size());
        Single.concat(observablesInBiquge)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<BookDetailBeanInBiquge>() {
                    @Override
                    public void accept(BookDetailBeanInBiquge bookDetailBeanInBiquge) throws Exception {

                        CollBookBean oldCollBook = collBooks.get(0);

                        for (CollBookBean collBookItem : collBooks) {
                            if (collBookItem.getTitle().equals(bookDetailBeanInBiquge.getData().getName())) {
                                oldCollBook = collBookItem;
                                break;
                            }
                        }

                        //如果是oldBook是update状态，或者newCollBook与oldBook章节数不同
                        showNotification(oldCollBook);
                        updateCategory(oldCollBook);

                        if (!oldCollBook.getLastChapter().equals(bookDetailBeanInBiquge.getData().getLastChapter())) {
                            oldCollBook.setLastChapter(bookDetailBeanInBiquge.getData().getLastChapter());
                            Log.d(TAG,"+更新书籍 "+oldCollBook.getTitle() + oldCollBook.getLastChapter());
                            updateCategory(oldCollBook);
                            oldCollBook.setUpdate(true);
                            showNotification(oldCollBook);

                            Vibrator vibrator = (Vibrator) App.getContext().getSystemService(App.getContext().VIBRATOR_SERVICE);
                            vibrator.vibrate(4000);
                        } else {
                            oldCollBook.setUpdate(false);
                        }
                        newCollBooksMerge.add(oldCollBook);
                        Log.d(TAG,"+更新" );
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
//                                updateCategory(newCollBooksMerge);
//                                //异步存储到数据库中
                        BookRepository.getInstance().saveCollBooks(newCollBooksMerge);

//                                Log.d("+?5缓存", "运行");
                    }
                });
    }


    public void createDownloadTask(CollBookBean collBookBean) {
        ExecutorService folder =  Executors.newSingleThreadExecutor();
        folder.submit(new Runnable() {
            @Override
            public void run() {

                Log.d("+1消息","发送");

                DownloadTaskBean task = new DownloadTaskBean();
                Log.d("+2消息","发送");

                task.setTaskName(collBookBean.getTitle());
                Log.d("+3消息",collBookBean.getTitle());

                task.setBookId(collBookBean.get_id());
                Log.d("+4消息","发送");

                task.setBookChapters(collBookBean.getBookChapters());
                Log.d("+5消息","发送");

                task.setLastChapter(collBookBean.getBookChapters().size());
                Log.d("+6消息","发送");

                RxBus.getInstance().post(task);
                Log.d("+7消息","发送");
            }
        });


    }
    //更新每个CollBook的目录
    private void updateCategory(CollBookBean collBookBean) {
        List<Single<BookDetailBeanInBiquge>> observablesInBiquge = new ArrayList<>();

        List<BookChapterBean> bookChapterBeans = new ArrayList<>();

           if(true){
               return;
           }
        Disposable disposable = RemoteRepository.getInstance()
                .getChapterListByBiquge(collBookBean.get_id())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<BookChapterPackageByBiquge>() {
                    @Override
                    public void accept(BookChapterPackageByBiquge bookChapterPackageByBiquge) throws Exception {

                        List<BookChapterPackageByBiquge.DataBean.ListBeanX.ListBean> listBeans = new ArrayList<>();
                        for (BookChapterPackageByBiquge.DataBean.ListBeanX mlistBeanX : bookChapterPackageByBiquge.getData().getList()) {
                            if (null != mlistBeanX && null != mlistBeanX.getList()) {
                                listBeans.addAll(mlistBeanX.getList());
                            }
                        }
                        for (int i = 0; i < listBeans.size() - 1; i++) {
                            if (null != listBeans.get(i)) {

                                BookChapterBean bookChapterBeanTemp = new BookChapterBean();
                                bookChapterBeanTemp.setLink("BQG" + String.valueOf(listBeans.get(i).getId()));
                                bookChapterBeanTemp.setTitle(listBeans.get(i).getName());
                                bookChapterBeanTemp.setValidInZhuishu(false);
                                bookChapterBeanTemp.setId(MD5Utils.strToMd5By16(bookChapterBeanTemp.getLink()));
                                Log.d(TAG, "+章节名  " + i + " " + listBeans.get(i).getName());
                                bookChapterBeanTemp.setBookId(collBookBean.get_id());

                                bookChapterBeans.add(bookChapterBeanTemp);

                            }
                        }
//                        BookRepository.getInstance()
//                                .saveBookChaptersWithAsync(bookChapterBeans);
                        //设置目录
                        collBookBean.setBookChapters(bookChapterBeans);
                        //存储收藏
                        BookRepository.getInstance()
                                .saveCollBookWithAsync(collBookBean);
//                        mView.succeedToBookShelf();

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        Log.e(TAG, throwable.getCause() + "   " + throwable.getMessage());
//                        mView.errorToBookShelf();

                    }
                });


    }


    private synchronized void showNotification(CollBookBean newCollBook) {
        Intent openIntent = new Intent(this, ReadActivity.class)
                .putExtra(EXTRA_IS_COLLECTED, true)
                .putExtra(EXTRA_COLL_BOOK, newCollBook);
        openIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, newCollBook.getTitle().hashCode(), openIntent, 0);



        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(App.getContext());
//
//        notificationManagerCompat.notify(newCollBook.getTitle().hashCode(), notification);
//        notificationManager.notify(newCollBook.getTitle().hashCode(), notification);
        Log.d(TAG,"+通知");


        Notification notification ;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH);
            Log.i(TAG, mChannel.toString());
            assert notificationManager != null;
            notificationManager.createNotificationChannel(mChannel);
//            notification = new Notification.Builder(this)
//                    .setChannelId(id)
//                    .setContentTitle("5 new messages")
//                    .setContentText("hahaha")
//                    .setSmallIcon(R.mipmap.ic_launcher).build();

           notification = new Notification.Builder(this)
                   .setChannelId(id)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setContentIntent(pendingIntent)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setAutoCancel(true)
                   .setShowWhen(true)
                   .setContentTitle(newCollBook.getTitle())
                    .setContentText(newCollBook.getLastChapter())
                    .setDefaults(Notification.DEFAULT_ALL)
                    .build();
        } else {
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setChannelId(id)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setContentIntent(pendingIntent)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setAutoCancel(true)
                    .setShowWhen(true)
                    .setContentTitle(newCollBook.getTitle())
                    .setContentText(newCollBook.getLastChapter())
                    .setDefaults(Notification.DEFAULT_ALL);
            notification = notificationBuilder.build();
        }
        assert notificationManager != null;
        notificationManager.notify(newCollBook.getTitle().hashCode(), notification);


    }

    @Override
    public void onDestroy() {
       service = null;
        Log.d(TAG,"+onDestroy");
        SharedPreUtils.getInstance().putBoolean(REFRESH_SERVICE_STATUS,false);
        if (localWakeLock !=null&& localWakeLock.isHeld()) {
            localWakeLock.setReferenceCounted(false);
            localWakeLock.release();
            localWakeLock =null;
        }
        disposable.dispose();
        SchedulerUtils.setWakeAtTime(App.getContext(),10*60*1000);

        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        SharedPreUtils.getInstance().putBoolean(REFRESH_SERVICE_STATUS,false);
        SchedulerUtils.setWakeAtTime(App.getContext(),10*60*1000);

        super.onLowMemory();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        SharedPreUtils.getInstance().putBoolean(REFRESH_SERVICE_STATUS,false);

        return super.onUnbind(intent);
    }

    @Override
    public void onTrimMemory(int level) {
        SharedPreUtils.getInstance().putBoolean(REFRESH_SERVICE_STATUS,false);
        SchedulerUtils.setWakeAtTime(App.getContext(),10*60*1000);
        super.onTrimMemory(level);
    }


    public static boolean isServiceRunning() {
        return  SharedPreUtils.getInstance().getBoolean(REFRESH_SERVICE_STATUS,false);
    }
}
