package com.takeuseatcalls.di;

import android.app.KeyguardManager;
import android.content.Context;
import android.media.AudioManager;
import android.os.PowerManager;
import android.os.Vibrator;

import com.takeuseatcalls.domain.call.CallManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class AppModule {

    @Provides
    public static AudioManager provideAudioManager(@ApplicationContext Context context) {
        return (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    @Provides
    public static Vibrator provideVibrator(@ApplicationContext Context context) {
        return (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Provides
    public static KeyguardManager provideKeyguardManager(@ApplicationContext Context context) {
        return (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
    }

    @Provides
    public static PowerManager providePowerManager(@ApplicationContext Context context) {
        return (PowerManager) context.getSystemService(Context.POWER_SERVICE);
    }

    @Provides
    @Singleton
    public static CallManager provideCallManager() {
        return new CallManager();
    }
}
