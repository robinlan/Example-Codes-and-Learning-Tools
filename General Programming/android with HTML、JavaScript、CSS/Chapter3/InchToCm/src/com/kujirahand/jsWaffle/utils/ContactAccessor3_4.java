package com.kujirahand.jsWaffle.utils;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.provider.Contacts;
import android.provider.Contacts.ContactMethodsColumns;
import android.provider.Contacts.People;

public class ContactAccessor3_4 extends ContactAccessor {
	
	public Intent getContactPickerIntent() {  
		  return new Intent(Intent.ACTION_PICK, People.CONTENT_URI);  
	}
	
	public HashMap<String, String> getContactData(Activity a, Intent data) {
		HashMap<String, String> result = new HashMap<String, String>();
		Cursor c = null;
		String id = "";
		String name = "";
		String email = "";
		String number = "";
		try {
			c = a.managedQuery(data.getData(), null, null, null, null);
			if (c.moveToFirst()) {
				id = c.getString(c.getColumnIndexOrThrow(People._ID));
			}
			if (c.moveToFirst()) {
				name = c.getString(c.getColumnIndexOrThrow(People.NAME));
			}
			if (c.moveToFirst()) {
				number = c.getString(c.getColumnIndexOrThrow(People.NUMBER));
			}
			c = a.getContentResolver().query(
				Contacts.ContactMethods.CONTENT_EMAIL_URI, null,
				Contacts.ContactMethods.PERSON_ID + " = ?",
				new String[] { id }, null);
			while (c.moveToNext()) {
				int emailIndex = c.getColumnIndexOrThrow(ContactMethodsColumns.DATA);
				String s = c.getString(emailIndex);
				if (email != "") email += ",";
				email += s;
			}
			// set info
			result.put("name", name);
			result.put("number", number);
			result.put("email", email);
		} finally {
			if (c != null) {
				c.close();
			}
		}
		return result;
	}
}
