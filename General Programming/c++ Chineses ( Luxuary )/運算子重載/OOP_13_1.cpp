//---------------------------------------------------------------------------
// 程式名稱: oop_13_1.cpp
// 程式功能: 定義一個類別的夥伴函式

#include <iostream.h>

class Sheepwalk{
  friend void Sheepdog(Sheepwalk &, int);
public:
Sheepwalk(){SheepCount = 0};
void printCount(){cout <<"共有" <<　SheepCount << "隻羊\n"};
private:
int SheepCount;
};

void Sheepdog(Sheepwalk &s, int hunt)
{
    s.SheepCount += hunt;
}

int main(void)
{
   Sheepwalk Chiayi;

   Chiayi.print();
   Sheepdog(Chiayi,10);
   Chiayi.print();
   Sheepdog(Chiayi,15);
   Chiayi.print();
}

//---------------------------------------------------------------------------
 