package cn.edu.ruc.cloudcomputing.book.chapter04;

import java.io.IOException;
import java.util.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;
import org.apache.hadoop.util.*;

public class WordCount extends Configured implements Tool {
	public static class Map extends Mapper<LongWritable, Text, Text, IntWritable> {	
	    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String line = value.toString();
              //以“#”为分隔符，将输入的文件分割成单个记录
			StringTokenizer tokenizerArticle = new StringTokenizer(line,"#"); 
			//对每个记录进行处理
			while(tokenizerArticle.hasMoreTokens()){
				//将每个记录分成姓名和分数两个部分
				StringTokenizer tokenizerLine = new StringTokenizer(tokenizerArticle.nextToken());
				while(tokenizerLine.hasMoreTokens()){
					String strName = tokenizerLine.nextToken();   
					if(tokenizerLine.hasMoreTokens()){
						String strScore = tokenizerLine.nextToken();
						Text name = new Text(strName);//姓名  
						int scoreInt = Integer.parseInt(strScore);//该组织的状况得分
						context.write(name, new IntWritable(scoreInt));
					}
				}
			}
		}
	}

	
	public static class Reduce extends Reducer<Text, IntWritable, Text, IntWritable> {
		public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
			int sum = 0;
			int count=0;
			Iterator<IntWritable> iterator = values.iterator();
			while (iterator.hasNext()){
				sum += iterator.next().get();	
				count++;
			} 
			int average = (int) sum/count;
			context.write(key, new IntWritable(average));
		}
	}

	public int run(String [] args) throws Exception {
	     Job job = new Job(getConf());
	     job.setJarByClass(Score_Process.class);
	     job.setJobName("Score_Process");
	     job.setOutputKeyClass(Text.class);
	     job.setOutputValueClass(IntWritable.class);
	     job.setMapperClass(Map.class);
	     job.setCombinerClass(Reduce.class);
	     job.setReducerClass(Reduce.class);
	     job.setInputFormatClass(TextInputFormat.class);
	     job.setOutputFormatClass(TextOutputFormat.class);
	
	     FileInputFormat.setInputPaths(job, new Path(args[0]));
	     FileOutputFormat.setOutputPath(job, new Path(args[1]));
	     boolean success = job.waitForCompletion(true);
	     return success ? 0 : 1;
	}
	public static void main(String[] args) throws Exception {
	     int ret = ToolRunner.run(new Score_Process(), args);
	     System.exit(ret);
	}
}


