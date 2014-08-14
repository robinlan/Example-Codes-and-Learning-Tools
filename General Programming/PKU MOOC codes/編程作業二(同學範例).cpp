#include <iostream>
#include <string>
#include <stdio.h>
using namespace std;

int main() {
class student
{
private:
string Name,ID;
int Age,FstSAve,SndSAve,TrdSAve,FthSAve,Average;
public:
student(string name, int age, string id, int fstsave, int sndsave, int trdsave, int fthsave)
{
Name = name;
Age = age;
ID = id;
FstSAve = fstsave;
SndSAve = sndsave;
TrdSAve = trdsave;
FthSAve = fthsave;
}
string getName()
{
return Name;
}
string getID()
{
return ID;
}
int getAge()
{
return Age;
}
int getAverage()
{
Average = (FstSAve + SndSAve + TrdSAve + FthSAve)/4;
return Average;
}
};
int age,fstsave,sndsave,trdsave,fthsave,average,age_;
string name, id, name_, id_;
name.resize(128);
id.resize(128);
scanf("%[^,],%d,%[^,],%d,%d,%d,%d", &name[0], &age, &id[0], &fstsave, &sndsave, &trdsave, &fthsave);
student someone(name,age,id,fstsave,sndsave,trdsave,fthsave);
name_ = someone.getName();
age_ = someone.getAge();
id_ = someone.getID();
average = someone.getAverage();
cout<<name_<<","<<age_<<","<<id_<<","<<average<<endl;
system("pause"); 
return 0;
}
