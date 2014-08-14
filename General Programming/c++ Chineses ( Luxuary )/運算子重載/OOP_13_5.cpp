//---------------------------------------------------------------------------
// 程式名稱: oop_13_5.cpp
// 程式功能: 定義一個++與--運算子的覆載函式

#include <iostream.h>
class Sheepwalk{
     friend ostream & operator<<(ostream &, Sheepwalk&);
public:
Sheepwalk(){SheepCount = 0;}; 
Sheepwalk &Sheepdog(int); 
operator int()const{return SheepCount;};
Sheepwalk& operator ++();
Sheepwalk& operator --();
//Sheepwalk operator ++(int);
int operator ++(int);
Sheepwalk operator --(int);
private:
int SheepCount;
};

Sheepwalk& Sheepwalk::operator++() //++運算子覆載函式(前置式)
{
    ++SheepCount;
return *this;
}

//Sheepwalk Sheepwalk::operator++(int) //++運算子覆載函式(後置式)
//{
//    Sheepwalk temp=*this;
//++SheepCount;
//return temp;
//}

int Sheepwalk::operator++(int) //++運算子覆載函式(後置式)
{

return SheepCount++;

}

Sheepwalk& Sheepwalk::operator--() //--運算子覆載函式(前置式)
{ 
    --SheepCount;
return *this;
}

Sheepwalk Sheepwalk::operator--(int) //--運算子覆載函式(後置式)
{
    Sheepwalk temp=*this;
--SheepCount;
return temp;
}
Sheepwalk& Sheepwalk::Sheepdog(int hunt)
{
    SheepCount += hunt;
    return *this;
}

//<<運算子覆載函式
ostream & operator<<(ostream &output, Sheepwalk &s)
{
output <<"這裏有" << s.SheepCount << "隻羊\n";
return output;
}


int main(void)
{
   Sheepwalk Chiayi;
   int sheep;

   sheep = Chiayi++;
   cout << "原先有" << sheep <<"隻羊\n";
   cout << Chiayi++;
   Chiayi.Sheepdog(15);
   sheep = Chiayi--;
   cout << "目前有" << sheep <<"隻羊\n";
   cout << Chiayi;

}


//---------------------------------------------------------------------------
