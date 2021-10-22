package com.takeuseatcalls.di;

import android.content.Context;

import androidx.core.app.NotificationManagerCompat;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ServiceComponent;
import dagger.hilt.android.qualifiers.ApplicationContext;

@Module
@InstallIn(ServiceComponent.class)
public abstract class ServiceModule {

    @Provides
    public static NotificationManagerCompat provideNotificationManager(@ApplicationContext Context context) {
        return NotificationManagerCompat.from(context);
    }
}
