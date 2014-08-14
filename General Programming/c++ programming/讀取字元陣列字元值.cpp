#include<iostream>
using namespace std;

int main()
{
    char s[]="Robin";
    cout<<"以陣列方式顯示s字串:"<<"\n";
    for(int i=0;i<5;i++)
       cout<<"s["<<i<<"]="<<s[i]<<"\n"; 
    cout<<"以指標方式顯示s字串:"<<"\n";
    for(int i=0;i<5;i++)
       cout<<"*(s+"<<i<<")="<<*(s+i)<<"\n";
    system("pause");
    return 0; 
} 
