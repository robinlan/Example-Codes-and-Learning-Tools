package cn.edu.ruc.cloudcomputing.book.chapter07;

import java.io.*;
import java.util.*;
import org.apache.hadoop.io.*;

public class MyMapre {
	public static void main(String args[]) throws IOException{
		MapWritable a = new MapWritable();
		a.put(new IntWritable(1),new Text("Hello"));
		a.put(new IntWritable(2),new Text("World"));

		MapWritable b = new MapWritable();
		WritableUtils.cloneInto(b,a);
		System.out.println(b.get(new IntWritable(1)));
		System.out.println(b.get(new IntWritable(2)));
	}
}
