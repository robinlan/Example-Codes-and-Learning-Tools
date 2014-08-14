#include<stdio.h> 
#include<stdlib.h>
#include<ctype.h> 
#include<string.h>

void strUpper(char *str);
void strLower(char *str);
void show(void(*pf)(char*),char *str); 

int main()
{
    char str[100]; 
    void(*pf)(char*);
    
    fgets(str,100,stdin);
    str[strlen(str)-1]='\0'; 
    
    pf=strUpper;
    show(pf,str); 
    pf=strLower;
    show(pf,str);
    
    system("pause"); 
    return 0;
} 

void strUpper(char *p)
{
    while(*p!='\0')
    { 
       printf("%c",toupper(*p));
       p++; 
    } 
} 

void strLower(char *p)
{
    while(*p!='\0')
    { 
       printf("%c",tolower(*p));
       p++; 
    } 
} 

void show(void(*pf)(char*),char *str)
{
    (*pf)(str); 
    printf("\n"); 
} 
