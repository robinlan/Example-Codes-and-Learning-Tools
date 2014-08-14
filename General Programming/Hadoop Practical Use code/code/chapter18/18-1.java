package cn.edu.ruc.cloudcomputing.book.chapter18;

import java.util.Random;
import java.io.IOException;

import org.apache.Hadoop.mapred.MapReduceBase;
import org.apache.Hadoop.mapred.Mapper;
import org.apache.Hadoop.mapred.OutputCollector;
import org.apache.Hadoop.mapred.Reporter;

import org.apache.Hadoop.io.Text;
import org.apache.Hadoop.io.LongWritable;
/**
 *
 */
public class HadoopMapper extends MapReduceBase implements Mapper<Text,Text,Text,LongWritable> {
 
public void map(Text key, Text value, OutputCollector<Text, LongWritable> output, Reporter reporter)
            throws IOException {
 
    	Random generator = new Random();
    	int i;
 
    	final int iter = 100000;
 
    	for (i =0; i < iter; i++)
    	{
    		double x = generator.nextDouble();
    		double y = generator.nextDouble();
 
    		double z;
 
    		z = x*x + y*y;
 
    		if (z <= 1)
    			output.collect(new Text("VALUE"), new LongWritable(1));
    		else
    			output.collect(new Text ("VALUE"), new LongWritable(0));
 
    	}
    }
}
}
