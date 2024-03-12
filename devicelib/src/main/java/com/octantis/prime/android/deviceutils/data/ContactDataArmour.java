package com.octantis.prime.android.deviceutils.data;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;


import com.octantis.prime.android.deviceutils.DeviceMain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ContactDataArmour implements Serializable {

    public static long serialVersionUID = 4290939387652332756L;

    public String name = "";
    public String mobile = "";
    public String updated_time;
    public String lastTimeContacted;
    public int timesContacted;
    public int starred;
    public String email = "";

    public static List<ContactDataArmour> getContactList(List<ContactDataArmour> contactDataArmourList) {
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        ContentResolver contentResolver = DeviceMain.getApp().getContentResolver();
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        if (cursor == null) {
            return contactDataArmourList;
        }
        while (cursor.moveToNext()) {
            String contactId = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
            Cursor query = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                    null, null, null);
            if (query == null) {
                return contactDataArmourList;
            }
            while (query.moveToNext()) {
                ContactDataArmour contactInfo = new ContactDataArmour();
                contactInfo.name = query.getString(query.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                contactInfo.mobile = query.getString(query.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                contactInfo.updated_time = query.getString(query.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.CONTACT_LAST_UPDATED_TIMESTAMP));
                contactInfo.lastTimeContacted = query.getString(query.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.LAST_TIME_CONTACTED));
                contactInfo.timesContacted = query.getInt(query.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.TIMES_CONTACTED));
                contactInfo.starred = query.getInt(query.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.STARRED));
                @SuppressLint("Recycle") Cursor emails = contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId, null, null);
                if (emails == null) {
                    return contactDataArmourList;
                }
                while (emails.moveToNext()) {
                    contactInfo.email = emails.getString(emails.getColumnIndexOrThrow(
                            ContactsContract.CommonDataKinds.Email.DATA));
                }
                contactDataArmourList.add(contactInfo);
            }
            if (!query.isClosed()) {
                query.close();
            }
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }
        return contactDataArmourList;
    }

    public static List<ContactDataArmour> getContactList1() {
        List<ContactDataArmour> contactDataArmourList = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        Cursor cursor = null;
        try {
            ContentResolver contentResolver = DeviceMain.getApp().getContentResolver();
            cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, null, null, null, null);
            while (cursor.moveToNext()) {
                ContactDataArmour contactInfo = new ContactDataArmour();
                contactInfo.name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                contactInfo.mobile = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                contactInfo.updated_time = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.CONTACT_LAST_UPDATED_TIMESTAMP));
                contactInfo.lastTimeContacted = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.LAST_TIME_CONTACTED));
                contactInfo.timesContacted = cursor.getInt(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.TIMES_CONTACTED));
                contactInfo.starred = cursor.getInt(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.STARRED));
                contactDataArmourList.add(contactInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        long endTime = System.currentTimeMillis(); //结束时间
        long runTime = endTime - startTime;
        System.out.println("runTime = " + runTime);
        return contactDataArmourList;
    }


    public static List<ContactDataArmour> getContactListActivity(List<ContactDataArmour> contactDataArmourList) {
        Cursor cursor = null;
        try {
            ContentResolver contentResolver = DeviceMain.getApp().getContentResolver();
            cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, null, null, null, null);
            if (cursor == null) {
                return contactDataArmourList;
            }
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String number = cursor.getString(cursor.getColumnIndexOrThrow(
                        ContactsContract.CommonDataKinds.Phone.NUMBER));
                ContactDataArmour contactInfo = new ContactDataArmour();
                contactInfo.name = name;
                contactInfo.mobile = number;
                contactDataArmourList.add(contactInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return contactDataArmourList;
    }


}
