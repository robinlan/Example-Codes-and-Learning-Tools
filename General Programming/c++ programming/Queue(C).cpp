#include<stdio.h> 
#include<stdlib.h> 

struct t_node
{
   int data;
   struct t_node *nextPtr;   
};

typedef struct t_node Node;

void printQueue(Node *currentPtr);
void enQueue(Node **headPtr,Node**tailPtr,int val);
int deQueue(Node **headPtr,Node**tailPtr); 

int main(void)
{
    Node *head,*tail;
    
    head = NULL;
    tail = NULL;
    
    enQueue(&head,&tail,4);
    printQueue(head);
    enQueue(&head,&tail,8);
    printQueue(head); 
    enQueue(&head,&tail,15);
    printQueue(head); 
    enQueue(&head,&tail,16);
    printQueue(head); 
    enQueue(&head,&tail,23);
    printQueue(head);  
    
    printf("%d is out!!\n",deQueue(&head,&tail));
    printQueue(head); 
    printf("%d is out!!\n",deQueue(&head,&tail));
    printQueue(head); 
    printf("%d is out!!\n",deQueue(&head,&tail));
    printQueue(head); 
    
    system("pause"); 
    return 0; 
}

void printQueue(Node *currentPtr)
{
   if(currentPtr==NULL)
      printf("The queue is empty!!\n\n");
   else 
   {
      printf("Queue: \n");
      while(currentPtr!=NULL)
      {
         printf("%d-->",currentPtr->data);
         currentPtr=currentPtr->nextPtr; 
      }
      printf("NULL\n\n");                      
   }    
} 

void enQueue(Node **headPtr,Node**tailPtr,int val)
{
   Node *newPtr;
   newPtr=(Node*)malloc(sizeof(Node));
   if(newPtr==NULL)
      printf("Out of memory!!\n\n");
   else
   {
      newPtr->data=val;
      newPtr->nextPtr=NULL;
      if(*headPtr==NULL)
      { 
         *headPtr=newPtr; 
         *tailPtr=newPtr; 
      }
      else
      {
         (*tailPtr)->nextPtr=newPtr;
         *tailPtr=newPtr; 
      } 
   }       
} 

int deQueue(Node **headPtr,Node**tailPtr)
{
   int val;
   Node *tempPtr;
   
   if(*headPtr==NULL)return -1;
   
   val=(*headPtr)->data;
   tempPtr=*headPtr; 
   *headPtr=(*headPtr)->nextPtr;
   
   if(tailPtr==NULL)
      *tailPtr=NULL;
   
   free(tempPtr);
   
   return val;    
} 
