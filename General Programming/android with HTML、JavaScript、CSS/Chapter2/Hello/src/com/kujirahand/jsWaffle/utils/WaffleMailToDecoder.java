package com.kujirahand.jsWaffle.utils;

import java.net.URLDecoder;
import java.util.Hashtable;

public class WaffleMailToDecoder {
	
	public String protocol;
	public String mail;
	public String subject;
	public String body;
	public String attach;
	public Hashtable<String, String> query;
	
	public WaffleMailToDecoder(String uri) {
		String s = uri;
		int i;
		//
		i = s.indexOf(':');
		if (i >= 0) {
			protocol = s.substring(0, i);
			s = s.substring(i + 1);
		}
		//
		i = s.indexOf('?');
		if (i >= 0) {
			mail = s.substring(0, i);
			s = s.substring(i + 1);
		}
		//
		query = new Hashtable<String, String>();
		String[] args = s.split("&");
		for (int j = 0; j < args.length; j++) {
			String arg = args[j];
			String[] kv = arg.split("=", 2);
			String key = arg;
			String val = "";
			if (kv.length >= 2) {
				key = kv[0];
				val = kv[1];
			}
			try {
				key = URLDecoder.decode(key);
				val = URLDecoder.decode(val);
			} catch (Exception e) {
			}
			query.put(key, val);
		}
		try {
			subject = query.get("subject");
			body = query.get("body");
			attach = query.get("attach");
		}
		finally {
			if (subject == null) { subject = ""; }
			if (body == null) { body = ""; }
		}
	}
}
