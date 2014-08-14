#include <iostream>
using namespace std; 

int main(){ 
char str[80];
cin.getline(str, 80, ',');
string name(str);
cin.getline(str, 80, ',');
int age = atoi(str);
cin.getline(str, 80, ',');
string number(str);
cin.getline(str, 80, ',');
int average1 = atoi(str);
cin.getline(str, 80, ',');
int average2 = atoi(str);
cin.getline(str, 80, ',');
int average3 = atoi(str);
cin.getline(str, 80);
int average4 = atoi(str);
cout<<name<<age<<number<<average1<<average2<<average3<<average4<<endl; 
system("pause");
return 0;
} 
