package com.takeuseatcalls.data.remote;

import android.content.Context;

import com.takeuseatcalls.data.ContactsContentProvider;
import com.takeuseatcalls.model.CallContact;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleOnSubscribe;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ContactRepo {
    private final ContactApi contactApi;
    private final Context context;

    public ContactRepo(ContactApi contactApi, Context context) {
        this.contactApi = contactApi;
        this.context = context;
    }

    public Single<CallContact> getContactUserByNumber(String number) {


        return Single.create((SingleOnSubscribe<CallContact>) emitter -> {

            emitter.onSuccess(ContactsContentProvider.getContactByNumber(context, number));

        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(contact -> {
                    if (contact.getPhone().isEmpty()) return contactApi.getContact(number);
                    return Single.just(contact);
                });
    }
}
