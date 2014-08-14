#include<iostream>
using namespace std;
void setscore(int &);
void show(char[],int);

struct student
{
   char name[8];
   int chinese; 
}; 

int main()
{
   struct student David={"張三",50};
   show(David.name,David.chinese);
   setscore(David.chinese); //or用 : setscore(&David) 
   cout<<"\n國文補考後"<<endl;
   show(David.name,David.chinese);
   system("pause");
   return 0; 
}

void show(char name[],int score)
{
   cout<<"姓名:"<<name<<endl;
   cout<<"國文:"<<score<<endl;  
}

void setscore(int &score)   //此時這裡改成 : void setscore(struct student *stu) 
{                          
   score = 60;              //此時這裡改成 : stu->chinese=60; 
} 
