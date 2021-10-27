package com.takeuseatcalls.service;

import static android.content.Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Context;
import android.content.Intent;
import android.telecom.Call;
import android.telecom.InCallService;

import com.takeuseatcalls.domain.call.CallManager;
import com.takeuseatcalls.domain.call.CallNotificationManager;
import com.takeuseatcalls.ui.PhoneActivity;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CallService extends InCallService {

    @Inject
    public CallNotificationManager callNotificationManager;

    @Inject
    public CallManager callManager;

    private final Call.Callback callListener = new Call.Callback() {
        @Override
        public void onStateChanged(Call call, int state) {
            super.onStateChanged(call, state);

            if (state != Call.STATE_DISCONNECTED && callManager.call == null) {
                callNotificationManager.setupNotification();
            }

            if (state == Call.STATE_ACTIVE) {
                //COUNTER
            } else if (state == Call.STATE_DISCONNECTED || state == Call.STATE_DISCONNECTING) {
                //COUNTER DETACH
            }
        }
    };

    @Override
    public void onCallAdded(Call call) {
        super.onCallAdded(call);

        startCallActivity(this);

        callManager.call = call;
        callManager.inCallService = this;
        callManager.registerCallback(callListener);
        callNotificationManager.setupNotification();
    }

    @Override
    public void onCallRemoved(Call call) {
        super.onCallRemoved(call);

        callManager.call = null;
        callManager.inCallService = null;
        callNotificationManager.cancelNotification();
    }

    public static void startCallActivity(Context context) {
        Intent intent = new Intent(context, PhoneActivity.class);
        intent.addFlags(FLAG_ACTIVITY_BROUGHT_TO_FRONT | FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
