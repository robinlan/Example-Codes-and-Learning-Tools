#include<iostream>
using namespace std;

int main()
{
    int *p1=new int;
    int *p2=new int;
    cout<<"輸入的一個數:";
    cin>>*p1;
    cout<<"輸入第二個數:";
    cin>>*p2;
    cout<<*p1<<"*"<<*p2<<"="<<(*p1)*(*p2)<<"\n";
    delete p1;
    delete p2;  
    system("pause");
    return 0; 
} 
