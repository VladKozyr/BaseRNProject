package com.takeuseatcalls.di;

import android.content.Context;

import com.takeuseatcalls.data.remote.ContactApi;
import com.takeuseatcalls.data.remote.ContactRepo;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public abstract class NetworkModule {

    @Provides
    public static OkHttpClient provideNetworkClient() {
        return new OkHttpClient.Builder()
                .build();
    }

    @Provides
    public static Retrofit provideRetrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl("https://takeuaseatcalls.com")
                .client(client)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    public static ContactApi provideContactApi(Retrofit retrofit) {
        return retrofit.create(ContactApi.class);
    }

    @Provides
    public static ContactRepo provideContactRepo(ContactApi contactApi, @ApplicationContext Context context) {
        return new ContactRepo(contactApi, context);
    }
}
