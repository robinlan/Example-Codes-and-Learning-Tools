package mahout.fansy.utils.fpg;

import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.util.ReflectionUtils;
import org.apache.mahout.common.HadoopUtil;

public class ReadAndWritePatterns {

	/*
	 * 私有化 构造函数，只能使用方法调用
	 */
	private ReadAndWritePatterns(){}
	
	/**
	 * 读取<key,value>序列数据，写入HDFS文件系统
	 * @param input 输入序列文件
	 * @param output 输出HDFS文件（txt格式）
	 * @param jobtracker 使用的jobtracker地址
	 * @param regex  使用的解析类，用于解析key和value
	 * @return 任务是否成功
	 * @throws IOException
	 */
	public static boolean readAndWritePatterns(String input,String output,String jobtracker,AKVRegex regex) throws IOException{
		boolean flag=true;
		
		Configuration conf=new Configuration();
		conf.set("mapred.job.tracker", jobtracker);
		
		FileSystem fsIn = FileSystem.get(URI.create(input), conf);
		FileSystem fsOut = FileSystem.get(URI.create(output), conf);
		HadoopUtil.delete(conf, new Path(output));
	    Path pathIn = new Path(input);
	    Path pathOut = new Path(output);
	    
	    SequenceFile.Reader reader = null;
	    FSDataOutputStream out = fsOut.create(pathOut);
	    try {
	      reader = new SequenceFile.Reader(fsIn, pathIn, conf);
	      Writable key = (Writable)
	        ReflectionUtils.newInstance(reader.getKeyClass(), conf);
	      Writable value = (Writable)
	        ReflectionUtils.newInstance(reader.getValueClass(), conf);
	      while (reader.next(key, value)) {
	    	  // read
	    	  String k=regex.keyRegex(key); 
	    	  String v=regex.valueRegex(value);
	          // write
	    	  if("".equals(v)||v==null){
	    		  continue;
	    	  }
	    	  out.writeChars(k+"\t"+v+"\n");
	      }
	    } catch(IOException e){
	    	flag=false;
	    }finally {
	      IOUtils.closeStream(reader);
	    }
		return flag;
	}
}
