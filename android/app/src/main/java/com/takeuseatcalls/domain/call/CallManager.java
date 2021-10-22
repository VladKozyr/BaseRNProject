package com.takeuseatcalls.domain.call;

import android.telecom.Call;
import android.telecom.InCallService;
import android.telecom.VideoProfile;

import javax.inject.Singleton;

@Singleton
public class CallManager {

    public Call call = null;

    public InCallService inCallService = null;

    public void registerCallback(Call.Callback callback) {

        if (call != null) {
            call.registerCallback(callback);
        }
    }

    public String getCurrentNumber() {
        String number = "";
        if (call != null) {
            String rawNumber = call.getDetails().getHandle().toString();
            if (rawNumber.startsWith("tel:")) {
                number = rawNumber.substring("tel:".length());
            }
        }
        return number;
    }

    public void unregisterCallback(Call.Callback callback) {
        if (call != null) {
            call.unregisterCallback(callback);
        }
    }

    public int getState() {
        if (call == null) {
            return Call.STATE_DISCONNECTED;
        } else {
            return call.getState();
        }
    }

    public void dialpadPress(char c) {
        if (call != null) {
            call.playDtmfTone(c);
            call.stopDtmfTone();
        }
    }

    public void accept() {
        if (call != null) {
            call.answer(VideoProfile.STATE_AUDIO_ONLY);
        }
    }

    public void reject() {
        if (call != null) {
            if (call.getState() == Call.STATE_RINGING) {
                call.reject(false, null);
            } else {
                call.disconnect();
            }
        }
    }
}