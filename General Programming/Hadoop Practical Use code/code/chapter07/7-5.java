package cn.edu.ruc.cloudcomputing.book.chapter07;

import java.io.IOException;
import java.net.URI;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.*;

public class SequenceFileWriteDemo {
	private static String[] myValue = {
	"hello world",
	"bye world",
	"hello hadoop",
	"bye hadoop"
	};
	public static void main(String[] args) throws IOException {
	String uri = "你想要生成的SequenceFile的位置";
	Configuration conf = new Configuration();
	FileSystem fs = FileSystem.get(URI.create(uri), conf);
	Path path = new Path(uri);
	IntWritable key = new IntWritable();
	Text value = new Text();
	SequenceFile.Writer writer = null;
	try {
	writer = SequenceFile.createWriter(fs, conf, path,key.getClass(), value.getClass());
	for (int i = 0; i < 5000000; i++) {
	key.set(5000000 - i);
value.set(myValue[i%myValue.length]);
		writer.append(key, value);
		}
		} finally {
		IOUtils.closeStream(writer);
		}
	}
}
