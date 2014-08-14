package cn.edu.ruc.cloudcomputing.book.chapter07;

import java.io.*;
import org.apache.hadoop.io.*;

public class MyMapre {
	public static void strings(){
		String s="\u0041\u00DF\u6771\uD801\uDC00";
		System.out.println(s.length());
		System.out.println(s.indexOf("\u0041"));
		System.out.println(s.indexOf("\u00DF"));
		System.out.println(s.indexOf("\u6771"));
		System.out.println(s.indexOf("\uD801\uDC00"));
	}
	public static void texts(){
		Text t = new Text("\u0041\u00DF\u6771\uD801\uDC00");
		System.out.println(t.getLength());
		System.out.println(t.find("\u0041"));
		System.out.println(t.find("\u00DF"));
		System.out.println(t.find("\u6771"));
		System.out.println(t.find("\uD801\uDC00"));
	}
	public static void main(String args[]){
		strings();
		texts();	
	}
}
