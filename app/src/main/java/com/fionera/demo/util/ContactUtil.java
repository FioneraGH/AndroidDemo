package com.fionera.demo.util;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.fionera.demo.DemoApplication;

/**
 * Created by fionera on 16-1-12.
 */
public class ContactUtil {

    public static String getContact(Intent intent) {
        String contactName = "";
        String phoneNumber = "";
        Uri uri = intent.getData();
        ContentResolver cr = DemoApplication.getInstance().getContentResolver();
        Cursor cursor = cr.query(uri, null, null, null, null);
        while (cursor != null && cursor.moveToNext()) {
            contactName = cursor.getString(
                    cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id, null, null);

            if (phone != null && phone.moveToNext()) {
                phoneNumber = phone.getString(
                        phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            }
            if (phone != null) {
                phone.close();
            }
        }
        if (cursor != null) {
            cursor.close();
        }

        return contactName + "," + phoneNumber;
    }
}
