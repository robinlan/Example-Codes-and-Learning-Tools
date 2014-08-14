#include<iostream>
using namespace std;

int main()
{
    char fruit1[3][11]={"apple","watermelon","banana"};
    char *fruit2[3]={"apple","watermelon","banana"};
    cout<<"二維字元陣列:"<<"\n";
    for(int i=0;i<3;i++)
    {
      cout<<"第 "<<i+1<<" 個元素:"<<fruit1[i];
      cout<<",所佔位址:"<<(int *)fruit1[i]<<"\n"; 
    }
    cout<<"\n"; 
    cout<<"指標陣列:"<<"\n";
    for(int i=0;i<3;i++)
    {
      cout<<"第 "<<i+1<<" 個元素:"<<fruit2[i];
      cout<<",所佔位址:"<<(int *)fruit2[i]<<"\n"; 
    }  
    system("pause");
    return 0; 
} 
