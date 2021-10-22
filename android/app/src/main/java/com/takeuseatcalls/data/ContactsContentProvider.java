package com.takeuseatcalls.data;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.takeuseatcalls.model.CallContact;

public class ContactsContentProvider {

    public static CallContact getContactByNumber(Context context, String number) {

        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        String[] projection = {ContactsContract.PhoneLookup.DISPLAY_NAME,
                ContactsContract.PhoneLookup.NUMBER,
                ContactsContract.PhoneLookup.PHOTO_URI};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);

        String name = "";
        String phoneNumber = "";
        String photoUri = "";

        try {
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    name = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
                    photoUri = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.PHOTO_URI));
                    phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.NUMBER));
                }
            }
        } catch (Exception e) {
            cursor.close();
        }

        CallContact contact = new CallContact();
        contact.setFirstName(name);
        contact.setPhotoUrl(photoUri);
        contact.setPhone(phoneNumber);

        return contact;
    }
}
