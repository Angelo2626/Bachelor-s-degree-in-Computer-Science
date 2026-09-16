#include<stdio.h>
#include<stdlib.h>

struct node{
    int data;
    struct node* next;
};

void riempi(struct node** head, int n);
void shiftsx(struct node* head);
void stampa(struct node* head);
void shiftrx(struct node** head);

int main(){

    struct node* head=NULL;
    riempi(&head, 8);
    riempi(&head, 2);
    riempi(&head, 10);
    riempi(&head, 6);
    riempi(&head, 3);
    
    stampa(head);
    shiftsx(head);
    stampa(head);
    shiftrx(&head);
    stampa(head);
    shiftrx(&head);
    stampa(head);
    return 0;
}

void riempi(struct node** head, int n){
    struct node* nuovo=(struct node*)malloc(sizeof(struct node));

    if(nuovo==NULL){
        printf("Errore nell'allocazione della memoria.\n");
    }

    nuovo->data=n;
    nuovo->next=(*head);

    (*head)=nuovo;
}

void shiftsx(struct node* head){
    if(head==NULL || head->next==NULL){
        return;
    }
    struct node* tmp=head;
    int i;
    i=tmp->data;
    while(tmp->next!=NULL){
        tmp->data=tmp->next->data;
        tmp=tmp->next;
    }
    tmp->data=i;
}

void stampa(struct node* head){
    while(head!=NULL){
        printf("|%d|->", head->data);
        head=head->next;
    }

    printf("NULL\n");
}

void shiftrx(struct node** head){
    if((*head)==NULL || (*head)->next==NULL){
        return;
    }
    struct node* tmp=*head;
    struct node* pen=NULL;
    while(tmp->next!=NULL){
        pen=tmp;
        tmp=tmp->next;
    }
    pen->next=NULL;
    tmp->next=(*head);
    (*head)=tmp;
}