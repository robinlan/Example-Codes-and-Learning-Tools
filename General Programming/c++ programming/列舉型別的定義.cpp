#include<iostream>
using namespace std;

enum DayOfWeek
{
   Sunday,
   Monday,
   Tuesday=5,
   Wednesday,
   Thursday,
   Friday=5,
   Saturday  
}; 

int main()
{
   cout<<"Mondayªº­È="<<Monday<<endl;
   cout<<"Wednesdayªº­È="<<Wednesday<<endl;
   cout<<"Fridayªº­È="<<Friday<<endl; 
   system("pause");
   return 0; 
}
