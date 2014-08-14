package cn.edu.ruc.cloudcomputing.book.chapter05;

import java.io.IOException;

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
import org.apache.hadoop.mapreduce.Partitioner;

public class Sort {

//map将输入中的value转化成IntWritable类型，作为输出的key
  public static class Map extends Mapper<Object, Text, IntWritable, IntWritable>{    

    private static IntWritable data = new IntWritable();      

    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
      String line = value.toString();
      data.set(Integer.parseInt(line));
      context.write(data, new IntWritable(1));
    }
  }
//reduce将输入的key复制到输出的value，然后根据输入的
//value-list中元素的个数决定key的输出次数
//用全局linenum来代表key的位次
  public static class Reduce extends Reducer<IntWritable,IntWritable,IntWritable,IntWritable> {
    private static IntWritable linenum = new IntWritable(1);

    public void reduce(IntWritable key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
      for (IntWritable val : values) {
        context.write(linenum , key);
	linenum = new IntWritable(linenum.get() + 1);
      }
    }
  }
//自定义Partition函数，此函数根据输入数据的最大值和MapReduce框架中
//Partition的数量获取将输入数据按照大小分块的边界，然后根据输入数值和
//边界的关系返回对应的Partition ID
   public static class Partition extends Partitioner <IntWritable,IntWritable> {
		@Override  
        public int getPartition(IntWritable key, IntWritable value, int numPartitions) {
			
	int Maxnumber = 65223;
	int bound = Maxnumber/numPartitions + 1;
	int keynumber = key.get();
	for(int i = 0; i < numPartitions; i++){
		if(keynumber < bound*i && keynumber >= bound*(i-1))
			return i-1;
		}
		return -1;
			
		}
  }

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
    if (otherArgs.length != 2) {
      System.err.println("Usage: wordcount <in> <out>");
      System.exit(2);
    }
    Job job = new Job(conf, "Sort");
    job.setJarByClass(Sort.class);
    job.setMapperClass(Map.class);
    job.setReducerClass(Reduce.class);
    job.setPartitionerClass(Partition.class);
    job.setOutputKeyClass(IntWritable.class);
    job.setOutputValueClass(IntWritable.class);
    FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
    FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
