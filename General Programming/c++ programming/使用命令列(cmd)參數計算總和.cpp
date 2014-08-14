#include<iostream>
using namespace std;

int main(int argc,char *argv[])
{
    int sum=0;
    if(argc==1)//Τ祘Α嘿 
        cout<<"ゼ块把计!"<<"\n";
    else
    {
        for(int i=1;i<argc;i++)
           sum+=atoi(argv[i]);//atoi()盢﹃锣俱计 
        cout<<"块把计羆㎝:"<<sum<<"\n"; 
    } 
    system("pause");
    return 0; 
} 
