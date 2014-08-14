package cn.edu.ruc.cloudcomputing.book.chapter07;

import java.io.IOException;
import java.net.URI;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.*;

public class MapFileWriteFile {
private static final String[] myValue = {
	"hello world",
	"bye world",
	"hello hadoop",
	"bye hadoop"
	};
	public static void main(String[] args) throws IOException {
	String uri = "你想要生成SequenceFile的位置";
	Configuration conf = new Configuration();
	FileSystem fs = FileSystem.get(URI.create(uri), conf);
	IntWritable key = new IntWritable();
	Text value = new Text();
	MapFile.Writer writer = null;
	try {
		writer = new MapFile.Writer(conf, fs, uri,key.get
Class(), value.getClass());
		for (int i = 0; i < 500; i++) {
		key.set(i);
		value.set(myValue[i % myValue.length]);
		writer.append(key, value);
		}
		} finally {
		IOUtils.closeStream(writer);
		}
	}
}
