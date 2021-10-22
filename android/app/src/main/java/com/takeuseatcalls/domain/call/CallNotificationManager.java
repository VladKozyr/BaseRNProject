package com.takeuseatcalls.domain.call;

import static android.app.NotificationManager.IMPORTANCE_HIGH;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telecom.Call;
import android.view.View;
import android.widget.RemoteViews;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.takeuseatcalls.R;
import com.takeuseatcalls.data.remote.ContactRepo;
import com.takeuseatcalls.model.CallContact;
import com.takeuseatcalls.receivers.CallBroadcastReceiver;
import com.takeuseatcalls.ui.PhoneActivity;
import com.takeuseatcalls.utils.Constants;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CallNotificationManager {

    public static final int ID = 420;
    public static final String CHANNEL_ID = "call_notification_channel";
    public static final int PRIORITY = NotificationCompat.PRIORITY_LOW;
    private static final int ACCEPT_CALL_CODE = 0;
    private static final int DECLINE_CALL_CODE = 1;

    private final NotificationManagerCompat notificationManager;
    private final ContactRepo contactRepo;
    private final CallManager callManager;
    private final Context context;

    private final PendingIntent contentPendingIntent;

    @RequiresApi(api = Build.VERSION_CODES.O)
    private final NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Call", NotificationManager.IMPORTANCE_DEFAULT);

    @Inject
    public CallNotificationManager(
            NotificationManagerCompat notificationManager,
            ContactRepo contactRepo, CallManager callManager,
            @ApplicationContext Context context) {
        this.notificationManager = notificationManager;
        this.contactRepo = contactRepo;
        this.callManager = callManager;
        this.context = context;

        Intent mainIntent = new Intent(context, PhoneActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        contentPendingIntent = PendingIntent.getActivity(context, 0, mainIntent, 0);
    }

    public void setupNotification() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(channel);
        }

        String number = callManager.getCurrentNumber();

        contactRepo.getContactUserByNumber(number)
                .observeOn(Schedulers.io())
                .safeSubscribe(new SingleObserver<CallContact>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull CallContact callContact) {
                        buildNotification(callContact);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        CallContact contact = new CallContact();
                        contact.setPhone(number);
                        buildNotification(contact);
                    }
                });
    }

    public void buildNotification(CallContact contact) {
        int callState = callManager.getState();
        int contextTextID = 0;

        switch (callState) {
            case Call.STATE_RINGING:
                contextTextID = R.string.is_calling;
                break;
            case Call.STATE_DIALING:
                contextTextID = R.string.dialing;
                break;
            case Call.STATE_DISCONNECTING:
                contextTextID = R.string.call_status_disconnecting;
                break;
            case Call.STATE_DISCONNECTED:
                contextTextID = R.string.call_status_disconnected;
                break;
            default:
                contextTextID = R.string.ongoing_call;
        }

        String name = contact.getFirstName() + contact.getLastName();
        if (name.trim().isEmpty()) name = contact.getPhone();

        RemoteViews notificationView = new RemoteViews(context.getPackageName(), R.layout.call_notification);
        notificationView.setTextViewText(R.id.notification_caller_name, name);
        notificationView.setTextViewText(R.id.notification_call_status, context.getString(contextTextID));
        notificationView.setOnClickPendingIntent(R.id.notification_decline_call, getCallPendingIntent(Constants.DECLINE_CALL, DECLINE_CALL_CODE));
        notificationView.setOnClickPendingIntent(R.id.notification_accept_call, getCallPendingIntent(Constants.ACCEPT_CALL, ACCEPT_CALL_CODE));

        int visibility = View.VISIBLE;
        if (callState != Call.STATE_RINGING) visibility = View.GONE;

        notificationView.setViewVisibility(R.id.notification_accept_call, visibility);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_phone_vector)
                .setContentIntent(contentPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(Notification.CATEGORY_CALL)
                .setCustomContentView(notificationView)
                .setOngoing(true)
                .setSound(null)
                .setUsesChronometer(callState == Call.STATE_ACTIVE)
                .setChannelId(CHANNEL_ID)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle());

        Notification notification = builder.build();
        notificationManager.notify(ID, notification);
    }

    public void cancelNotification() {
        notificationManager.cancel(ID);
    }

    private Intent getCallIntent(String callAction) {
        Intent intent = new Intent(context, CallBroadcastReceiver.class);
        intent.setAction(callAction);
        return intent;
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private PendingIntent getCallPendingIntent(String callAction, int rc) {
        return PendingIntent.getBroadcast(
                context,
                rc,
                getCallIntent(callAction),
                PendingIntent.FLAG_CANCEL_CURRENT);
    }
}
