package com.carlos.rxretrofit.api;

import java.util.Base64;
import java.util.PrimitiveIterator;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class WebService {

    private static final String BASE_URL = "https://api.github.com/users/";
    private static WebService instance;
    private Retrofit retrofit;
    private HttpLoggingInterceptor httpLoggingInterceptor;
    private OkHttpClient.Builder httpClientBuilder;

    public  WebService () {
        httpLoggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClientBuilder = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor);
        retrofit = new Retrofit.Builder()
                  .baseUrl(BASE_URL)
                  .client(httpClientBuilder.build())
                  .addConverterFactory(GsonConverterFactory.create())
                  .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                  .build();
    }

    public  static synchronized WebService getInstance(){
        if(instance == null){
            instance = new WebService();
        }
        return  instance;
    }


    public WebServiceApi createService(){
        return  retrofit.create(WebServiceApi.class);
    }


}
