#include<stdio.h>
#include<stdlib.h>

struct node{
    int data;
    struct node* next;
};

void inserisci(struct node** head,int n);
void stampa(struct node* head);
void somma(struct node* head);

int main(){
    struct node* head=NULL;
    int n;
    for(int i=0; i<5; i++){
        printf("Inserisci valore: ");
        scanf("%d", &n);
        inserisci(&head,n);
    }
    stampa(head);
    somma(head);
    stampa(head);
    return 0;
}

void inserisci(struct node** head, int n){
    struct node* nuovo=(struct node*)malloc(sizeof(struct node));
    if(nuovo==NULL){
        printf("Errore nell'allocazione della memoria.\n");
        exit(1);
    }
    nuovo->data=n;
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

void somma(struct node* head){
    struct node* tmp=head;
    int somma=0;
    while(tmp!=NULL){
        somma=somma+tmp->data;
        tmp=tmp->next;
    }
    inserisci(&head, somma);
}