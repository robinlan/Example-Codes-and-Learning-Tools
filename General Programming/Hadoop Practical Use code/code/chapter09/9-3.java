package cn.edu.ruc.cloudcomputing.book.chapter09;

import java.util.*;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.FileSystem;

public class FileCopyWithProgress {
	public static void main(String[] args) throws Exception {
		String localSrc = args[0];
		String dst = args[1];
		InputStream in = new BufferedInputStream(new FileInputStream(localSrc));
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(dst), conf);
		OutputStream out = fs.create(new Path(dst), new Progressable() {
			public void progress() {
				System.out.print("*");
			}
		});
		IOUtils.copyBytes(in, out, 4096, true);
	}
}
