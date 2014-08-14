#include<iostream>
using namespace std;

typedef struct
{
   char name[8];
   int chinese,math; 
}student; 

int main()
{
   student stu[2]={{"David",90,70},{"Lily",86,80}};
   for(int i=0;i<2;i++)
   {
      cout<<"姓名:"<<stu[i].name<<endl;
      cout<<"國文:"<<stu[i].chinese<<endl;
      cout<<"數學:"<<stu[i].math<<endl;
      cout<<"------------\n";           
   } 
   system("pause");
   return 0; 
}
