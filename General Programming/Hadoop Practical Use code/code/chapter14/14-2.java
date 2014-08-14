package cn.edu.ruc.cloudcomputing.book.chapter14;

import java.io.IOException;
import org.apache.pig.PigServer;
public class tst_local{ 
	public static void main(String[] args) {
		try {
			PigServer pigServer = new PigServer("local");
			runIdQuery(pigServer, "/path/Student");//µ÷ÓÃº¯Êý
		}
		catch(Exception e) {}
	}
	public static void runIdQuery(PigServer pigServer, String inputFile) throws IOException {
		pigServer.registerQuery("A = load '" + inputFile + "' using PigStorage(':') as (Sno:chararray,Sname:chararray,Ssex:chararray,Sage:int,Sdept:chararray);");
		pigServer.registerQuery("B = foreach A generate Sname,Sage; ");
		pigServer.store("B", "/path/tstJavaLocal.out");
	}
}
