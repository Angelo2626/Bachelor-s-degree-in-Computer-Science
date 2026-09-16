#include<stdio.h>
#include<stdlib.h>
struct node{
    int valore;
    struct node* next;
};

void inserisci(struct node** head);
void stampa(struct node* tmp);
int multi3(struct node* temp, int c);

int main(){

    struct node* head=NULL;
    for(int i=0; i<5; i++){
        inserisci(&head);
    }

    struct node* tmp=head;
    stampa(tmp);
    struct node* temp=head;
    int c=0;
    c=multi3(temp, c);
    printf("\nCi sono %d multipli di 3.\n", c);

    return 0;
}

void inserisci(struct node** head){
    struct node* nuovo=(struct node*)malloc(sizeof(struct node));
    if(nuovo==NULL){
        printf("Errore nell'allocazione della memoria.\n");
        exit(1);
    }
    printf("Inserisci valore: ");
    scanf("%d", &nuovo->valore);
    nuovo->next=(*head);

    (*head)=nuovo;
}

void stampa(struct node* tmp){
    if(tmp==NULL){
        printf("NULL\n");
        return; 
    }else{
        printf("|%d|->", tmp->valore);
        stampa(tmp->next);
    }
}

int multi3(struct node* temp, int c){
    if(temp==NULL){
        return c;
    }else{
        if(temp->valore%3 == 0){
            c++;
        }
        return multi3(temp->next, c);
    }
}