package com.ldp.reader.model.remote;

import android.util.Log;

import com.ldp.reader.utils.Constant;
import com.ldp.reader.utils.LenientGsonConverterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ldp on 17-4-20.
 */

public class RemoteHelper {
    private static final String TAG = "RemoteHelper";
    private static RemoteHelper sInstance;
    private Retrofit mRetrofit,mRetrofitByOwn;
    private OkHttpClient mOkHttpClient;
    private RemoteHelper(){
        Interceptor logIntercept = new HttpLoggingInterceptor();
        ((HttpLoggingInterceptor) logIntercept).setLevel(HttpLoggingInterceptor.Level.BODY);
        mOkHttpClient = new OkHttpClient.Builder()
                .addInterceptor(logIntercept)
                .addInterceptor(
                        chain -> {
                            Request request = chain.request()
                                    .newBuilder()
                                    .removeHeader("User-Agent")//移除旧的
//                                        .addHeader("User-Agent", WebSettings.getDefaultUserAgent(App.getContext()))//添加真正的头部
                                    .addHeader("User-Agent", " okhttp/3.5.0")//添加真正的头部
                                    .build();

                            HttpUrl requestUrl = request.url();
                            Response response = null;
                            try {
                                response = chain.proceed(request);
                            }
                            catch (IOException e){
                                Log.d(TAG, "intercept: ");

                            }
                            return response;
                        }
                ).readTimeout(70, TimeUnit.SECONDS).retryOnConnectionFailure(true).build();

        mRetrofit = new Retrofit.Builder()
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(Constant.API_BASE_URL)
                .build();
        mRetrofitByOwn= new Retrofit.Builder()
                .client(mOkHttpClient)
                .addConverterFactory(LenientGsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(Constant.API_BASE_URL_OWN)
                .build();

    }

    public static RemoteHelper getInstance(){
        if (sInstance == null){
            synchronized (RemoteHelper.class) {
                if (sInstance == null) {
                    sInstance = new RemoteHelper();
                }
            }
        }
        return sInstance;
    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }

    public Retrofit getRetrofitByOwn() {
        return mRetrofitByOwn;
    }


    public OkHttpClient getOkHttpClient(){
        return mOkHttpClient;
    }
}
