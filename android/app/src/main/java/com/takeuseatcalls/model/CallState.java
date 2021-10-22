package com.takeuseatcalls.model;

import static android.telecom.Call.STATE_ACTIVE;
import static android.telecom.Call.STATE_CONNECTING;
import static android.telecom.Call.STATE_DIALING;
import static android.telecom.Call.STATE_DISCONNECTED;
import static android.telecom.Call.STATE_DISCONNECTING;
import static android.telecom.Call.STATE_HOLDING;
import static android.telecom.Call.STATE_NEW;
import static android.telecom.Call.STATE_RINGING;
import static android.telecom.Call.STATE_SELECT_PHONE_ACCOUNT;

import com.takeuseatcalls.R;

public enum CallState {
    UNKNOWN(-1, R.string.call_status_unknown),
    NEW(STATE_NEW, R.string.call_status_new),
    ACTIVE(STATE_ACTIVE, R.string.call_status_active),
    HOLDING(STATE_HOLDING, R.string.call_status_holding),
    DIALING(STATE_DIALING, R.string.call_status_dialing),
    INCOMING(STATE_RINGING, R.string.call_status_incoming),
    CONNECTING(STATE_CONNECTING, R.string.call_status_connecting),
    DISCONNECTED(STATE_DISCONNECTED, R.string.call_status_disconnected),
    DISCONNECTING(STATE_DISCONNECTING, R.string.call_status_disconnecting),
    SELECT_PHONE_ACCOUNT(STATE_SELECT_PHONE_ACCOUNT, R.string.call_status_phone_account);

    private final int stateActive;
    private final int stringRes;

    CallState(int stateActive, int stringRes) {
        this.stateActive = stateActive;
        this.stringRes = stringRes;
    }

    public int getStringRes() {
        return stringRes;
    }

    public static CallState getState(int state) {
        CallState callState = UNKNOWN;

        for (CallState s : CallState.values()) {
            if (s.stateActive == state) callState = s;
        }
        return callState;
    }
}