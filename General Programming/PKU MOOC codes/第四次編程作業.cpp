#include <stdio.h>
#include <stdlib.h>
#include <string.h> 
#include <stdio.h>
#include <string>
#include <stdlib.h>
#include <iostream>
using namespace std;

#define LEN 550 

int InputBigNum(char n[])
{
  char tmp[LEN];  int i,m; 
  for (i=0;i<LEN;i++)  {  n[i]=0;  }
  if(scanf("%s",tmp)<1)  {  return -1;  }
  m=strlen(tmp);
  for(i=0;i<m;i++)  {  n[i]=tmp[m-i-1]-'0';  }
}

void OutputBigNum(char n[])
{
  int i,p,f=0;
  for(p=LEN-1;p>=0;p--)
  {  if(n[p]!=0)  {  f=1;  break;  }  }
  if(f)
  {  for(p;p>=0;p--)  printf("%c",n[p]+'0');  }
  else  {  printf("0");  }
}     
 
void AddBigNum(char a[], char b[], char c[] ) 
{
  int i,tmp=0; 
  for(i=0;i<LEN;i++)
  {
    c[i]=a[i]+b[i]+tmp;
    if(c[i]>=10)  {  tmp=1; c[i]=c[i]%10;  }
    else{ tmp=0;  } 
  }
} 


int SubBigNum(char a[], char b[], char c[])
{
  int i,tmp=0;
  for(i=LEN-1;i>=0;i--)
  {
    if(a[i]>b[i])  break;
    if(a[i]<b[i])  {  return 0; }  
  } 

  for(i=0;i<LEN;i++)
  {
    c[i]=a[i]-b[i]-tmp+10;
    if(c[i]<10)  {  tmp=1;  }
    else  {  tmp=0;  c[i]=c[i]-10;  }
  }
  return 1; 
} 

void MulBigNum(char a[], char b[], char c[])
{
  int i,j,tmp=0,m=0,d[LEN];
  for(i=0;i<LEN;i++)  {  d[i]=0;  } 
  
  for(i=LEN-1;i>=0;i--)  {if (a[i]!=0)  break;  } 
  m+=i;
  for(i=LEN-1;i>=0;i--)  {  if (b[i]!=0)  break;  } 
  m+=i; 
  m++;
   
  for(i=0;i<=m;i++)
  {
    for(j=i;j>=0;j--)
    {  d[i]+=a[j]*b[i-j];  }
    d[i]+=tmp;
    if(d[i]>=10)  {  tmp=d[i]/10;  d[i]=d[i]%10;  }
    else tmp=0;  
  } 
  for(i=0;i<LEN;i++)  {  c[i]=d[i];  } 
}

int DivBigNum(char a[], char b[], char c[])
{
  int i,j,k,p,m,n; 
  char d[LEN],e[LEN];  
  for(i=LEN-1;i>=0;i--)  {  d[i]=b[i];  e[i]=0;  } 
  for(i=LEN-1;i>=0;i--)  {  if(a[i]!=0)  break;  }
  m=i+1;  
  p=0; 
  for(i=LEN-1;i>=0;i--)  
  {
    if(b[i]!=0)
    {  p=1;  break;  }
  } 
  if(!p)  return 0; 
  n=i+1;
  if(m>n)  
  {
    for(i=n-1;i>=0;i--)  {  d[i+m-n]=d[i];  }  
    for(i=m-n-1;i>=0;i--)  {  d[i]=0;  }
  }
  for(i=m-n;i>=0;i--)
  {
    while(1)
    {
      p=SubBigNum(a,d,a); 
      if(!p)  break;
      else{  e[i]++;  }
    } 
    for(j=0;j<LEN-1;j++)  {  d[j]=d[j+1];  }
    
  }  

  for(i=LEN-1;i>=0;i--)  {  c[i]=e[i];  }  
  return 1;
}  

int main()
{
  while(1){
  char a[LEN],b[LEN],c[LEN],n[LEN];
  char command;
  int i,j,m;
  InputBigNum(a);
  cin>>command;
  InputBigNum(b);
  
  switch(command){
   case '+':
         AddBigNum(a,b,c); 
         OutputBigNum(c);
         printf("\n\n");
         break;
   case '-':
        for(i=LEN-1;i>=0;i--){
           m=a[i]-b[i];
           if(m<0)break;
        }
         if(m<0)
         {
         SubBigNum(b,a,c);
         printf("-"); 
         OutputBigNum(c); 
         printf("\n\n");            
         }
         else{
         SubBigNum(a,b,c); 
         OutputBigNum(c);
         printf("\n\n");
         }
         break;
   case '*':
         MulBigNum(a,b,c); 
         OutputBigNum(c);
         printf("\n\n");
         break;
    case '/':
         DivBigNum(a,b,c); 
         OutputBigNum(c);
         printf("\n\n");
         break;
    default:
         cout<<"輸入錯誤"<<endl;
    }
  }
  system("pause");
  return 0;
}

/*#include <stdio.h>
#include <stdlib.h>
#include <string.h> 
大數四維運算，以char陣列存數字 需string.h 

#define LEN 550 設定處理最大位數

INPUT:將數字存入某陣列
 
輸入：存放目標陣列名 輸出：0代表成功 -1代表失敗
1.清空目標陣列=0  
2.接收輸入放入暫存陣列，失敗則傳回-1 
3.計算數字位數(strlen) 
4.從個位開始依序放入目標陣列，內容從字元轉成數字
  若個位為1 ,n[0]為1(不是1的ASCII) 

int InputBigNum(char n[])
{
  char tmp[LEN];  int i,m; 
  for (i=0;i<LEN;i++)  {  n[i]=0;  }
  if(scanf("%s",tmp)<1)  {  return -1;  }
  m=strlen(tmp);
  for(i=0;i<m;i++)  {  n[i]=tmp[m-i-1]-'0';  }
}
 
OutputBigNum:傾印出數字

輸入：要被印出來的數陣列名 無return 
1.找到非0之數字開頭元素號p 找到f=1 @要是發現全部都是0找不到開頭則f=0
2.倒序轉成字元印出，個位最後傾印，印完不換行 
  @要是發現全部都是0會印出一個0


void OutputBigNum(char n[])
{
  int i,p,f=0;
  for(p=LEN-1;p>=0;p--)
  {  if(n[p]!=0)  {  f=1;  break;  }  }
  if(f)
  {  for(p;p>=0;p--)  printf("%c",n[p]+'0');  }
  else  {  printf("0");  }
}     
 
AddBigNum: 兩數相加

輸入：被加數a，加數b，結果存放陣列c 無return
1.從個位開始一個個相加(a+b+tmp)，如有進位存放在tmp

void AddBigNum(char a[], char b[], char c[] ) 
{
  int i,tmp=0; 
  for(i=0;i<LEN;i++)
  {
    c[i]=a[i]+b[i]+tmp;
    if(c[i]>=10)  {  tmp=1; c[i]=c[i]%10;  }
    else{ tmp=0;  } 
  }
} 

SubBigNum 兩數相減

輸入：被減數a，減數b,結果存放陣列c 有return(成功相減:1,a<b:0) 
IF a<b則c不變; 
0;檢查a是否>=b，若否則傳回-1結束 
1.從個位開始一個個相減，結果加上10預防負數(a-b-tmp)
 若減出<10(原為負數)
 則tmp=1及把預防負數的10從結果裡減掉  


int SubBigNum(char a[], char b[], char c[])
{
  int i,tmp=0;
  for(i=LEN-1;i>=0;i--)
  {
    if(a[i]>b[i])  break;
    if(a[i]<b[i])  {  return 0; }  
  } 

  for(i=0;i<LEN;i++)
  {
    c[i]=a[i]-b[i]-tmp+10;
    if(c[i]<10)  {  tmp=1;  }
    else  {  tmp=0;  c[i]=c[i]-10;  }
  }
  return 1; 
} 

MulBigNum 兩數相乘

輸入：被乘數a，乘數b,結果存放陣列c 無return 
0.清空暫存結果的陣列d (用d的目的是避免c和a.b相同時出錯) 
1.m=a,b兩者的位數和+1，作為結果可能的最大存位   
2.積的第n位數=a[n]b[0]+a[n-1]b[1]+.....a[0]b[n]+上一個位數的進位tmp 依此由[0]計算每個d 
3.把d內數寫入c 


void MulBigNum(char a[], char b[], char c[])
{
  int i,j,tmp=0,m=0,d[LEN];
  for(i=0;i<LEN;i++)  {  d[i]=0;  } 
  
  for(i=LEN-1;i>=0;i--)  {if (a[i]!=0)  break;  } 
  m+=i;
  for(i=LEN-1;i>=0;i--)  {  if (b[i]!=0)  break;  } 
  m+=i; 
  m++;
   
  for(i=0;i<=m;i++)
  {
    for(j=i;j>=0;j--)
    {  d[i]+=a[j]*b[i-j];  }
    d[i]+=tmp;
    if(d[i]>=10)  {  tmp=d[i]/10;  d[i]=d[i]%10;  }
    else tmp=0;  
  } 
  for(i=0;i<LEN;i++)  {  c[i]=d[i];  } 
}

 

DivBigNum 兩數相除

*此函數需要SubBigNum&AddBigNum支持
輸入：被乘數a，乘數b,結果存放陣列c 有return(成功相除:1,b==0:0)
b=0時c不變 
長除法原理 商到整數無條件捨去 
1.取得a[]及b[]的位數數目m,n
2.將乘數b乘上10的(m-n)次方，也就是前移(m-n)位
3.由i=0開始將a和b*i比較，找出滿足b*i≦A＜B*(i+1)的i
4.商的m-n+1位就是i,a=a-b*i,b=b/10
5.重複m-n次


int DivBigNum(char a[], char b[], char c[])
{
  int i,j,k,p,m,n; 
  char d[LEN],e[LEN];  
  for(i=LEN-1;i>=0;i--)  {  d[i]=b[i];  e[i]=0;  } 
  for(i=LEN-1;i>=0;i--)  {  if(a[i]!=0)  break;  }
  m=i+1;  
  p=0; 
  for(i=LEN-1;i>=0;i--)  
  {
    if(b[i]!=0)
    {  p=1;  break;  }
  } 
  if(!p)  return 0; 
  n=i+1;
  if(m>n)  
  {
    for(i=n-1;i>=0;i--)  {  d[i+m-n]=d[i];  }  
    for(i=m-n-1;i>=0;i--)  {  d[i]=0;  }
  }
  for(i=m-n;i>=0;i--)
  {
    while(1)
    {
      p=SubBigNum(a,d,a); 
      if(!p)  break;
      else{  e[i]++;  }
    } 
    for(j=0;j<LEN-1;j++)  {  d[j]=d[j+1];  }
    
  }  

  for(i=LEN-1;i>=0;i--)  {  c[i]=e[i];  }  
  return 1;
}  


 
int main()
{
  char a[LEN],b[LEN],c[LEN];
  int p;
  while(1)
  {
    InputBigNum(a);
    InputBigNum(b);
    p=DivBigNum(a,b,c); 
    OutputBigNum(c);
    printf("\n\n");
  }
}*/ 
