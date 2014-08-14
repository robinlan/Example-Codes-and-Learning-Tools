#include<iostream>
using namespace std;

int main()
{
    int n[2][3]={{11,12,13},{21,22,23}};
    int max,min;
    for(int i=0;i<2;i++)
       for(int j=0;j<3;j++)
          if(i==0&&j==0)
          {
             max=n[0][0];
             min=n[0][0];           
          } 
          else
          {
              if(*(*(n+i)+j)>max)
                 max=*(*(n+i)+j); 
              if(*(*(n+i)+j)<min)
                 min=*(*(n+i)+j);
          }
    cout<<"陣列中最大值:"<<max<<"\n";
    cout<<"陣列中最小值:"<<min<<"\n"; 
    system("pause");
    return 0; 
} 
