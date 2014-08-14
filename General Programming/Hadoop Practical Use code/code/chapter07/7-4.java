package cn.edu.ruc.cloudcomputing.book.chapter07;

import java.io.*;
import org.apache.hadoop.io.*;

public class NumPair implements WritableComparable<NumPair> {
private LongWritable line;
	private LongWritable location;
	public NumPair() {
		set(new LongWritable(0), new LongWritable(0));
	}
	public void set(LongWritable first, LongWritable second) 
{
		this.line=first;
		this.location=second;		
	}
public NumPair(LongWritable first,LongWritable second){
		set(first,second);
	}
	public NumPair(int first, int second){
	set(new LongWritable(first),new LongWritable(second));
	}
	public LongWritable getLine(){
		return line;
	}
	public LongWritable getLocation(){
		return location;
	}
	@Override
	public void readFields(DataInput in) throws IOException 
{
		line.readFields(in);
		location.readFields(in);
	}
	@Override
	public void write(DataOutput out) throws IOException {
		line.write(out);
		location.write(out);		
	}
	public boolean equals(NumPair o){
		if((this.line==o.line)&&(this.location==o.location))
				return true;
		return false;
	}
	@Override
	public int hashCode(){
		return line.hashCode()*13+location.hashCode();
	}
	@Override
	public int compareTo(NumPair o) {
	if((this.line==o.line)&&(this.location==o.location))
			return 0;
		return -1;
	}
}
