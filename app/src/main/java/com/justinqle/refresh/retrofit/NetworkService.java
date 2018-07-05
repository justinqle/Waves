package com.justinqle.refresh.retrofit;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.justinqle.refresh.activities.MainActivity;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*
Singleton
 */
public class NetworkService {

    private static NetworkService mInstance;
    private static final String BASE_URL = "https://oauth.reddit.com";
    private static Retrofit mRetrofit;

    private NetworkService() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.getContextOfApplication());

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor((chain) -> {
                    final String accessToken = sharedPreferences.getString("access_token", null);
                    Request request = chain.request().newBuilder()
                            .addHeader("Authorization", "bearer " + accessToken)
                            .addHeader("User-Agent", "android:com.justinqle.refresh:v1.0.0 (by /u/doctor_re)")
                            .addHeader("Accept", "application/json")
                            .build();
                    return chain.proceed(request);
                }).authenticator(new TokenAuthenticator())
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    public static NetworkService getInstance() {
        if (mInstance == null) {
            mInstance = new NetworkService();
        }
        return mInstance;
    }

    public RedditApi getJSONApi() {
        return mRetrofit.create(RedditApi.class);
    }

}
