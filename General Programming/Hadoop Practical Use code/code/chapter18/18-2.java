package cn.edu.ruc.cloudcomputing.book.chapter18;

import java.io.IOException;
import java.util.Iterator;

import org.apache.Hadoop.mapred.MapReduceBase;
import org.apache.Hadoop.mapred.OutputCollector;
import org.apache.Hadoop.mapred.Reducer;
import org.apache.Hadoop.mapred.Reporter;

import org.apache.Hadoop.io.Text;
import org.apache.Hadoop.io.LongWritable;
import org.apache.Hadoop.io.DoubleWritable;
 
public class HadoopReducer extends MapReduceBase implements Reducer<Text,LongWritable,Text,DoubleWritable> {
public void reduce(Text key, Iterator<LongWritable> value, OutputCollector<Text, DoubleWritable> output, Reporter reporter)
           throws IOException {
 
        double pi = 0;
        double inside = 0;
        double outside = 0;
 
        while (value.hasNext())
        {
        	if (value.next().get() == (long)1)
        	    inside++;
        	else
        		outside++;
        }
 
        pi = (4*inside)/(inside + outside);
 
        output.collect(new Text ("pi"), new DoubleWritable(pi));
    }
}
