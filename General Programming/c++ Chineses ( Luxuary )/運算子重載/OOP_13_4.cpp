//---------------------------------------------------------------------------
// 程式名稱: oop_13_4.cpp
// 程式功能: 定義一個!運算子覆載函式

#include <iostream.h>
class Sheepwalk{
     friend ostream & operator<<(ostream &, Sheepwalk&);
public:
Sheepwalk(){SheepCount = 0;}; 
Sheepwalk &Sheepdog(int); 
int operator!();
private:
int SheepCount;
};

int Sheepwalk::operator!()
{ 
    return !SheepCount;
}

Sheepwalk& Sheepwalk::Sheepdog(int hunt)
{
    SheepCount += hunt;
    return *this;
}

ostream & operator<<(ostream &output, Sheepwalk &s)
{
output <<"這裏有" << s.SheepCount << "隻羊\n";
return output;
}


int main(void)
{
   Sheepwalk Chiayi;

   cout << Chiayi;
   if (!Chiayi)  //如果為真，表示Chiayi物件中羊隻數量為零
Chiayi.Sheepdog(15);
else
Chiayi.Sheepdog(5);
   cout << Chiayi;
   
}

//---------------------------------------------------------------------------
