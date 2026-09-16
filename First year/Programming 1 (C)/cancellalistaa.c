#include<stdio.h>
#include<stdlib.h>

struct node{
    int data;
    struct node* next;
};

void inserisci(struct node** head);
void stampa(struct node* head);
void cancella(struct node** head);

int main(){
    struct node* head=NULL;
    for(int i=0; i<5; i++){
        inserisci(&head);
    }
    stampa(head);
    cancella(&head);
    return 0;
}

void inserisci(struct node** head){
    struct node* nuovo=(struct node*)malloc(sizeof(struct node));
    if(nuovo==NULL){
        printf("Errore nell'allocazione della memoria.\n");
        exit(1);
    }
    printf("Inserisci un numero: ");
    scanf("%d", &nuovo->data);
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
        printf("%d->", tmp->data);
        tmp=tmp->next;
    }
    printf("NULL\n");
}

void cancella(struct node** head){
    struct node* tmp=*head;
    struct node* prev=NULL;
    struct node* next;
    while(tmp!=NULL){
        next=tmp->next;
        if(tmp->data%2==0){
            if(prev==NULL){
                *head=next;
            }else{
                prev->next=next;
            }
            free(tmp);
        }else{
            prev=tmp;
        }
        tmp=next;
    }
    stampa(*head);
}