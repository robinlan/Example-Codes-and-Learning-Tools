package cn.edu.ruc.cloudcomputing.book.chapter16;

/*student.java*/
import java.io.File;
import java.io.IOException;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericData.Record;
import org.apache.avro.util.Utf8;

public class student {
	String fileName = "student.db";
	String prefix = "{\"type\":\"record\",\"name\":\"Student\",\"fields\":[";	
String suffix = "]}";
     String fieldSID= "{\"name\":\"SID\",\"type\":\"int\"}";
	String fieldName ="{\"name\":\"Name\",\"type\":\"string\"}";
	String fieldDept ="{\"name\":\"Dept\",\"type\":\"string\"}";
     String fieldPhone="{\"name\":\"Phone\",\"type\":\"string\"}";
	String fieldAge = "{\"name\":\"Age\",\"type\":\"int\"}";
	Schema studentSchema = Schema.parse(prefix + fieldSID + ","+ fieldName + "," + fieldDept + ","  + fieldPhone + ","  + fieldAge + suffix);
	Schema extractSchema = Schema.parse(prefix + fieldName + "," + fieldPhone + suffix);
        int SID=0;
	
	public static void main(String[] args) throws IOException {
		student st = new student();
		st.init();
		st.print();
		st.printExtraction();
	}
     /**
      *初始化添加学生记录
      **/ 
	public void init() throws IOException {		
	DataFileWriter<Record> writer = new DataFileWriter<Record>(
		new GenericDatumWriter<Record>(studentSchema)).create(
				studentSchema, new File(fileName));
		try {
		writer.append(createStudent("Zhanghua", "Law", "15201161111", 25));
                   writer.append(createStudent("Lili", "Economy", "15201162222", 24));
                   writer.append(createStudent("Wangyu", "Information", "15201163333", 25));
                   writer.append(createStudent("Zhaoxin", "Art", "15201164444", 23));
                   writer.append(createStudent("Sunqin", "Physics", "15201165555", 25));
                   writer.append(createStudent("Zhouping", "Math", "15201166666", 23));
			
		} finally {
			writer.close();
		}
	}

	/**
      *将学生信息添加到记录中
      **/ 
	private Record createStudent(String name, String dept,String phone, int age) {
		Record student = new GenericData.Record(studentSchema);
            student.put("SID", (++SID));
		student.put("Name", new Utf8(name));
		student.put("Dept", new Utf8(dept));
            student.put("Phone", new Utf8(phone));
		student.put("Age", age);
            System.out.println("Successfully added "+name);
		return student;
	}

     /**
      *输出学生信息
      **/ 
	public void print() throws IOException {		
		GenericDatumReader<Record> dr = new GenericDatumReader<Record>();
		dr.setExpected(studentSchema);
		DataFileReader<Record> reader = new DataFileReader<Record>(new File(fileName), dr);
	System.out.println("\nprint all the records from database");
		try {
			while (reader.hasNext()) {
			Record student = reader.next();
		System.out.println(student.get("SID").toString()+" "+student.get("Name")+" "+student.get("Dept")+" "+student.get("Phone")+" "+student.get("Age").toString());
			}
		} finally {
			reader.close();
		}
	}
	/**
      *输出学生姓名和电话
      **/
	public void printExtraction() throws IOException {		
		GenericDatumReader<Record> dr = new GenericDatumReader<Record>();
		dr.setExpected(extractSchema);
		DataFileReader<Record> reader = new DataFileReader<Record>(new File(fileName), dr);
		System.out.println("\nExtract Name & Phone of the records from database");
		try {
			while (reader.hasNext()) {
			Record student = reader.next();
		System.out.println(student.get("Name").toString() + " " + student.get("Phone").toString() + "\t");
			}
		} finally {
			reader.close();
		}
	}
}
