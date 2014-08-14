#include<iostream>
using namespace std;

int main()
{
    char *p=new char[31];//動態陣列,最多30個字母
    cout<<"輸入英文單字:";
    cin>>p;
    int n=strlen(p);//找出字串長度
    for(int i=0;i<n;i++)
    {
       if(*(p+i)>='A'&&*(p+i)<='Z')//大寫變小寫 
          *(p+i)+=32;
       else if(*(p+i)>='a'&&*(p+i)<='z')//小寫變大寫 
          *(p+i)-=32;  
    } 
    cout<<p<<"\n";
    delete[] p; 
    system("pause");
    return 0; 
} 
