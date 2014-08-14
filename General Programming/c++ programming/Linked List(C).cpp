#include<stdio.h>
#include<stdlib.h>

struct t_node
{
   int data;
   struct t_node *nextPtr; 
}; 

typedef struct t_node Node; 

Node *insertList(Node *np,int val);  
Node *deleteList(Node *np,int val);   
void dispList(Node *np); 

int main(void)
{
   Node *head;
   head=NULL;
   
   Node n1,n2;
   
   n1.data=4;
   n1.nextPtr=&n2;
   n2.data=15;
   n2.nextPtr=NULL;
   head =&n1;
    
   dispList(head); 
   
   head=insertList(head,8); 
   dispList(head);
   
   head=deleteList(head,15); 
   dispList(head);
   
   system("pause");
   return 0; 
} 

void dispList(Node *np)
{
    if(np==NULL)
    {
       printf("The list is empty\n\n");         
    } 
    else
    {
       printf("Link list:");
       while(np!=NULL)
       {
          printf("%d--> ",np->data);
          np=np->nextPtr;            
       } 
       printf("NULL\n"); 
    } 
} 

Node *insertList(Node *np,int val)
{
    Node *newPtr,*previousPtr,*currentPtr;
    newPtr=(Node *)malloc(sizeof(Node));
    if(newPtr==NULL)
    {
       printf("Out of memory!\n");
       return NULL;             
    } 
    
    newPtr->data=val;
    newPtr->nextPtr=NULL;
    
    previousPtr = NULL;
    currentPtr=np; 
    
    while(currentPtr!=NULL &&val>currentPtr->data)
    {
       previousPtr=currentPtr;
       currentPtr=currentPtr->nextPtr;                    
    }
    if(previousPtr==NULL)
    {
       newPtr->nextPtr=np;
       np=newPtr;                  
    }
    else
    {
       previousPtr->nextPtr=newPtr;
       newPtr->nextPtr=currentPtr; 
    } 
    return np; 
} 

Node *deleteList(Node *np,int val)
{
    Node *currentPtr,*previousPtr,*tempPtr;
    
    if(val==np->data)
    {
       tempPtr=np;
       np=np->nextPtr;
       free(tempPtr);
       return np;              
    } 
    
    previousPtr=np;
    currentPtr=np->nextPtr;
    while(currentPtr!=NULL&&currentPtr->data!=val)
    {
       previousPtr=currentPtr;
       currentPtr=currentPtr->nextPtr;                                 
    } 
    if(currentPtr!=NULL)
    {
       tempPtr=currentPtr;
       previousPtr->nextPtr=currentPtr->nextPtr;
       free(tempPtr);                 
    } 
    return np; 
}  
