#include<iostream>
using namespace std;

union student 
{
   char grade[12];
   int score; 
}; 

int main()
{
   union student David;
   char ch;
   cout<<"請選擇輸入項目:(1)以分數評分(2)以等第評分";
   cin>>ch;
   switch(ch)
   {
      case '1':
         cout<<"請輸入分數:";
         cin>>David.score;
         break;
      case '2':
         cout<<"請輸入等第:";
         cin>>David.grade;
         break;    
   }
   cout<<"David占用記憶體="<<sizeof(David)<<endl;
   cout<<"分數:"<<David.score<<endl; 
   cout<<"等第:"<<David.grade<<endl;
   system("pause");
   return 0; 
}
