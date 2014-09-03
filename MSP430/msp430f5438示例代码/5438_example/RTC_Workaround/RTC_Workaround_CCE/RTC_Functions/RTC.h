#ifndef RTC_H_
#define RTC_H_

struct Time { int Year, Month, DayOfWeek, Day, Hour, Minute, Second; };

extern int SetRTCYEAR(int year); 	
extern int SetRTCMON(int month);
extern int SetRTCDAY(int day);
extern int SetRTCDOW(int dow);
extern int SetRTCHOUR(int hour);
extern int SetRTCMIN(int min);
extern int SetRTCSEC(int sec);

extern int GetRTCTIM0(void); 	
extern int GetRTCTIM1(void); 	
extern int GetRTCDATE(void); 	
extern int GetRTCYEAR(void); 	

int TestRTCYear(struct Time TaD);
int TestRTCMonth(struct Time TaD);
int TestRTCDow(struct Time TaD);
int TestRTCDay(struct Time TaD);
int TestRTCHour(struct Time TaD);
int TestRTCMinute(struct Time TaD);
int TestRTCSecond(struct Time TaD);


#endif /*RTC_H_*/
