// 程式名稱: oop_DATE.h
// 程式功能: 定義一個Date類別

#ifndef DATE_H   //可防止重複引入此表頭檔
#define DATE_H

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <iostream.h>
#include <dos.h>

class Date{
public:
Date ();
        Date (long j);
        Date (int y, int m, int d);
        Date (const char * dat);
        Date (const Date &dt);
        ~Date() {};

        const enum format_type {MDY, DAY, MONTH, FULL, EUROPEAN};
        const enum {OFF, ON};
        const enum {BUF_SIZE=40};
        //overloading
        operator char *( void );  // Date to character - via type casting

        Date & operator + (int i);

        Date & operator - (int i);
        long  operator - (const Date &dt);

        const Date &operator += (int i);
        const Date &operator -= (int i);

        Date & operator ++ ();               // Prefix increment
        Date  operator ++ (int);            // Postfix increment
        Date & operator -- ();               // Prefix decrement
        Date  operator -- (int);            // Postfix decrement

        int operator <  (const Date &dt);   // TML - Convert to member
        int operator <= (const Date &dt);   // functions from 'friend'
        int operator >  (const Date &dt);   // functions
        int operator >= (const Date &dt);
        int operator == (const Date &dt);
        int operator != (const Date &dt);

        friend ostream &operator << (ostream &os, const Date &dt);

        const  char * formatDate() const;
        static void   setFormat (int format);

        long  julDate() const;	// returns julian date
	int   isLeapYear()  const;	// returns 1 if leap year, 0 if not

        const Date &Today();                    // Sets to current system date
        const Date &Set(int nMonth, int nDay, int nYear);

        int  DaysInMonth();      // Number of days in month (1..31)
       	int  NYear4() const;	// eg. 1992
        int  NMonth() const;
        int  Day() const;        // Numeric Day of date object
        int  NDOW() const;
 private:
   unsigned long julian;       // julian day, since 1/1/4713 B.C.
   int year;                   // year in number see NYear4()
   unsigned char month;	       // month in number see NMonth()
   unsigned char day;          // day in number see Day()
   unsigned char dayOfWeek;

   void julian_to_mdy ();          // convert julian day to mdy
   void julian_to_wday ();         // convert julian day to day_of_week
   void mdy_to_julian ();          // convert mdy to julian day

   static char charBuf[BUF_SIZE];
};
#endif

