/*student.c*/
#include <avro.h>
#include <inttypes.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

avro_schema_t student_schema;
/*id用于添加记录时为学生建立学号*/
int64_t id =0;

/*定义学生模式，拥有字段学号、姓名、学院、电话和年龄*/
#define STUDENT_SCHEMA \
"{\"type\":\"record\",\
  \"name\":\"Student\",\
  \"fields\":[\
      {\"name\": \"SID\", \"type\": \"long\"},\
      {\"name\": \"Name\", \"type\": \"string\"},\
      {\"name\": \"Dept\", \"type\": \"string\"},\
      {\"name\": \"Phone\", \"type\": \"string\"},\
      {\"name\": \"Age\", \"type\": \"int\"}]}"

/*把JSON定义的模式解析成模式的数据结构*/
void init(void)
{
      avro_schema_error_t error;
      if(avro_schema_from_json(STUDENT_SCHEMA,
sizeof(STUDENT_SCHEMA),
&student_schema,&error)){
              fprintf(stderr,"Failed to parse student schema\n");
              exit(EXIT_FAILURE);
       }
}

/*添加学生记录*/
void add_student(avro_file_writer_t db, const char *name, const char *dept, const char *phone, int32_t age)
{
	avro_datum_t student = avro_record("Student", NULL);

        avro_datum_t sid_datum = avro_int64(++id);
        avro_datum_t name_datum = avro_string(name);
        avro_datum_t dept_datum = avro_string(dept);
        avro_datum_t age_datum = avro_int32(age);
        avro_datum_t phone_datum = avro_string(phone);

     /*创建学生记录*/
	if (avro_record_set(student, "SID", sid_datum)
            || avro_record_set(student, "Name", name_datum)
            || avro_record_set(student, "Dept", dept_datum)
            || avro_record_set(student, "Age", age_datum)
            || avro_record_set(student, "Phone", phone_datum)) {
        fprintf(stderr, "Failed to create student datum structure");
        exit(EXIT_FAILURE);
        }
    
      /*将记录添加到数据库文件中*/
	 if (avro_file_writer_append(db, student)) {
         fprintf(stderr, "Failed to add student datum to database");
         exit(EXIT_FAILURE);
        }
	
     /*解除引用，释放内存空间*/
	avro_datum_decref(sid_datum);
     avro_datum_decref(name_datum);
     avro_datum_decref(dept_datum);
     avro_datum_decref(age_datum);
     avro_datum_decref(phone_datum);
     avro_datum_decref(student);

     fprintf(stdout, "Successfully added %s\n", name);
}

/*输出数据库中的学生信息*/
int show_student(avro_file_reader_t db, 
avro_schema_t reader_schema)
{
        int rval;
        avro_datum_t student;

        rval = avro_file_reader_read(db, reader_schema, &student);

        if (rval == 0) {
             int64_t i64;
             int32_t i32;
             char *p;
             avro_datum_t sid_datum, name_datum, dept_datum,
phone_datum, age_datum;

            if (avro_record_get(student, "SID", &sid_datum) == 0) {
                        avro_int64_get(sid_datum, &i64);
                        fprintf(stdout, "%"PRId64"  ", i64);
                }
          if (avro_record_get(student, "Name", &name_datum) == 0) {
                        avro_string_get(name_datum, &p);
                        fprintf(stdout, "%12s  ", p);
                }
          if (avro_record_get(student, "Dept", &dept_datum) == 0) {
                        avro_string_get(dept_datum, &p);
                        fprintf(stdout, "%12s  ", p);
                }
       if (avro_record_get(student, "Phone", &phone_datum) == 0) {
                        avro_string_get(phone_datum, &p);
                        fprintf(stdout, "%12s  ", p);
                }
            if (avro_record_get(student, "Age", &age_datum) == 0) {
                        avro_int32_get(age_datum, &i32);
                        fprintf(stdout, "%d", i32);
                }
                fprintf(stdout, "\n");

                /*释放记录*/
                avro_datum_decref(student);
        }
        return rval;
}

int main(void)
{
        int rval;
        avro_file_reader_t dbreader;
        avro_file_writer_t db;
        avro_schema_t extraction_schema, name_schema,
phone_schema;
        int64_t i;
        const char *dbname = "student.db";

        init();

        /*如果student.db存在，则删除*/
        unlink(dbname);
        /*创建数据库文件*/
        rval = avro_file_writer_create(dbname, student_schema, &db);
        if (rval) {
                fprintf(stderr, "Failed to create %s\n", dbname);
                exit(EXIT_FAILURE);
        }

        /*向数据库文件中添加学生信息*/
        add_student(db, "Zhanghua", "Law", "15201161111", 25);
        add_student(db, "Lili", "Economy", "15201162222", 24);
        add_student(db,"Wangyu","Information","15201163333", 25);
        add_student(db, "Zhaoxin", "Art", "15201164444", 23);
        add_student(db, "Sunqin", "Physics", "15201165555", 25);
        add_student(db, "Zhouping", "Math", "15201166666", 23);
        avro_file_writer_close(db);

        fprintf(stdout, "\nPrint all the records from database\n");

        /*读取并输出所有的学生信息*/
        avro_file_reader(dbname, &dbreader);
        for (i = 0; i < id; i++) {
                if (show_student(dbreader, NULL)) {
                        fprintf(stderr, "Error printing student\n");
                        exit(EXIT_FAILURE);
                }
        }
        avro_file_reader_close(dbreader);

        /*输出学生的姓名和电话信息*/
        extraction_schema = avro_schema_record("Student", NULL);
        name_schema = avro_schema_string();
        phone_schema = avro_schema_string();
        avro_schema_record_field_append(extraction_schema,
"Name", name_schema);
        avro_schema_record_field_append(extraction_schema, "Phone", phone_schema);

        /*只读取每个学生的姓名和电话*/
        fprintf(stdout,
                "\n\nExtract Name & Phone of the records from database\n");
        avro_file_reader(dbname, &dbreader);
        for (i = 0; i < id; i++) {
                if (show_student(dbreader, extraction_schema)) {
                        fprintf(stderr, "Error printing student\n");
                        exit(EXIT_FAILURE);
                }
        }
        avro_file_reader_close(dbreader);
        avro_schema_decref(name_schema);
        avro_schema_decref(phone_schema);
        avro_schema_decref(extraction_schema);

        /*最后释放学生模式*/
        avro_schema_decref(student_schema);
        return 0;
}
