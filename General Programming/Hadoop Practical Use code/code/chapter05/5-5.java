package cn.edu.ruc.cloudcomputing.book.chapter05;

import java.io.IOException;
import java.util.*;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class MTjoin {
  public static int time = 0;

  public static class Map extends Mapper<Object, Text, Text, Text>{
 //在map中先区分输入行属于左表还是右表，然后对两列值进行分割，
//连接列保存在key值，剩余列和左右表标志保存在value中，最后输出
    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
       String line = value.toString();
       int i = 0;
		//输入文件首行，不处理
       if(line.contains("factoryname") == true || line.contains("addressID") == true){
    	   return;
       }
		//找出数据中的分割点
       while(line.charAt(i) >= '9' || line.charAt(i) <= '0'){
		i++;
	}
       
  if(line.charAt(0) >= '9'||line.charAt(0) <= '0') {
  //左表
	   int j = i-1; 
	   while(line.charAt(j) != ' ')  j--;
     String[] values = {line.substring(0,j),line.substring(i)};    	   
    	context.write(new Text(values[1]), new Text("1+" + values[0]));
       }
       else{ //右表
	   int j = i + 1;
	   while(line.charAt(j) != ' ')  j++;
           String[] values = {line.substring(0,i+1),line.substring(j)};
    	   context.write(new Text(values[0]), new Text("2+" + values[1])); 
    	   
       }
    }
  }
  
  public static class Reduce extends Reducer<Text,Text,Text,Text> {
//Reduce解析map输出，将value中数据按照左右表分别保存，然后求//笛卡儿积，输出
	public void reduce(Text key, Iterable<Text> values,Context context) throws IOException, InterruptedException {
    	
	 if(time == 0){  //输出文件第一行
	 	context.write(new Text("factoryname"),new Text("addressname"));
	 	time++;
	 }
	    	
	 int factorynum = 0;
	 String factory[] = new String[10];
	 int addressnum = 0;
	 String address[] = new String[10];
	 Iterator ite = values.iterator();
	 while(ite.hasNext())
	 {
	 	String record = ite.next().toString();
	    	int len = record.length();
	    	int i = 2;
	    	char type = record.charAt(0);
	    	String factoryname = new String();
	    	String addressname = new String();
	    	if(type == '1'){   //左表
	    		factory[factorynum] = record.substring(2);
	    		factorynum++;
	    	}
	    	else{  //右表
	    		address[addressnum] = record.substring(2);
	    		addressnum++;
	    	}
	 }
	 if(factorynum != 0&&addressnum != 0){  //求笛卡儿积
	 	for(int m = 0; m < factorynum; m++){
	 		for(int n = 0; n < addressnum; n++){
	 			context.write(new Text(factory[m]),new Text(address[n]));
	    		}
	    	}
	 }
	}
  }

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
    if (otherArgs.length != 2) {
      System.err.println("Usage: wordcount <in> <out>");
      System.exit(2);
    }
    Job job = new Job(conf, "multiple table join");
    job.setJarByClass(MTjoin.class);
    job.setMapperClass(Map.class);
    job.setReducerClass(Reduce.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(Text.class);
    FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
    FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
