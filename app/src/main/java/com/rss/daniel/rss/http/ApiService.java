package com.rss.daniel.rss.http;

import android.util.Log;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;


/**
 * Created by https://gist.github.com/macsystems/01d7e80554efd344b1f9
 */
public class ApiService {

    public static <T> T createXmlAdapterFor(final Class<T> api) {
        Retrofit retrofit = new Retrofit.Builder()
       // https://github.com/square/retrofit/issues/1194
                .baseUrl("https://github.com/square/retrofit/issues/1194/")
                .client(new OkHttpClient()) // Use OkHttp3 client
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // RxJava adapter
                .addConverterFactory(SimpleXmlConverterFactory.create()) // Simple XML converter
                .build();
        return retrofit.create(api);
    }
}
