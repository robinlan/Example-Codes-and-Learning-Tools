#include<stdio.h> 
#include<stdlib.h> 

struct t_node
{
   int data;
   struct t_node *nextPtr;   
};

typedef struct t_node Node;

void printStack(Node *currentPtr);
void push(Node **topPtrPtr,int val); 
int pop(Node **topPtrPtr); 

int main(void)
{
    Node *stackPtr = NULL;
    
    push(&stackPtr,8);
    printStack(stackPtr);
    push(&stackPtr,5);
    printStack(stackPtr); 
    push(&stackPtr,4);
    printStack(stackPtr); 
    push(&stackPtr,16);
    printStack(stackPtr); 
    push(&stackPtr,23);
    printStack(stackPtr); 
    push(&stackPtr,42);
    printStack(stackPtr);  
    
    printf("%d is poped!!\n",pop(&stackPtr));
    printStack(stackPtr);
    printf("%d is poped!!\n",pop(&stackPtr));
    printStack(stackPtr);
    printf("%d is poped!!\n",pop(&stackPtr));
    printStack(stackPtr);
    
    system("pause");
    return 0;
} 

typedef struct t_node Node;

void printStack(Node *currentPtr)
{
   if(currentPtr==NULL)
      printf("The stack is empty!!\n\n");
   else 
   {
      printf("Stack: \n");
      while(currentPtr!=NULL)
      {
         printf("%d-->",currentPtr->data);
         currentPtr=currentPtr->nextPtr; 
      }
      printf("NULL\n\n");                      
   }  
} 

void push(Node **topPtrPtr,int val)//**可直接對 *stackPtr 做修改 
{
   Node *newPtr;
   newPtr=(Node*)malloc(sizeof(Node));
   if(newPtr==NULL)
   {
      printf("Out of memory!!");             
   } 
   else
   {
      newPtr->data=val;
      newPtr->nextPtr=*topPtrPtr;
      *topPtrPtr=newPtr; 
   } 
} 

int pop(Node **topPtrPtr)
{
   Node *tempPtr;
   int popValue;
   
   if(*topPtrPtr==NULL)return -1;
   
   tempPtr=*topPtrPtr;
   *topPtrPtr=(*topPtrPtr)->nextPtr;
   popValue=tempPtr->data;
   free(tempPtr);
   return popValue;
} 
