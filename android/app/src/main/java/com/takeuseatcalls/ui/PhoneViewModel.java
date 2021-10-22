package com.takeuseatcalls.ui;

import android.media.AudioManager;
import android.telecom.Call;
import android.telecom.CallAudioState;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.takeuseatcalls.data.remote.ContactRepo;
import com.takeuseatcalls.domain.call.CallManager;
import com.takeuseatcalls.model.CallContact;
import com.takeuseatcalls.model.CallState;

import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class PhoneViewModel extends ViewModel {
    private final CallManager callManager;
    private final AudioManager audioManager;
    private final Call.Callback callback = new Call.Callback() {
        @Override
        public void onStateChanged(Call call, int state) {
            super.onStateChanged(call, state);
            callState.setValue(CallState.getState(state));
        }
    };

    public MutableLiveData<CallState> callState = new MutableLiveData<>();
    public MutableLiveData<CallContact> callContact = new MutableLiveData<>();

    public MutableLiveData<Boolean> isSpeakerOn = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> isMicrophoneMute = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> isDialupShown = new MutableLiveData<>(false);
    public MutableLiveData<String> dialpadString = new MutableLiveData<>("");

    @Inject
    public PhoneViewModel(CallManager callManager, AudioManager audioManager, ContactRepo contactRepo) {
        this.callManager = callManager;
        this.audioManager = audioManager;

        callManager.registerCallback(callback);
        callState.setValue(CallState.getState(callManager.getState()));

        String number = callManager.getCurrentNumber();
        contactRepo.getContactUserByNumber(number)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .safeSubscribe(new SingleObserver<CallContact>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onSuccess(@NonNull CallContact contact) {
                        callContact.setValue(contact);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        CallContact contact = new CallContact();
                        contact.setPhone(number);
                        callContact.setValue(contact);
                    }
                });
    }

    public void onSpeakerToggleSpeaker() {
        boolean state = !Objects.equals(isSpeakerOn.getValue(), true);
        isSpeakerOn.setValue(state);
        audioManager.setSpeakerphoneOn(state);

        int newRoute = CallAudioState.ROUTE_EARPIECE;
        if (state) {
            newRoute = CallAudioState.ROUTE_SPEAKER;
        }

        if (callManager.call != null) {
            callManager.inCallService.setAudioRoute(newRoute);
        }
    }

    public void onMicrophoneToggle() {
        boolean state = !Objects.equals(isMicrophoneMute.getValue(), true);
        isMicrophoneMute.setValue(state);
        audioManager.setMicrophoneMute(state);

        if (callManager.inCallService != null) {
            callManager.inCallService.setMuted(state);
        }
    }

    public void onDialupToggle() {
        isDialupShown.setValue(!Objects.equals(isDialupShown.getValue(), true));
    }

    public void onCallStart() {
        callManager.accept();
    }

    public void onCallEnd() {
        callManager.reject();
    }

    public void onDialpadPress(char c) {
        dialpadString.setValue(dialpadString.getValue() + c);
        callManager.dialpadPress(c);
    }

    @Override
    protected void onCleared() {
        callManager.unregisterCallback(callback);
    }
}
