package com.takeuseatcalls.react;

import android.app.Activity;
import android.app.role.RoleManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telecom.TelecomManager;
import android.widget.Toast;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.takeuseatcalls.utils.Constants;

public class TakeUsEatCallsModule extends ReactContextBaseJavaModule implements ActivityEventListener {

    TakeUsEatCallsModule(ReactApplicationContext context) {
        super(context);
        getReactApplicationContext().addActivityEventListener(this);
    }

    @ReactMethod
    public void setup(
            final String baseUrl,
            final String refreshToken
    ) {

        Activity activity = getCurrentActivity();
        assert activity != null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            RoleManager roleManager = (RoleManager) activity.getSystemService(Context.ROLE_SERVICE);

            if (roleManager != null &&
                    roleManager.isRoleAvailable(RoleManager.ROLE_DIALER) &&
                    !roleManager.isRoleHeld(RoleManager.ROLE_DIALER)) {

                Intent requestRoleIntent = roleManager.createRequestRoleIntent(RoleManager.ROLE_DIALER);
                activity.startActivityForResult(requestRoleIntent, Constants.REQUEST_CODE_SET_DEFAULT_DIALER);
            }
        } else {
            Intent defaultDialerIntent = new Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER);
            defaultDialerIntent.putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, activity.getPackageName());

            try {
                activity.startActivityForResult(defaultDialerIntent, Constants.REQUEST_CODE_SET_DEFAULT_DIALER);
            } catch (Exception ex) {
                //TODO send error for bridge promise
            }
        }

        Toast.makeText(getReactApplicationContext(), "Android Native Toast. Setup method", Toast.LENGTH_LONG).show();
    }

    @Override
    public String getName() {
        return "TakeUsEatCallsNative";
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onNewIntent(Intent intent) {
    }
}
