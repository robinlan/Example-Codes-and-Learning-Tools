#include<iostream>
using namespace std;
#define nil -1
#define MaxSize 20 

struct treenode 
{
   int left;
   char name[12];
   int right; 
}; 

int main()
{
   struct treenode node[MaxSize]={{1,"Micro",2},{3,"David",4},{5,"Tom",nil},{nil,"Amy",nil},{6,"Iverson",nil},{nil,"Nancy",nil},{nil,"Harper",nil}}; 
   char key[12];
   int p=0;
   cout<<"輸入搜尋資料"; 
   cin>>key;
   while(p!=nil)
   {
      if(strcmp(key,node[p].name)==0)//strcmp()相減兩者字母順序(ex:Iverson、Micro相減小於零)          
      { 
         cout<<"找到了\n";
         break; 
      } 
      else if(strcmp(key,node[p].name)<0)
         p=node[p].left; 
      else
         p=node[p].right;    
   } 
   system("pause");
   return 0; 
}
