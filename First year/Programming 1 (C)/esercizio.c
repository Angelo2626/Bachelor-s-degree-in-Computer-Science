#include<stdio.h>
#include<stdlib.h>
#include<time.h>

struct node{
    int data;
    struct node* next;
};

void inserisci(struct node** head);
void stampa(struct node* head);
void cancellanodo(struct node** head);

int main(){
    srand(time(NULL));
    struct node* head=NULL;
    for(int i=0; i<10; i++){
        inserisci(&head);
    }
    stampa(head);
    cancellanodo(&head);
    stampa(head);
    return 0;
}

void inserisci(struct node** head){
    struct node* nuovo=(struct node*)malloc(sizeof(struct node));
    if(nuovo==NULL){
        printf("Errore nell'allocazione della memoria.\n");
        exit(1);
    }
    
    nuovo->data=rand()%(9+1-1)+1;
    nuovo->next=NULL;
    if((*head)==NULL){
        *head=nuovo;
    }else{
        struct node* tmp=*head;
        while(tmp->next!=NULL){
            tmp=tmp->next;
        }
        tmp->next=nuovo;
    }
}

void stampa(struct node* head){
    struct node* tmp=head;
    while(tmp!=NULL){
        printf("|%d|->", tmp->data);
        tmp=tmp->next;
    }
    printf("NULL\n");
}

void cancellanodo(struct node** head){ //cancella nodi con numeri maggiori di 5
    struct node* temp=*head;
    struct node* prev=NULL;
    struct node* next;
    while(temp!=NULL){
        next=temp->next;
        if(temp->data>5){
            if(prev==NULL){
                *head=next;
            }else{
                prev->next=next;
            }
            free(temp);
        }else{
            prev=temp;
        }
        temp=next;
    }
}