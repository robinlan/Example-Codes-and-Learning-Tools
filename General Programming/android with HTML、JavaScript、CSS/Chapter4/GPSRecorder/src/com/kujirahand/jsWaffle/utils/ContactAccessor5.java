package com.kujirahand.jsWaffle.utils;

import java.lang.reflect.Field;
import java.util.HashMap;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

/*
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.CommonDataKinds.Email;
*/

public class ContactAccessor5 extends ContactAccessor {
	// ref) Contact API -- http://kaotaro.blogspot.com/2010/07/android-tipscontacts-api.html
	// ref) Contact API -- http://www.coderanch.com/t/512048/Android/Mobile/Contact-API
	// ref) URI -- http://android.roof-balcony.com/shori/content-provider/getcontent/
	// ref) reflection -- http://www.ne.jp/asahi/hishidama/home/tech/java/reflection.html#h2_field
	// ref) http://www.electrodream.jp/iphonedev/index.php/2010/04/%E7%9D%80%E4%BF%A1%E9%9F%B3%E3%82%A2%E3%83%97%E3%83%AA%E3%80%81%E7%B0%A1%E5%8D%98%E3%81%A8%E3%81%AF%E3%81%84%E3%81%88%E3%82%84%E3%81%AF%E3%82%8A%E3%81%84%E3%82%8D%E3%81%84%E3%82%8D%E3%81%82%E3%82%8B/
	
	Class<?> _ContactsContract;
	Class<?> _ContactsContract_Contacts;
	Class<?> _CommonDataKinds_Email;
	Class<?> _CommonDataKinds_Phone;
	
	final static String ContactsContact = "android.provider.ContactsContract";
	final static String ContactsContract_Contacts = "android.provider.ContactsContract$Contacts";
	final static String ContactsContract_CommonDataKinds = "android.provider.ContactsContract$CommonDataKinds";
	
	public ContactAccessor5() {
		_ContactsContract = c(ContactsContact);
		_ContactsContract_Contacts = c(ContactsContract_Contacts);
		_CommonDataKinds_Email = c(ContactsContract_CommonDataKinds + "$Email");
		_CommonDataKinds_Phone = c(ContactsContract_CommonDataKinds + "$Phone");
	}
	
	public Class<?> c(String className) {
		try {
			return Class.forName(className);
		} catch (Exception e) {
			return null;
		}
	}
	
	public Uri getFieldUri(Class<?> klass, String field) {
		if (klass == null) return null;
		try {
			Field f = klass.getField(field);
			if (f == null) return null;
			return (Uri)f.get(null);
		} catch (Exception e) {
			return null;
		}
	}
	
	public String getFieldStr(Class<?> klass, String field) {
		if (klass == null) return null;
		try {
			Field f = klass.getField(field);
			if (f == null) return null;
			return (String)f.get(null);
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override  
    public Intent getContactPickerIntent() {  
		Uri uri = getFieldUri(_ContactsContract_Contacts, "CONTENT_URI");
        return new Intent(Intent.ACTION_PICK, uri);  
    }

	@Override
	public HashMap<String, String> getContactData(Activity a, Intent data) {
		HashMap<String, String> result = new HashMap<String, String>();
		Cursor c = null;
		String id = "";
		String name = "";
		String email = "";
		String number = "";
		try {
			ContentResolver cr = a.getContentResolver();
			c = a.managedQuery(data.getData(),null, null, null, null);
			if (c.moveToFirst()) {
				id = c.getString(c.getColumnIndexOrThrow(
						getFieldStr(_ContactsContract_Contacts, "_ID"))); 
			}
			if (c.moveToFirst()) {
				name = c.getString(c.getColumnIndexOrThrow(
						getFieldStr(_ContactsContract_Contacts, "DISPLAY_NAME")));
			}
			if (c.moveToFirst()) {
				number = c.getString(c.getColumnIndexOrThrow(
						getFieldStr(_ContactsContract_Contacts, "DISPLAY_NAME")));
			}
			// --- email
			Uri cu = getFieldUri(_CommonDataKinds_Email, "CONTENT_URI");
			String ci = getFieldStr(_CommonDataKinds_Email, "CONTACT_ID");
			c = cr.query(
					cu, null, ci + " = ?",
					new String[] { id }, null);
			while (c.moveToNext()) {
				int emailIndex = c.getColumnIndex(getFieldStr(_CommonDataKinds_Email, "DATA"));
				String s = c.getString(emailIndex);
				if (email != "") email += ",";
				email += s;
			}
			// --- phone number
			cu = getFieldUri(_CommonDataKinds_Phone, "CONTENT_URI");
			ci = getFieldStr(_CommonDataKinds_Phone, "CONTACT_ID");
			c = cr.query(
					cu, null, ci + " = ?",
					new String[] { id }, null);
			if (c.moveToFirst()) {
				int phoneIndex = c.getColumnIndex(getFieldStr(_CommonDataKinds_Phone, "DATA"));
				number = c.getString(phoneIndex);
			}
		} finally {
			if (c != null) c.close();
		}
		result.put("name", name);
		result.put("email", email);
		result.put("number", number);
		return result;
	}  
}
