#include <iostream>
using namespace std; 

class CStudent
{
    private:
        string Name,Number;
        int Age,FirstGrade,SecondGrade,ThirdGrade,ForthGrade,averageGrade;
    public:
        CStudent(string name, int age, string number, int firstGrade, int secondGrade, int thirdGrade, int forthGrade)
        {
            Name = name;
            Age = age;
            Number = number;
            FirstGrade = firstGrade;
            SecondGrade = secondGrade;
            ThirdGrade = thirdGrade;
            ForthGrade = forthGrade;
        }
    string getName()
    {
        return Name;
    }
    string getNumber()
    {
        return Number;
    }
    int getAge()
    {
        return Age;
    }
    int getAverage()
    {
        averageGrade = (FirstGrade + SecondGrade + ThirdGrade + ForthGrade)/4;
        return averageGrade;
    }
};
int main()
{
    int AVERAGE,AGE;
    string NAME, NUMBER; 
    
    char str[80];
    cin.getline(str, 80, ',');
    string name(str);
    cin.getline(str, 80, ',');
    int age = atoi(str);
    cin.getline(str, 80, ',');
    string number(str);
    cin.getline(str, 80, ',');
    int firstGrade = atoi(str);
    cin.getline(str, 80, ',');
    int secondGrade = atoi(str);
    cin.getline(str, 80, ',');
    int thirdGrade = atoi(str);
    cin.getline(str, 80);
    int forthGrade = atoi(str);
    
    CStudent std(name,age,number,firstGrade,secondGrade,thirdGrade,forthGrade);
    NAME = std.getName();
    AGE = std.getAge();
    NUMBER = std.getNumber();
    AVERAGE = std.getAverage();
    cout<<NAME<<","<<AGE<<","<<NUMBER<<","<<AVERAGE<<endl;
      
    system("pause");
    return 0; 
}
