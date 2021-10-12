package com.takeuseatcalls.react;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

import android.content.Context;
import android.content.IntentFilter;

import java.util.Date;
import android.widget.*;

public class TakeUsEatCallsModule extends ReactContextBaseJavaModule {
    private static ReactApplicationContext reactContext;

    TakeUsEatCallsModule(ReactApplicationContext context) {
        super(context);
        reactContext = context;
    }

    @ReactMethod
    public void setup(
        final String baseUrl,
        final String refreshToken
    ) {
        // TODO: implement technical task starting from here
        Toast.makeText(reactContext, "Android Native Toast. Setup method", Toast.LENGTH_LONG).show();
    }

    @Override
    public String getName() {
        return "TakeUsEatCallsNative";
    }
}
