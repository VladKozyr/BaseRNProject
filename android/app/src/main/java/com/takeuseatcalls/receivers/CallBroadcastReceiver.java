package com.takeuseatcalls.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.takeuseatcalls.domain.call.CallManager;
import com.takeuseatcalls.service.CallService;
import com.takeuseatcalls.utils.Constants;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CallBroadcastReceiver extends BroadcastReceiver {

    @Inject
    public CallManager callManager;

    @Override
    public void onReceive(Context context, Intent intent) {

        switch (intent.getAction()) {
            case Constants.ACCEPT_CALL:
                CallService.startCallActivity(context);
                callManager.accept();
                break;
            case Constants.DECLINE_CALL:
                callManager.reject();
        }
    }
}
