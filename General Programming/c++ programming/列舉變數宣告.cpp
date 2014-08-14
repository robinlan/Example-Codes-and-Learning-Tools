#include<iostream>
using namespace std;

enum MouseKeys 
{
   Left=0,
   Right=1,
   Middle=2,  
}; 

int main()
{
   enum MouseKeys button;
   button=Middle;
   if(button==Left)
      cout<<"·Æ¹«¥ªÁä­È="<<Left<<endl; 
   else if(button==Right)
      cout<<"·Æ¹«¥kÁä­È="<<Right<<endl; 
   else if(button==Middle)
      cout<<"·Æ¹«¤¤¶¡Áä="<<Middle<<endl;  
   system("pause");
   return 0; 
}
