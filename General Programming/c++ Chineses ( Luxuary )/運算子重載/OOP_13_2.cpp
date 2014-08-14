//---------------------------------------------------------------------------
// 程式名稱: oop_13_2.cpp
// 程式功能: 利用this指標存取資料成員

#include <iostream.h>

class Sheepwalk{
public: 
Sheepwalk(){SheepCount = 0;};
Sheepwalk &Sheepdog(int);
void printCount();
private:
int SheepCount;
};

void Sheepwalk::printCount()
{ 
cout <<"共有" << this->SheepCount << "隻羊\n";
}

Sheepwalk& Sheepwalk::Sheepdog(int hunt)
{
    (*this).SheepCount += hunt;
    return *this;
}

int main(void)
{
   Sheepwalk Chiayi;

   Chiayi.printCount();
   Chiayi.Sheepdog(10).Sheepdog(15); //獵到十隻羊後馬上又獵到十五隻羊
   Chiayi.printCount();
   
}

//---------------------------------------------------------------------------
 