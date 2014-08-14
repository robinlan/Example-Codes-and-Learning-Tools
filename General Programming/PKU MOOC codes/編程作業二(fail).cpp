#include <iostream>
using namespace std; 

class CStudent
{
    private: 
        char stdName[20];
        int Age;
        char stdNumber[20];
        int firstGrade,secondGrade,thirdGrade,forthGrade;
    public: 
        void createName(char *name);
        void obtainName(char *name);
        void createAge(int *age);
        void obtainAge();
        void createNumber(char *number);
        void obtainNumber(char *number);
        void averageGrade(CStudent s1);
        void createFirstGrade(int *grade);
        void createSecondGrade(int *grade);
        void createThirdGrade(int *grade); 
        void createForthGrade(int *grade);     
};
void CStudent::createName(char *name)
{
    strcpy(stdName,name); 
}
void CStudent::obtainName(char *name)
{
    strcpy(name,stdName);
    cout<<name<<","; 
}
void CStudent::createAge(int *age)
{
    Age=age; 
}
void CStudent::obtainAge()
{
    cout<<Age<<","; 
}
void CStudent::createNumber(char *number)
{
    strcpy(stdNumber,number); 
}
void CStudent::obtainName(char *number)
{
    strcpy(number,stdNumber); 
    cout<<number<<",";
}
void CStudent::averageGrade(CStudent1 s1)
{
    averageGrade=(s1.firstGrade+s1.secondGrade+s1.thirdGrade+s1.forthGrade)/4;
    cout<<averageGrade<<endl;     
}
void CStudent::createFirstGrade(int *grade)
{
    firstGrade=grade; 
}
void CStudent::createSecomdGrade(int *grade)
{
    secondGrade=grade; 
}
void CStudent::createThirdGrade(int *grade)
{
    thirdGrade=grade;
}
void CStudent::createForthGrade(int *grade)
{
    forthGrade=grade; 
}
int main()
{
    CStudent std;
    char str[80];
    cin.getline(str, 80, ',');
    string name(str);
    cin.getline(str, 80, ',');
    int age = atoi(str);
    cin.getline(str, 80, ',');
    string number(str);
    cin.getline(str, 80, ',');
    int grade1 = atoi(str);
    cin.getline(str, 80, ',');
    int grade2 = atoi(str);
    cin.getline(str, 80, ',');
    int grade3 = atoi(str);
    cin.getline(str, 80);
    int grade4 = atoi(str);
    
    std.createName(name);
    std.createAge(age);
    std.createNumber(number); 
    std.createFirstGrade(grade1);
    std.createSecondGrade(grade2);
    std.createThirdGrade(grade3);
    std.createForthGrade(grade4);
    
    std.obtainName(name);
    std.obtainAge();
    std.obtainNumber(number);   
    std.averageGrade(std); 
      
    system("pause");
    return 0; 
}
