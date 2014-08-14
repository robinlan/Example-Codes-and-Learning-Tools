#include<stdio.h>
#include<stdlib.h>
#include<string.h>
enum {MAX_LEN=256,MAX_LINE=1000};

int main(void)
{
   char *p[MAX_LINE];
   char buf[MAX_LEN+1];
   int i,j;
   
   i=0;
   while(i<MAX_LINE&&(fgets(buf,MAX_LEN+1,stdin)!=NULL))//fgets(buf,n,stdin)讀取字串長度n(一整行字串直到換行健enter)存到buf裡從stdin(鍵盤) 
   {
      p[i]=(char*)malloc(strlen(buf)+1);//加入"\0" 
      if(p[i]!=NULL)
      {
         strcpy(p[i],buf);//將p[i]存進buf裡
         i++;           
      } 
   } 
   
   for(j=0;j<i;j++)
   {
      printf("%s",p[j]);
      free(p[j]);             
   } 
   system("pause");
   return 0; 
} 

 
