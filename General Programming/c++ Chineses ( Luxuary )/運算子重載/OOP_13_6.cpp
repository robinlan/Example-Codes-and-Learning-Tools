//---------------------------------------------------------------------------
// 程式名稱: oop_13_6.cpp
// 程式功能: 定義一個Vector類別，用來儲存整數向量
 
#include <iostream.h>

typedef int TempType;

class Vector{
  //friend ostream & operator<<(ostream &, Vector &);
  public:
        Vector(int initialSize=5);
        ~ Vector(){delete []elements;};
        TempType & operator[](int offset);
        int size(){return NumOfElements;};
  private:
        int NumOfElements;  //元素個數
        TempType *elements; //第一個元素存放的位址
};

//建構元
Vector:: Vector (int initialSize)
{
   elements = new TempType[initialSize];;
   NumOfElements = initialSize; 
}

TempType & Vector::operator[](int offset)
{
   if(offset >= NumOfElements)
	{
      TempType *Extended = new TempType[offset + 1];
      for(int i=0; i< NumOfElements; i++) 
Extended[i] = elements[i];
    delete []elements;
    elements = Extended;
    NumOfElements = offset + 1;
   }
   return elements[offset];
}


int main(void){
  Vector V;

  for(int i=0; i<V.size(); i++)
    V[i] = i+1;

  for(int i=0; i<V.size(); i++)
    cout << V[i] << "\t";
  cout << endl;

  V[8]=100;
  for(int i=0; i<V.size(); i++)
    cout << V[i] << "\t";

}



//---------------------------------------------------------------------------
