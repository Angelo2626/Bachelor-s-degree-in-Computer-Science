#include<stdio.h>
#include<stdlib.h>

struct node{
    int data;
    struct node* next;
};

void inserisci(struct node** head);
void stampa(struct node* head);
void prodotto(struct node** head);

int main(){
    struct node* head=NULL;
    for(int i=0; i<5; i++){
        inserisci(&head);
    }

    stampa(head);
    prodotto(&head);
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
    nuovo->next=*head;

    *head=nuovo;
}

void stampa(struct node* head){
    struct node* tmp=head;
    while(tmp!=NULL){
        printf("%d->", tmp->data);
        tmp=tmp->next;
    }
    printf("NULL\n");
}

void prodotto(struct node** head){
    struct node* tmp=*head;
    int prev=tmp->data, prv=0;
    tmp=tmp->next;
    while(tmp!=NULL){
        prv=tmp->data;
        tmp->data=prev*(tmp->data);
        prev=prv;
        tmp=tmp->next;
    }
    stampa(*head);
}