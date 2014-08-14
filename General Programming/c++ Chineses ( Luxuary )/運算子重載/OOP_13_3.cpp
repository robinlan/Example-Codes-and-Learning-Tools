//---------------------------------------------------------------------------
// 程式名稱: oop_13_3.cpp
// 程式功能: 定義<<運算子覆載函式

#include <iostream.h>
#include <stdlib.h>

class Date{
friend istream & operator>>(istream &, Date &);
friend ostream & operator<<(ostream &,const Date &);
public:
Date(int=1900, int=1, int=1); //預設引數建構元
void setDate(int, int, int);
private:
int year;
int month;
int day;
};

istream & operator>>(istream &input, Date &d)
{
    //在這裏將資料輸入到Date類別
char tmp[11];
input.getline(tmp, 11);   //輸入年(4位數)
tmp[4]=tmp[7]=tmp[10]='\0';
d.year=atoi(tmp);
d.month=atoi(tmp+5);
d.day=atoi(tmp+8);
return input;  
}

ostream & operator<<(ostream &output, const Date &d)
{
    //在這裏將資料輸出到ostream串流
output << d.year << "/" << d.month << "/" << d.day;
return output;  
}

//<<運算子覆載函式
void Date::setDate(int y, int m, int d)
{    
	year = y;     
	month = m;
	day = d;
}

//Date建構元，用來初始化資料成員的內容
Date::Date(int y, int m, int d)
{
  year=y; month=m; day=d; //可以直接存取private資料成員
}



//---------------------------------------------------------------------------
 
