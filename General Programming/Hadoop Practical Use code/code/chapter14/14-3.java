package cn.edu.ruc.cloudcomputing.book.chapter14;

import java.io.IOException;
import org.apache.pig.PigServer;
public class tst_mapreduce{
	public static void main(String[] args) {
		try {
			PigServer pigServer = new PigServer("mapreduce");//mapreduce模式
			runIdQuery(pigServer,"/path/Student");//调用函数
		}
		catch(Exception e) {}
	}
	public static void runIdQuery(PigServer pigServer, String inputFile) throws IOException {
		pigServer.registerQuery("A = load '" + inputFile + "' using PigStorage(':') as (Sno:chararray,Sname:chararray,Ssex:chararray,Sage:int,Sdept:chararray);");
		pigServer.registerQuery("B = foreach A generate Sname,Sage; ");
		pigServer.store("B", "/path/tstJavaMapReduce.out");
	}
}
